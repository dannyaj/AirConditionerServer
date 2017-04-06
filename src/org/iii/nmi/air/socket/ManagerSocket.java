package org.iii.nmi.air.socket;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.iii.nmi.air.factory.InstanceFactory;
import org.iii.nmi.air.log.ExceptionHandler;
import org.iii.nmi.air.log.LogHandler;
import org.iii.nmi.air.queue.WebAirQueue;
import org.iii.nmi.air.rsprocess.Rsprocess;
import org.iii.nmi.air.sendcmd.SendCommand;
import org.iii.nmi.air.util.RspSqlHandler;

public class ManagerSocket
{
	private LogHandler logHandler;

	private ExceptionHandler exceptionHandler;

	private WebAirQueue webAirQueue;

	private Rsprocess rsProcess;

	private SendCommand sendCommandThread;

	private SerialParameters serialParameters;

	private Vector<WebSubServer> webSubServerVec;

	private Vector<SerialConnection> serialConnectionVec;

	private Vector<SerialConnection> airServerVec;

	private String socketWebPort;

	private String socketAirPort;

	private ServerSocket webSocket;

	private ServerSocket socketAir;

	private String forwardServerIp;

	private String forwardServerPort;

	private boolean forwardServerIsOpen;

	public ManagerSocket(WebAirQueue webAirQueue, Rsprocess rsProcess)
	{
		this.webAirQueue = webAirQueue;
		this.rsProcess = rsProcess;

		this.logHandler = InstanceFactory.createLogHandler();
		this.exceptionHandler = InstanceFactory.createExceptionHandler();
		this.serialParameters = new SerialParameters();

		init();
	}

	private void init()
	{
		Properties properties = new Properties();
		FileInputStream f;
		try
		{
			f = new FileInputStream("conf/socket.properties");
			properties.load(f);
			f.close();

			serialParameters.setPortName(properties.getProperty("PortName"));
			serialParameters.setBaudRate(properties.getProperty("BaudRate"));
			serialParameters.setFlowControlIn(serialParameters.stringToFlow(properties.getProperty("FlowControlIn")));
			serialParameters.setFlowControlOut(serialParameters.stringToFlow(properties.getProperty("FlowControlOut")));
			serialParameters.setParity(properties.getProperty("Parity"));
			serialParameters.setDatabits(Integer.parseInt(properties.getProperty("DataBits")));
			serialParameters.setStopbits(Integer.parseInt(properties.getProperty("StopBits")));
			socketWebPort = properties.getProperty("socketAirWebPort");
			socketAirPort = properties.getProperty("socketAirPort");
			forwardServerPort = properties.getProperty("forwardServerPort");
			serialConnectionVec = new Vector<SerialConnection>();
			airServerVec = new Vector<SerialConnection>();
			webSubServerVec = new Vector<WebSubServer>();

			webSocket = new ServerSocket(Integer.parseInt(socketWebPort));
			socketAir = new ServerSocket(Integer.parseInt(socketAirPort));

			this.forwardServerIp = properties.getProperty("forwardServerIp");
			this.forwardServerPort = properties.getProperty("forwardServerPort");
			this.forwardServerIsOpen = Boolean.parseBoolean(properties.getProperty("forwardServerIsOpen"));

			logHandler.info("manager socket initial finish");

		}
		catch(FileNotFoundException e)
		{
			exceptionHandler.putExceptionMessage("initial socket.properties not find");
			e.printStackTrace();
		}
		catch(IOException e)
		{
			exceptionHandler.putExceptionMessage("initial socket.properties FileInputStream IOException");
			e.printStackTrace();
		}

	}

	public void waitForForward() throws IOException
	{
		SerialConnection serialConnection = new SerialConnection(socketAir.accept(), this, serialParameters, forwardServerIp, forwardServerPort, forwardServerIsOpen);

		addAirSubServer(serialConnection);

		Thread thread = new Thread(serialConnection);
		thread.setDaemon(true);
		thread.start();

		logHandler.info(serialConnection.getSocketName() + " forward server connection ok");

		/*
		 * if(sendCommandThread == null) { sendCommandThread =
		 * InstanceFactory.createSendCommandThread(rsProcess, serialConnection);
		 * sendCommandThread.start(); }
		 */

	}

	public void openComPortConnection() throws IOException
	{
		SerialConnection serialConnection = new SerialConnection(this, serialParameters, forwardServerIp, forwardServerPort, forwardServerIsOpen);

		addSerialSubServer(serialConnection);

		try
		{
			serialConnection.openConnection();
		}
		catch(SerialConnectionException e)
		{

			e.printStackTrace();
			exceptionHandler.putExceptionMessage(e.getMessage());
		}
		logHandler.info(serialConnection.getName() + " : already Open");

		if(sendCommandThread == null)
		{
			sendCommandThread = InstanceFactory.createSendCommandThread(rsProcess, serialConnection);
			sendCommandThread.start();
		}

	}

	public void waitForWebClient() throws IOException
	{
		WebSubServer webSubServer = new WebSubServer(webSocket.accept(), this);
		addWebSubServer(webSubServer);
		Thread thread = new Thread(webSubServer);
		thread.setDaemon(true);
		thread.start();
		logHandler.info(webSubServer.getName() + " Web: already connection NServer");
	}

	public void shutdown()
	{
		webAirQueue.clearAllCommands();
		Enumeration<SerialConnection> serial = serialConnectionVec.elements();
		SerialConnection serialConnection;

		while(serial.hasMoreElements())
		{
			serialConnection = serial.nextElement();
			serialConnection.clearAllCommands();
			serialConnection.setShutdownFlag(true);
			serialConnection.shutdown();
			closeSerialConnection(serialConnection);
		}

		removeSerialConnectioVector(serialConnectionVec);

		Enumeration<SerialConnection> airServerVecSocketEnum = airServerVec.elements();

		while(airServerVecSocketEnum.hasMoreElements())
		{
			serialConnection = airServerVecSocketEnum.nextElement();
			serialConnection.clearAllCommands();
			serialConnection.setShutdownFlag(true);
			serialConnection.shutdown();
		}

		removeForwardSubServerVector(airServerVec);

		if(sendCommandThread != null)
			sendCommandThread.setClosed(true);
		rsProcess.clearAllCommands();
		RspSqlHandler.clear();
		Enumeration<WebSubServer> webServer = webSubServerVec.elements();
		WebSubServer webSubServer;

		while(webServer.hasMoreElements())
		{
			webSubServer = webServer.nextElement();
			shutdownWebSubServer(webSubServer);
		}

		removeWebSubServerVector(webSubServerVec);

		InstanceFactory.createRequestList().clearAll();

		logHandler.info("close Serial Connection finish");

		try
		{
			webSocket.close();
			logHandler.info("close web socket finish");
		}
		catch(IOException e)
		{
			exceptionHandler.putExceptionMessage("close web Socket IOException");
			e.printStackTrace();
		}

	}

	public void removeDeadConnections()
	{
		Enumeration<SerialConnection> serialEnum = serialConnectionVec.elements();
		Enumeration<SerialConnection> airServerEnum = airServerVec.elements();
		Enumeration<WebSubServer> webServerEnum = webSubServerVec.elements();

		Vector<SerialConnection> serialConnectionVector = new Vector<SerialConnection>();
		Vector<WebSubServer> webSubServerVector = new Vector<WebSubServer>();
		Vector<SerialConnection> airServerVector = new Vector<SerialConnection>();

		SerialConnection serialConnection;
		WebSubServer webSubServer;

		while(serialEnum.hasMoreElements())
		{
			serialConnection = serialEnum.nextElement();
			if(!serialConnection.isOpen())
			{
				logHandler.info(serialConnection.getName() + " serialConnection: removed");
				serialConnectionVector.addElement(serialConnection);
				checkForwardIsClosed(serialConnection);
			}

		}

		removeSerialConnectioVector(serialConnectionVector);

		while(airServerEnum.hasMoreElements())
		{
			serialConnection = airServerEnum.nextElement();
			if(serialConnection.isSocketClosed())
			{
				logHandler.info(serialConnection.getSocketName() + " airSubServer: removed");
				airServerVector.addElement(serialConnection);
				checkForwardIsClosed(serialConnection);
			}

		}

		removeForwardSubServerVector(airServerVector);

		while(webServerEnum.hasMoreElements())
		{
			webSubServer = webServerEnum.nextElement();
			if(webSubServer.isClosed())
			{
				logHandler.info(webSubServer.getName() + " webSubServer: removed");
				webSubServerVector.addElement(webSubServer);
			}
		}

		removeWebSubServerVector(webSubServerVector);

	}

	private void checkForwardIsClosed(SerialConnection serialConnection)
	{
		if(serialConnection.isForwardServerIsOpen())
		{
			if(serialConnection.getForwardSocket() == null)
			{
				serialConnection.shutdown();
			}

			if(serialConnection.getForwardSocket() != null && serialConnection.getForwardSocket().isClosed())
			{
				serialConnection.shutdown();
			}
		}

	}

	public Vector<SerialConnection> getSerialConnectionList()
	{
		removeDeadConnections();
		return serialConnectionVec;
	}

	public Vector<WebSubServer> getWebSubServerList()
	{
		removeDeadConnections();
		return webSubServerVec;
	}

	private void removeWebSubServerVector(Vector<WebSubServer> webSubServerVector)
	{
		webSubServerVec.removeAll(webSubServerVector);

	}

	private void shutdownWebSubServer(WebSubServer webSubServer)
	{
		webSubServer.shutdown();
	}

	private void removeForwardSubServerVector(Vector<SerialConnection> airServerVector)
	{
		airServerVec.removeAll(airServerVector);
	}

	private void removeSerialConnectioVector(Vector<SerialConnection> serialConnectionVector)
	{
		serialConnectionVec.removeAll(serialConnectionVector);

	}

	public void closeSerialConnection(SerialConnection serialConnection)
	{
		serialConnection.closeConnection();

	}

	private void addWebSubServer(WebSubServer webSubServer)
	{
		webSubServerVec.addElement(webSubServer);

	}

	private void addAirSubServer(SerialConnection serialConnection)
	{
		airServerVec.addElement(serialConnection);

	}

	private void addSerialSubServer(SerialConnection serialConnection)
	{
		serialConnectionVec.addElement(serialConnection);

	}

	public Rsprocess getRsProcess()
	{
		return rsProcess;
	}

	public WebAirQueue getWebAirQueue()
	{
		return webAirQueue;
	}

	public void logMessage(String message)
	{
		logHandler.info(message);
	}

	public void exceptionMessage(String message)
	{
		exceptionHandler.putExceptionMessage(message);
	}

}

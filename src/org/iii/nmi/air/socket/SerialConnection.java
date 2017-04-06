package org.iii.nmi.air.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.CommPortOwnershipListener;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

import org.iii.nmi.air.forward.ForwardSocket;
import org.iii.nmi.air.rsprocess.Rsprocess;
import org.iii.nmi.air.util.RspSqlHandler;

public class SerialConnection implements SerialPortEventListener, CommPortOwnershipListener, Runnable
{
	private Socket socket;

	private BufferedReader reader;

	private OutputStream writer;

	private OutputStream os;

	private InputStream is;

	private ManagerSocket managerSocket;

	private boolean shutdownFlag;

	private Rsprocess rsProcess;

	private CommPortIdentifier portId;

	private SerialPort sPort;

	private boolean open;

	private ForwardSocket forwardSocket;

	private SerialParameters parameters;

	private String forwardServerIp;

	private String forwardServerPort;

	private boolean forwardServerIsOpen;

	StringBuffer inputBuffer = new StringBuffer();

	private LinkedList<String> commands = new LinkedList<String>();

	public SerialConnection(ManagerSocket managerSocket,
			SerialParameters parameters,
			String forwardServerIp,
			String forwardServerPort,
			boolean forwardServerIsOpen)
	{
		this.forwardServerIp = forwardServerIp;
		this.forwardServerPort = forwardServerPort;
		this.forwardServerIsOpen = forwardServerIsOpen;
		if(this.managerSocket == null)
		{
			this.managerSocket = managerSocket;
		}

		if(this.rsProcess == null)
		{
			this.rsProcess = managerSocket.getRsProcess();
		}

		if(this.parameters == null)
		{
			this.parameters = parameters;
		}

		new ProcessCommand().start();

		reConnectionForwardServer();
	}

	public SerialConnection(Socket accept,
			ManagerSocket managerSocket,
			SerialParameters serialParameters,
			String forwardServerIp,
			String forwardServerPort,
			boolean forwardServerIsOpen) throws IOException
	{
		this.socket = accept;
		this.forwardServerIp = forwardServerIp;
		this.forwardServerPort = forwardServerPort;
		this.forwardServerIsOpen = forwardServerIsOpen;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = socket.getOutputStream();

		if(this.managerSocket == null)
		{
			this.managerSocket = managerSocket;
		}

		if(this.rsProcess == null)
		{
			this.rsProcess = managerSocket.getRsProcess();
		}

		if(this.parameters == null)
		{
			this.parameters = serialParameters;
		}

		new ProcessCommand().start();

		reConnectionForwardServer();
	}

	private void reConnectionForwardServer()
	{
		if(isForward())
		{
			Thread thread = new Thread(forwardSocket);
			thread.setDaemon(true);
			thread.start();
		}
		else
		{
			managerSocket.logMessage("connection forward server fail");
		}

	}

	private boolean isForward()
	{
		if(forwardServerIsOpen)
		{
			try
			{
				this.forwardSocket = new ForwardSocket(forwardServerIp, Integer.parseInt(forwardServerPort));
			}
			catch(NumberFormatException e)
			{

				e.printStackTrace();
				managerSocket.exceptionMessage(e.getMessage());
				return false;
			}
			catch(UnknownHostException e)
			{

				e.printStackTrace();
				managerSocket.exceptionMessage(e.getMessage());
				return false;
			}
			catch(IOException e)
			{

				e.printStackTrace();
				managerSocket.exceptionMessage(e.getMessage());
				return false;
			}

			return true;
		}
		else
		{
			return false;
		}

	}

	public void clearAllCommands()
	{
		commands.clear();
	}

	public void setShutdownFlag(boolean shutdownFlag)
	{
		this.shutdownFlag = shutdownFlag;
	}

	public void shutdown()
	{
		setShutdownFlag(true);
		clearAllCommands();
		try
		{
			if(socket != null)
				socket.close();

		}
		catch(IOException e)
		{
			managerSocket.exceptionMessage("forward server " + forwardSocket.getName() + " :socket close IOException");
			e.printStackTrace();
		}
		try
		{
			if(reader != null)
				reader.close();
		}
		catch(IOException e)
		{
			managerSocket.exceptionMessage("forward server " + forwardSocket.getName() + ": " + "BufferedReader close IOException");
			e.printStackTrace();
		}
		try
		{
			if(writer != null)
				writer.close();
		}
		catch(IOException e)
		{
			managerSocket.exceptionMessage("forward server " + forwardSocket.getName() + ": " + "OutputStream close IOException");
			e.printStackTrace();
		}

		if(forwardSocket != null)
		{
			forwardSocket.shutdown();
			managerSocket.logMessage("forward server " + forwardSocket.getName() + ": " + "already closed");

		}

	}

	public void closeConnection()
	{
		if(!open)
		{
			return;
		}

		setShutdownFlag(true);
		clearAllCommands();
		if(sPort != null)
		{
			try
			{

				os.close();
				is.close();
			}
			catch(IOException e)
			{
				managerSocket.exceptionMessage(e.getMessage());
			}
			finally
			{
				sPort.close();
			}

			portId.removePortOwnershipListener(this);
		}

		open = false;
	}

	public boolean isOpen()
	{
		return open;
	}

	/**
	 * Send a one second break signal.
	 */
	public void sendBreak()
	{
		sPort.sendBreak(1000);
	}

	public boolean isForwardServerIsOpen()
	{
		return forwardServerIsOpen;
	}

	public ForwardSocket getForwardSocket()
	{
		return forwardSocket;
	}

	public void sendCommand(String command)
	{
		String[] datas = command.split(";");
		if(datas.length == 0 || datas[0].equals(""))
		{
			managerSocket.logMessage("send command is null");
			return;
		}

		try
		{
			byte[] bytes = new byte[datas.length];

			for(int i = 0; i < datas.length; i++)
			{

				bytes[i] = (byte) Integer.parseInt(datas[i], 16);
			}

			os.write(bytes);
			managerSocket.logMessage("send Command To AirConditioner " + getName() + " : " + command);

		}
		catch(IOException e)
		{

			managerSocket.exceptionMessage(e.getMessage());
		}

	}

	public void ownershipChange(int type)
	{
		/*
		 * if(type == CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED) {
		 * PortRequestedDialog prd = new PortRequestedDialog(parent); }
		 */
	}

	public void openConnection() throws SerialConnectionException
	{
		try
		{

			portId = CommPortIdentifier.getPortIdentifier(parameters.getPortName());
		}
		catch(NoSuchPortException e)
		{

			managerSocket.logMessage(e.getMessage());
			throw new SerialConnectionException(e.getMessage());
		}

		try
		{
			sPort = (SerialPort) portId.open("AirConditioner", 30000);
		}
		catch(PortInUseException e)
		{

			managerSocket.logMessage(e.getMessage());
			throw new SerialConnectionException(e.getMessage());
		}

		try
		{
			setConnectionParameters();
		}
		catch(SerialConnectionException e)
		{
			sPort.close();
			throw e;
		}
		try
		{
			os = sPort.getOutputStream();
			is = sPort.getInputStream();
		}
		catch(IOException e)
		{
			sPort.close();
			throw new SerialConnectionException("Error opening i/o streams");
		}

		// Set notifyOnDataAvailable to true to allow event driven input.
		sPort.notifyOnDataAvailable(true);

		// Set notifyOnBreakInterrup to allow event driven break handling.
		sPort.notifyOnBreakInterrupt(true);

		try
		{
			sPort.addEventListener(this);
		}
		catch(TooManyListenersException e)
		{
			sPort.close();
			throw new SerialConnectionException("too many listeners added");
		}

		try
		{
			sPort.enableReceiveTimeout(30);
		}
		catch(UnsupportedCommOperationException e)
		{
			managerSocket.exceptionMessage(e.getMessage());
		}

		portId.addPortOwnershipListener(this);

		open = true;

	}

	private void setConnectionParameters() throws SerialConnectionException
	{
		int oldBaudRate = sPort.getBaudRate();
		int oldDatabits = sPort.getDataBits();
		int oldStopbits = sPort.getStopBits();
		int oldParity = sPort.getParity();
		// int oldFlowControl = sPort.getFlowControlMode();
		try
		{
			sPort.setSerialPortParams(parameters.getBaudRate(), parameters.getDatabits(), parameters.getStopbits(), parameters.getParity());
		}
		catch(UnsupportedCommOperationException e)
		{
			parameters.setBaudRate(oldBaudRate);
			parameters.setDatabits(oldDatabits);
			parameters.setStopbits(oldStopbits);
			parameters.setParity(oldParity);
			throw new SerialConnectionException("Unsupported parameter");
		}

		try
		{
			sPort.setFlowControlMode(parameters.getFlowControlIn() | parameters.getFlowControlOut());
		}
		catch(UnsupportedCommOperationException e)
		{
			throw new SerialConnectionException("Unsupported flow control");
		}
	}

	public void serialEvent(SerialPortEvent e)
	{

		int newData = 0;
		managerSocket.logMessage("SerialPortEvent wake up");
		switch(e.getEventType())
		{
		case SerialPortEvent.DATA_AVAILABLE:
			while(newData != -1)
			{
				try
				{
					newData = is.read();
					if(newData == -1)
					{
						break;
					}
					if('\r' == (char) newData)
					{
						inputBuffer.append(";");
					}
					else
					{
						inputBuffer.append(Integer.toHexString(newData));
						inputBuffer.append(";");
					}
				}
				catch(IOException e1)
				{

					e1.printStackTrace();
					managerSocket.exceptionMessage(e1.getMessage());
				}
			}

			String command = inputBuffer.toString();
			managerSocket.logMessage("from " + getName() + " serialPort receive " + " message: " + command);
			inputBuffer.setLength(0);

			putCommand(command);

			if(forwardSocket != null)
			{
				try
				{
					String cmd = RspSqlHandler.getPowerId() + ";" + RspSqlHandler.getDataLocate() + ";" + command;
					forwardSocket.sendToMessage(cmd + "\n");
				}
				catch(IOException e1)
				{
					managerSocket.exceptionMessage("forward " + forwardSocket.getName() + " IOException");
				}

				managerSocket.logMessage("forward " + forwardSocket.getName() + " to server message: " + command);
			}

			break;

		case SerialPortEvent.BI:
			managerSocket.logMessage("serial port receive break at " + Calendar.getInstance().getTime());
		}

	}

	public String getName()
	{
		return sPort.getName();
	}

	public String getSocketName()
	{
		return socket.getRemoteSocketAddress().toString();
	}

	private void putCommand(String command)
	{
		synchronized(commands)
		{
			commands.addLast(command);
			commands.notify();
		}

	}

	private class ProcessCommand extends Thread
	{

		public void run()
		{
			while(true)
			{
				if(shutdownFlag)
					break;

				synchronized(commands)
				{
					if(commands.size() == 0)
					{
						try
						{
							commands.wait();
						}
						catch(InterruptedException e)
						{

							e.printStackTrace();
						}
					}

					rsProcess.pushCommand(commands.removeFirst());
				}
			}
		}
	}

	public void run()
	{
		String message = null;
		try
		{
			while((message = reader.readLine()) != null)
			{
				if(message.equals(""))
				{

				}
				else
				{

					putCommand(message);
					managerSocket.logMessage(" receive forward server " + " message: " + message);

				}

			}
		}
		catch(IOException e)
		{
			managerSocket.exceptionMessage("receive forward server " + "  already close connection IOException");

		}
		finally
		{

			shutdown();
		}

	}

	public boolean isSocketClosed()
	{

		return socket.isClosed();
	}

}

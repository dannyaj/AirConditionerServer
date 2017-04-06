package org.iii.nmi.air.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.iii.nmi.air.db.DBHandlerIn;
import org.iii.nmi.air.factory.InstanceFactory;
import org.iii.nmi.air.handler.HandlerPool;
import org.iii.nmi.air.log.ExceptionHandler;
import org.iii.nmi.air.log.LogHandler;
import org.iii.nmi.air.queue.WebAirQueue;
import org.iii.nmi.air.report.ReportData;
import org.iii.nmi.air.reqlist.RequestList;
import org.iii.nmi.air.rsprocess.Rsprocess;
import org.iii.nmi.air.socket.ManagerSocket;
import org.iii.nmi.air.socket.WebSubServer;

public class AirConditionerMain
{

	private int threadPoolProcessAirWebQueueSize;

	private int threadPoolProcessRsQueueSize;

	private int handlerPoolSize;

	private int beanPoolSize;

	private LogHandler logHandler;

	private ExceptionHandler exceptionHandler;

	private DBHandlerIn dbHandler;

	private WebAirQueue webAirQueue;

	private HandlerPool handlerPool;

	private Rsprocess rsProcess;

	private ManagerSocket managerSocketHandler;

	private boolean deadFlag;

	private boolean restartFlag;

	private boolean reportStopFlag = true;

	private static boolean socketAirPortIsOpen;

	private boolean socketAirWebPortIsOpen;

	private boolean comPortIsOpen;

	private static int count = 0;

	private ReportData reportData;

	public AirConditionerMain()
	{
		init();
	}

	private void init()
	{
		mkdirEngine();
		poolSizeConfiguration();

		this.logHandler = InstanceFactory.createLogHandler();
		this.exceptionHandler = InstanceFactory.createExceptionHandler();
		this.dbHandler = InstanceFactory.createDBhandler();
		this.webAirQueue = InstanceFactory.createWebAirQueue();
		this.handlerPool = InstanceFactory.createHandlerPool(handlerPoolSize);
		InstanceFactory.createBeanPool(beanPoolSize);
		InstanceFactory.createThreadPoolProcessAirWebQueue(threadPoolProcessAirWebQueueSize, webAirQueue, handlerPool);
		this.rsProcess = InstanceFactory.createRsprocess(threadPoolProcessRsQueueSize, handlerPool);

		startThread();
		new CheckCount().start();

		reportData = new ReportData(deadFlag, reportStopFlag);
		reportData.setDaemon(true);
		reportData.start();
		
		if(!socketAirPortIsOpen)
		{
			reportStart();
		}

		Managementconsole();
	}
	

	public static boolean isSocketAirPortIsOpen()
	{
		return socketAirPortIsOpen;
	}

	public static void addCount()
	{
		count++;
	}

	private void Managementconsole()
	{
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		String message;

		try
		{
			while((message = buf.readLine()) != null)
			{
				if(message.equalsIgnoreCase("listW"))
				{

					Vector<WebSubServer> vec = managerSocketHandler.getWebSubServerList();
					Enumeration<WebSubServer> enums = vec.elements();
					int i = 1;
					while(enums.hasMoreElements())
					{
						logHandler.info(i + ":" + enums.nextElement().getName() + " Web");
						i++;
					}
				}
				else if(message.equalsIgnoreCase("listId"))
				{

					Properties properties = initProperties();
					String masterIpPowerId = properties.getProperty("MasterIpPowerId");
					String[] masterIpPowerIds = masterIpPowerId.split(";");

					int z = 1;
					for(int i = 0; i < masterIpPowerIds.length; i++)
					{
						String[] ids = masterIpPowerIds[i].split(",");
						logHandler.info(z + ":" + "MasterIp: " + ids[i]);

						for(int j = 1; j < ids.length; j++)
						{
							logHandler.info(z + ":" + "powerId: " + ids[j]);

						}

						z++;

					}

				}
				else if(message.equalsIgnoreCase("listL"))
				{

					RequestList reqList = InstanceFactory.createRequestList();
					int size = reqList.getRequestCommandSize();
					int k = 1;
					for(int i = 0; i < size; i++)
					{
						String requestCmd = reqList.get(i);
						logHandler.info(k + ":" + "Request Cmd: " + requestCmd);
						k++;

					}

				}
				else if(message.equalsIgnoreCase("shutdown"))
				{
					managerSocketHandler.shutdown();
					deadFlag = true;
					restartFlag = true;
					reportStopFlag = true;
					reportData.setDeadFlag(deadFlag);
					reportData.setReportStopFlag(reportStopFlag);
					logHandler.info("Air Conditioner Server already shutdown ");
				}
				else if(message.equalsIgnoreCase("restart"))
				{
					if(restartFlag)
					{
						logHandler.info("Air Conditioner Server restart finish ");
						startThread();
						restartFlag = false;
						deadFlag = false;

					}
					else
					{
						logHandler.info("Air Conditioner Server already start ");
					}
				}
				else if(message.equalsIgnoreCase("shutdowndb"))
				{

					dbHandler.disconnectDb();
					logHandler.info("Air Conditioner Server connection DB shutdown ");
				}
				else if(message.equalsIgnoreCase("restartdb"))
				{

					dbHandler.resetConnectionDB();
					logHandler.info("Air Conditioner Server connection DB already restart ");
				}
				else if(message.equalsIgnoreCase("report"))
				{

					reportStart();

				}
				else if(message.equalsIgnoreCase("reportS"))
				{

					reportStop();

				}
			}
		}
		catch(IOException e)
		{

			e.printStackTrace();
		}

	}

	private void reportStop()
	{
		reportStopFlag = true;
		reportData.setReportStopFlag(reportStopFlag);
		logHandler.info("report already stop");
	}

	private void reportStart()
	{
		if(!reportStopFlag)
		{
			logHandler.info("report already start");
			return;
		}

		Properties properties = initProperties();
		reportData.setProperties(properties);
		reportData.setDeadFlag(deadFlag);
		reportStopFlag = false;
		reportData.setReportStopFlag(reportStopFlag);
	}

	private Properties initProperties()
	{
		Properties properties = new Properties();
		FileInputStream is = null;
		try
		{
			is = new FileInputStream(new File("conf/air.properties"));

			properties.load(is);

		}
		catch(FileNotFoundException e)
		{

			e.printStackTrace();
		}
		catch(IOException e)
		{

			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch(IOException e)
			{

				e.printStackTrace();
			}
		}
		return properties;
	}

	private void startThread()
	{

		Properties properties = initSocketProperties();
		this.socketAirPortIsOpen = Boolean.parseBoolean(properties.getProperty("socketAirPortIsOpen"));
		this.socketAirWebPortIsOpen = Boolean.parseBoolean(properties.getProperty("socketAirWebPortIsOpen"));
		this.comPortIsOpen = Boolean.parseBoolean(properties.getProperty("comPortIsOpen"));

		managerSocketHandler = InstanceFactory.createManagerSocketHandler(webAirQueue, rsProcess);

		Thread thread;

		if(socketAirWebPortIsOpen)
		{
			thread = new Thread(new Runnable()
			{

				public void run()
				{
					logHandler.info("AirConditioner Server start waiting for Web connection");
					try
					{
						while(true)
						{

							managerSocketHandler.waitForWebClient();

						}

					}
					catch(IOException e)
					{
						exceptionHandler.putExceptionMessage("waitForWebClient IOException");

					}

				}

			});
			thread.setDaemon(true);
			thread.start();
		}

		if(socketAirPortIsOpen)
		{
			thread = new Thread(new Runnable()
			{

				public void run()
				{
					logHandler.info("AirConditioner Server start waiting for forward connection");
					try
					{
						while(true)
						{

							managerSocketHandler.waitForForward();

						}

					}
					catch(IOException e)
					{
						exceptionHandler.putExceptionMessage("waitForForward IOException");

					}

				}

			});
			thread.setDaemon(true);
			thread.start();
		}

		if(comPortIsOpen)
		{
			logHandler.info("AirConditioner Server start waiting for Com Portconnection");
			try
			{

				managerSocketHandler.openComPortConnection();
			}
			catch(IOException e)
			{
				exceptionHandler.putExceptionMessage("openComPort Connection IOException");
			}
		}
		thread = new Thread(new Runnable()
		{

			public void run()
			{
				while(true)
				{
					if(!deadFlag)
					{
						try
						{
							Thread.sleep(3 * 1000);
							managerSocketHandler.removeDeadConnections();
						}
						catch(InterruptedException e)
						{

							e.printStackTrace();
						}
					}
					else
					{
						deadFlag = false;
						break;
					}
				}
			}

		});
		thread.setDaemon(true);
		thread.start();

	}

	private Properties initSocketProperties()
	{
		Properties properties = new Properties();
		FileInputStream is = null;
		try
		{
			is = new FileInputStream(new File("conf/socket.properties"));

			properties.load(is);

		}
		catch(FileNotFoundException e)
		{

			e.printStackTrace();
		}
		catch(IOException e)
		{

			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch(IOException e)
			{

				e.printStackTrace();
			}
		}
		return properties;

	}

	private void poolSizeConfiguration()
	{
		Properties properties = new Properties();
		FileInputStream is = null;
		try
		{
			is = new FileInputStream(new File("conf/socket.properties"));

			properties.load(is);

			this.threadPoolProcessAirWebQueueSize = Integer.parseInt(properties.getProperty("ThreadPoolProcessAirWebQueueSize"));
			this.handlerPoolSize = Integer.parseInt(properties.getProperty("HandlerPoolSize"));
			this.threadPoolProcessRsQueueSize = Integer.parseInt(properties.getProperty("ThreadPoolProcessRsQueueSize"));
			this.beanPoolSize = Integer.parseInt(properties.getProperty("BeanPoolSize"));
		}
		catch(FileNotFoundException e)
		{

			e.printStackTrace();
		}
		catch(IOException e)
		{

			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch(IOException e)
			{

				e.printStackTrace();
			}
		}

	}

	private void mkdirEngine()
	{
		File logFile = new File("Log");
		logFile.mkdir();

		logFile = new File("ExceptionLog");
		logFile.mkdir();
		logFile = null;

	}

	private class CheckCount extends Thread
	{
		public void run()
		{

			while(true)
			{

				try
				{

					Thread.sleep(3 * 1000);

					if(count >= 5)
					{
						logHandler.info("count >=5 so restart Server");
						managerSocketHandler.shutdown();
						deadFlag = true;
						restartFlag = true;
						reportStopFlag = true;
						reportData.setDeadFlag(deadFlag);
						reportData.setReportStopFlag(reportStopFlag);
						logHandler.info("Air Conditioner Server already shutdown ");
						Thread.sleep(3 * 1000);

						if(restartFlag)
						{
							logHandler.info("Air Conditioner Server restart finish ");
							startThread();
							restartFlag = false;
							deadFlag = false;
							reportStopFlag = false;
							reportData.setDeadFlag(deadFlag);
							reportData.setReportStopFlag(reportStopFlag);
						}
						else
						{
							logHandler.info("Air Conditioner Server already start ");
						}

						count = 0;

					}

				}
				catch(InterruptedException e)
				{

					e.printStackTrace();
				}

			}
		}
	}

	public static void main(String[] args)
	{
		new AirConditionerMain();
		

	}

}

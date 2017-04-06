package org.iii.nmi.air.factory;

import org.iii.nmi.air.dao.BeanPool;
import org.iii.nmi.air.db.DBHandlerImpl;
import org.iii.nmi.air.db.DBHandlerIn;
import org.iii.nmi.air.handler.HandlerPool;
import org.iii.nmi.air.log.ExceptionHandler;
import org.iii.nmi.air.log.LogHandler;
import org.iii.nmi.air.queue.ThreadPoolProcessAirWebQueue;
import org.iii.nmi.air.queue.WebAirQueue;
import org.iii.nmi.air.reqlist.RequestList;
import org.iii.nmi.air.rsprocess.Rsprocess;
import org.iii.nmi.air.sendcmd.SendCommand;
import org.iii.nmi.air.socket.ManagerSocket;
import org.iii.nmi.air.socket.SerialConnection;

public class InstanceFactory
{
	private static LogHandler logHandler = null;

	private static ExceptionHandler exceptionHandler = null;

	private static RequestList reqList = null;

	public static LogHandler createLogHandler()
	{
		if(logHandler == null)
		{
			synchronized(InstanceFactory.class)
			{
				if(logHandler == null)
					logHandler = new LogHandler();
			}
		}
		return logHandler;
	}

	public static ExceptionHandler createExceptionHandler()
	{
		if(exceptionHandler == null)
		{
			synchronized(InstanceFactory.class)
			{
				if(exceptionHandler == null)
					exceptionHandler = new ExceptionHandler();
			}
		}
		return exceptionHandler;
	}

	public static DBHandlerIn createDBhandler()
	{
		return new DBHandlerImpl();
	}

	public static WebAirQueue createWebAirQueue()
	{
		return new WebAirQueue();
	}

	public static HandlerPool createHandlerPool(int size)
	{
		return new HandlerPool(size);
	}

	public static BeanPool createBeanPool(int size)
	{
		return new BeanPool(size);
	}

	public static ThreadPoolProcessAirWebQueue createThreadPoolProcessAirWebQueue(int size, WebAirQueue webQueue, HandlerPool handlerPool)
	{
		return new ThreadPoolProcessAirWebQueue(size, webQueue, handlerPool);
	}

	public static RequestList createRequestList()
	{
		if(reqList == null)
		{
			synchronized(InstanceFactory.class)
			{
				if(reqList == null)
					reqList = new RequestList();
			}
		}
		return reqList;
	}

	public static Rsprocess createRsprocess(int size, HandlerPool handlerPool)
	{
		return new Rsprocess(size, handlerPool);
	}

	public static SendCommand createSendCommandThread(Rsprocess rsProcess, SerialConnection serialConnection)
	{
		return new SendCommand(rsProcess, serialConnection);
	}

	public static ManagerSocket createManagerSocketHandler(WebAirQueue webAirQueue, Rsprocess rsProcess)
	{
		return new ManagerSocket(webAirQueue, rsProcess);
	}
}

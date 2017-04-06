package org.iii.nmi.air.queue;

import org.iii.nmi.air.factory.InstanceFactory;
import org.iii.nmi.air.handler.Handler;
import org.iii.nmi.air.handler.HandlerPool;
import org.iii.nmi.air.log.LogHandler;

public class ThreadPoolProcessAirWebQueue
{
	private WebAirQueue webQueue;

	private LogHandler logHandler;

	private HandlerPool handlerPool;

	public ThreadPoolProcessAirWebQueue(int size,
			WebAirQueue webQueue,
			HandlerPool handlerPool)
	{
		this.webQueue = webQueue;
		this.logHandler = InstanceFactory.createLogHandler();
		this.handlerPool = handlerPool;

		for(int i = 0; i < size; i++)
		{
			new Thread(new ThreadProcessAirWebQueue()).start();
		}
	}

	private class ThreadProcessAirWebQueue implements Runnable
	{

		public void run()
		{
			while(true)
			{
				String command = webQueue.getCommand();
				if(command != null)
					parseCommand(command);

			}

		}

		private void parseCommand(String command)
		{
			if(command == null)
			{
				logHandler.info("WebubServer receive web command is null");

				return;
			}

			Handler handler = handlerPool.getRequestHandler();
			handler.processReqCommand(command);
			handlerPool.releaseRequestHandler(handler);

		}

	}
}

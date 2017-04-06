package org.iii.nmi.air.rsprocess;

import java.util.LinkedList;

import org.iii.nmi.air.handler.Handler;
import org.iii.nmi.air.handler.HandlerPool;

public class Rsprocess
{
	private LinkedList<String> commands = new LinkedList<String>();

	private HandlerPool handlerPool;

	public Rsprocess(int size, HandlerPool handlerPool)
	{
		this.handlerPool = handlerPool;

		for(int i = 0; i < size; i++)
		{
			new ProcessRsCommand().start();
		}
		

	}

	public synchronized int getCommandSize()
	{
		return commands.size();
	}

	public void clearAllCommands()
	{
		commands.clear();
	}

	public void pushCommand(String command)
	{
		synchronized(commands)
		{
			commands.addLast(command);
			commands.notifyAll();
		}

	}

	private class ProcessRsCommand extends Thread
	{

		private void parseCommand(String command)
		{
			Handler handler = handlerPool.getResponseHandler();
			handler.processRspCommand(command);
			handlerPool.releaseResponseHandler(handler);

		}

		public void run()
		{
			while(true)
			{

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

					if(commands.size() != 0)
						parseCommand(commands.removeFirst());

				}
			}
		}
	}

}

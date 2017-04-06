package org.iii.nmi.air.queue;

import java.util.LinkedList;

public class WebAirQueue
{
	private LinkedList<String> webCommands = new LinkedList<String>();

	public void clearAllCommands()
	{
		webCommands.clear();
	}

	public void putCommand(String command)
	{
		synchronized(webCommands)
		{
			webCommands.addLast(command);
			webCommands.notifyAll();
		}
	}

	public String getCommand()
	{
		synchronized(webCommands)
		{
			while(webCommands.size() == 0)
			{
				try
				{
					webCommands.wait();
				}
				catch(InterruptedException e)
				{

					e.printStackTrace();
				}
			}

			if(webCommands.size() != 0)
				return (String) webCommands.removeFirst();
			else
				return null;

		}
	}
}

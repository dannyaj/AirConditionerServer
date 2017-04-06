package org.iii.nmi.air.test.web;

import java.util.LinkedList;

public class TaskImpl implements TaskInf
{
	private LinkedList<String> queue;

	public TaskImpl()
	{
		queue = new LinkedList<String>();
	}

	public void putTask(String message)
	{
		synchronized(queue)
		{
			queue.addLast(message);
			queue.notifyAll();
		}
	}

	public String getTask()
	{
		synchronized(queue)
		{
			while(queue.size() == 0)
			{
				try
				{
					queue.wait();
				}
				catch(InterruptedException e)
				{
					
					e.printStackTrace();
				}

			}
			return (String) queue.removeFirst();
		}

		

	}

}

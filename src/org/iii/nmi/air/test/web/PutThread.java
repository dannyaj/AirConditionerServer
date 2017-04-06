package org.iii.nmi.air.test.web;

import java.util.LinkedList;
import java.util.Vector;

public class PutThread extends Thread
{

	private TaskInf task;

	private LinkedList linkedList;

	public PutThread(TaskInf task, LinkedList linkedList)
	{
		this.task = task;
		this.linkedList = linkedList;

	}

	public void run()
	{
		while(true)
		{

			try
			{
				while(linkedList.size() != 0)
				{
					task.putTask((String) linkedList.removeFirst());
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				Thread.sleep(50);
			}
			catch(InterruptedException e)
			{
				// TODO �۰ʲ��� catch �϶�
				e.printStackTrace();
			}
		}
	}
}

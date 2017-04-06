package org.iii.nmi.air.test.web;

import java.util.Vector;

public class GetThread extends Thread
{
	private TaskInf task;

	SocketImpl socktCon = null;

	public GetThread(TaskInf task, String ip, int port)
	{
		this.task = task;

		socktCon = new SocketImpl(ip, port);
		Thread thread = new Thread(socktCon);
		thread.setDaemon(true);
		thread.start();
	}

	public void run()
	{
		while(true)
		{
			String message = task.getTask();

			if(message != null)
			{

				socktCon.sendToMessage(message);
			}
		}
	}
}

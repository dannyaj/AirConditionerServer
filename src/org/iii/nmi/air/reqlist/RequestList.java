package org.iii.nmi.air.reqlist;

import java.util.LinkedList;

public class RequestList
{

	private LinkedList<String> cmdList = new LinkedList<String>();

	public synchronized void add(String reqCommand)
	{

		cmdList.addLast(reqCommand);
		// notifyAll();

	}

	public synchronized String getCommand()
	{

		/*
		 * while(cmdList.size() == 0) { try {
		 * 
		 * wait(); } catch(InterruptedException e) {
		 * 
		 * e.printStackTrace(); } }
		 */

		if(cmdList.size() != 0)
			return cmdList.removeFirst();
		else
			return null;

	}

	public synchronized void clearAll()
	{

		cmdList.clear();
	}

	public synchronized int getRequestCommandSize()
	{
		return cmdList.size();
	}

	public synchronized String get(int i)
	{
		return cmdList.get(i);
	}

}

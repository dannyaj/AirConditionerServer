package org.iii.nmi.air.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.iii.nmi.air.queue.WebAirQueue;

public class WebSubServer implements Runnable
{

	private Socket socket;

	private BufferedReader reader;

	private OutputStream writer;

	private ManagerSocket managerServer;

	private WebAirQueue webAirQueue;

	public WebSubServer(Socket socket, ManagerSocket managerServer)
		throws IOException
	{
		this.socket = socket;
		this.managerServer = managerServer;
		this.webAirQueue = managerServer.getWebAirQueue();
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = socket.getOutputStream();
	}

	public boolean isClosed()
	{
		return socket.isClosed();
	}

	public String getName()
	{
		return socket.getRemoteSocketAddress().toString();
	}

	public void shutdown()
	{
		try
		{
			socket.close();

			managerServer.logMessage(getName() + " webSubServer" + ": " + "already closed");
		}
		catch(IOException e)
		{
			managerServer.exceptionMessage(getName() + " webSubServer" + ": " + "socket close IOException");
			e.printStackTrace();
		}
		try
		{
			reader.close();
		}
		catch(IOException e)
		{
			managerServer.exceptionMessage(getName() + " webSubServer" + ": " + "BufferedReader close IOException");
			e.printStackTrace();
		}
		try
		{
			writer.close();
		}
		catch(IOException e)
		{
			managerServer.exceptionMessage(getName() + " webSubServer" + ": " + "OutputStream close IOException");
			e.printStackTrace();
		}
	}

	public void run()
	{
		String message;
		try
		{
			while((message = reader.readLine()) != null)
			{
				webAirQueue.putCommand(message);
				managerServer.logMessage(getName() + " WebSubServer receive Web message: " + message);
			}
		}
		catch(IOException e)
		{
			managerServer.exceptionMessage(getName() + " WebSubServer  already close connection IOException");

		}
		finally
		{

			shutdown();
		}

	}

}

package org.iii.nmi.air.forward;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.iii.nmi.air.factory.InstanceFactory;
import org.iii.nmi.air.log.LogHandler;

public class ForwardSocket implements Runnable
{
	private Socket socket;

	private BufferedReader clientReader;

	private OutputStream serverWriter;

	private LogHandler logHandler;

	public ForwardSocket(String ip, int port)
		throws UnknownHostException,
			IOException
	{
		this.logHandler = InstanceFactory.createLogHandler();
		initSocket(ip, port);

	}

	private void initSocket(String ip, int port)
		throws UnknownHostException, IOException
	{
		this.socket = new Socket(ip, port);
		this.clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.serverWriter = socket.getOutputStream();

		logHandler.info("forward " + getName() + " server connection ok");

	}

	public void run()
	{
		String message = null;
		try
		{
			while((message = clientReader.readLine()) != null)
			{
				logHandler.info("receive from forward server message: " + message);
			}
		}
		catch(IOException e)
		{

			logHandler.info(e.getMessage());

		}
		finally
		{
			shutdown();
		}
	}

	public void shutdown()
	{
		try
		{
			socket.close();
		}
		catch(IOException e)
		{

			logHandler.info(getName() + " socket close IOException");
		}

		try
		{
			clientReader.close();
		}
		catch(IOException e)
		{

			logHandler.info(getName() + " socket close IOException");
		}

		try
		{
			serverWriter.close();
		}
		catch(IOException e)
		{

			logHandler.info(getName() + " socket close IOException");
		}

	}

	public void sendToMessage(String message) throws IOException
	{

		if(message != null)
		{
			serverWriter.write((message + "\n").getBytes());

		}

	}

	public String getName()
	{
		return socket.getRemoteSocketAddress().toString();
	}

	public Socket getSocket()
	{
		return socket;
	}

	public boolean isClosed()
	{
		return socket.isClosed();
	}

}

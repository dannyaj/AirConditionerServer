package org.iii.nmi.air.test.web;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class SocketImpl implements SocketInf, Runnable
{

	private Socket socket;

	private BufferedReader clientReader;

	OutputStream serverWriter;

	public SocketImpl(String ip, int port)
	{

		initSocket(ip, port);

	}

	private void initSocket(String ip, int port)
	{
		try
		{
			socket = new Socket(ip, port);
			clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			serverWriter = socket.getOutputStream();

			System.out.println("connection server ok....");

		}
		catch(UnknownHostException e)
		{

			e.printStackTrace();
		}
		catch(IOException e)
		{

		}

	}

	public void sendToMessage(String message)
	{
		try
		{

			if(message != null)
			{
				serverWriter.write((message + "\n").getBytes());
				System.out.println("EMS send command to NServer " + message);
			}
		}
		catch(FileNotFoundException e)
		{

			e.printStackTrace();
		}
		catch(IOException e)
		{

			e.printStackTrace();
		}

	}

	private void closed()
	{
		try
		{
			socket.close();
			clientReader.close();
			serverWriter.close();
		}
		catch(IOException e)
		{

		}
	}

	public void run()
	{
		String message;
		try
		{
			while((message = clientReader.readLine()) != null)
			{
				System.out.println("receive from server: " + message);

				// Thread.sleep(50);
				// closed();
			}
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

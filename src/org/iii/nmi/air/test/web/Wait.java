package org.iii.nmi.air.test.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Wait
{
	public static void main(String[] args)
	{
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{

				int i = 1;
				try
				{
					ServerSocket s = new ServerSocket(8189);

					for(;;)
					{
						Socket incoming = s.accept();
						System.out.println("ID : " + i + ", Connection Started");

						i++;
					}
				}
				catch(Exception e)
				{
					System.out.println("Exception in line 19 : " + e);
				}

			}
		});
		try
		{
			t.join();
		}
		catch(InterruptedException e)
		{
			
			e.printStackTrace();
		}
		t.start();

		System.out.println("End of main");
	}
}

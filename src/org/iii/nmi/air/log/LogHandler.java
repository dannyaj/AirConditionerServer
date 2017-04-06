package org.iii.nmi.air.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class LogHandler
{

	private Logger logger = Logger.getLogger(LogHandler.class);

	public LogHandler()
	{
		init();
	}

	private void init()
	{
		Properties properties = new Properties();
		FileInputStream is = null;
		try
		{
			is = new FileInputStream(new File("conf/sys.properties"));

			properties.load(is);
			

			PropertyConfigurator.configure(properties);
		}
		catch(FileNotFoundException e)
		{

			e.printStackTrace();
		}
		catch(IOException e)
		{

			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch(IOException e)
			{
				
				e.printStackTrace();
			}
		}
	}

	public void info(String message)
	{
		logger.info(message);

	}
}

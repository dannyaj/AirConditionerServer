package org.iii.nmi.air.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class ExceptionHandler
{
	public synchronized void  putExceptionMessage(String message)
	{
		Date date = new Date();
		Locale locale = new Locale("en", "US");

		DateFormat mediumFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		String targetDir = "ExceptionLog/" + mediumFormat.format(date).toString();
		File f = new File(targetDir);
		f.mkdir();
		String filepath = targetDir + File.separator + "Exception.txt";
		f = new File(filepath);

		try
		{
			FileOutputStream os = new FileOutputStream(f, true);
			os.write((date.toString() + " : " + message + "\n").getBytes());

			os.flush();
			os.close();
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
}

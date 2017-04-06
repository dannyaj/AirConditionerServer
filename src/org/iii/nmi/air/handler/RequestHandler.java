package org.iii.nmi.air.handler;

import java.sql.Timestamp;
import java.util.Date;

import org.iii.nmi.air.dao.Aircondprofile;
import org.iii.nmi.air.dao.BeanPool;
import org.iii.nmi.air.dao.SqlHandler;
import org.iii.nmi.air.dao.SqlHandlerException;
import org.iii.nmi.air.dao.Webaircondlog;

public class RequestHandler extends Handler
{

	private final int len = 2;

	@Override
	public void processReqCommand(String reqCmd)
	{
		String[] cmd = reqCmd.split(";");
		if(cmd.length != 8)
		{
			logHandler.info("WebubServer receive web command length is error: " + reqCmd);

			return;
		}

		if(cmd[0].length() != len || cmd[1].length() != len || cmd[2].length() != len || cmd[3].length() != len || cmd[4].length() != len || cmd[5].length() != len || cmd[6].length() != len)
		{
			logHandler.info("WebubServer receive web command length is error: " + reqCmd);

			return;
		}

		Webaircondlog webaircondlog = BeanPool.getWebaircondlog();
		webaircondlog.setWebcmdpackage(reqCmd);
		Timestamp timeStamp = new Timestamp(new Date().getTime());
		webaircondlog.setWebcmdtime(timeStamp);
		webaircondlog.setUpdatetime(timeStamp);

		try
		{
			SqlHandler.insertWebAirCondLog(webaircondlog);

		}
		catch(SqlHandlerException e)
		{

			e.printStackTrace();
			exceptionHandler.putExceptionMessage(e.getMessage());

		}
		finally
		{
			webaircondlog.reset();
			BeanPool.releaseWebaircondlog(webaircondlog);
		}
		/*Aircondprofile aircondprofile = BeanPool.getAircondprofile();
		aircondprofile.setMasterip(cmd[0]);
		aircondprofile.setAircondid(cmd[2]);
		aircondprofile.setUpdatetime(timeStamp);
		try
		{
			if(!SqlHandler.selectAirCondProfileAirCondId(aircondprofile, tableName))
			{
				SqlHandler.insertAirCondProfileAirCondId(aircondprofile);
			}
			else
			{
				SqlHandler.updateAirCondProfileMasterIp(aircondprofile);
			}
		}
		catch(SqlHandlerException e)
		{
			e.printStackTrace();
			exceptionHandler.putExceptionMessage(e.getMessage());
		}
		finally
		{
			aircondprofile.reset();
			BeanPool.releaseAircondprofile(aircondprofile);
		}*/

		reqList.add(reqCmd);
	}

	@Override
	public void processRspCommand(String RspCmd)
	{
		// TODO Auto-generated method stub

	}

}

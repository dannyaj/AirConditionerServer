package org.iii.nmi.air.handler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.iii.nmi.air.dao.Aircondprofile;
import org.iii.nmi.air.dao.BeanPool;
import org.iii.nmi.air.dao.SqlHandler;
import org.iii.nmi.air.dao.SqlHandlerException;
import org.iii.nmi.air.dao.WrapperSqlHandler;
import org.iii.nmi.air.main.AirConditionerMain;
import org.iii.nmi.air.util.RspSqlHandler;

public class ResponseHandler extends Handler
{

	@Override
	public void processReqCommand(String reqCmd)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void processRspCommand(String rspCmd)
	{

		if(rspCmd == null)
			return;
		try
		{
			String[] cmds = rspCmd.split(";");

			if(AirConditionerMain.isSocketAirPortIsOpen())
			{
				
				String powerId = cmds[0];
				RspSqlHandler.setPowerId(powerId);
				int dataLocate = Integer.parseInt(cmds[1]);
				RspSqlHandler.setDataLocate(dataLocate);
				
				String[] commands = new String[cmds.length - 2];
				
				System.arraycopy(cmds, 2, commands, 0, cmds.length -2);
				
				processCommand(rspCmd, commands);
			}
			else
			{
				processCommand(rspCmd, cmds);
			}

		}
		finally
		{
			RspSqlHandler.clear();
		}

	}

	private void processCommand(String rspCmd, String[] cmds)
	{
		if(cmds[1] == null || cmds[1].equals(""))
		{
			exceptionHandler.putExceptionMessage("Response Command is error : " + rspCmd);
			logHandler.info("Response Command is error : " + rspCmd);
			RspSqlHandler.clear();
			AirConditionerMain.addCount();
			logHandler.info("count + 1");

			return;
		}

		if(!cmds[1].equals(readByte) && !cmds[1].equals(writeByte))
		{
			exceptionHandler.putExceptionMessage("Response Command is error : " + rspCmd);
			logHandler.info("Response Command is error : " + rspCmd);
			RspSqlHandler.clear();
			AirConditionerMain.addCount();
			logHandler.info("count + 1");
			return;
		}
		if(cmds[1].equals(readError) || cmds[1].equals(writeError))
		{
			logHandler.info("83H readError OR 86H writeError");
			RspSqlHandler.clear();
			AirConditionerMain.addCount();
			logHandler.info("count + 1");
			return;
		}

		if(cmds[1].equals(writeByte))
		{
			processWrite(cmds);
		}
		else if(cmds[1].equals(readByte))
		{

			processRead(cmds);
		}
		else
		{

			exceptionHandler.putExceptionMessage("Response Command is error : " + rspCmd);
			logHandler.info("Response Command is error : " + rspCmd);
			RspSqlHandler.clear();
			AirConditionerMain.addCount();
			logHandler.info("count + 1");

		}

	}

	private void processRead(String[] cmds)
	{

		int locate = RspSqlHandler.getDataLocate();
		String powerId = RspSqlHandler.getPowerId();
		int idx = locate - RspSqlHandler.getCmdIdx();
		if(idx < 0)
			return;
		try
		{
			if(cmds[2].equals(allByteHex))
			{

				Aircondprofile aircondprofile = null;
				try
				{
					aircondprofile = BeanPool.getAircondprofile();
					aircondprofile.setMasterip(cmds[0]);
					aircondprofile.setUpdatetime(new Timestamp(new Date().getTime()));
					aircondprofile.setAircondid(powerId);
					aircondprofile.setPowerstatus(new BigDecimal(Integer.parseInt(cmds[4])));
					aircondprofile.setLockstatus(new BigDecimal(Integer.parseInt(cmds[6])));
					aircondprofile.setSetmode(new BigDecimal(Integer.parseInt(cmds[8])));
					aircondprofile.setSetfanspeed(new BigDecimal(Integer.parseInt(cmds[10])));
					aircondprofile.setAlarmstatus(new BigDecimal(Integer.parseInt(cmds[12])));
					aircondprofile.setValvecontact(new BigDecimal(Integer.parseInt(cmds[14])));
					aircondprofile.setFanspeedcontact(new BigDecimal(Integer.parseInt(cmds[16])));
					aircondprofile.setChillcontact(new BigDecimal(Integer.parseInt(cmds[18])));
					aircondprofile.setHeatingcontact(new BigDecimal(Integer.parseInt(cmds[20])));
					aircondprofile.setRoomtemp(new BigDecimal(Integer.parseInt(cmds[24], 16) / 2));
					aircondprofile.setPmv(new BigDecimal(PMV_Calc((Integer.parseInt(cmds[24], 16) / 2), hm)));
					aircondprofile.setSetpoint(new BigDecimal(Integer.parseInt(cmds[26], 16) / 2));

					if(SqlHandler.selectAirCondProfileAirCondId(aircondprofile, tableName))
					{

						SqlHandler.updateAirCondProfileAll(aircondprofile);

						SqlHandler.insertAirCondInfo(aircondprofile);

					}
					else
					{

						SqlHandler.insertAirCondProfile(aircondprofile);
						SqlHandler.insertAirCondInfo(aircondprofile);
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
					RspSqlHandler.clear();

				}
			}
			else
			{

				for(int i = 4; i < (cmds.length - 2); i++)
				{
					try
					{
						processDb(idx, cmds[i], powerId);
					}
					catch(SqlHandlerException e)
					{

						e.printStackTrace();
						exceptionHandler.putExceptionMessage(e.getMessage());
					}

					i++;
					idx++;
				}
			}
		}
		finally
		{
			RspSqlHandler.clear();
		}

	}

	private void processDb(int idx, String data, String powerId)
		throws SqlHandlerException
	{
		Aircondprofile aircondprofile = null;
		try
		{
			aircondprofile = BeanPool.getAircondprofile();
			aircondprofile.setUpdatetime(new Timestamp(new Date().getTime()));
			aircondprofile.setAircondid(powerId);
			if(idx == 0)
			{
				aircondprofile.setPowerstatus(new BigDecimal(Integer.parseInt(data)));

				WrapperSqlHandler sqlHandler = RspSqlHandler.get(idx);
				sqlHandler.updateAirCondProfile(aircondprofile);
			}
			else if(idx == 1)
			{
				aircondprofile.setLockstatus(new BigDecimal(Integer.parseInt(data)));
				WrapperSqlHandler sqlHandler = RspSqlHandler.get(idx);
				sqlHandler.updateAirCondProfile(aircondprofile);
			}
			else if(idx == 2)
			{
				aircondprofile.setSetmode(new BigDecimal(Integer.parseInt(data)));
				WrapperSqlHandler sqlHandler = RspSqlHandler.get(idx);
				sqlHandler.updateAirCondProfile(aircondprofile);
			}
			else if(idx == 3)
			{
				aircondprofile.setSetfanspeed(new BigDecimal(Integer.parseInt(data)));
				WrapperSqlHandler sqlHandler = RspSqlHandler.get(idx);
				sqlHandler.updateAirCondProfile(aircondprofile);
			}
			else if(idx == 4)
			{
				aircondprofile.setAlarmstatus(new BigDecimal(Integer.parseInt(data)));
				WrapperSqlHandler sqlHandler = RspSqlHandler.get(idx);
				sqlHandler.updateAirCondProfile(aircondprofile);
			}
			else if(idx == 5)
			{
				aircondprofile.setValvecontact(new BigDecimal(Integer.parseInt(data)));
				WrapperSqlHandler sqlHandler = RspSqlHandler.get(idx);
				sqlHandler.updateAirCondProfile(aircondprofile);
			}
			else if(idx == 6)
			{
				aircondprofile.setFanspeedcontact(new BigDecimal(Integer.parseInt(data)));
				WrapperSqlHandler sqlHandler = RspSqlHandler.get(idx);
				sqlHandler.updateAirCondProfile(aircondprofile);
			}
			else if(idx == 7)
			{
				aircondprofile.setChillcontact(new BigDecimal(Integer.parseInt(data)));
				WrapperSqlHandler sqlHandler = RspSqlHandler.get(idx);
				sqlHandler.updateAirCondProfile(aircondprofile);
			}
			else if(idx == 8)
			{
				aircondprofile.setHeatingcontact(new BigDecimal(Integer.parseInt(data)));
				WrapperSqlHandler sqlHandler = RspSqlHandler.get(idx);
				sqlHandler.updateAirCondProfile(aircondprofile);
			}
			else if(idx == 10)
			{
				aircondprofile.setRoomtemp(new BigDecimal((Integer.parseInt(data, 16)) / 2));
				aircondprofile.setPmv(new BigDecimal(PMV_Calc((Integer.parseInt(data, 16) / 2), hm)));
				WrapperSqlHandler sqlHandler = RspSqlHandler.get(idx);
				sqlHandler.updateAirCondProfile(aircondprofile);
			}
			else if(idx == 11)
			{
				aircondprofile.setSetpoint(new BigDecimal((Integer.parseInt(data, 16)) / 2));
				WrapperSqlHandler sqlHandler = RspSqlHandler.get(idx);
				sqlHandler.updateAirCondProfile(aircondprofile);
			}
		}
		finally
		{
			aircondprofile.reset();
			BeanPool.releaseAircondprofile(aircondprofile);
		}

	}

	private synchronized void processWrite(String[] cmds)
	{
		Integer locate = Integer.parseInt(cmds[3], 16);
		int idx = locate - RspSqlHandler.getCmdIdx();
		if(idx < 0)
			return;

		WrapperSqlHandler sqlHandler = RspSqlHandler.get(idx);

		Aircondprofile aircondprofile = BeanPool.getAircondprofile();
		String powerId = RspSqlHandler.getPowerId();
		aircondprofile.setAircondid(powerId);
		/*
		 * if(Integer.parseInt(cmds[2], 16) < 10) {
		 * aircondprofile.setAircondid("0" + cmds[2]); } else
		 * if(cmds[2].equals("a") || cmds[2].equals("b")|| cmds[2].equals("c")||
		 * cmds[2].equals("d")|| cmds[2].equals("e")|| cmds[2].equals("f")) {
		 * aircondprofile.setAircondid("0" +cmds[2]); }
		 */

		aircondprofile.setMasterip(cmds[0]);
		aircondprofile.setUpdatetime(new Timestamp(new Date().getTime()));
		aircondprofile.setPowerstatus(new BigDecimal(Integer.parseInt(cmds[5], 16)));
		aircondprofile.setLockstatus(new BigDecimal(Integer.parseInt(cmds[5], 16)));
		aircondprofile.setSetmode(new BigDecimal(Integer.parseInt(cmds[5], 16)));
		aircondprofile.setSetfanspeed(new BigDecimal(Integer.parseInt(cmds[5], 16)));
		aircondprofile.setAlarmstatus(new BigDecimal(Integer.parseInt(cmds[5], 16)));
		aircondprofile.setValvecontact(new BigDecimal(Integer.parseInt(cmds[5], 16)));
		aircondprofile.setFanspeedcontact(new BigDecimal(Integer.parseInt(cmds[5], 16)));
		aircondprofile.setChillcontact(new BigDecimal(Integer.parseInt(cmds[5], 16)));
		aircondprofile.setHeatingcontact(new BigDecimal(Integer.parseInt(cmds[5], 16)));

		aircondprofile.setRoomtemp(new BigDecimal((Integer.parseInt(cmds[5], 16)) / 2));
		aircondprofile.setSetpoint(new BigDecimal((Integer.parseInt(cmds[5], 16)) / 2));
		try
		{
			if(SqlHandler.selectAirCondProfileAirCondId(aircondprofile, tableName))
			{
				sqlHandler.updateAirCondProfile(aircondprofile);
			}
			else
			{
				SqlHandler.insertAirCondProfileAirCondId(aircondprofile);
				sqlHandler.updateAirCondProfile(aircondprofile);
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
			RspSqlHandler.clear();

		}

	}
}

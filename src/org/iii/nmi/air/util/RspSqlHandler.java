package org.iii.nmi.air.util;

import java.util.Hashtable;

import org.iii.nmi.air.dao.AlarmStatusSqlHandler;
import org.iii.nmi.air.dao.ChillContactSqlHandler;
import org.iii.nmi.air.dao.FanSpeedContactSqlHandler;
import org.iii.nmi.air.dao.HeatingContactSqlHandler;
import org.iii.nmi.air.dao.LockStatusSqlHandler;
import org.iii.nmi.air.dao.PowerStatusSqlHandler;
import org.iii.nmi.air.dao.RoomTempSqlHandler;
import org.iii.nmi.air.dao.SetFanSpeedSqlHandler;
import org.iii.nmi.air.dao.SetModeSqlHandler;
import org.iii.nmi.air.dao.SetPointSqlHandler;
import org.iii.nmi.air.dao.ValveContactSqlHandler;
import org.iii.nmi.air.dao.WrapperSqlHandler;

public class RspSqlHandler
{
	private static final int cmdIdx = 16;

	private static int dataLocate = 0;

	private static String powerId;

	private static Hashtable<Integer, WrapperSqlHandler> rspSqlHash = new Hashtable<Integer, WrapperSqlHandler>();

	static
	{
		rspSqlHash.put(0, new PowerStatusSqlHandler());
		rspSqlHash.put(1, new LockStatusSqlHandler());
		rspSqlHash.put(2, new SetModeSqlHandler());
		rspSqlHash.put(3, new SetFanSpeedSqlHandler());
		rspSqlHash.put(4, new AlarmStatusSqlHandler());
		rspSqlHash.put(5, new ValveContactSqlHandler());
		rspSqlHash.put(6, new FanSpeedContactSqlHandler());
		rspSqlHash.put(7, new ChillContactSqlHandler());
		rspSqlHash.put(8, new HeatingContactSqlHandler());
		rspSqlHash.put(10, new RoomTempSqlHandler());
		rspSqlHash.put(11, new SetPointSqlHandler());

	}

	public static synchronized WrapperSqlHandler get(Integer i)
	{
		return rspSqlHash.get(i);
	}

	public static synchronized int getDataLocate()
	{
		return dataLocate;
	}

	public static synchronized void setDataLocate(int dataLocate)
	{
		RspSqlHandler.dataLocate = dataLocate;
	}

	public static synchronized int getCmdIdx()
	{
		return cmdIdx;
	}

	public static synchronized String getPowerId()
	{
		return powerId;
	}

	public static synchronized void setPowerId(String powerId)
	{
		RspSqlHandler.powerId = powerId;
	}

	public static synchronized void clear()
	{
		
		RspSqlHandler.powerId = null;

	}

}

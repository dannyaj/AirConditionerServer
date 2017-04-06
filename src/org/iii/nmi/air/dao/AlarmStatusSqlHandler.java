package org.iii.nmi.air.dao;

public class AlarmStatusSqlHandler implements WrapperSqlHandler
{

	public void updateAirCondProfile(Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		SqlHandler.updateAirCondProfileAlarmStatus(aircondProfile);

	}
}

package org.iii.nmi.air.dao;

public class SetFanSpeedSqlHandler implements WrapperSqlHandler
{

	public void updateAirCondProfile(Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		SqlHandler.updateAirCondProfileSetFanSpeed(aircondProfile);

	}
}

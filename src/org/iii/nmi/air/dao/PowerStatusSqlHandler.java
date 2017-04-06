package org.iii.nmi.air.dao;

public class PowerStatusSqlHandler implements WrapperSqlHandler
{

	public void updateAirCondProfile(Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		SqlHandler.updateAirCondProfilePowerStatus(aircondProfile);

	}
}

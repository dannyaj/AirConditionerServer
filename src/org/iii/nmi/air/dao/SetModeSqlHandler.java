package org.iii.nmi.air.dao;

public class SetModeSqlHandler implements WrapperSqlHandler
{

	public void updateAirCondProfile(Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		SqlHandler.updateAirCondProfileSetMode(aircondProfile);

	}
}

package org.iii.nmi.air.dao;

public class SetPointSqlHandler implements WrapperSqlHandler
{

	public void updateAirCondProfile(Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		SqlHandler.updateAirCondProfileSetPoint(aircondProfile);

	}
}

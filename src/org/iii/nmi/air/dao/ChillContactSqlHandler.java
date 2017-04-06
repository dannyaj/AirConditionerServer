package org.iii.nmi.air.dao;

public class ChillContactSqlHandler implements WrapperSqlHandler
{

	public void updateAirCondProfile(Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		SqlHandler.updateAirCondProfileChillContact(aircondProfile);

	}
}

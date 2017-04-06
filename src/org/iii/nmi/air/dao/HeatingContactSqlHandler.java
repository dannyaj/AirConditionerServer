package org.iii.nmi.air.dao;

public class HeatingContactSqlHandler implements WrapperSqlHandler
{

	public void updateAirCondProfile(Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		SqlHandler.updateAirCondProfileHeatingContact(aircondProfile);

	}
}

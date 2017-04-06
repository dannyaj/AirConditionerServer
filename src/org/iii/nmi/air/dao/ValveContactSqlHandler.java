package org.iii.nmi.air.dao;

public class ValveContactSqlHandler implements WrapperSqlHandler
{

	public void updateAirCondProfile(Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		SqlHandler.updateAirCondProfileValveContact(aircondProfile);

	}
}

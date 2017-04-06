package org.iii.nmi.air.dao;

public class FanSpeedContactSqlHandler implements WrapperSqlHandler
{

	public void updateAirCondProfile(Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		SqlHandler.updateAirCondProfileFanSpeedContact(aircondProfile);

	}
}

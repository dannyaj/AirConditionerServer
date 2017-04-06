package org.iii.nmi.air.dao;

public class LockStatusSqlHandler implements WrapperSqlHandler
{

	public void updateAirCondProfile(Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		SqlHandler.updateAirCondProfileLockStatus(aircondProfile);

	}
}

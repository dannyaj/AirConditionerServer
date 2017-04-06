package org.iii.nmi.air.dao;

public class RoomTempSqlHandler implements WrapperSqlHandler
{

	public void updateAirCondProfile(Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		SqlHandler.updateAirCondProfileRoomTemp(aircondProfile);

	}
}

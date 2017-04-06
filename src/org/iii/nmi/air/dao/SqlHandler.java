package org.iii.nmi.air.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.iii.nmi.air.db.DBHandlerIn;
import org.iii.nmi.air.factory.InstanceFactory;
import org.iii.nmi.air.log.LogHandler;

public class SqlHandler
{

	private static DBHandlerIn dbHandler;

	private static LogHandler logHandler = InstanceFactory.createLogHandler();

	private static String insertWebAirCondLogSql;

	private static String insertAirCondInfoSql;

	private static String insertAirCondProfileSql;

	private static String insertAirCondProfileAirCondIdSql;

	private static String updateAirCondProfileMasterIpSql;

	private static String updateAirCondProfilePowerStatusSql;

	private static String updateAirCondProfileLockStatusSql;

	private static String updateAirCondProfileSetModeSql;

	private static String updateAirCondProfileSetFanSpeedSql;

	private static String updateAirCondProfileAlarmStatusSql;

	private static String updateAirCondProfileValveContactSql;

	private static String updateAirCondProfileFanSpeedContactSql;

	private static String updateAirCondProfileChillContactSql;

	private static String updateAirCondProfileHeatingContactSql;

	private static String updateAirCondProfileRoomTempSql;

	private static String updateAirCondProfileSetPointSql;

	private static String updateAirCondProfileAllSql;

	private static String selectAirCondProfileAirCondIdSql;

	public static void setSelectAirCondProfileAirCondIdSql(String selectAirCondProfileAirCondIdSql)
	{
		SqlHandler.selectAirCondProfileAirCondIdSql = selectAirCondProfileAirCondIdSql;
	}

	public static void setLogHandler(LogHandler logHandler)
	{
		SqlHandler.logHandler = logHandler;
	}

	public static void setInsertAirCondProfileAirCondIdSql(String insertAirCondProfileAirCondIdSql)
	{
		SqlHandler.insertAirCondProfileAirCondIdSql = insertAirCondProfileAirCondIdSql;
	}

	public static void setUpdateAirCondProfileMasterIpSql(String updateAirCondProfileMasterIpSql)
	{
		SqlHandler.updateAirCondProfileMasterIpSql = updateAirCondProfileMasterIpSql;
	}

	public static void setUpdateAirCondProfilePowerStatusSql(String updateAirCondProfilePowerStatusSql)
	{
		SqlHandler.updateAirCondProfilePowerStatusSql = updateAirCondProfilePowerStatusSql;
	}

	public static void setUpdateAirCondProfileLockStatusSql(String updateAirCondProfileLockStatusSql)
	{
		SqlHandler.updateAirCondProfileLockStatusSql = updateAirCondProfileLockStatusSql;
	}

	public static void setUpdateAirCondProfileSetModeSql(String updateAirCondProfileSetModeSql)
	{
		SqlHandler.updateAirCondProfileSetModeSql = updateAirCondProfileSetModeSql;
	}

	public static void setUpdateAirCondProfileSetFanSpeedSql(String updateAirCondProfileSetFanSpeedSql)
	{
		SqlHandler.updateAirCondProfileSetFanSpeedSql = updateAirCondProfileSetFanSpeedSql;
	}

	public static void setUpdateAirCondProfileAlarmStatusSql(String updateAirCondProfileAlarmStatusSql)
	{
		SqlHandler.updateAirCondProfileAlarmStatusSql = updateAirCondProfileAlarmStatusSql;
	}

	public static void setUpdateAirCondProfileValveContactSql(String updateAirCondProfileValveContactSql)
	{
		SqlHandler.updateAirCondProfileValveContactSql = updateAirCondProfileValveContactSql;
	}

	public static void setUpdateAirCondProfileFanSpeedContactSql(String updateAirCondProfileFanSpeedContactSql)
	{
		SqlHandler.updateAirCondProfileFanSpeedContactSql = updateAirCondProfileFanSpeedContactSql;
	}

	public static void setUpdateAirCondProfileChillContactSql(String updateAirCondProfileChillContactSql)
	{
		SqlHandler.updateAirCondProfileChillContactSql = updateAirCondProfileChillContactSql;
	}

	public static void setUpdateAirCondProfileHeatingContactSql(String updateAirCondProfileHeatingContactSql)
	{
		SqlHandler.updateAirCondProfileHeatingContactSql = updateAirCondProfileHeatingContactSql;
	}

	public static void setUpdateAirCondProfileRoomTempSql(String updateAirCondProfileRoomTempSql)
	{
		SqlHandler.updateAirCondProfileRoomTempSql = updateAirCondProfileRoomTempSql;
	}

	public static void setUpdateAirCondProfileSetPointSql(String updateAirCondProfileSetPointSql)
	{
		SqlHandler.updateAirCondProfileSetPointSql = updateAirCondProfileSetPointSql;
	}

	public static void setUpdateAirCondProfileAllSql(String updateAirCondProfileAllSql)
	{
		SqlHandler.updateAirCondProfileAllSql = updateAirCondProfileAllSql;
	}

	public static void setInsertAirCondProfileSql(String insertAirCondProfileSql)
	{
		SqlHandler.insertAirCondProfileSql = insertAirCondProfileSql;
	}

	public static void setInsertAirCondInfoSql(String insertAirCondInfoSql)
	{
		SqlHandler.insertAirCondInfoSql = insertAirCondInfoSql;
	}

	public static void setInsertWebAirCondLogSql(String insertWebAirCondLogSql)
	{
		SqlHandler.insertWebAirCondLogSql = insertWebAirCondLogSql;
	}

	public static void setDbHandler(DBHandlerIn dbHandler)
	{
		SqlHandler.dbHandler = dbHandler;
	}

	private static boolean selectExecuteSQL(String SQL, String Id, String IdName, StatementParamsSetter paramsSetter)
		throws SqlHandlerException
	{
		Connection con = null;
		PreparedStatement st = null;

		try
		{
			con = dbHandler.getConnection();
			st = con.prepareStatement(SQL);
			paramsSetter.setParametersOf(st);
			ResultSet resultSet = st.executeQuery();

			while(resultSet.next())
			{
				if(resultSet.getString(IdName) != null && resultSet.getString(IdName).equals(Id))
				{

					return true;
				}

			}

			return false;

		}
		catch(NullPointerException e)
		{
			throw new SqlHandlerException("db connection is null");
		}
		catch(SQLException e)
		{

			e.printStackTrace();
			throw new SqlHandlerException(e.getMessage());
		}
		finally
		{
			try
			{
				if(st != null)
					st.close();
			}
			catch(SQLException e)
			{

				e.printStackTrace();
				throw new SqlHandlerException("PreparedStatement close SQLException");
			}
			finally
			{

				dbHandler.releaseConnection(con);
			}

		}
	}

	private static void executeSQL(String SQL, StatementParamsSetter paramsSetter)
		throws SqlHandlerException
	{
		Connection con = null;
		PreparedStatement st = null;

		try
		{
			con = dbHandler.getConnection();
			st = con.prepareStatement(SQL);
			paramsSetter.setParametersOf(st);
			st.execute();

		}
		catch(NullPointerException e)
		{
			throw new SqlHandlerException("db connection is null");
		}
		catch(SQLException e)
		{

			e.printStackTrace();
			throw new SqlHandlerException(e.getMessage());
		}
		finally
		{
			try
			{
				if(st != null)
					st.close();
			}
			catch(SQLException e)
			{

				e.printStackTrace();
				throw new SqlHandlerException("PreparedStatement close SQLException");
			}
			finally
			{

				dbHandler.releaseConnection(con);
			}

		}
	}

	public static synchronized boolean selectAirCondProfileAirCondId(final Aircondprofile aircondProfile, String IdName)
		throws SqlHandlerException
	{
		return selectExecuteSQL(selectAirCondProfileAirCondIdSql, aircondProfile.getAircondid(), IdName, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setString(1, aircondProfile.getAircondid());

					logHandler.info("select AirCondProfile Table AircondId: " + "AircondId: " + aircondProfile.getAircondid());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("select AirCondProfile Table AircondId SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfileSetPoint(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfileSetPointSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setDouble(1, aircondProfile.getSetpoint().doubleValue());
					st.setTimestamp(2, aircondProfile.getUpdatetime());
					st.setString(3, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table SetPoint: " + "AircondId: " + aircondProfile.getAircondid() + " SetPoint: " + aircondProfile.getSetpoint());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile Table SetPoint SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfileRoomTemp(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfileRoomTempSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setDouble(1, aircondProfile.getRoomtemp().doubleValue());
					st.setTimestamp(2, aircondProfile.getUpdatetime());
					st.setDouble(3, aircondProfile.getPmv().doubleValue());
					st.setString(4, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table RoomTemp: " + "AircondId: " + aircondProfile.getAircondid() + " RoomTemp: " + aircondProfile.getRoomtemp()+ " Pmv: " + aircondProfile.getPmv());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile Table RoomTemp SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfileHeatingContact(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfileHeatingContactSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setInt(1, aircondProfile.getHeatingcontact().intValue());
					st.setTimestamp(2, aircondProfile.getUpdatetime());
					st.setString(3, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table HeatingContact: " + "AircondId: " + aircondProfile.getAircondid() + " HeatingContact: " + aircondProfile.getHeatingcontact());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile Table HeatingContact SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfileChillContact(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfileChillContactSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setInt(1, aircondProfile.getChillcontact().intValue());
					st.setTimestamp(2, aircondProfile.getUpdatetime());
					st.setString(3, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table ChillContact: " + "AircondId: " + aircondProfile.getAircondid() + " ChillContact: " + aircondProfile.getChillcontact());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile Table ChillContact SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfileFanSpeedContact(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfileFanSpeedContactSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setInt(1, aircondProfile.getFanspeedcontact().intValue());
					st.setTimestamp(2, aircondProfile.getUpdatetime());
					st.setString(3, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table FanSpeedContact: " + "AircondId: " + aircondProfile.getAircondid() + " FanSpeedContact: " + aircondProfile.getFanspeedcontact());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile Table FanSpeedContact SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfileValveContact(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfileValveContactSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setInt(1, aircondProfile.getValvecontact().intValue());
					st.setTimestamp(2, aircondProfile.getUpdatetime());
					st.setString(3, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table ValveContact: " + "AircondId: " + aircondProfile.getAircondid() + " ValveContact: " + aircondProfile.getValvecontact());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile Table ValveContact SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfileAlarmStatus(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfileAlarmStatusSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setInt(1, aircondProfile.getAlarmstatus().intValue());
					st.setTimestamp(2, aircondProfile.getUpdatetime());
					st.setString(3, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table AlarmStatus: " + "AircondId: " + aircondProfile.getAircondid() + "AlarmStatus: " + aircondProfile.getAlarmstatus());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile Table Alarmstatus SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfileSetFanSpeed(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfileSetFanSpeedSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setInt(1, aircondProfile.getSetfanspeed().intValue());
					st.setTimestamp(2, aircondProfile.getUpdatetime());
					st.setString(3, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table SetFanSpeed: " + "AircondId: " + aircondProfile.getAircondid() + " SetFanSpeed: " + aircondProfile.getSetfanspeed());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile Table SetFanSpeed SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfileSetMode(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfileSetModeSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setInt(1, aircondProfile.getSetmode().intValue());
					st.setTimestamp(2, aircondProfile.getUpdatetime());
					st.setString(3, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table SetMode: " + "AircondId: " + aircondProfile.getAircondid() + " SetMode: " + aircondProfile.getSetmode());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile Table SetMode SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfileLockStatus(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfileLockStatusSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setInt(1, aircondProfile.getLockstatus().intValue());
					st.setTimestamp(2, aircondProfile.getUpdatetime());
					st.setString(3, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table LockStatus: " + "AircondId: " + aircondProfile.getAircondid() + " Lockstatus: " + aircondProfile.getLockstatus());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile Table Lockstatus SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfilePowerStatus(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfilePowerStatusSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setInt(1, aircondProfile.getPowerstatus().intValue());
					st.setTimestamp(2, aircondProfile.getUpdatetime());
					st.setString(3, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table Powerstatus: " + "AircondId: " + aircondProfile.getAircondid() + " PowerStatus: " + aircondProfile.getPowerstatus());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile Table PowerStatus SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfileMasterIp(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfileMasterIpSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setString(1, aircondProfile.getMasterip());
					st.setTimestamp(2, aircondProfile.getUpdatetime());
					st.setString(3, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table MasterIP: " + "AircondId: " + aircondProfile.getAircondid() + " MasterIP: " + aircondProfile.getMasterip());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile Table MasterIP SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void insertAirCondProfileAirCondId(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(insertAirCondProfileAirCondIdSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setString(1, aircondProfile.getAircondid());
					st.setString(2, aircondProfile.getMasterip());
					st.setTimestamp(3, aircondProfile.getUpdatetime());

					logHandler.info("Insert into AirCondProfile Table: " + "AircondId: " + aircondProfile.getAircondid() + " MasterIP: " + aircondProfile.getMasterip());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("Insert into AirCondProfile AirCondId SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void updateAirCondProfileAll(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(updateAirCondProfileAllSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setInt(1, aircondProfile.getPowerstatus().intValue());
					st.setInt(2, aircondProfile.getLockstatus().intValue());
					st.setInt(3, aircondProfile.getSetmode().intValue());
					st.setInt(4, aircondProfile.getSetfanspeed().intValue());
					st.setInt(5, aircondProfile.getAlarmstatus().intValue());
					st.setInt(6, aircondProfile.getValvecontact().intValue());
					st.setInt(7, aircondProfile.getFanspeedcontact().intValue());
					st.setInt(8, aircondProfile.getChillcontact().intValue());
					st.setInt(9, aircondProfile.getHeatingcontact().intValue());
					st.setDouble(10, aircondProfile.getRoomtemp().doubleValue());
					st.setDouble(11, aircondProfile.getSetpoint().doubleValue());
					st.setTimestamp(12, aircondProfile.getUpdatetime());
					st.setDouble(13, aircondProfile.getPmv().doubleValue());
					st.setString(14, aircondProfile.getAircondid());

					logHandler.info("update AirCondProfile Table: " + "AircondId: " + aircondProfile.getAircondid() + ", " + " Powerstatus: " + aircondProfile.getPowerstatus() + ", " + "Lockstatus: " + aircondProfile.getLockstatus() + ", " + "Setmode: " + aircondProfile.getSetmode() + ", " + "Setfanspeed: " + aircondProfile.getSetfanspeed() + ", " + "Alarmstatus: " + aircondProfile.getAlarmstatus() + ", " + "Valvecontact: " + aircondProfile.getValvecontact() + ", " + "Fanspeedcontact: " + aircondProfile.getFanspeedcontact() + ", " + "Chillcontact: " + aircondProfile.getChillcontact() + ", " + "Heatingcontact: " + aircondProfile.getHeatingcontact() + ", " + "Roomtemp: " + aircondProfile.getRoomtemp() + ", " + "Setpoint: " + aircondProfile.getSetpoint()+ ", " + "Pmv: " + aircondProfile.getPmv().doubleValue());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("update AirCondProfile SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void insertAirCondProfile(final Aircondprofile aircondProfile)
		throws SqlHandlerException
	{
		executeSQL(insertAirCondProfileSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setString(1, aircondProfile.getAircondid());
					st.setString(2, aircondProfile.getMasterip());
					st.setTimestamp(3, aircondProfile.getUpdatetime());
					st.setInt(4, aircondProfile.getPowerstatus().intValue());
					st.setInt(5, aircondProfile.getLockstatus().intValue());
					st.setInt(6, aircondProfile.getSetmode().intValue());
					st.setInt(7, aircondProfile.getSetfanspeed().intValue());
					st.setInt(8, aircondProfile.getAlarmstatus().intValue());
					st.setInt(9, aircondProfile.getValvecontact().intValue());
					st.setInt(10, aircondProfile.getFanspeedcontact().intValue());
					st.setInt(11, aircondProfile.getChillcontact().intValue());
					st.setInt(12, aircondProfile.getHeatingcontact().intValue());
					st.setDouble(13, aircondProfile.getRoomtemp().doubleValue());
					st.setDouble(14, aircondProfile.getSetpoint().doubleValue());
					st.setDouble(15, aircondProfile.getPmv().doubleValue());
					logHandler.info("Insert into AirCondProfile Table: " + "AircondId: " + aircondProfile.getAircondid() + " MasterIP: " + aircondProfile.getMasterip() + ", " + "Powerstatus: " + aircondProfile.getPowerstatus() + ", " + "Lockstatus: " + aircondProfile.getLockstatus() + ", " + "Setmode: " + aircondProfile.getSetmode() + ", " + "Setfanspeed: " + aircondProfile.getSetfanspeed() + ", " + "Alarmstatus: " + aircondProfile.getAlarmstatus() + ", " + "Valvecontact: " + aircondProfile.getValvecontact() + ", " + "Fanspeedcontact: " + aircondProfile.getFanspeedcontact() + ", " + "Chillcontact: " + aircondProfile.getChillcontact() + ", " + "Heatingcontact: " + aircondProfile.getHeatingcontact() + ", " + "Roomtemp: " + aircondProfile.getRoomtemp() + ", " + "Setpoint: " + aircondProfile.getSetpoint()+ ", " + "Pmv: " + aircondProfile.getPmv().doubleValue());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("Insert into AirCondProfile SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void insertAirCondInfo(final Aircondprofile aircondprofile)
		throws SqlHandlerException
	{
		executeSQL(insertAirCondInfoSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{

					st.setString(1, aircondprofile.getAircondid());
					st.setString(2, aircondprofile.getMasterip());
					st.setTimestamp(3, aircondprofile.getUpdatetime());
					st.setInt(4, aircondprofile.getPowerstatus().intValue());
					st.setInt(5, aircondprofile.getLockstatus().intValue());
					st.setInt(6, aircondprofile.getSetmode().intValue());
					st.setInt(7, aircondprofile.getSetfanspeed().intValue());
					st.setInt(8, aircondprofile.getAlarmstatus().intValue());
					st.setInt(9, aircondprofile.getValvecontact().intValue());
					st.setInt(10, aircondprofile.getFanspeedcontact().intValue());
					st.setInt(11, aircondprofile.getChillcontact().intValue());
					st.setInt(12, aircondprofile.getHeatingcontact().intValue());
					st.setDouble(13, aircondprofile.getRoomtemp().doubleValue());
					st.setDouble(14, aircondprofile.getSetpoint().doubleValue());
					st.setDouble(15, aircondprofile.getPmv().doubleValue());
					logHandler.info("Insert into AirCondInfo Table: " + "AircondId: " + aircondprofile.getAircondid() + " MasterIP: " + aircondprofile.getMasterip() + ", " + ", " + "Powerstatus: " + aircondprofile.getPowerstatus() + ", " + "Lockstatus: " + aircondprofile.getLockstatus() + ", " + "Setmode: " + aircondprofile.getSetmode() + ", " + "Setfanspeed: " + aircondprofile.getSetfanspeed() + ", " + "Alarmstatus: " + aircondprofile.getAlarmstatus() + ", " + "Valvecontact: " + aircondprofile.getValvecontact() + ", " + "Fanspeedcontact: " + aircondprofile.getFanspeedcontact() + ", " + "Chillcontact: " + aircondprofile.getChillcontact() + ", " + "Heatingcontact: " + aircondprofile.getHeatingcontact() + ", " + "Roomtemp: " + aircondprofile.getRoomtemp() + ", " + "Setpoint: " + aircondprofile.getSetpoint()+ ", " + "Pmv: " + aircondprofile.getPmv().doubleValue());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("Insert into AirCondInfo SQLException " + e.getMessage());

				}

			}

		});
	}

	public static synchronized void insertWebAirCondLog(final Webaircondlog webaircondlog)
		throws SqlHandlerException
	{
		executeSQL(insertWebAirCondLogSql, new StatementParamsSetter()
		{

			public void setParametersOf(PreparedStatement st)
				throws SqlHandlerException
			{
				try
				{
					st.setString(1, webaircondlog.getWebcmdpackage());
					st.setTimestamp(2, webaircondlog.getWebcmdtime());
					st.setTimestamp(3, webaircondlog.getUpdatetime());

					logHandler.info("Insert into WebAirCondLog Table: " + "Webcmdpackage: " + webaircondlog.getWebcmdpackage());

				}
				catch(SQLException e)
				{

					e.printStackTrace();
					throw new SqlHandlerException("Insert into WebAirCondLog SQLException " + e.getMessage());

				}

			}

		});
	}
}

package org.iii.nmi.air.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.iii.nmi.air.dao.SqlHandler;
import org.iii.nmi.air.factory.InstanceFactory;
import org.iii.nmi.air.log.ExceptionHandler;
import org.iii.nmi.air.log.LogHandler;

public class DBHandlerImpl implements DBHandlerIn
{
	private final String dbConfigFilePath = "conf/dbms.properties";

	private LogHandler logHandler;

	private ExceptionHandler exceptionHandler;

	private BasicDataSource ds;

	private boolean dbState;

	public DBHandlerImpl()
	{
		this.logHandler = InstanceFactory.createLogHandler();
		this.exceptionHandler = InstanceFactory.createExceptionHandler();

		init();
		SqlHandler.setDbHandler(this);
	}

	private void init()
	{
		Properties p = new Properties();
		FileInputStream f;
		try
		{
			f = new FileInputStream(dbConfigFilePath);
			p.load(f);
			f.close();
			ds = new BasicDataSource();
			ds.setDriverClassName(p.getProperty("Db.Driver"));
			ds.setUsername(p.getProperty("Db.User"));
			ds.setPassword(p.getProperty("Db.Password"));
			ds.setUrl(p.getProperty("Db.Url"));
			ds.setMaxActive(Integer.parseInt(p.getProperty("Db.MaxActive")));
			ds.setMaxIdle(Integer.parseInt(p.getProperty("Db.MaxIdle")));
			ds.setMaxWait(Integer.parseInt(p.getProperty("Db.MaxWait")));
			ds.setValidationQuery("SELECT count(*) FROM dual");
			ds.setTestOnBorrow(true);
			ds.setTestOnReturn(true);
			dbState = true;

			SqlHandler.setInsertWebAirCondLogSql(p.getProperty("insertWebAirCondLogSql"));
			SqlHandler.setInsertAirCondInfoSql(p.getProperty("insertAirCondInfoSql"));
			SqlHandler.setInsertAirCondProfileSql(p.getProperty("insertAirCondProfileSql"));
			SqlHandler.setInsertAirCondProfileAirCondIdSql(p.getProperty("insertAirCondProfileAirCondIdSql"));
			SqlHandler.setUpdateAirCondProfileMasterIpSql(p.getProperty("updateAirCondProfileMasterIpSql"));
			SqlHandler.setUpdateAirCondProfilePowerStatusSql(p.getProperty("updateAirCondProfilePowerStatusSql"));
			SqlHandler.setUpdateAirCondProfileLockStatusSql(p.getProperty("updateAirCondProfileLockStatusSql"));
			SqlHandler.setUpdateAirCondProfileSetModeSql(p.getProperty("updateAirCondProfileSetModeSql"));
			SqlHandler.setUpdateAirCondProfileSetFanSpeedSql(p.getProperty("updateAirCondProfileSetFanSpeedSql"));
			SqlHandler.setUpdateAirCondProfileAlarmStatusSql(p.getProperty("updateAirCondProfileAlarmStatusSql"));
			SqlHandler.setUpdateAirCondProfileValveContactSql(p.getProperty("updateAirCondProfileValveContactSql"));
			SqlHandler.setUpdateAirCondProfileFanSpeedContactSql(p.getProperty("updateAirCondProfileFanSpeedContactSql"));
			SqlHandler.setUpdateAirCondProfileChillContactSql(p.getProperty("updateAirCondProfileChillContactSql"));
			SqlHandler.setUpdateAirCondProfileHeatingContactSql(p.getProperty("updateAirCondProfileHeatingContactSql"));
			SqlHandler.setUpdateAirCondProfileRoomTempSql(p.getProperty("updateAirCondProfileRoomTempSql"));
			SqlHandler.setUpdateAirCondProfileSetPointSql(p.getProperty("updateAirCondProfileSetPointSql"));
			SqlHandler.setUpdateAirCondProfileAllSql(p.getProperty("updateAirCondProfileAllSql"));
			SqlHandler.setSelectAirCondProfileAirCondIdSql(p.getProperty("selectAirCondProfileAirCondIdSql"));
			logHandler.info("Connection db initial finish");

		}
		catch(FileNotFoundException e)
		{

			exceptionHandler.putExceptionMessage("initial conf/dbms.properties not find");
			e.printStackTrace();
		}
		catch(IOException e)
		{
			exceptionHandler.putExceptionMessage("initial dbms.properties FileInputStream IOException");
			e.printStackTrace();
		}
	}

	public boolean disconnectDb()
	{

		dbState = false;
		logHandler.info("disconnect db finish");
		return true;
	}

	public Connection getConnection() throws SQLException
	{

		if(dbState)
		{
			return ds.getConnection();
		}
		else
		{
			logHandler.info("Connection db initial not finish OR Connection db already disconnect");
			return null;
		}
	}

	public void releaseConnection(Connection con)
	{
		try
		{

			if(con == null)
			{

				logHandler.info("releaseConnection is null!");
				throw new NullPointerException("releaseConnection is null!");
			}
		}
		catch(NullPointerException e)
		{

			exceptionHandler.putExceptionMessage("releaseConnection is null!");
		}
		try
		{
			if(con != null)
			{

				con.close();
			}
		}
		catch(SQLException e)
		{

			exceptionHandler.putExceptionMessage("Connection close SQLException");
			e.printStackTrace();
		}
	}

	public boolean resetConnectionDB()
	{

		if(!dbState)
		{
			init();
			return true;
		}
		else
		{
			logHandler.info("Connection db already create finish");
			return false;
		}
	}

}

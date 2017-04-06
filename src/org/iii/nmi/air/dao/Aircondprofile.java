package org.iii.nmi.air.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Aircondprofile
{

	/** identifier field */
	private String masterip;

	/** persistent field */
	private String aircondid;

	/** nullable persistent field */
	private Timestamp updatetime;

	/** nullable persistent field */
	private BigDecimal powerstatus;

	/** nullable persistent field */
	private BigDecimal lockstatus;

	/** nullable persistent field */
	private BigDecimal setmode;

	/** nullable persistent field */
	private BigDecimal setfanspeed;

	/** nullable persistent field */
	private BigDecimal alarmstatus;

	/** nullable persistent field */
	private BigDecimal valvecontact;

	/** nullable persistent field */
	private BigDecimal fanspeedcontact;

	/** nullable persistent field */
	private BigDecimal chillcontact;

	/** nullable persistent field */
	private BigDecimal heatingcontact;

	/** nullable persistent field */
	private BigDecimal roomtemp;

	/** nullable persistent field */
	private BigDecimal setpoint;
	
	private BigDecimal pmv;

	public void reset()
	{
		this.masterip = null;

		this.aircondid = null;

		this.updatetime = null;

		this.powerstatus = null;

		this.lockstatus = null;

		this.setmode = null;

		this.setfanspeed = null;

		this.alarmstatus = null;

		this.valvecontact = null;

		this.fanspeedcontact = null;

		this.chillcontact = null;

		this.heatingcontact = null;

		this.roomtemp = null;

		this.setpoint = null;

		this.updatetime = null;
		this.pmv = null;
	}

	public String getMasterip()
	{
		return masterip;
	}

	public void setMasterip(String masterip)
	{
		this.masterip = masterip;
	}

	public String getAircondid()
	{
		return aircondid;
	}

	public void setAircondid(String aircondid)
	{
		this.aircondid = aircondid;
	}

	public Timestamp getUpdatetime()
	{
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime)
	{
		this.updatetime = updatetime;
	}

	public BigDecimal getPowerstatus()
	{
		return powerstatus;
	}

	public void setPowerstatus(BigDecimal powerstatus)
	{
		this.powerstatus = powerstatus;
	}

	public BigDecimal getLockstatus()
	{
		return lockstatus;
	}

	public void setLockstatus(BigDecimal lockstatus)
	{
		this.lockstatus = lockstatus;
	}

	public BigDecimal getSetmode()
	{
		return setmode;
	}

	public void setSetmode(BigDecimal setmode)
	{
		this.setmode = setmode;
	}
	
	

	public BigDecimal getPmv()
	{
		return pmv;
	}

	public void setPmv(BigDecimal pmv)
	{
		this.pmv = pmv;
	}

	public BigDecimal getSetfanspeed()
	{
		return setfanspeed;
	}

	public void setSetfanspeed(BigDecimal setfanspeed)
	{
		this.setfanspeed = setfanspeed;
	}

	public BigDecimal getAlarmstatus()
	{
		return alarmstatus;
	}

	public void setAlarmstatus(BigDecimal alarmstatus)
	{
		this.alarmstatus = alarmstatus;
	}

	public BigDecimal getValvecontact()
	{
		return valvecontact;
	}

	public void setValvecontact(BigDecimal valvecontact)
	{
		this.valvecontact = valvecontact;
	}

	public BigDecimal getFanspeedcontact()
	{
		return fanspeedcontact;
	}

	public void setFanspeedcontact(BigDecimal fanspeedcontact)
	{
		this.fanspeedcontact = fanspeedcontact;
	}

	public BigDecimal getChillcontact()
	{
		return chillcontact;
	}

	public void setChillcontact(BigDecimal chillcontact)
	{
		this.chillcontact = chillcontact;
	}

	public BigDecimal getHeatingcontact()
	{
		return heatingcontact;
	}

	public void setHeatingcontact(BigDecimal heatingcontact)
	{
		this.heatingcontact = heatingcontact;
	}

	public BigDecimal getRoomtemp()
	{
		return roomtemp;
	}

	public void setRoomtemp(BigDecimal roomtemp)
	{
		this.roomtemp = roomtemp;
	}

	public BigDecimal getSetpoint()
	{
		return setpoint;
	}

	public void setSetpoint(BigDecimal setpoint)
	{
		this.setpoint = setpoint;
	}

}

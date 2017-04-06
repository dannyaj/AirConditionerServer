package org.iii.nmi.air.dao;

import java.sql.Timestamp;

public class Webaircondlog
{

	/** nullable persistent field */
	private String webcmdpackage;

	/** nullable persistent field */
	private Timestamp webcmdtime;

	/** nullable persistent field */
	private Timestamp updatetime;

	public Webaircondlog()
	{

	}

	public Webaircondlog(String webcmdpackage,
			Timestamp webcmdtime,
			Timestamp updatetime)
	{
		this.webcmdpackage = webcmdpackage;
		this.webcmdtime = webcmdtime;
		this.updatetime = updatetime;
	}

	public void reset()
	{
		this.webcmdpackage = null;
		this.webcmdtime = null;
		this.updatetime = null;
	}

	public String getWebcmdpackage()
	{
		return webcmdpackage;
	}

	public void setWebcmdpackage(String webcmdpackage)
	{
		this.webcmdpackage = webcmdpackage;
	}

	public Timestamp getWebcmdtime()
	{
		return webcmdtime;
	}

	public void setWebcmdtime(Timestamp webcmdtime)
	{
		this.webcmdtime = webcmdtime;
	}

	public Timestamp getUpdatetime()
	{
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime)
	{
		this.updatetime = updatetime;
	}

}

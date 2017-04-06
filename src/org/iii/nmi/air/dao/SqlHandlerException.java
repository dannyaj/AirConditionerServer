package org.iii.nmi.air.dao;



public class SqlHandlerException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1077950043102634264L;
	private String message;

	public SqlHandlerException(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}
}

package org.iii.nmi.air.socket;

public class SerialConnectionException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2887694728615668591L;

	/**
	 * 
	 */


	/**
	 * Constructs a SerialConnectionException with the specified detail message.
	 * 
	 * @param s
	 *            the detail message.
	 */
	public SerialConnectionException(String str)
	{
		super(str);
	}

	/**
	 * Constructs a SerialConnectionException with no detail message.
	 */
	public SerialConnectionException()
	{
		super();
	}
}

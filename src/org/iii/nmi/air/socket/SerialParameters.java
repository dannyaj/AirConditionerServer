package org.iii.nmi.air.socket;

import javax.comm.SerialPort;

public class SerialParameters
{
	private String portName;

	private int baudRate;

	private int flowControlIn;

	private int flowControlOut;

	private int databits;

	private int stopbits;

	private int parity;

	public SerialParameters()
	{

	}

	public SerialParameters(String portName,
			int baudRate,
			int flowControlIn,
			int flowControlOut,
			int databits,
			int stopbits,
			int parity)
	{

		this.portName = portName;
		this.baudRate = baudRate;
		this.flowControlIn = flowControlIn;
		this.flowControlOut = flowControlOut;
		this.databits = databits;
		this.stopbits = stopbits;
		this.parity = parity;

	}

	/**
	 * Sets port name.
	 * 
	 * @param portName
	 *            New port name.
	 */
	public void setPortName(String portName)
	{
		this.portName = portName;
	}

	/**
	 * Gets port name.
	 * 
	 * @return Current port name.
	 */
	public String getPortName()
	{
		return portName;
	}

	/**
	 * Sets baud rate.
	 * 
	 * @param baudRate
	 *            New baud rate.
	 */
	public void setBaudRate(int baudRate)
	{
		this.baudRate = baudRate;
	}

	/**
	 * Sets baud rate.
	 * 
	 * @param baudRate
	 *            New baud rate.
	 */
	public void setBaudRate(String baudRate)
	{
		this.baudRate = Integer.parseInt(baudRate);
	}

	public String getDatabitsString()
	{
		switch(databits)
		{
		case SerialPort.DATABITS_5:
			return "5";
		case SerialPort.DATABITS_6:
			return "6";
		case SerialPort.DATABITS_7:
			return "7";
		case SerialPort.DATABITS_8:
			return "8";
		default:
			return "8";
		}
	}

	public void setDatabits(int databits)
	{
		this.databits = databits;
	}

	public void setStopbits(int stopbits)
	{
		this.stopbits = stopbits;
	}

	public void setParity(String parity)
	{
		if(parity.equals("None"))
		{
			this.parity = SerialPort.PARITY_NONE;
		}
		if(parity.equals("Even"))
		{
			this.parity = SerialPort.PARITY_EVEN;
		}
		if(parity.equals("Odd"))
		{
			this.parity = SerialPort.PARITY_ODD;
		}
	}

	public void setParity(int parity)
	{
		this.parity = parity;
	}

	public int getBaudRate()
	{
		return baudRate;
	}

	public int getDatabits()
	{
		return databits;
	}

	public int getStopbits()
	{
		return stopbits;
	}

	public int getFlowControlIn()
	{
		return flowControlIn;
	}

	public int getFlowControlOut()
	{
		return flowControlOut;
	}

	public int getParity()
	{
		return parity;
	}

	public String getBaudRateString()
	{
		return Integer.toString(baudRate);
	}

	public String getFlowControlInString()
	{
		return flowToString(flowControlIn);
	}

	public String getFlowControlOutString()
	{
		return flowToString(flowControlOut);
	}

	private String flowToString(int flowControl)
	{

		switch(flowControl)
		{
		case SerialPort.FLOWCONTROL_NONE:
			return "None";
		case SerialPort.FLOWCONTROL_XONXOFF_OUT:
			return "Xon/Xoff Out";
		case SerialPort.FLOWCONTROL_XONXOFF_IN:
			return "Xon/Xoff In";
		case SerialPort.FLOWCONTROL_RTSCTS_IN:
			return "RTS/CTS In";
		case SerialPort.FLOWCONTROL_RTSCTS_OUT:
			return "RTS/CTS Out";
		default:
			return "None";
		}
	}

	public String getParityString()
	{
		switch(parity)
		{
		case SerialPort.PARITY_NONE:
			return "None";
		case SerialPort.PARITY_EVEN:
			return "Even";
		case SerialPort.PARITY_ODD:
			return "Odd";
		default:
			return "None";
		}
	}

	public String getStopbitsString()
	{
		switch(stopbits)
		{
		case SerialPort.STOPBITS_1:
			return "1";
		case SerialPort.STOPBITS_1_5:
			return "1.5";
		case SerialPort.STOPBITS_2:
			return "2";
		default:
			return "1";
		}
	}

	public int stringToFlow(String flowControl)
	{
		if(flowControl.equals("None"))
		{
			return SerialPort.FLOWCONTROL_NONE;
		}
		if(flowControl.equals("Xon/Xoff Out"))
		{
			return SerialPort.FLOWCONTROL_XONXOFF_OUT;
		}
		if(flowControl.equals("Xon/Xoff In"))
		{
			return SerialPort.FLOWCONTROL_XONXOFF_IN;
		}
		if(flowControl.equals("RTS/CTS In"))
		{
			return SerialPort.FLOWCONTROL_RTSCTS_IN;
		}
		if(flowControl.equals("RTS/CTS Out"))
		{
			return SerialPort.FLOWCONTROL_RTSCTS_OUT;
		}
		return SerialPort.FLOWCONTROL_NONE;
	}

	public void setFlowControlIn(int flowControlIn)
	{
		this.flowControlIn = flowControlIn;
	}

	public void setFlowControlOut(int flowControlOut)
	{
		this.flowControlOut = flowControlOut;
	}
}

package org.iii.nmi.air.report;

import java.util.Properties;

import org.iii.nmi.air.crc.CRC16;
import org.iii.nmi.air.factory.InstanceFactory;
import org.iii.nmi.air.reqlist.RequestList;

public class ReportData extends Thread
{
	private boolean deadFlag, reportStopFlag;

	Properties properties = null;

	private String masterIpPowerId;

	private String readByte;

	private String startByte;

	private String endByte;

	private long time;

	private String[] masterIpPowerIds;

	public ReportData(boolean deadFlag, boolean reportStopFlag)
	{
		this.deadFlag = deadFlag;
		this.reportStopFlag = reportStopFlag;
	}

	public void setDeadFlag(boolean deadFlag)
	{
		this.deadFlag = deadFlag;
	}

	public void setReportStopFlag(boolean reportStopFlag)
	{
		this.reportStopFlag = reportStopFlag;
	}

	public void setProperties(Properties properties)
	{
		this.properties = properties;
		this.masterIpPowerId = properties.getProperty("MasterIpPowerId");
		this.readByte = properties.getProperty("ReadByte");
		this.startByte = properties.getProperty("StartByte");
		this.endByte = properties.getProperty("EndByte");
		this.time = Long.parseLong(properties.getProperty("Time"));
		this.masterIpPowerIds = masterIpPowerId.split(";");

	}

	public void run()
	{

		while(true)
		{

			if(deadFlag == true || reportStopFlag == true)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch(InterruptedException e)
				{

					e.printStackTrace();
				}
			}
			else
			{
				if(masterIpPowerIds.length == 0)
					continue;

				for(int i = 0; i < masterIpPowerIds.length; i++)
				{
					String[] ids = masterIpPowerIds[i].split(",");

					if(ids[1] == null || ids[1].equals(""))
						break;

					String masterIp = ids[0];

					for(int j = 1; j < ids.length; j++)
					{
						String powerId = ids[j];

						String command = masterIp + ";" + readByte + ";" + powerId + ";" + startByte + ";00;" + endByte + ";";
						String[] datas = command.split(";");

						byte[] bytes = new byte[datas.length];

						for(int k = 0; k < datas.length; k++)
						{

							bytes[k] = (byte) Integer.parseInt(datas[k], 16);
						}

						String crcStr = Integer.toHexString(CRC16.crc16(bytes));
						if(crcStr.length() == 1)
						{
							crcStr = "000" + crcStr;
						}
						else if(crcStr.length() == 2)
						{
							crcStr = "00" + crcStr;
						}
						else if(crcStr.length() == 3)
						{
							crcStr = "0" + crcStr;
						}
						int count = crcStr.length() - 4;

						String crc16H = crcStr.substring(count, count + 2);
						String crc16L = crcStr.substring(count + 2, count + 4);

						String reqCmd = command + crc16L + ";" + crc16H + ";";

						RequestList reqList = InstanceFactory.createRequestList();
						reqList.add(reqCmd);
					}

				}

				try
				{
					Thread.sleep(time * 1000);
				}
				catch(InterruptedException e)
				{

					e.printStackTrace();
				}
			}

		}
	}

}

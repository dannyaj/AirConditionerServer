package org.iii.nmi.air.handler;

import org.iii.nmi.air.factory.InstanceFactory;
import org.iii.nmi.air.log.ExceptionHandler;
import org.iii.nmi.air.log.LogHandler;
import org.iii.nmi.air.reqlist.RequestList;

public abstract class Handler
{

	protected final String readError = "83";

	protected final String writeError = "86";

	protected final String powerStatus = "10";

	protected final String lockStatus = "11";

	protected final String setMode = "12";

	protected final String setFanSpeed = "13";

	protected final String alarmStatus = "14";

	protected final String valveContact = "15";

	protected final String fanSpeedContact = "16";

	protected final String chillContact = "17";

	protected final String heatingContact = "18";

	protected final String roomTemp = "1A";

	protected final String setPoint = "1B";

	protected final String writeByte = "6";

	protected final String readByte = "3";

	protected final String allByteHex = "18";
	
	protected final double hm = 50;

	protected static String tableName = "AIRCONDID";

	public LogHandler logHandler;

	public ExceptionHandler exceptionHandler;

	public RequestList reqList;

	public Handler()
	{
		this.logHandler = InstanceFactory.createLogHandler();
		this.exceptionHandler = InstanceFactory.createExceptionHandler();
		this.reqList = InstanceFactory.createRequestList();
	}

	public abstract void processReqCommand(String reqCmd);

	public abstract void processRspCommand(String RspCmd);

	double PMV_Calc(double TempData, double HumidData)
	{
		double PMV; // PMV
		double CLO = 1.0; // 著衣量
		double MET = 1.2; // 代謝率
		double WME = 0; // 人體所做的機械功
		double TA = TempData; // 空氣溫度
		double TR = 22; // 輻射溫度
		double VEL = 0.2; // 空氣流速
		double RH = HumidData; // 濕度
		double PA = 0; // 人體周遭水分子的空氣壓力

		if(PA == 0)
			PA = RH * 10 * Math.exp(16.6536 - 4030.183 / (TA + 235));

		double ICL = 0.155 * CLO;
		double M = MET * 58.15;
		double W = WME * 58.15;
		double MW = M - W;

		double FCL;
		if(ICL < 0.078)
			FCL = 1 + 1.29 * ICL;
		else
			FCL = 1.05 + 0.648 * ICL;

		double HCF = 12.1 * Math.sqrt(VEL);
		double TAA = TA + 273;
		double TRA = TR + 273;

		double TCLA = TAA + (35.5 - TA) / (3.5 * (6.45 * ICL + 0.1));

		double P1 = ICL * FCL;
		double P2 = P1 * 3.96;
		double P3 = P1 * 100;
		double P4 = P1 * TAA;
		double P5 = 308.7 - 0.028 * MW + P2 * Math.pow((TRA / 100), 4);
		double XN = TCLA / 100;
		double XF = XN;
		double N = 0;
		double EPS = 0.00015;

		double HCN, HC = 0;
		while((N == 0) || (Math.abs(XF - XN) > EPS))
		{
			XF = (XF + XN) / 2;
			HCN = 2.38 * Math.pow(Math.abs(100 * XF - TAA), 0.25);

			if(HCF > HCN)
				HC = HCF;
			else
				HC = HCN;

			XN = (P5 + P4 * HC - P2 * Math.pow(XF, 4)) / (100 + P3 * HC);
			N = N + 1;

			if(N > 150)
			{
				System.out.println(" The times of iteration is 150 !! ");
				break;
			}
		}

		double TCL = 100 * XN - 273;
		double HL1 = 3.05 * 0.001 * (5733 - 6.99 * MW - PA);

		double HL2;
		if(MW > 58.15)
			HL2 = 0.42 * (MW - 58.15);
		else
			HL2 = 0;

		double HL3 = 1.7 * 0.00001 * M * (5867 - PA);
		double HL4 = 0.0014 * M * (34 - TA);
		double HL5 = 3.96 * FCL * (Math.pow(XN, 4) - Math.pow((TRA / 100), 4));
		double HL6 = FCL * HC * (TCL - TA);
		double TS = 0.303 * Math.exp(-0.036 * M) + 0.028;

		PMV = TS * (MW - HL1 - HL2 - HL3 - HL4 - HL5 - HL6);
		return PMV;
	}
}

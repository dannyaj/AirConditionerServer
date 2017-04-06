package org.iii.nmi.air.sendcmd;

import java.io.IOException;

import org.iii.nmi.air.factory.InstanceFactory;
import org.iii.nmi.air.log.LogHandler;
import org.iii.nmi.air.reqlist.RequestList;
import org.iii.nmi.air.rsprocess.Rsprocess;
import org.iii.nmi.air.socket.SerialConnection;
import org.iii.nmi.air.util.RspSqlHandler;

public class SendCommand extends Thread
{

	private RequestList reqList;

	private Rsprocess rsProcess;

	private SerialConnection serialConnection;

	private boolean isClosed;

	private LogHandler logHandler;

	public SendCommand(Rsprocess rsProcess, SerialConnection serialConnection)
	{
		this.reqList = InstanceFactory.createRequestList();
		this.rsProcess = rsProcess;
		this.serialConnection = serialConnection;
		this.logHandler = InstanceFactory.createLogHandler();
		this.isClosed = false;

	}

	public void setClosed(boolean isClosed)
	{
		this.isClosed = isClosed;

	}

	public void run()
	{
		logHandler.info("SendCommand already run");
		int count = 0;
		while(true)
		{
			if(isClosed == true)
			{
				break;
			}

			if(RspSqlHandler.getPowerId() != null)
			{
				count++;
			}

			if(count == 10)
			{
				RspSqlHandler.clear();
				logHandler.info("receive com port response Timeout or process response Timeout count = 10 ");
				count = 0;
			}
			if(rsProcess.getCommandSize() == 0 && RspSqlHandler.getPowerId() == null)
			{

				String cmd = reqList.getCommand();

				if(cmd != null)
				{

					String[] cmds = cmd.split(";");
					String powerId = cmds[2];
					int dataLocate = Integer.parseInt(cmds[3], 16);
					RspSqlHandler.setPowerId(powerId);
					RspSqlHandler.setDataLocate(dataLocate);

					/*if(serialConnection.getForwardSocket() != null)
					{
						try
						{
							serialConnection.getForwardSocket().sendToMessage("powerId;"+powerId + "\n");
							serialConnection.getForwardSocket().sendToMessage("dataLocate;"+dataLocate + "\n");
							
							logHandler.info("forward server PowerId: "+powerId+ " DataLocate: "+dataLocate);
						}
						catch(IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}*/

					serialConnection.sendCommand(cmd);

				}

			}

			try
			{
				Thread.sleep(1500);
			}
			catch(InterruptedException e)
			{

				e.printStackTrace();
			}

		}

		logHandler.info("SendCommandThread already closed");

	}
}

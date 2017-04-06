package org.iii.nmi.air.handler;

import java.util.ArrayList;
import java.util.List;

public class HandlerPool
{
	private int size;

	private List<Handler> reqList = new ArrayList<Handler>();

	private List<Handler> rspList = new ArrayList<Handler>();

	public HandlerPool(int size)
	{
		this.size = size;

		for(int i = 0; i < size; i++)
		{
			reqList.add(new RequestHandler());

			rspList.add(new ResponseHandler());
		}

	}

	public Handler getRequestHandler()
	{
		int lastIndex = reqList.size() - 1;
		if(lastIndex != -1)
		{
			return reqList.remove(lastIndex);
		}
		else
		{
			return new RequestHandler();
		}
	}

	public void releaseRequestHandler(Handler requestHandler)
	{
		if(reqList.size() == size)
		{
			return;
		}
		else
		{
			reqList.add(requestHandler);
		}
	}

	public Handler getResponseHandler()
	{
		int lastIndex = rspList.size() - 1;
		if(lastIndex != -1)
		{
			return rspList.remove(lastIndex);
		}
		else
		{
			return new ResponseHandler();
		}
	}

	public void releaseResponseHandler(Handler rspHandler)
	{
		if(rspList.size() == size)
		{
			return;
		}
		else
		{
			rspList.add(rspHandler);
		}
	}
}

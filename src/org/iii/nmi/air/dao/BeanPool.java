package org.iii.nmi.air.dao;

import java.util.ArrayList;
import java.util.List;

public class BeanPool
{
	private static int size;

	private static List<Aircondinfo> aircondinfoList = new ArrayList<Aircondinfo>();

	private static List<Aircondprofile> aircondprofileList = new ArrayList<Aircondprofile>();

	private static List<Webaircondlog> webaircondlogList = new ArrayList<Webaircondlog>();

	public BeanPool(int size)
	{
		BeanPool.size = size;

		for(int i = 0; i < size; i++)
		{
			aircondinfoList.add(new Aircondinfo());
			aircondprofileList.add(new Aircondprofile());
			webaircondlogList.add(new Webaircondlog());
		}
	}

	public static Aircondinfo getAircondinfo()
	{
		int lastIndex = aircondinfoList.size() - 1;
		if(lastIndex != -1)
		{
			return aircondinfoList.remove(lastIndex);
		}
		else
		{
			return new Aircondinfo();
		}
	}

	public static void releaseAircondinfo(Aircondinfo aircondinfo)
	{
		if(aircondinfoList.size() == size)
		{
			return;
		}
		else
		{
			aircondinfoList.add(aircondinfo);
		}
	}

	public static Aircondprofile getAircondprofile()
	{
		int lastIndex = aircondprofileList.size() - 1;
		if(lastIndex != -1)
		{
			return aircondprofileList.remove(lastIndex);
		}
		else
		{
			return new Aircondprofile();
		}
	}

	public static void releaseAircondprofile(Aircondprofile aircondprofile)
	{
		if(aircondprofileList.size() == size)
		{
			return;
		}
		else
		{
			aircondprofileList.add(aircondprofile);
		}
	}

	public static Webaircondlog getWebaircondlog()
	{
		int lastIndex = webaircondlogList.size() - 1;
		if(lastIndex != -1)
		{
			return webaircondlogList.remove(lastIndex);
		}
		else
		{
			return new Webaircondlog();
		}
	}

	public static void releaseWebaircondlog(Webaircondlog webaircondlog)
	{
		if(webaircondlogList.size() == size)
		{
			return;
		}
		else
		{
			webaircondlogList.add(webaircondlog);
		}
	}
}

package org.iii.nmi.air.test.web;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class OvalBorder implements Border
{
	protected int ovalWidth = 5;
	protected int ovalHeight = 5;
	
	protected boolean ovalTopLeft = true;
	protected boolean ovalTopRight = true;
	protected boolean ovalBottomLeft = true;
	protected boolean ovalBottomRight = true;
	
	protected Color lightColor = Color.LIGHT_GRAY;
	protected Color darkColor = Color.GRAY;
	
	public OvalBorder()
	{
	}
	
	public OvalBorder(int _w, int _h)
	{
		ovalWidth = _w;
		ovalHeight = _h;
	}
	
	public OvalBorder(int _w, int _h, Color _topColor, Color _bottomColor)
	{
		ovalWidth = _w;
		ovalHeight = _h;
		lightColor = _topColor;
		darkColor = _bottomColor;
	}
	
	public void setOval(boolean _tl, boolean _tr, boolean _bl, boolean _br)
	{
		ovalTopLeft = _tl;
		ovalTopRight = _tr;
		ovalBottomLeft = _bl;
		ovalBottomRight = _br;
	}
	
	public Insets getBorderInsets(Component c) {
		// TODO Auto-generated method stub
		return new Insets(ovalHeight, ovalWidth, ovalHeight, ovalWidth);
	}

	public boolean isBorderOpaque() {
		// TODO Auto-generated method stub
		return true;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		width--;
		height--;
		
		int ovalHeightTL = ovalHeight;
		int ovalHeightBL = ovalHeight;
		int ovalHeightTR = ovalHeight;
		int ovalHeightBR = ovalHeight;
		int ovalWidthTL = ovalWidth;
		int ovalWidthBL = ovalWidth;
		int ovalWidthTR = ovalWidth;
		int ovalWidthBR = ovalWidth;
		
		if(!ovalTopLeft)
		{
			ovalHeightTL = 0;
			ovalWidthTL = 0;
		}
		
		if(!ovalTopRight)
		{
			ovalHeightTR = 0;
			ovalWidthTR = 0;
		}
		
		if(!ovalBottomLeft)
		{
			ovalHeightBL = 0;
			ovalWidthBL = 0;
		}
		
		if(!ovalBottomRight)
		{
			ovalHeightBR = 0;
			ovalWidthBR = 0;
		}
		
		g.setColor(lightColor);
		g.drawLine((x + 1), (y - 1) + height - ovalHeightBL, (x + 1), (y + 1) + ovalHeightTL);
		g.drawArc((x + 1), (y + 1), 2 * ovalWidthTL, 2 * ovalHeightTL, 180, -90);
		g.drawLine((x + 1) + ovalWidthTL, (y + 1), (x - 1) + width - ovalWidthTR, (y + 1));
		g.drawArc((x - 1) + width - 2 * ovalWidthTR, (y + 1), 2 * ovalWidthTR, 2 * ovalHeightTR, 90, -90);

		g.setColor(darkColor);
		g.drawLine((x - 1) + width, (y - 1) + ovalHeightTR, (x - 1) + width, (y - 1) + height - ovalHeightBR);
		g.drawArc((x - 1) + width - 2 * ovalWidthTR, (y - 2) + height - 2 * ovalHeightBR, 2 * ovalWidthBR, 2 * ovalHeightBR, 0, -90);
		g.drawLine((x + 1) + ovalWidthBL, (y - 1) + height, (x - 1) + width - ovalWidthBR, (y - 1) + height);
		g.drawArc((x + 1), (y - 1) + height - 2 * ovalHeightBL, 2 * ovalWidthBL, 2 * ovalHeightBL, -90, -90);
	}

}

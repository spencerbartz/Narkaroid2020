package com.spencerbartz.narkaroid;

import java.awt.*;

public class Ending
{
	int xCoord, yCoord;
	
	public Ending()
	{
		reset();
	}
	
	public void reset()
	{
		xCoord = 200;
		yCoord = 500;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, RenderingPanel.SCREEN_WIDTH, RenderingPanel.SCREEN_HEIGHT);
		
		g.setColor(Color.white);
		g.drawString("Congratulations You won!", xCoord, yCoord);
		g.drawString("CREDITS", xCoord + 50, yCoord + 40);
		g.drawString("Programming - Spencer Bartz", xCoord + 10, yCoord + 60);
		g.drawString("Graphics - Spencer Bartz", xCoord + 10, yCoord + 80);
		g.drawString("Music - Anthony Hager", xCoord + 10, yCoord + 100);
		g.drawString("Testing - Spencer Bartz", xCoord + 10, yCoord + 120);
		//g.drawString("Special thanks to ", xCoord + 10, yCoord + 140);
	
		yCoord--;
	}
	
	public boolean isFinished()
	{
		return yCoord == -180;
	}
}

package com.spencerbartz.narkaroid;

import java.awt.Image;
import java.awt.Graphics;

public class Laser implements Collidable
{
	private int xCoord, yCoord;
	private int width = 8; 
	public static int height = 28;
	private int speed = 5;
	private boolean offScreen = false;
	public static int DEFAULT_REACT = 0;
	
	static Image laserImage;
	
	public static final int BLOCK_REACT = 100;
	
	public void draw(Graphics g)
	{
		g.drawImage(laserImage, xCoord, yCoord, null);
	}
	
	public void move()
	{
		if(yCoord <= -height)
		{
			offScreen = true;
			return;
		}
		
		yCoord -= speed;
	}

	public void collide(Collidable c)
	{
		offScreen = true;
	}

	public int getHeight()
	{
		return height;
	}

	public int getWidth()
	{
		return width;
	}

	public int getXCoord()
	{
		return xCoord;
	}
	
	public void setXCoord(int xCoord)
	{
		this.xCoord = xCoord;
	}

	public int getYCoord()
	{
		return yCoord;
	}
	
	public void setYCoord(int yCoord)
	{
		this.yCoord = yCoord;
	}

	public void react(int reactionCode)
	{
		offScreen = true;
	}
	
	public boolean isOffScreen()
	{
		return offScreen;
	}

	public int getSpeed()
	{
		return speed;
	}
}
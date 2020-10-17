package com.spencerbartz.narkaroid;


import java.awt.Image;
import java.awt.Graphics;

public class PowerUp implements Collidable
{
	private int xCoord, yCoord;
	private int originalYCoord;
	private int width, height;
	private boolean activated = false;
	private boolean offScreen = false;
	private int pwrUpType;

	public static final int DEFAULT_WIDTH = 40;
	public static final int DEFAULT_HEIGHT = 25;
	public static final int PADDLE_REACT = 50;
	public static final int DROP_SPEED = 2;
	
	//codes for different types of power ups
	static final int SPEEDUP = 0;
	static final int LASER = 1;
	static final int EXTRA_LIFE = 2;
	
	//total power ups
	static final int PWRUP_TYPES = 3;	
	static final Image [] PWRUP_IMAGES = new Image[PWRUP_TYPES];
	
	public PowerUp(int pwrUpType)
	{
		this.pwrUpType = pwrUpType;
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
	}
	
	public void activate()
	{
		activated = true;
	}
	
	public void draw(Graphics g)
	{
		if(activated && !offScreen)
			g.drawImage(PWRUP_IMAGES[pwrUpType], xCoord, yCoord, null);
	}
	
	public void move()
	{
		if(activated)
		{
			yCoord += DROP_SPEED;
		
			if(yCoord > 600)
				offScreen = true;
		}
	}
	
	public void collide(Collidable c)
	{
		
	}
	
	public void react(int reactionCode)
	{
		offScreen = true;
		activated = false;
		yCoord = 700;
	}
	
	public void reset()
	{
		activated = false;
		offScreen = false;
		yCoord = originalYCoord;
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
		originalYCoord = yCoord;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}

	public int getSpeed()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int getType()
	{
		return pwrUpType;
	}
	
	public boolean isOffScreen()
	{
		return offScreen;
	}
	
	public boolean isActivated()
	{
		return activated;
	}
}

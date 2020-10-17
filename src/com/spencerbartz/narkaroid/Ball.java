package com.spencerbartz.narkaroid;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Ball implements Drawable, Collidable
{
	private int width, height;
	private int xCoord, yCoord;
	
	//set to true if heading in the positive x direction, or positive y direction
	private boolean xHeading, yHeading;
	private int speed;
	private int baseSpeedX;
	private int baseSpeedY;
	private boolean offScreen = false;
	private boolean started = false;
	private BufferedImage ballImg;
	
	public static final int PADDLE_REACT = 0;
	public static final int PADDLE_REACT_MOVING = 1;
	public static final int BLOCK_REACT_BOTTOM = 2;
	public static final int BLOCK_REACT_LEFT = 3;
	public static final int BLOCK_REACT_RIGHT = 4;
	public static final int BLOCK_REACT_TOP = 5;
	
	public Ball()
	{
		ImageLoader imageLoader = ImageLoader.INSTANCE;
		ballImg = imageLoader.loadImage("ball.gif");
		reset();
	}
	
	public void reset()
	{
		width = ballImg.getWidth(null);
		height = ballImg.getHeight(null);
		xCoord = (RenderingPanel.SCREEN_WIDTH / 2) - (width / 2);
		yCoord = 385;	
		xHeading = (int)(Math.random() * 2) == 1 ? false : true;
		yHeading = (int)(Math.random() * 2) == 1 ? false : true;
		speed = 0;
		baseSpeedX = 3;
		baseSpeedY = 3;
		offScreen = false;
		started = false;
	}
	
	public void draw(Graphics g)
	{
		if(offScreen)
			return;
		
		g.drawImage(ballImg, xCoord, yCoord, null);
	}
	
	public void move()
	{
		if(!started)
			return;
		
		//bounce off left side of screen (start heading in positive x direction)
		if(xCoord <= 0)
			xHeading = true;
		
		//bounce off right side of screen (start heading in negative x direction)
		if(xCoord + width >= RenderingPanel.SCREEN_WIDTH)
			xHeading = false;
		
		//bounce off top of screen (start heading in positive y)
		if(yCoord <= 0)
			yHeading = true;
		
		//if ball hits bottom of screen, player dies
		if(yCoord + height >= RenderingPanel.SCREEN_HEIGHT)
			offScreen = true;
		
		//add or subtract to x coordinate, causing x coord location change on next repaint
		if(xHeading)
			xCoord += (baseSpeedX + speed);
		else
			xCoord -= (baseSpeedX + speed);
		
		//add or subtract to y coordinate, causing y coord location change on next repaint
		if(yHeading)
			yCoord += (baseSpeedY + speed);
		else
			yCoord -= (baseSpeedY + speed);	
		
		//System.out.println("X: " + xCoord + " Y: " + yCoord);
	}
	
	public void collide(Collidable c)
	{
		
	}
	
	//When ball collides with something, that object tells the ball what it was (by the reaction code)
	//based on the reaction code, the ball decides how to react to the colliion
	public void react(int reactionCode)
	{
		//if we hit the paddle, start moving in the negative y direction
		if(reactionCode == PADDLE_REACT)
			yHeading = false;
		else if(reactionCode == PADDLE_REACT_MOVING)
		{
			yHeading = false;
			//if(speed == 0)
				//speed = (int)(Math.random() * 3);
		}
		else if(reactionCode == BLOCK_REACT_BOTTOM)
		{
			yHeading = true;
			
			/*
			if((int)(Math.random() * 1) == 0)
				if(speed > 0)
					speed--;
			*/
		}
		else if(reactionCode == BLOCK_REACT_LEFT)
		{
			xHeading = false;
		}
		else if(reactionCode == BLOCK_REACT_RIGHT)
		{
			xHeading = true;
		}
		else if(reactionCode == BLOCK_REACT_TOP)
		{
			yHeading = false;
		}
	}
	
	public int getXCoord()
	{
		return xCoord;
	}
	
	public int getYCoord()
	{
		return yCoord;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void start()
	{
		started = true;
	}
	
	public boolean isStarted()
	{
		return started;
	}
	
	public void setXCoord(int xCoord)
	{
		this.xCoord = xCoord;
	}
	
	public boolean isOffScreen()
	{
		return offScreen;
	}

	public int getSpeed()
	{
		return baseSpeedY + speed;
	}
}

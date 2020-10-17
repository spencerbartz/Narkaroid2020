package com.spencerbartz.narkaroid;

import java.awt.Graphics;
import java.awt.Image;

public class Block implements Drawable, Collidable
{
	private int xCoord, yCoord;
	private int width, height;
	private boolean destroyed = false;
	private int blockType;
	private int originalBlockType;
	private PowerUp powerUp = null;
	
	public static final int DEFAULT_WIDTH = 40;
	public static final int DEFAULT_HEIGHT = 25;
	public static final int BALL_REACT = 100;
	
	static final int GREEN_BLOCK = 0;
	static final int RED_BLOCK = 1;
	static final int ORANGE_BLOCK = 2;
	static final int BLUE_BLOCK = 3;
	static final int YELLOW_BLOCK = 4;
	static final int BLOCK_TYPES = 5;	
	static final Image [] BLOCK_IMAGES = new Image[BLOCK_TYPES];
	
	//constructor for block whose xCoord and yCoord will be set later by a BlockHolder (special arrangement)
	public Block(int blockType)
	{
		this(0,0, blockType);
	}
	
	//same as above but with power up capability
	public Block(int blockType, PowerUp powerUp)
	{
		this(0,0, blockType);
		this.powerUp = powerUp;
	}
	
	public Block(int xCoord, int yCoord)
	{
		this(xCoord, yCoord, DEFAULT_WIDTH, DEFAULT_HEIGHT, GREEN_BLOCK);
	}
	
	public Block(int xCoord, int yCoord, int blockType)
	{
		this(xCoord, yCoord, DEFAULT_WIDTH, DEFAULT_HEIGHT, blockType);
	}
	
	public Block(int xCoord, int yCoord, int width, int height, int blockType)
	{
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.width = width;
		this.height = height;
		this.blockType = blockType;
		this.originalBlockType = blockType;
	}
	
	//reset one block
	public void reset()
	{
		destroyed = false;
		blockType = originalBlockType;
		
		if(powerUp != null)
			powerUp.reset();
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(BLOCK_IMAGES[blockType], xCoord, yCoord, BLOCK_IMAGES[blockType].getWidth(null), BLOCK_IMAGES[blockType].getHeight(null), null);
	}
	
	public void collide(Collidable c)
	{
		boolean bottom = false;
		boolean top = false;
		boolean left = false;
		boolean right = false;
		StringBuffer debug = new StringBuffer();
		
		boolean collide = false;
//		collision from bottom
		for(int i = c.getSpeed() - 1; i >= 0; i--)
		{
			if(c.getYCoord() == yCoord + height - i)
				collide = true;
		}
			
		if
		(
				(collide) && 
				(c.getXCoord() + c.getWidth() > xCoord && c.getXCoord() < xCoord + width)
		)
		{
			bottom = true;
			//c.react(Ball.BLOCK_REACT_BOTTOM);
			//react(BALL_REACT);
			debug.append("bottom - BALL X: " + c.getXCoord() + " Y: " + c.getYCoord() + " BLOCK X: " + xCoord + " Ybot: " + (yCoord + height));
		}
//		collision from top
		else if
		(
			(c.getYCoord() + c.getHeight() == yCoord || c.getYCoord() + c.getHeight() == yCoord + 1 || c.getYCoord() + c.getHeight() == yCoord + 2) &&
			(c.getXCoord() + c.getWidth() > xCoord && c.getXCoord() < xCoord + width)
		)
		{
			top = true;
			//c.react(Ball.BLOCK_REACT_TOP);
			//react(BALL_REACT);
			debug.append("top - BALL X: " + c.getXCoord() + " Ybot: " + (c.getYCoord() + c.getHeight()) + " BLOCK X: " + xCoord + " Y: " + yCoord);
		}
		//collision from left side
		else if
		(
			(c.getXCoord() + c.getWidth() == xCoord || c.getXCoord() + c.getWidth() == xCoord + 1 || c.getXCoord() + c.getWidth() == xCoord + 2)  &&
			(c.getYCoord() < yCoord + height && c.getYCoord() + c.getHeight() > yCoord)
		)
		{
			left = true;
			//c.react(Ball.BLOCK_REACT_LEFT);
			//react(BALL_REACT);
			debug.append("left - BALL Xrt: " + (c.getXCoord() + c.getWidth()) + " Y: " + c.getYCoord() + " BLOCK X: " + xCoord + " Y: " + yCoord);
		}
		//collision from right side
		else if
		(
			(c.getXCoord() == xCoord + width || c.getXCoord() == xCoord + width - 1 || c.getXCoord() == xCoord + width - 2) &&
			(c.getYCoord() < yCoord + height && c.getYCoord() + c.getHeight() > yCoord)
		)
		{
			right = true;
			//c.react(Ball.BLOCK_REACT_RIGHT);
			//react(BALL_REACT);
			debug.append("right - BALL X: " + c.getXCoord() + " Y: " + c.getYCoord() + " BLOCK Xrt: " + (xCoord + width) + " Y: " + yCoord);
		}
	
		
		if(top)
		{
			if(c instanceof Ball)
			{
				c.react(Ball.BLOCK_REACT_TOP);
			}
			else if(c instanceof Laser)
			{
				c.react(Laser.DEFAULT_REACT);
			}
		}
		
		if(bottom)
		{
			if(c instanceof Ball)
			{
				c.react(Ball.BLOCK_REACT_BOTTOM);
			}
			else if(c instanceof Laser)
			{
				c.react(Laser.DEFAULT_REACT);
			}
		}
		
		if(left)
		{
			if(c instanceof Ball)
			{
				c.react(Ball.BLOCK_REACT_LEFT);
			}
			else if(c instanceof Laser)
			{
				c.react(Laser.DEFAULT_REACT);
			}
		}
		
		if(right)
		{
			if(c instanceof Ball)
			{
				c.react(Ball.BLOCK_REACT_RIGHT);
			}
			else if(c instanceof Laser)
			{
				c.react(Laser.DEFAULT_REACT);
			}
		}
		

		//TODO: what is this?
		if(debug.length() > 0)
		{
			//System.out.println(debug.toString());
			react(BALL_REACT);
		}
	}
	
	public void react(int reactionCode)
	{
		if(reactionCode == BALL_REACT)
		{
			blockType--;
			
			if(blockType == -1)
				destroyed = true;
		}
		
		if(destroyed && powerUp != null)
			powerUp.activate();
	}
	
	public int getXCoord()
	{
		return xCoord;
	}
	
	public void setXCoord(int xCoord)
	{
		this.xCoord = xCoord;
		if(powerUp != null)
			powerUp.setXCoord(xCoord);
	}
	
	public int getYCoord()
	{
		return yCoord;
	}
	
	public void setYCoord(int yCoord)
	{
		this.yCoord = yCoord;
		if(powerUp != null)
			powerUp.setYCoord(yCoord);
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public boolean isDestroyed()
	{
		return destroyed;
	}
	
	public void setPowerUp(PowerUp powerUp)
	{
		this.powerUp = powerUp;
		powerUp.setXCoord(xCoord);
		powerUp.setYCoord(yCoord);
	}

	public boolean hasPowerUp()
	{
		return powerUp != null;
	}
	
	public PowerUp getPowerUp()
	{
		return powerUp;
	}
	
	public int getSpeed()
	{
		return 0;
	}
}

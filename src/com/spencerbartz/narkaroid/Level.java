package com.spencerbartz.narkaroid;

import java.awt.*;
import java.awt.image.BufferedImage;

/*
 States of a level:
 1: Intro
 	started = false, cleared = false, finished = false
 2: Active 
 	started = true, cleared = false, finished = false
 3: Cleared (show outro the second cleared = true)
    started = true, cleared = true, finished = false
 4: Finished (advance to next level)
 	started = true, cleared = true, finished = true
 */

public class Level 
{
	private Image bg;
	private Image startImg;
	static Image outroImage;
	private Image contImg;
	static Image pausedImage;
	private BlockHolder blockHolder;
	private int introDelay;
	private int outroDelay;
	private int contDelay;
	private boolean started = false;
	private boolean cleared = false;
	private boolean cont = false;
	private boolean finished = false;
	private static final int DEF_INTRO_DELAY = 100;
	private static final int DEF_OUTRO_DELAY = 100;
	private static final int DEF_CONT_DELAY = 200;
	
	LevelManager parent;
	
	//TODO decouple this class with its parent class, LevelManager
	public Level(Image bg, Image startImg, Image contImg, BlockHolder blockHolder, LevelManager parent)
	{
		this(bg, startImg, contImg, blockHolder, DEF_INTRO_DELAY, DEF_OUTRO_DELAY, DEF_CONT_DELAY);
		this.parent = parent;
	}
	
	public Level(Image bg, Image startImg, Image contImg, BlockHolder blockHolder, int introDelay, int outroDelay, int contDelay)
	{
		this.bg = bg;
		this.startImg = startImg;
		this.contImg = contImg;
		this.blockHolder = blockHolder;
		this.introDelay = introDelay;
		this.outroDelay = outroDelay;
		this.contDelay = contDelay;
		
		ImageLoader imageLoader = ImageLoader.INSTANCE;
		//load image for level cleared
		BufferedImage clearImage = imageLoader.loadImage("levelclear.gif");
		Level.outroImage = clearImage;
				
		// load image for paused
		BufferedImage pausedImage = imageLoader.loadImage("paused.gif");
		Level.pausedImage = pausedImage;
	}
	
	public void reset()
	{
		started = false;
		cleared = false;
		cont = false;
		finished = false;
		blockHolder.reset();
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(bg, 0, 0, null);
		blockHolder.draw(g);
	}
	
	//This function is called the first time we enter the level
	//A graphic 'Level N Start' is shown on the screen for DEF_INTRO_DELAY seconds
	//During this time the paddle cannot move
	public void intro(Graphics g)
	{
		int xPos = RenderingPanel.SCREEN_WIDTH / 2 - startImg.getWidth(null) / 2;
		int yPos = RenderingPanel.SCREEN_HEIGHT / 2 - startImg.getHeight(null) / 2;
		
		if(!started)
			g.drawImage(startImg, xPos, yPos, null);
		
		introDelay--;
		
		if(introDelay == 0)
		{
			started = true;
			//reset delay in case we start this level again sometime
			introDelay = DEF_INTRO_DELAY;
			//System.out.println("LEVEL " + parent.getCurrentLevelNum() + " INTRO FINISHED");
		}
	}
	
	//This function is called when player has died and wishes to continue playing
	//A graphic stating current level is shown on screen for DEF_CONT_DELAY seconds
	//During this time the paddle cannot move
	public void cont(Graphics g)
	{
		//x,y coordinates for 'Level N' image
		int xPos = RenderingPanel.SCREEN_WIDTH / 2 - contImg.getWidth(null) / 2;
		int yPos = RenderingPanel.SCREEN_HEIGHT / 2 - contImg.getHeight(null) / 2;
		
		//The image 'Level N' is shown on screen instead of 'Level N Start'
		if(cont)
			g.drawImage(contImg, xPos, yPos, null);
		
		contDelay--;
		
		if(contDelay == 0)
		{
			
			System.out.println("LEVEL " + parent.getCurrentLevelNum() + " CONTINUE FINISHED");
			//when cont = false, stop showing 'Level N' graphic on screen, gameplay can resume 
			cont = false;
			//reset delay in case of additional continues
			contDelay = DEF_CONT_DELAY;
		}
	}
	
	//TODO remove this "parent" stuff and do it within LevelManager.levelOutro()
	public void outro(Graphics g, LevelManager parent)
	{	
		int xPos = RenderingPanel.SCREEN_WIDTH / 2 - outroImage.getWidth(null) / 2;
		int yPos = RenderingPanel.SCREEN_HEIGHT / 2 - outroImage.getHeight(null) / 2;
		
		if(!finished)
			g.drawImage(outroImage, xPos, yPos, null);
		
		outroDelay--;
		
		//System.out.println("Outro Delay " + outroDelay);
		
		//TODO this is in the wrong place and needs to be refactored
		if(outroDelay == 0)
		{
			if(parent.allPowerUpsOffScreen())
			{
				//System.out.println("LEVEL " + parent.getCurrentLevelNum() + " OUTRO FINISHED");
				
				finished = true;
				started = false;
				//parent.printLevelState();
				//advance to the next level
				if(parent.getCurrentLevelNum() < parent.NUM_LEVELS - 1)
					parent.setCurrentLevel(parent.getCurrentLevelNum() + 1);
				else
					parent.setGameWon(true);
			}

			//reset delay in case we finish this level again sometime
			outroDelay = DEF_OUTRO_DELAY;
		}
	}
	
	public void paused(Graphics g)
	{
		int xPos = RenderingPanel.SCREEN_WIDTH / 2 - outroImage.getWidth(null) / 2;
		int yPos = RenderingPanel.SCREEN_HEIGHT / 2 - outroImage.getHeight(null) / 2;
		g.drawImage(pausedImage, xPos, yPos, null);
	}
	
	public void collide(Collidable c)
	{
		blockHolder.collide(c);
		//TODO Something might be wrong with this allPowerUpsFinished thing
		//if(blockHolder.allBlocksDestroyed() && blockHolder.allPowerUpsFinished())
		if(blockHolder.allBlocksDestroyed())
			cleared = true;
	}
	
	public int getBlocksDestroyed()
	{
		return blockHolder.getBlocksDestroyed();
	}
	
	public boolean isStarted()
	{
		return started;
	}
	
	public void setStarted(boolean started)
	{
		this.started = started;
	}
	
	public boolean isCleared()
	{
		return cleared;
	}
	
	public boolean isFinished()
	{
		return finished;
	}
	
	public boolean isContinuing()
	{
		return cont;
	}
	
	public void setContinuing(boolean cont)
	{
		this.cont = cont;
	}
	
	public BlockHolder getBlockHolder()
	{
		return blockHolder;
	}
}
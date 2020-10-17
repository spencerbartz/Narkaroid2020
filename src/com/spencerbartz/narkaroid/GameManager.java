package com.spencerbartz.narkaroid;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Vector;

import javax.print.DocFlavor.URL;
import javax.swing.JOptionPane;

public class GameManager 
{
	LevelManager levelManager;
	Ball ball;
	Paddle paddle;
	private Vector<Laser> laserQueue;

	private BufferedImage titleScreen;
	private BufferedImage gameOverScreen;
	private Ending ending;
	
	private boolean gameStarted = false;
	private boolean gameOver = false;
	private boolean paused = false;
	private int screenWidth = -1;
	private int screenHeight = -1;
	
	public GameManager()
	{
		ImageLoader imageLoader = ImageLoader.INSTANCE;
		
		titleScreen = imageLoader.loadImage("titlescreen.jpg");
		
		//load game over screen
		gameOverScreen = imageLoader.loadImage("gameover.jpg");
		
		//create ending sequence for the game
		ending = new Ending();
		
		//create manager object for all levels in the game
		levelManager = new LevelManager();
		
		screenWidth = levelManager.getMaxWidth();
		screenHeight = levelManager.getMaxHeight();
		
		//create ball
		ball = new Ball();
		
		//create paddle
		paddle = new Paddle(ball);
		
		laserQueue = new Vector<Laser>();

		// load different power up images
		for (int i = 0; i < PowerUp.PWRUP_TYPES; i++) {
			PowerUp.PWRUP_IMAGES[i] = imageLoader.loadImage("pow" + i
					+ ".gif");

		}

		Laser.laserImage = imageLoader.loadImage("laser.gif");
		
	}
	
	public Paddle getPaddle()
	{
		return paddle;
	}
	
	public void reset()
	{
		paddle.respawn();
		ball.reset();
		levelManager.reset();
		
		gameStarted = false;
		gameOver = false;
	}
	
	public void resetBall()
	{
		ball.reset();
	}
	
	public void resetPaddle()
	{
		paddle.reset();
	}
	
	public int getScreenWidth()
	{
		return screenWidth;
	}
	
	public int getScreenHeight()
	{
		return screenHeight;
	}
	
	public void doOneCycle(Graphics gr)
	{
		SoundManager soundManager = SoundManager.INSTANCE;
		
		//draw game elements
    	if(gameStarted && !gameOver)
    	{
	    	levelManager.draw(gr);
	    	levelManager.move();
	    	
	    	//If any, draw intros, outros, continues, and the game ending	
	    	//level intro
    	    if(!levelManager.getCurrentLevel().isStarted() && !levelManager.isGameWon())
    	    {
    	    	if(paddle.isEnabled())
    	    	{
    	    		paddle.reset();
    	    		ball.reset();
    	    	}
    	    	
    	    	//Show 'Level N Start' Graphic    	    	
    	    	levelManager.levelIntro(gr);	
    	    	
    	    	//Start bg music for this level
    	    	soundManager.startLevelBGM(levelManager.getCurrentLevelNum());
    	    }
    	    //level continue (after dying)
    	    //TODO stop falling powerups during this time
    	    else if(levelManager.getCurrentLevel().isContinuing())
    	    {
    	    	if(paddle.isEnabled())
    	    	{
    	    		paddle.reset();
    	    		ball.reset();
    	    	}
    	    	
    	    	//Start bg music for level N
    	    	//soundManager.startLevelBGM(levelManager.getCurrentLevelNum());
    	    	
    	    	//Show 'Level N' Graphic     	    	
    	    	levelManager.continueLevel(gr);
    	    	
    	    	levelManager.stopPowerUps();    	    	
    	    }
    	    //level outro (after all blocks on level are destroyed)
    	    else if(levelManager.getCurrentLevel().isCleared() && !levelManager.getCurrentLevel().isFinished() && !levelManager.isGameWon())
    	    {
    	    	if(paddle.isEnabled())
    	    	{
    	    		paddle.reset();
    	    		ball.reset();
    	    	}
    	    	
    	    	//Stop bg music for current level
    	    	soundManager.stopLevelBGM(levelManager.getCurrentLevelNum());
    	    	
    	    	//Show 'Level Clear' Graphic
    	    	levelManager.getCurrentLevel().outro(gr, levelManager);
    	    }
    	    //game is won
    	    else if(levelManager.isGameWon())
    	    {
    	    	ending.draw(gr);
    	    	
    	    	if(ending.isFinished())
    	    	{
    	    		reset();
    	    		ending.reset();
    	    	}
    	    }
    	    //regular game play
    	    else
    	    {
    	    	if(!paddle.isEnabled())
    	    		paddle.enable();
    	    }
	    	
	    	//Draw normal game screen in progress, move sprites, and check collisions
	    	if(!levelManager.isGameWon())
	    	{
	    		if(!paused)
	    		{
	    			paddle.move();
	    			ball.move();
	    	    	for(int i = 0; i < laserQueue.size(); i++)
	    	    		laserQueue.get(i).move();
	    		}
	    		else
	    		{
	    			levelManager.getCurrentLevel().paused(gr);
	    			soundManager.stopLevelBGM(levelManager.getCurrentLevelNum());
	    		}
	    			
    	    	//TODO make rendering queue draw this crap
    	    	paddle.draw(gr);
    	    	ball.draw(gr);
    	    	
    	    	//System.out.println(laserQueue.size());
    	    	//gr.drawString("LaserQueue " + laserQueue.size(), 10, 20);;
    	    	
    	    	for(int i = 0; i < laserQueue.size(); i++)
    	    		laserQueue.get(i).draw(gr);
    	    	
    	    	//check collisions
	    		paddle.collide(ball);
	    		levelManager.collide(ball);
	    		levelManager.collide(paddle);
	    		
	    		
	    		for(int i = 0; i < laserQueue.size(); i++)
	    		{
	    			levelManager.collide(laserQueue.elementAt(i));
	    			//clean up offScreen lasers
	    			if(laserQueue.elementAt(i).isOffScreen())
	    				laserQueue.remove(i);
	    		}
	    		
	    		//check for firing laser
	    		if(paddle.queueLaser())
	    		{
	    			Laser laser = new Laser();
	    			laser.setXCoord(paddle.getXCoord() + paddle.getWidth()/2);
	    			laser.setYCoord(paddle.getYCoord() - laser.getHeight());
	    			laserQueue.add(laser);
	    		}
	    		
	    		
	    		//update player's score
	    		paddle.setScore(levelManager.getTotalBlocksDestroyed() * 100);
	    	
	    		//check for death
	    		if(ball.isOffScreen())
	    		{
	    			if(paddle.getLives() > 0)
	    			{
	    				//subtract a life from player, place paddle in middle of screen
	    				//show 'Level N' graphic on screen
	    				paddle.loseLife();
	    				levelManager.getCurrentLevel().setContinuing(true);
	    			}
	    			else
	    			{
	    				gameOver = true;
	    				//soundManager.stopLevelBGM(levelManager.getCurrentLevelNum());
	    				//reset the game so the player can play again
	    				reset();
	    			}				    			
	    		}
	    	}
    	}
    	else if(!gameStarted && !gameOver)
    		gr.drawImage(titleScreen, 0, 0, titleScreen.getWidth(), titleScreen.getHeight(), null);
    	else
    		gr.drawImage(gameOverScreen, 0, 0, gameOverScreen.getWidth(), gameOverScreen.getHeight(), null);
	}
	
	public boolean isGameStarted()
	{
		return gameStarted;
	}
	
	public void setGameStarted(boolean gameStarted)
	{
		this.gameStarted = gameStarted;
	}
	
	public boolean isGameOver()
	{
		return gameOver;
	}
	
	public void setGameOver(boolean gameOver)
	{
		this.gameOver = gameOver;
	}
	
	public boolean isPaused()
	{
		return paused;
	}
	
	public void setPaused(boolean paused)
	{
		this.paused = paused;
	}
	
	public void cheat(KeyEvent e)
	{
		//cheat
		int lvl = levelManager.getCurrentLevelNum();
		if(e.getKeyCode() == KeyEvent.VK_0)
			lvl = 0;
		if(e.getKeyCode() == KeyEvent.VK_1)
			lvl = 1;
		if(e.getKeyCode() == KeyEvent.VK_2)
			lvl = 2;
		
		if(lvl != levelManager.getCurrentLevelNum())
			levelManager.setCurrentLevel(lvl);
	}
}

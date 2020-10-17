package com.spencerbartz.narkaroid;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class RenderingPanel extends JPanel implements Runnable, KeyListener, MouseListener
{
	private static final long serialVersionUID = 7373961741837831705L;
	
	//animation objects
	private Thread animThread;
	private BufferedImage offScreenBuffer;
	
	private GameManager gameManager;
	
	static final int SCREEN_WIDTH = 650;
	static final int SCREEN_HEIGHT = 500;
	
	public RenderingPanel(GameManager gameManager)
	{
		this.gameManager = gameManager;
		this.setBackground(Color.BLUE);
		
		setSize(gameManager.getScreenWidth(), gameManager.getScreenHeight());
	
		setFocusable(true);
		requestFocus();
	}
	
	public void reset()
	{
		gameManager.setGameStarted(false);
		gameManager.reset();
	}

	
	public void start()
	{
		animThread = new Thread(this);
	    animThread.start();
	}

	public void stop()
	{
		animThread = null;
	}
	
	public void run()
	{
		//set up offscreen buffer to eliminate flicker
		Graphics g = this.getGraphics();
		offScreenBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics gr = offScreenBuffer.getGraphics();
		
		Thread curThread = Thread.currentThread();
		
		gameManager.resetBall();
		gameManager.resetPaddle();
		
		//Main Game loop
		while (animThread == curThread)
	    {
	    	try
	        {	
	    		//clear the screen
	    		gr.clearRect(0, 0, getWidth(), getHeight());
	    		
	    		gameManager.doOneCycle(gr);
	    		
	    		//draw the offscreen buffer on to the panel
	    		g.drawImage(offScreenBuffer, 0, 0, this);
	    		
	    		//Wait 10 ms in between frames
	    		Thread.sleep(10);
	        }
	        catch (InterruptedException e) {}
	    }
	}

	public void mouseClicked(MouseEvent e)
	{
		if(gameManager.isGameOver())
		{
			gameManager.setGameOver(false);
			gameManager.setGameStarted(false);
			return;
		}
		
		//If the game has not been started, start it!
		if(!gameManager.isGameStarted())
			gameManager.setGameStarted(true);
	}

	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void pause()
	{
		if(gameManager.isPaused())
			gameManager.setPaused(false);
		else
			gameManager.setPaused(true);
	}

	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			pause();

		gameManager.cheat(e);

	}

	public void keyReleased(KeyEvent e)
	{	
	}

	public void keyTyped(KeyEvent e)
	{	
	}
}

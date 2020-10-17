package com.spencerbartz.narkaroid;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

import javazoom.jl.decoder.JavaLayerException;


public class SoundRunner implements Runnable
{
	SoundPlayer player = null;
	Thread playerThread = null;
	AtomicBoolean pause = new AtomicBoolean(false);
	boolean running = false;
	int pausedOnFrame = 0;
	
	public SoundRunner(SoundPlayer player)
	{
		this.player = player;
	}
	
	public void startPlayer()
	{
		if(playerThread == null)
		{
			playerThread = new Thread(this);
			running = true;
			playerThread.start();
		}
	}
	
	public void stopPlayer()
	{
		if(!pause.get()) 
		{
			player.stop();
			pause.set(true);
		}
	}
	
	public boolean isPaused()
	{
		return pause.get();
	}
	
	public void run() 
	{
//		try {
//			while(player.play(10))
//				System.out.println("HORRIE");
//
//		} catch (JavaLayerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		while(running)
		{
			try 
			{
				if(player != null && player.play(10)) 
				{
					if(pause.get()) 
					{
						running = false;
						LockSupport.park();
					}
				}
			} 
			catch(JavaLayerException e) 
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
package com.spencerbartz.narkaroid;

import java.awt.*;

public class FadingImage extends Thread
{
	private Image img;

	// fading stuff
	static final int ONE_SECOND = 1000; // One second in milliseconds
	int frameRate = 40;
	// Default fade change frequency frames per sec
	int fadeTime = 2; // Default time to fade completely in seconds
	int count; // Repaint cycle counter
	AlphaComposite composite; // Alpha value for the image
	float alphaStep; // Alpha increment for fade step
	int countDelta = 1;
	boolean done = false;
	long frameInterval = ONE_SECOND / frameRate;
	boolean started = false;
	boolean countUp = false;

	public FadingImage(Image img)
	{
		this.img = img;
		setFadage();
	}

	private void setFadage()
	{
		composite = AlphaComposite.SrcOver;
		count = frameRate * fadeTime; // Set repaint counter
		alphaStep = 1.0f / count;
		setDaemon(true);
	}

	public void run()
	{
		started = true;
		while(true)
		{
			// Update alpha composite for next frame
			if(count == 6)
			{
				//count = frameRate * fadeTime;
				countUp = true;
			}
				
			if(count == frameRate * fadeTime)
				countUp = false;
			
			if(countUp)
				count += countDelta;
			else
				count -= countDelta;
			
			composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					count * alphaStep);

			try
			{
				Thread.sleep(frameInterval);
			}
			catch (InterruptedException ex)
			{
			}
		}
	}

	public void draw(Graphics g, int xCoord, int yCoord)
	{
		Graphics2D g2D = (Graphics2D) g;
		
		AlphaComposite oldComposite = (AlphaComposite) g2D.getComposite();
		
		// Set current alpha
		g2D.setComposite(composite);

		g2D.drawImage(img, xCoord, yCoord, null);
	
		g2D.setComposite(oldComposite);
	}

	public void drawFullImage(Graphics g, int xCoord, int yCoord)
	{
		count = frameRate * fadeTime;
		g.drawImage(img, xCoord, yCoord, null);
	}
	
	public boolean isDone()
	{
		return done;
	}

	public boolean isStarted()
	{
		return started;
	}

	public int getWidth()
	{
		return img.getWidth(null);
	}
}

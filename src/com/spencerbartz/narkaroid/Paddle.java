package com.spencerbartz.narkaroid;

import java.awt.*;
import java.awt.event.*;
//import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Paddle implements KeyListener, Drawable, Collidable
{
	private BufferedImage paddleImg;
	private FadingImage engineFlame;
	private Ball ball;
	private int width, height;
	private int xCoord, yCoord;
	private boolean moveLeft, moveRight;
	private int moveSpeed;
	private boolean enabled;
	private int laserAmmo;
	private boolean firing = false;
	
	//player information is contained within the paddle
	private static int DEFAULT_LIVES = 3;
	private static int DEFAULT_AMMO = 5;
	private int lives;
	private int score;
	Font font;
	
	
	private static final int DEFAULT_SPEED = 4;
	public static final int PWRUP_REACT_SPEED = 100;
	public static final int PWRUP_REACT_LASER = 101;
	public static final int PWRUP_REACT_LIFE = 102;
	
	public Paddle(Ball ball)
	{
		this.ball = ball;
		ImageLoader imageLoader = ImageLoader.INSTANCE;
		
		paddleImg = imageLoader.loadImage("paddle.gif");
		BufferedImage engineImg = imageLoader.loadImage("engineflame.gif");
		engineFlame = new FadingImage(engineImg);
		
		//load custom font to draw player statistics
		try 
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream is = classLoader.getResourceAsStream("Yellowr.ttf");
            //InputStream is = NarkaroidFrame.class.getResourceAsStream("fonts/Yellowr.ttf");
            
			font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont((float)18.0);
		} 
		catch (Exception ex) 
		{ 
            ex.printStackTrace(); 
            System.err.println("Font not loaded.  Using arial font.");
            font = new Font("Arial", Font.BOLD,14);
        }
		
		lives = DEFAULT_LIVES;
		laserAmmo = DEFAULT_AMMO;
		
		engineFlame.start();

	}
	
	public void reset()
	{
		width = paddleImg.getWidth(null);
		height = paddleImg.getHeight(null);
		xCoord = (RenderingPanel.SCREEN_WIDTH / 2) - (width / 2);
		yCoord = 400;
		moveLeft = false;
		moveRight = false;
		moveSpeed = DEFAULT_SPEED;
		enabled = false;	
		lives = DEFAULT_LIVES;
		laserAmmo = DEFAULT_AMMO;
	}
	
	public void respawn()
	{
		reset();
		
		//player info
		lives = DEFAULT_LIVES;
		score = 0;
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(!enabled)
			return;	
		
		//inserted 10/25/2011
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			if(!ball.isStarted())
				ball.start();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			moveLeft = true;
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			moveRight = true;
		else if(e.getKeyCode() == KeyEvent.VK_UP && laserAmmo > 0)
		{
			firing = true;
			laserAmmo--;
		}
	}
	
	public void keyReleased(KeyEvent e) 
	{	
		if(!enabled)
			return;
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			moveLeft = false;
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			moveRight = false;
	}

	public void keyTyped(KeyEvent e)
	{
		//Not all keys (especially VK) result in KeyTyped events.
		if(!enabled)
			return;
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(paddleImg, xCoord, yCoord, null);
		
		if(moveRight || moveLeft)
		{
			engineFlame.drawFullImage(g, xCoord, yCoord + height);
			engineFlame.drawFullImage(g, xCoord + width - engineFlame.getWidth(), yCoord + height);
		}
		else
		{
			engineFlame.draw(g, xCoord, yCoord + height);
			engineFlame.draw(g, xCoord + width - engineFlame.getWidth(), yCoord + height);
		}
		
		//draw player info
		g.setFont(font);
		g.setColor(Color.red);
		g.drawString("Score: " + score, 10, 430);
		g.drawString("Lives: " + lives, 10, 450);
		g.drawString("Speed: " + moveSpeed, 10, 470);
		g.drawString("Laser: " + laserAmmo, 10, 490);
	}
	
	public void move()
	{
		if(moveLeft && xCoord > 0)
		{
			xCoord -= moveSpeed;
			if(!ball.isStarted())
				ball.setXCoord(ball.getXCoord() - moveSpeed);
		}
		else if(moveRight && xCoord + width < RenderingPanel.SCREEN_WIDTH)
		{
			xCoord += moveSpeed;
			if(!ball.isStarted())
				ball.setXCoord(ball.getXCoord() + moveSpeed);
		}
	}
	
	public boolean queueLaser()
	{
		if(firing)
		{
			firing = false;
			return true;
		}
		else
			return false;
	}
	
	public void collide(Collidable c)
	{
		if(c instanceof PowerUp)
		{
			if
			(
				(c.getYCoord() + c.getHeight() >= yCoord && c.getYCoord() + c.getHeight() <= yCoord + height) &&
				(c.getXCoord() + c.getWidth() >= xCoord && c.getXCoord() <= xCoord + width )
			)
			{
				c.react(PowerUp.PADDLE_REACT);
				int type = ((PowerUp)c).getType();
				
				switch(type)
				{
					case PowerUp.SPEEDUP:
						react(PWRUP_REACT_SPEED);
						break;
					case PowerUp.LASER:
						react(PWRUP_REACT_LASER);
						break;
					case PowerUp.EXTRA_LIFE:
						react(PWRUP_REACT_LIFE);
						break;
					default:
						System.err.println("Unhandled Power Up");
				}
			}
		}
		//TODO FIX collision to be more like blocks
		else if(
				(c.getYCoord() + c.getHeight() >= yCoord) && (c.getYCoord() + c.getHeight() <= yCoord + height) &&
				((c.getXCoord() + c.getWidth() >= xCoord) && (c.getXCoord() <= xCoord + width))
		  )
		{
			//if(moveLeft || moveRight)
				//c.react(Ball.PADDLE_REACT_MOVING);
			//else
				c.react(Ball.PADDLE_REACT);
		}
	}
	
	public void react(int reactionCode)
	{
		if(reactionCode == PWRUP_REACT_SPEED)
			moveSpeed++;
		else if(reactionCode == PWRUP_REACT_LASER)
			laserAmmo += 5;
		else if(reactionCode == PWRUP_REACT_LIFE)
			lives++;
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

	public void enable()
	{
		enabled = true; 
	}
	
	public void disable()
	{
		enabled = false;
	}

	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void clearPowerUps()
	{
		
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void addToScore(int newPoints)
	{
		score += newPoints;
	}
	
	public int getLives()
	{
		return lives;
	}
	
	public void loseLife()
	{
		lives--;
	}

	public int getSpeed()
	{
		return 0;
	}
}

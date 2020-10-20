package com.spencerbartz.narkaroid;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Ball
 * @author Spencer Bartz
 */
public class Ball implements Drawable, Collidable {
	private int width;
	private int height;
	private int xCoord;
	private int yCoord;
	private int speed;
	private int baseSpeedX;
	private int baseSpeedY;
	private boolean xHeadingIsPositive;
	private boolean yHeadingIsPositive;
	private boolean offScreen = false;
	private boolean started = false;
	private BufferedImage ballImg;

	public static final int PADDLE_REACT        = 0;
	public static final int PADDLE_REACT_MOVING = 1;
	public static final int BLOCK_REACT_BOTTOM  = 2;
	public static final int BLOCK_REACT_LEFT    = 3;
	public static final int BLOCK_REACT_RIGHT   = 4;
	public static final int BLOCK_REACT_TOP     = 5;

	/**
	 * Constructor
	 */
	public Ball() {
		ImageLoader imageLoader = ImageLoader.INSTANCE;
		ballImg = imageLoader.loadImage("images/ball.gif");
		reset();
	}

	/**
	 * reset()
	 */
	public void reset() {
		width = ballImg.getWidth(null);
		height = ballImg.getHeight(null);
		xCoord = (RenderingPanel.SCREEN_WIDTH / 2) - (width / 2);
		yCoord = 385;
		xHeadingIsPositive = (int) (Math.random() * 2) == 1 ? false : true;
		yHeadingIsPositive = (int) (Math.random() * 2) == 1 ? false : true;
		speed = 0;
		baseSpeedX = 3;
		baseSpeedY = 3;
		offScreen = false;
		started = false;
	}

	/**
	 * draw()
	 * @param g
	 */
	public void draw(Graphics g) {
		if (!offScreen) {
			g.drawImage(ballImg, xCoord, yCoord, null);
		}
	}

	/**
	 * move()
	 */
	public void move() {
		if (started) {

			// Bounce off left side of screen (start heading in positive x direction)
			if (xCoord <= 0) {
				xHeadingIsPositive = true;
			}
	
			// Bounce off right side of screen (start heading in negative x direction)
			if (xCoord + width >= RenderingPanel.SCREEN_WIDTH) {
				xHeadingIsPositive = false;
			}
	
			// Bounce off top of screen (start heading in positive y)
			if (yCoord <= 0) {
				yHeadingIsPositive = true;
			}
	
			// If ball hits bottom of screen, player dies
			if (yCoord + height >= RenderingPanel.SCREEN_HEIGHT) {
				offScreen = true;
			}
	
			// Add or subtract to x coordinate, causing x coord location change on next repaint
			if (xHeadingIsPositive) {
				xCoord += (baseSpeedX + speed);
			} else {
				xCoord -= (baseSpeedX + speed);
			}
				
			// Add or subtract to y coordinate, causing y coord location change on next repaint
			if (yHeadingIsPositive) {
				yCoord += (baseSpeedY + speed);
			} else {
				yCoord -= (baseSpeedY + speed);
			}
		}
	}

	public void collide(Collidable c) {}
	
	/**
	 * react()
	 * @param reactionCode
	 * When ball collides with something, that object tells the ball what it was (by the reaction code).
	 * Based on the reaction code, the ball decides how to react to the collision
	 */
	public void react(int reactionCode) {
		// if we hit the paddle, start moving "up" (in the negative y direction)
		if (reactionCode == PADDLE_REACT) {
			yHeadingIsPositive = false;
		} else if (reactionCode == PADDLE_REACT_MOVING) {
			// TODO: Manipulate speed here
			yHeadingIsPositive = false;
		} else if (reactionCode == BLOCK_REACT_BOTTOM) {
			yHeadingIsPositive = true;
		} else if (reactionCode == BLOCK_REACT_LEFT) {
			xHeadingIsPositive = false;
		} else if (reactionCode == BLOCK_REACT_RIGHT) {
			xHeadingIsPositive = true;
		} else if (reactionCode == BLOCK_REACT_TOP) {
			yHeadingIsPositive = false;
		}
	}

	public int getXCoord() {
		return xCoord;
	}

	public int getYCoord() {
		return yCoord;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void start() {
		started = true;
	}

	public boolean isStarted() {
		return started;
	}

	public void setXCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public boolean isOffScreen() {
		return offScreen;
	}

	public int getSpeed() {
		return baseSpeedY + speed;
	}
}

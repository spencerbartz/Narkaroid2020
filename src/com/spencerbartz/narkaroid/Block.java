package com.spencerbartz.narkaroid;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Block
 * @author Spencer Bartz
 */
public class Block implements Drawable, Collidable {
	private int xCoord;
	private int yCoord;
	private int width;
	private int height;
	private int blockType;
	private int startingBlockType;
	private boolean destroyed = false;
	private PowerUp powerUp = null;

	public static final int DEFAULT_WIDTH  = 40;
	public static final int DEFAULT_HEIGHT = 25;
	public static final int BALL_REACT     = 100;

	static final int GREEN_BLOCK  = 0;
	static final int RED_BLOCK    = 1;
	static final int ORANGE_BLOCK = 2;
	static final int BLUE_BLOCK   = 3;
	static final int YELLOW_BLOCK = 4;
	static final int BLOCK_TYPES  = 5;
	
	static final Image[] BLOCK_IMAGES = new Image[BLOCK_TYPES];

	/**
	 * Constructor
	 * @param blockType
	 * Creates a Block whose xCoord and yCoord will be set by a BlockHolder.
	 */
	public Block(int blockType) {
		this(0, 0, blockType);
	}

	/**
	 * Constructor
	 * @param blockType
	 * @param powerUp
	 * Creates a Block with PowerUp capability whose xCoord and yCoord will be set by a BlockHolder.
	 */
	public Block(int blockType, PowerUp powerUp) {
		this(0, 0, blockType);
		this.powerUp = powerUp;
	}

	/**
	 * Constructor
	 * @param xCoord
	 * @param yCoord
	 * Creates a standard Green (easy) Block at the given coordinates 
	 */
	public Block(int xCoord, int yCoord) {
		this(xCoord, yCoord, DEFAULT_WIDTH, DEFAULT_HEIGHT, GREEN_BLOCK);
	}

	/**
	 * Constructor
	 * @param xCoord
	 * @param yCoord
	 * @param blockType
	 * Creates a "blockType" Block at the given coordinates
	 */
	public Block(int xCoord, int yCoord, int blockType) {
		this(xCoord, yCoord, DEFAULT_WIDTH, DEFAULT_HEIGHT, blockType);
	}

	/**
	 * Constructor
	 * @param xCoord
	 * @param yCoord
	 * @param width
	 * @param height
	 * @param blockType
	 * Creates a "blockType" Block at the given coordinates and of the given height
	 */
	public Block(int xCoord, int yCoord, int width, int height, int blockType) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.width = width;
		this.height = height;
		this.blockType = blockType;
		this.startingBlockType = blockType;
	}

	/**
	 * reset()
	 */
	public void reset() {
		destroyed = false;
		blockType = startingBlockType;

		if (powerUp != null) {
			powerUp.reset();
		}
	}

	/**
	 * draw()
	 * @param g
	 */
	public void draw(Graphics g) {
		g.drawImage(
				BLOCK_IMAGES[blockType], 
				xCoord, 
				yCoord, 
				BLOCK_IMAGES[blockType].getWidth(null),
				BLOCK_IMAGES[blockType].getHeight(null), 
				null
		);
	}

	/**
	 * collide()
	 * @param c
	 */
	public void collide(Collidable c) {
		boolean bottom  = false;
		boolean top     = false;
		boolean left    = false;
		boolean right   = false;
		boolean collide = false;
		
		StringBuffer debug = new StringBuffer();

		// Collision from bottom
		for (int i = c.getSpeed() - 1; i >= 0; i--) {
			if (c.getYCoord() == yCoord + height - i) {
				collide = true;
			}
		}

		if ((collide) && (c.getXCoord() + c.getWidth() > xCoord && c.getXCoord() < xCoord + width)) {
			bottom = true;
			// c.react(Ball.BLOCK_REACT_BOTTOM);
			// react(BALL_REACT);
			debug.append("bottom - BALL X: " + c.getXCoord() + " Y: " + c.getYCoord() + " BLOCK X: " + xCoord
					+ " Ybot: " + (yCoord + height));
		}
		
		// Collision from top
		else if ((c.getYCoord() + c.getHeight() == yCoord || c.getYCoord() + c.getHeight() == yCoord + 1
				|| c.getYCoord() + c.getHeight() == yCoord + 2)
				&& (c.getXCoord() + c.getWidth() > xCoord && c.getXCoord() < xCoord + width)) {
			top = true;
			// c.react(Ball.BLOCK_REACT_TOP);
			// react(BALL_REACT);
			debug.append("top - BALL X: " + c.getXCoord() + " Ybot: " + (c.getYCoord() + c.getHeight()) + " BLOCK X: "
					+ xCoord + " Y: " + yCoord);
		}
		
		// Collision from left side
		else if ((c.getXCoord() + c.getWidth() == xCoord || c.getXCoord() + c.getWidth() == xCoord + 1
				|| c.getXCoord() + c.getWidth() == xCoord + 2)
				&& (c.getYCoord() < yCoord + height && c.getYCoord() + c.getHeight() > yCoord)) {
			left = true;
			// c.react(Ball.BLOCK_REACT_LEFT);
			// react(BALL_REACT);
			debug.append("left - BALL Xrt: " + (c.getXCoord() + c.getWidth()) + " Y: " + c.getYCoord() + " BLOCK X: "
					+ xCoord + " Y: " + yCoord);
		}
		
		// Collision from right side
		else if ((c.getXCoord() == xCoord + width || c.getXCoord() == xCoord + width - 1
				|| c.getXCoord() == xCoord + width - 2)
				&& (c.getYCoord() < yCoord + height && c.getYCoord() + c.getHeight() > yCoord)) {
			right = true;
			// c.react(Ball.BLOCK_REACT_RIGHT);
			// react(BALL_REACT);
			debug.append("right - BALL X: " + c.getXCoord() + " Y: " + c.getYCoord() + " BLOCK Xrt: " + (xCoord + width)
					+ " Y: " + yCoord);
		}

		if (top) {
			if (c instanceof Ball) {
				c.react(Ball.BLOCK_REACT_TOP);
			} else if (c instanceof Laser) {
				c.react(Laser.DEFAULT_REACT);
			}
		}

		if (bottom) {
			if (c instanceof Ball) {
				c.react(Ball.BLOCK_REACT_BOTTOM);
			} else if (c instanceof Laser) {
				c.react(Laser.DEFAULT_REACT);
			}
		}

		if (left) {
			if (c instanceof Ball) {
				c.react(Ball.BLOCK_REACT_LEFT);
			} else if (c instanceof Laser) {
				c.react(Laser.DEFAULT_REACT);
			}
		}

		if (right) {
			if (c instanceof Ball) {
				c.react(Ball.BLOCK_REACT_RIGHT);
			} else if (c instanceof Laser) {
				c.react(Laser.DEFAULT_REACT);
			}
		}

		// TODO: what is this? I think it's in place of the commented out calls to react()
		if (debug.length() > 0) {
			// System.out.println(debug.toString());
			react(BALL_REACT);
		}
	}

	/**
	 * react()
	 * @param reactionCode
	 */
	public void react(int reactionCode) {
		if (reactionCode == BALL_REACT) {
			blockType--;

			if (blockType == -1) {
				destroyed = true;
			}
		}

		if (destroyed && powerUp != null) {
			powerUp.activate();
		}
	}

	public int getXCoord() {
		return xCoord;
	}

	public void setXCoord(int xCoord) {
		this.xCoord = xCoord;
		if (powerUp != null)
			powerUp.setXCoord(xCoord);
	}

	public int getYCoord() {
		return yCoord;
	}

	public void setYCoord(int yCoord) {
		this.yCoord = yCoord;
		if (powerUp != null)
			powerUp.setYCoord(yCoord);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setPowerUp(PowerUp powerUp) {
		this.powerUp = powerUp;
		powerUp.setXCoord(xCoord);
		powerUp.setYCoord(yCoord);
	}

	public boolean hasPowerUp() {
		return powerUp != null;
	}

	public PowerUp getPowerUp() {
		return powerUp;
	}

	public int getSpeed() {
		return 0;
	}
}

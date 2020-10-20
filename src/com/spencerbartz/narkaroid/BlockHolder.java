package com.spencerbartz.narkaroid;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * BlockHolder
 * @author Spencer Bartz
 */
public class BlockHolder implements Drawable {
	private int cols;
	private int rows;
	private Block[][] blocks;
	private int initialX;
	private int initialY;
	private int blocksDestroyed;

	/**
	 * Constructor 
	 * @param cols
	 * @param rows
	 * For generic block arrangement
	 */
	public BlockHolder(int cols, int rows) {
		loadBlockImages();

		this.cols = cols;
		this.rows = rows;
		blocks = new Block[cols][rows];
		initialX = (RenderingPanel.SCREEN_WIDTH / 2) - ((cols / 2) * Block.DEFAULT_WIDTH);
		initialY = 30;

		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				blocks[i][j] = new Block(initialX + i * Block.DEFAULT_WIDTH, initialY + j * Block.DEFAULT_HEIGHT);
			}
		}

		blocksDestroyed = 0;
	}

	/**
	 * Constructor
	 * @param cols
	 * @param rows
	 * @param blocks
	 * @param initialX
	 * @param initialY
	 * For specialized block arrangement passed by array
	 */
	public BlockHolder(int cols, int rows, Block[][] blocks, int initialX, int initialY) {
		loadBlockImages();

		this.cols = cols;
		this.rows = rows;
		this.blocks = new Block[cols][rows];
		this.initialX = initialX;
		this.initialY = initialY;

		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				this.blocks[i][j] = blocks[i][j];
				this.blocks[i][j].setXCoord(initialX + i * Block.DEFAULT_WIDTH);
				this.blocks[i][j].setYCoord(initialY + j * Block.DEFAULT_HEIGHT);
			}
		}
		
		blocksDestroyed = 0;
	}

	/**
	 * loadBlockImages()
	 */
	private void loadBlockImages() {
		ImageLoader imageLoader = ImageLoader.INSTANCE;
		// load all the different colored block images. Since there are many blocks,
		// all block objects will statically share the same images
		BufferedImage greenBlock  = imageLoader.loadImage("images/greenblock.gif");
		BufferedImage redBlock    = imageLoader.loadImage("images/redblock.gif");
		BufferedImage orangeBlock = imageLoader.loadImage("images/orangeblock.gif");
		BufferedImage blueBlock   = imageLoader.loadImage("images/blueblock.gif");
		BufferedImage yellowBlock = imageLoader.loadImage("images/yellowblock.gif");
		
		Block.BLOCK_IMAGES[0] = greenBlock;
		Block.BLOCK_IMAGES[1] = redBlock;
		Block.BLOCK_IMAGES[2] = orangeBlock;
		Block.BLOCK_IMAGES[3] = blueBlock;
		Block.BLOCK_IMAGES[4] = yellowBlock;
	}

	/**
	 * reset()
	 */
	public void reset() {
		blocksDestroyed = 0;
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				blocks[i][j].reset();
			}
		}
	}

	/**
	 * draw()
	 * @param g
	 */
	public void draw(Graphics g) {
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				if (!blocks[i][j].isDestroyed()) {
					blocks[i][j].draw(g);
				}
			}
		}
	}

	/**
	 * collide()
	 * @param c
	 */
	public void collide(Collidable c) {
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				if (!blocks[i][j].isDestroyed()) {
					blocks[i][j].collide(c);
					
					if (blocks[i][j].isDestroyed()) {
						blocksDestroyed++;
					}
				}
			}
		}
	}

	/**
	 * getBlocksDestroyed()
	 * @return int - number of blocks currently destroyed
	 */
	public int getBlocksDestroyed() {
		return blocksDestroyed;
	}

	/**
	 * allBlocksDestroyed()
	 * @return boolean - true if all blocks destroyed, false otherwise
	 * Compares number of blocks destroyed to total number of blocks to
	 * determine if all blocks in the BlockHolder have been destroyed.
	 */
	public boolean allBlocksDestroyed() {
		return blocksDestroyed == (rows * cols);
	}

	/**
	 * allPowerUpsFinished()
	 * @return boolean - true if all power ups are off screen, false otherwise
	 */
	public boolean allPowerUpsFinished() {
		// TODO test this shitty function 7-18 (2011?) caused
		// ArrayIndexOutOfBoundsException: 5
		/*
		 * Exception in thread "Thread-4" java.lang.ArrayIndexOutOfBoundsException: 5 at
		 * horrienoid.BlockHolder.allPowerUpsFinished(BlockHolder.java:93) at
		 * horrienoid.Level.collide(Level.java:131) at
		 * horrienoid.LevelManager.collide(LevelManager.java:213) at
		 * horrienoid.HorrieNoidApplet.run(HorrieNoidApplet.java:295) at
		 * java.lang.Thread.run(Thread.java:595) MOST LIKELY CAUSE - rows and cols are
		 * backwards. fix later
		 */

		// save locations of power ups to improve efficiency (this wastefully checks all blocks)
		// 10/25/2011 --> switched rows and cols, looks like it fixed it.
		for (int i = 0; i < cols; i++)
			for (int j = 0; j < rows; j++)
				if (blocks[i][j].hasPowerUp() && !blocks[i][j].getPowerUp().isOffScreen())
					return false;

		return true;
	}

	/**
	 * getBlock()
	 * @param col
	 * @param row
	 * @return Block - The Block Object at a specific row & column
	 */
	public Block getBlock(int col, int row) {
		if (col < cols && row < rows) {
			return blocks[col][row];
		} else {
			return null;
		}
	}

	/**
	 * getRows()
	 * @return int - the number of rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * getCols()
	 * @return int - the number of columns
	 */
	public int getCols() {
		return cols;
	}
}

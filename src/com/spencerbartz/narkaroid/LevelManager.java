package com.spencerbartz.narkaroid;


import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager 
{
	public static final int NUM_LEVELS = 3;
	private Level [] levels;
	PowerUp [][] powerUps;
	private int currentLevel = 0;
	private boolean gameWon = false;
	
	BufferedImage [] bgImages;
	BufferedImage [] startImages;
	BufferedImage [] contImages;
	
	//LVL0
	private static final int LVL0_COLS = 14;
	private static final int LVL0_ROWS = 5;
	private static final int LVL0_PWRUPS = 4;
	
	//LVL1
//	private static final int LVL1_COLS = 14;
//	private static final int LVL1_ROWS = 5;
//	private static final int LVL1_PWRUPS = 2;
	
	//LVL1
	private static final int LVL1_COLS = 2;
	private static final int LVL1_ROWS = 2;
	private static final int LVL1_PWRUPS = 4;
	
	//LVL2
	//private static final int LVL2_COLS = 14;
	//private static final int LVL2_ROWS = 6;
	//private static final int LVL2_PWRUPS = 3;
	
	//LVL2
	private static final int LVL2_COLS = 2;
	private static final int LVL2_ROWS = 2;
	private static final int LVL2_PWRUPS = 4;
	
	
	//Level States
	public static final int STARTED = 0;
	public static final int CLEARED = 1;
	public static final int CONTINUING = 2;
	public static final int OUTRO_FINISHED = 3;
	public static final int GAME_WON = 4;
	
	private int currentLevelState = -1;
	
	public LevelManager()
	{
		ImageLoader imageLoader = ImageLoader.INSTANCE;
		
		//load background images and start images for levels
		bgImages = new BufferedImage[NUM_LEVELS];
		startImages = new BufferedImage[NUM_LEVELS];
		contImages = new BufferedImage[NUM_LEVELS];
		
		for(int i = 0; i < bgImages.length; i++)
		{	
			bgImages[i] = imageLoader.loadImage("bg" + i + ".jpg");
			startImages[i] = imageLoader.loadImage("start" + i + ".gif");
			contImages[i] = imageLoader.loadImage("cont" + i + ".gif");
		}
		
		levels = new Level[NUM_LEVELS];
		
		//level 0 contains a generic block holder
		BlockHolder level0 = new BlockHolder(LVL0_COLS, LVL0_ROWS);
		levels[0] = new Level(bgImages[0], startImages[0], contImages[0], level0, this);
		
		BlockHolder level1 = new BlockHolder(LVL1_COLS, LVL1_ROWS);
		levels[1] = new Level(bgImages[1], startImages[1], contImages[1], level1, this);
		
		BlockHolder level2 = new BlockHolder(LVL2_COLS, LVL2_ROWS);
		levels[2] = new Level(bgImages[2], startImages[2], contImages[2], level2, this);
		
		/*
		//level 1 contains a custom block holder
		Block [][] level1Blocks = new Block[LVL1_COLS][LVL1_ROWS];
		int lvl1InitialX = (RenderingPanel.SCREEN_WIDTH / 2) - ((LVL1_COLS / 2) * Block.DEFAULT_WIDTH);
		int lvl1InitialY = 40;
		
		for(int i = 0; i < LVL1_COLS; i++)
			for(int j = 0; j < 2; j++)
				level1Blocks[i][j] = new Block(Block.RED_BLOCK);
	
		for(int i = 0; i < LVL1_COLS; i++)
			for(int j = 1; j < LVL1_ROWS; j++)
				level1Blocks[i][j] = new Block(Block.RED_BLOCK);
	
		BlockHolder level1 = new BlockHolder(LVL1_COLS, LVL1_ROWS, level1Blocks, lvl1InitialX, lvl1InitialY);
		levels[1] = new Level(bgImages[1], startImages[1], contImages[1], level1);
	
		//level 2 contains a hand crafted custom block holder
		Block [][] level2Blocks = 
		{
			{ 
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK), 
				new Block(Block.GREEN_BLOCK), new Block(Block.GREEN_BLOCK),  
				new Block(Block.YELLOW_BLOCK), new Block(Block.GREEN_BLOCK),
			},
			{
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK), 
				new Block(Block.GREEN_BLOCK), new Block(Block.GREEN_BLOCK),  
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
			},
			{
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK), 
				new Block(Block.GREEN_BLOCK), new Block(Block.GREEN_BLOCK), 
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
			},
			{
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK), 
				new Block(Block.GREEN_BLOCK), new Block(Block.GREEN_BLOCK),  
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
			},
			{
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK), 
				new Block(Block.GREEN_BLOCK), new Block(Block.GREEN_BLOCK),  
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
			},
			{
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK), 
				new Block(Block.GREEN_BLOCK), new Block(Block.GREEN_BLOCK), 
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
			},
			{
				new Block(Block.RED_BLOCK), new Block(Block.RED_BLOCK),
				new Block(Block.RED_BLOCK), new Block(Block.RED_BLOCK), 
				new Block(Block.RED_BLOCK), new Block(Block.RED_BLOCK),
			},
			{
				new Block(Block.RED_BLOCK), new Block(Block.RED_BLOCK),
				new Block(Block.RED_BLOCK), new Block(Block.RED_BLOCK), 
				new Block(Block.RED_BLOCK), new Block(Block.RED_BLOCK),
			},
			{
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
				new Block(Block.GREEN_BLOCK), new Block(Block.GREEN_BLOCK), 
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
			},
			{
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
				new Block(Block.GREEN_BLOCK), new Block(Block.GREEN_BLOCK), 
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
			},
			{
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
				new Block(Block.GREEN_BLOCK), new Block(Block.GREEN_BLOCK), 
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
			},
			{
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
				new Block(Block.GREEN_BLOCK), new Block(Block.GREEN_BLOCK), 
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
			},
			{
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
				new Block(Block.GREEN_BLOCK), new Block(Block.GREEN_BLOCK), 
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
			},
			{
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
				new Block(Block.GREEN_BLOCK), new Block(Block.GREEN_BLOCK), 
				new Block(Block.YELLOW_BLOCK), new Block(Block.YELLOW_BLOCK),
			},
		};
		
		
		
		//LVL2 initial coordinates for block holder
		int lvl2InitialX = (RenderingPanel.SCREEN_WIDTH / 2) - ((LVL2_COLS / 2) * Block.DEFAULT_WIDTH);
		int lvl2InitialY = 40;
		
		BlockHolder level2 = new BlockHolder(LVL2_COLS, LVL2_ROWS, level2Blocks, lvl2InitialX, lvl2InitialY);
		levels[2] = new Level(bgImages[2], startImages[2], contImages[2], level2);
	*/
		//create power ups for levels
		//power ups stored in double dimension array.  first index corresponds to level number
		powerUps = new PowerUp[NUM_LEVELS][];
		
		//lvl 0
		powerUps[0] = new PowerUp[LVL0_PWRUPS]; 
		powerUps[0][0] = new PowerUp(PowerUp.SPEEDUP);
		powerUps[0][1] = new PowerUp(PowerUp.LASER);
		powerUps[0][2] = new PowerUp(PowerUp.EXTRA_LIFE);
		powerUps[0][3] = new PowerUp(PowerUp.LASER);
		
		powerUps[1] = new PowerUp[LVL1_PWRUPS]; 
		powerUps[1][0] = new PowerUp(PowerUp.SPEEDUP);
		powerUps[1][1] = new PowerUp(PowerUp.LASER);
		powerUps[1][2] = new PowerUp(PowerUp.EXTRA_LIFE);
		powerUps[1][3] = new PowerUp(PowerUp.LASER);
		
		powerUps[2] = new PowerUp[LVL1_PWRUPS]; 
		powerUps[2][0] = new PowerUp(PowerUp.SPEEDUP);
		powerUps[2][1] = new PowerUp(PowerUp.LASER);
		powerUps[2][2] = new PowerUp(PowerUp.EXTRA_LIFE);
		powerUps[2][3] = new PowerUp(PowerUp.LASER);
		
		/*
		//lvl1
		powerUps[1] = new PowerUp[LVL1_PWRUPS]; 
		powerUps[1][0] = new PowerUp(PowerUp.LASER);
		powerUps[1][1] = new PowerUp(PowerUp.LASER);
		
		//lvl2
		powerUps[2] = new PowerUp[LVL2_PWRUPS]; 
		powerUps[2][0] = new PowerUp(PowerUp.SPEEDUP);
		powerUps[2][1] = new PowerUp(PowerUp.EXTRA_LIFE);
		powerUps[2][2] = new PowerUp(PowerUp.EXTRA_LIFE);
		*/
		
		//insert all power ups into blocks
		//TODO make sure 2 power ups aren't being inserted into same block!
		for(int i = 0; i < levels.length; i++)
		{			
			//System.out.println("level" + i);
			for(int j = 0; j < powerUps[i].length; j++)
			{
				Block temp;
				do
				{
					int powerUpCol = (int) (Math.random() * levels[i].getBlockHolder().getCols());
					int powerUpRow = (int) (Math.random() * levels[i].getBlockHolder().getRows());
			
					//System.out.println("col " + powerUpCol + " " + "row " + powerUpRow);
					
					temp = levels[i].getBlockHolder().getBlock(powerUpCol, powerUpRow);
				}
				while(temp.hasPowerUp());
				
				if(temp != null)
					temp.setPowerUp(powerUps[i][j]);
			}
		}
		
	}
	
	public void reset()
	{
		currentLevel = 0;
		
		for(int i = 0; i < levels.length; i++)
			levels[i].reset();
		
		gameWon = false;
	}
	
	public void draw(Graphics g)
	{
		levels[currentLevel].draw(g);
		
		for(int i = 0; i < powerUps[currentLevel].length; i++)
			powerUps[currentLevel][i].draw(g);
	}
	
	public void move()
	{
		for(int i = 0; i < powerUps[currentLevel].length; i++)
			powerUps[currentLevel][i].move();
	}
	
	public void collide(Collidable c)
	{
		if(c instanceof Paddle)
		{
			for(int i = 0; i < powerUps[currentLevel].length; i++)
				c.collide(powerUps[currentLevel][i]);
		}
		else //ball or laser 
		{
			levels[currentLevel].collide(c);
			if(levels[currentLevel].isFinished())
			{
				if(currentLevel == NUM_LEVELS - 1)
				{
					System.out.println("DEBUG Game WON!");
					gameWon = true;
				}
			}
		}
	}
	
	//try to stop a powerUp from falling when continuing on a level after dying
	public void stopPowerUps()
	{
		for(int i = 0; i < powerUps[currentLevel].length; i++)
		{
			if(powerUps[currentLevel][i].isActivated())
				powerUps[currentLevel][i].react(0);
		}
	}
	
	public void levelIntro(Graphics g)
	{
		levels[currentLevel].intro(g);
	}
	
	public void continueLevel(Graphics g)
	{
		levels[currentLevel].cont(g);
		
		if(levels[currentLevel].isContinuing() == false)
		{
			SoundManager soundManager = SoundManager.INSTANCE;
			soundManager.startLevelBGM(currentLevel);
		}
	}
	
	public void levelOutro(Graphics g)
	{
		
	}
	
	public void setCurrentLevel(int currentLevel)
	{
		this.currentLevel = currentLevel;
	}
	
	public Level getCurrentLevel()
	{
		return levels[currentLevel];
	}
	
	public int getCurrentLevelNum()
	{
		return currentLevel;
	}	
	
	public boolean isGameWon()
	{
		return gameWon;
	}
	
	public void setGameWon(boolean gameWon)
	{
		this.gameWon = gameWon;
	}
	
	public int getTotalBlocksDestroyed()
	{
		int total = 0;
		
		for(int i = 0; i < levels.length; i++)
			total += levels[i].getBlocksDestroyed();
		
		return total;
	}
	
	public int getCurrentLevelState()
	{
		return currentLevelState;
	}
	
	public int getMaxWidth()
	{
		return bgImages[0].getWidth();
	}
	
	public int getMaxHeight()
	{
		return bgImages[0].getHeight();
	}
	
	public void printLevelState()
	{
		System.out.println("Started: " + levels[currentLevel].isStarted() + " cleared: " + levels[currentLevel].isCleared() + " finished: " + levels[currentLevel].isFinished() + " continuing: " + levels[currentLevel].isContinuing());
	}
	
	public boolean allPowerUpsOffScreen()
	{
		for(int i = 0; i < powerUps[getCurrentLevelNum()].length; i++)
		{
			if(!powerUps[getCurrentLevelNum()][i].isOffScreen())
			{
				return false;
			}
		}
		
		return true;
	}
}
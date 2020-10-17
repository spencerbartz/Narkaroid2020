package com.spencerbartz.narkaroid;

public interface Collidable 
{
	public void collide(Collidable c);
	public void react(int reactionCode);
	public int getXCoord();
	public int getYCoord();
	public int getWidth();
	public int getHeight();
	public int getSpeed();
}

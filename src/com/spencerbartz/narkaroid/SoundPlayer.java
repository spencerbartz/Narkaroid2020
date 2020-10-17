package com.spencerbartz.narkaroid;

import java.io.BufferedInputStream;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class SoundPlayer extends AdvancedPlayer
{
	private int pausedOnFrame = 0;
	private BufferedInputStream bis;
	
	public SoundPlayer(BufferedInputStream inputStream) throws JavaLayerException 
	{
		super(inputStream);
		bis = inputStream;
		setPlayBackListener(new PlaybackListener() {
		    @Override
		    public void playbackFinished(PlaybackEvent event) {
		        pausedOnFrame = event.getFrame();
		    }
		});
	}
	
//	public boolean play(int frames) throws JavaLayerException
//	{
//		pausedOnFrame += frames;
//
//		if(!super.play(frames))
//		{
//			pausedOnFrame = 0;
//			return false;
//		}
//		else
//		{
//			return true;
//		}
//	}
}

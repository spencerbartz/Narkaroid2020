package com.spencerbartz.narkaroid;

public class PausableMp3 implements Mp3PlaybackListener, Runnable {

	public void playbackStarted(Mp3PlaybackEvent event) {}

	public void playbackStopped(Mp3PlaybackEvent event) {}

	public void playbackPaused(Mp3PlaybackEvent event) {}

	public void playbackFinished(Mp3PlaybackEvent event) {}
	
	public void run() {}
}

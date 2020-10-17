package com.spencerbartz.narkaroid;

public interface Mp3PlaybackListener {
	public void playbackStarted(Mp3PlaybackEvent event);
	public void playbackStopped(Mp3PlaybackEvent event);
	public void playbackPaused(Mp3PlaybackEvent event);
	public void playbackFinished(Mp3PlaybackEvent event);
}

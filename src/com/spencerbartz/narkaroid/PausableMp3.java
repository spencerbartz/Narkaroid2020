package com.spencerbartz.narkaroid;

import javazoom.jl.decoder.JavaLayerException;

public class PausableMp3 implements Mp3PlaybackListener, Runnable {

	private boolean isLooped;
	private boolean isPlaying = false;
	private String filePath;
	private PausableMp3Player player;
	private Thread playerThread;

	/**
	 * Constructor
	 * @param filePath
	 */
	public PausableMp3(String filePath, boolean isLooped) {
		this.filePath = filePath;
		this.isLooped = isLooped;
	}

	/**
	 * play()
	 */
	public void play() {
		if (player == null) {
			playerInitialize();
		}

		playerThread = new Thread(this, "AudioPlayerThread");
		playerThread.start();
		isPlaying = true;
	}

	/**
	 * playerInitialize()
	 */
	private void playerInitialize() {
		try {
			String urlStr = "file:///" + new java.io.File(".").getCanonicalPath() + "/" + filePath;
			player = new PausableMp3Player(new java.net.URL(urlStr), this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * pauseToggle()
	 */
	public void pauseToggle() {
		if (player.isPaused == true) {
			play();
		} else {
			this.pause();
		}
	}

	/**
	 * pause()
	 */
	public void pause() {
		player.pause();

		try {
			if (playerThread != null) {
				playerThread.interrupt();
				playerThread = null;
				isPlaying = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * stop()
	 */
	public void stop() {
		player.stop();
		isPlaying = false;
	}

	/**
	 * playbackStarted()
	 */
	public void playbackStarted(Mp3PlaybackEvent event) {
		System.out.println("playbackStarted -- SOURCE: " + event.source + " EVENT TYPE NAME: " + event.eventType.name + " FRAME INDEX: " + event.frameIndex);
	}

	/**
	 * playbackStopped()
	 */
	public void playbackStopped(Mp3PlaybackEvent event) {
		System.out.println("playbackStopped -- SOURCE: " + event.source + " EVENT TYPE NAME: " + event.eventType.name + " FRAME INDEX: " + event.frameIndex);
	}
	
	/**
	 * playbackPaused()
	 */
	public void playbackPaused(Mp3PlaybackEvent event) {
		System.out.println("playbackPaused -- SOURCE: " + event.source + " EVENT TYPE NAME: " + event.eventType.name + " FRAME INDEX: " + event.frameIndex);
	}

	/**
	 * playbackFinished()
	 */
	public void playbackFinished(Mp3PlaybackEvent event) {
		System.out.println("playbackFinished -- SOURCE: " + event.source + " EVENT TYPE NAME: " + event.eventType.name + " FRAME INDEX: " + event.frameIndex);
		if (isLooped) {
			player = null;
			play();
		}
	}

	/**
	 * run()
	 */
	public void run() {
		try {
			player.resume();
		} catch (JavaLayerException ex) {
			ex.printStackTrace();
		} 
	}
	
	public boolean isPaused() {
		return player.isPaused;
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
}

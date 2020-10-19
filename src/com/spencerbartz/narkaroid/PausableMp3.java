package com.spencerbartz.narkaroid;

public class PausableMp3 implements Mp3PlaybackListener, Runnable {

	private String filePath;
	private PausableMp3Player player;
	private Thread playerThread;
	boolean isLooped;

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
		if (this.player == null) {
			this.playerInitialize();
		}

		this.playerThread = new Thread(this, "AudioPlayerThread");
		this.playerThread.start();
	}

	/**
	 * playerInitialize()
	 */
	private void playerInitialize() {
		try {
			String urlAsString = "file:///" + new java.io.File(".").getCanonicalPath() + "/" + this.filePath;
			this.player = new PausableMp3Player(new java.net.URL(urlAsString), this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * pauseToggle()
	 */
	public void pauseToggle() {
		if (this.player.isPaused == true) {
			this.play();
		} else {
			this.pause();
		}
	}

	/**
	 * pause()
	 */
	public void pause() {
		this.player.pause();

		// DEPRECATED this.playerThread.stop();
		// this.playerThread = null;

		try {
			if (this.playerThread != null) {
				this.playerThread.interrupt();
				// this.playerThread.join();
				this.playerThread = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * stop()
	 */
	public void stop() {
		this.player.stop();
	}

	public void playbackStarted(Mp3PlaybackEvent event) {
	}

	public void playbackStopped(Mp3PlaybackEvent event) {
	}

	public void playbackPaused(Mp3PlaybackEvent event) {
	}

	public void playbackFinished(Mp3PlaybackEvent event) {
	}

	public void run() {
	}
}

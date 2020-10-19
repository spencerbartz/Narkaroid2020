package com.spencerbartz.narkaroid;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

/******************************************************
 * Class SoundManager
 * 
 * @author pavlvsmaximvs Handles music and sound effects for the game
 */

public class SoundManager {
	String soundEffects[] = {};
	ArrayList<SoundRunner> bgmList = new ArrayList<SoundRunner>();

	public final static SoundManager INSTANCE = new SoundManager();

	private SoundManager() {
		// Exists only to defeat instantiation.
	}

	public void loadSounds() {
		for (int i = 0; i < LevelManager.NUM_LEVELS; i++) {
			try {
				InputStream is = SoundManager.class.getClassLoader().getResourceAsStream("level" + i + ".mp3");
				BufferedInputStream bis = new BufferedInputStream(is);
				SoundPlayer player = new SoundPlayer(bis);
				SoundRunner file = new SoundRunner(player);
				bgmList.add(file);
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
		}
	}

	public void startLevelBGM(int levelIndex) {
		bgmList.get(levelIndex).startPlayer();
	}

	public void stopLevelBGM(int levelIndex) {
		bgmList.get(levelIndex).stopPlayer();
	}

	public boolean isPaused(int levelIndex) {
		return bgmList.get(levelIndex).isPaused();
	}
}
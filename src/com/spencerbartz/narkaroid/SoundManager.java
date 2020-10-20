package com.spencerbartz.narkaroid;

import java.util.ArrayList;



/**
 * Class SoundManager
 * 
 * @author pavlvsmaximvs 
 * Handles music and sound effects for the game
 */

public class SoundManager {
	private ArrayList<PausableMp3> levelMusicMp3s = new ArrayList<PausableMp3>();
	private PausableMp3 titleScreenMp3;
	
	public final static SoundManager INSTANCE = new SoundManager();

	private SoundManager() {
		// Exists only to defeat instantiation.
	}

	public void loadSounds() {
		for (int i = 0; i < LevelManager.NUM_LEVELS; i++) {
			PausableMp3 levelMusic = new PausableMp3("sounds/level" + i + ".mp3", true);
			levelMusicMp3s.add(levelMusic);
		}
		
		titleScreenMp3 = new PausableMp3("sounds/titlescreen.mp3", true);
	}
	
	public void startTitleScreenMusic() {
		titleScreenMp3.play();
	}
	
	public boolean isTitleScreenMusicPlaying() {
		return titleScreenMp3.isPlaying();
	}

	public void startLevelBGM(int levelIndex) {
		levelMusicMp3s.get(levelIndex).play();
	}

	public void stopLevelBGM(int levelIndex) {
		// bgmList.get(levelIndex).stopPlayer();
	}

	public boolean isPaused(int levelIndex) {
		// return bgmList.get(levelIndex).isPaused();
		return false;
	}
}

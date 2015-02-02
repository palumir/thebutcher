package audio;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import main.Main;

public class SoundClip {
	
	private Clip clip;
	private AudioInputStream inputStream;
	
	public SoundClip(String fileLoc) {
	      try {
	          URL url = this.getClass().getResource(fileLoc);
	          inputStream = AudioSystem.getAudioInputStream(url);
	          clip = AudioSystem.getClip();
	          clip.open(inputStream);
	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	}
	
	public Clip getClip() {
		return clip;
	}
}
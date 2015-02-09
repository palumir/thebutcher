package audio;

import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundClip {
	
	// Store all the clips for purging.
	private static ArrayList<SoundClip> allClips = new ArrayList<SoundClip>();
	
	// Clip if small, bigClip if large.
	private boolean small = true;
	private Clip clip;
	private BigClip bigClip;
	private AudioInputStream inputStream;
	
	// Is it playing?
	public boolean playing = false;
	
	public SoundClip(String fileLoc, boolean isSmall) {
	      try {
	    	  if(isSmall) {
		          URL url = this.getClass().getResource(fileLoc);
		          inputStream = AudioSystem.getAudioInputStream(url);
		          clip = AudioSystem.getClip();
		          clip.open(inputStream);
	    	  }
	    	  else {
	    	        URL url = this.getClass().getResource(fileLoc);
	    	        bigClip = new BigClip();
	    	        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
	    	        bigClip.open(ais);
	    	  }
	    	  small = isSmall;
		      allClips.add(this);
	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	}
	
	public static void stopSounds() {
		for(int i=0; i < allClips.size(); i++) {
			if(allClips.get(i).small) allClips.get(i).clip.stop();
			else allClips.get(i).bigClip.stop();
		}
	}
	
	public void setVolume(float f) {
		FloatControl gainControl;
		if(small) gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
		else gainControl = (FloatControl) this.bigClip.getControl(FloatControl.Type.MASTER_GAIN);
		// -15 => +30
		if(f>6) f = 6;
		gainControl.setValue(f);
	}
	
	public void loop(int i) {
		playing = true;
		if(small) { 
			clip.stop();
			clip.loop(i);
		}
		else {
			bigClip.stop();
			bigClip.loop(i);
		}
	}
	
	public void start() {
		playing = true;
		if(small) { 
			clip.stop();
			clip.start();
		}
		else { 
			bigClip.stop();
			bigClip.start();
		}
	}
	
	public void stop() {
		playing = false;
		if(small) clip.stop();
		else bigClip.stop();
	}
}
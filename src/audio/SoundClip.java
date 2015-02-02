package audio;

import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundClip {
	
	// Store all the clips for purging.
	private static ArrayList<SoundClip> allClips = new ArrayList<SoundClip>();
	
	// Clip if small, bigClip if large.
	private boolean small = true;
	private Clip clip;
	private BigClip bigClip;
	private AudioInputStream inputStream;
	
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
			if(allClips.get(i).small) allClips.get(i).getClip().stop();
			else allClips.get(i).getBigClip().stop();
		}
	}
	
	public Clip getClip() {
		return clip;
	}
	
	public BigClip getBigClip() {
		return bigClip;
	}
}
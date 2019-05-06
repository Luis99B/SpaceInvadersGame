//LuisBodart	A01635000
package spaceInvaders;

import java.io.*;
import javax.swing.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Aliens extends Sprites implements Constants {
	
	private final String alien1Img = "src/images/alien1.png", alien2Img = "src/images/alien2.png";
	private Bomb bomb;
	private final static String alienExplosion = "src/sfx/alienExplosion.wav";

	public Aliens(int x, int y) {
		this.x = x;
		this.y = y;
		ImageIcon ufo1 = new ImageIcon(alien1Img);
		setImage(ufo1.getImage());
		ImageIcon ufo2 = new ImageIcon(alien2Img);
		setImage(ufo2.getImage());
		bomb = new Bomb(x, y);
	}
	
	public void act(int direction) {
		this.x += direction;
	}
	
	public static void dead() {
			try {
				File sfx = new File(alienExplosion);
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(sfx));
				clip.start();
				while (clip.isRunning()) {				
					Thread.sleep(clip.getMicrosecondLength());
					clip.close();
				}
				//System.out.println("BOOM");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}
	
	public Bomb getBomb() { return bomb; }
	
}

class Bomb extends Sprites {
	
	private final String bombImg = "src/images/bomb.png";
	private boolean destroyed;
	private final static String alienShot = "src/sfx/playerShot.wav";
	
	public Bomb(int x, int y) {
		initBomb(x, y);
	}
	
	private void initBomb(int x, int y) {
		setDestroyed(true);
		this.x = x;
		this.y = y;
		ImageIcon smartB = new ImageIcon(bombImg);
		setImage(smartB.getImage());
		//sound();
	}
	
	public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
	public boolean isDestroyed() { return destroyed; }
	
	public static void sound() {
		try {
			File sfx = new File(alienShot);
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sfx));
			clip.start();
			while (clip.isRunning()) {				
				Thread.sleep(clip.getMicrosecondLength());
				clip.close();
			}
			//System.out.println("PEW-pew");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

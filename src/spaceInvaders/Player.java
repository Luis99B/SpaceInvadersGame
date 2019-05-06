//LuisBodart	A01635000
package spaceInvaders;

import java.io.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Player extends Sprites implements Constants {
	
	private final int START_X = BOARD_WIDTH / 2, START_Y = BOARD_HEIGHT - 95;
	private final String playerImg = "src/images/player.png";
    private int widht;
	private final static String playerExplosion = "src/sfx/playerExplosion.wav";
	
	public Player() {
		ImageIcon ship = new ImageIcon(playerImg);
		widht = ship.getImage().getWidth(null);
		setImage(ship.getImage());
        setX(START_X);
        setY(START_Y);
	}
	
	public void act() {
		x += dx;
		if (x <= 2) {
			x = 2;
		}
		if (x >= BOARD_WIDTH - 2 * widht) {
			x = BOARD_WIDTH - 2 * widht;
		}
	}
	
	public static void dead() {
		try {
			File sfx = new File(playerExplosion);
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sfx));
			clip.start();
			while (clip.isRunning()) {				
				Thread.sleep(clip.getMicrosecondLength());
				clip.close();
			}
			//System.out.println("DEAD");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			dx = -2;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			dx = 2;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			dx = 0;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			dx = 0;
		}
	}
	
}

class Shot extends Sprites {
	
	private final String shotImg = "src/images/shot.png";
	private final int H_SPACE = 12, V_SPACE = 2;
	private final static String playerShot = "src/sfx/playerShot.wav";
	
	public Shot(int x, int y) {
		initShot(x, y);
	}
	
	private void initShot(int x, int y) {
		ImageIcon pew = new ImageIcon(shotImg);
		setImage(pew.getImage());
		setX(x + H_SPACE);
		setY(y - V_SPACE);
		sound();
	}
	
	public static void sound() {
		try {
			File sfx = new File(playerShot);
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sfx));
			clip.start();
			while (clip.isRunning()) {				
				Thread.sleep(clip.getMicrosecondLength());
				clip.close();
			}
			//System.out.println("PEW");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

//LuisBodart	A01635000
package spaceInvaders;

import java.util.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class Board extends JPanel implements Constants, Runnable {
	
	private Dimension d;
	private Player player;
	private LinkedList<Aliens> aliens;
    private Shot shot;
	    
	private final int ALIEN_INIT_X = 150, ALIEN_INIT_Y = 25;
	private int direction = 1;
	private int deaths = 0;
	
	private final String explosion = "src/images/explosion.png";
	private String msg = "Game Over";
	private boolean inGame = true;
	
	private static int delay = 15;
	private static int changeDelay = 0;
	
	private Thread animator;
	
	public Board() {
		addKeyListener(new Keyboard());
		setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.BLACK);
        gameInit();
        setDoubleBuffered(true);
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		gameInit();
	}
	
	public void gameInit() {
		aliens = new LinkedList<>();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {
				Aliens alien = new Aliens(ALIEN_INIT_X + 45 * j, ALIEN_INIT_Y + 30 * i);
				aliens.add(alien);
			}
		}
		player = new Player();
        shot = new Shot(getX(), getY());
		if (animator == null || !inGame) {
            animator = new Thread(this);
            animator.start();
        }
	}
	
	private void drawAliens(Graphics g) {
		for (Aliens alien: aliens) {
			if (alien.isVisible()) {
				g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
			}
			if (alien.isDying()) {
				alien.die();
			}
		}
	}
	
	private void drawPlayer(Graphics g) {
		if (player.isVisible()) {
			g.drawImage(player.getImage(), player.getX(), player.getY(), this);
		}
		if (player.isDying()) {
			player.die();
			inGame = false;
		}
	}
	
	private void drawShot(Graphics g) {
		if (shot.isVisible()) {
			g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
		}
	}
	
	private void drawBombs(Graphics g) {
		for (Aliens a : aliens) {
			Bomb b = a.getBomb();
			if (!b.isDestroyed()) {
				g.drawImage(b.getImage(), b.getX(), b.getY(), this);
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, d.width, d.height);
		g.setColor(Color.GREEN);
		if (inGame) {
			g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
			drawAliens(g);
			drawPlayer(g);
			drawShot(g);
			drawBombs(g);
		}
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}
	
	private void animation() {
		changeDelay();
		if (deaths == NUM_ALIENS) {
			msg = "You Win";
			inGame = false;
		}
		// player
		player.act();
		// shot
		if (shot.isVisible()) {
			int shotX = shot.getX(), shotY = shot.getY();
			for (Aliens alien: aliens) {
				int alienX = alien.getX(), alienY = alien.getY();
				if (alien.isVisible() && shot.isVisible()) {
					if (shotX >= (alienX) && shotX <= (alienX + ALIEN_WIDTH) && shotY >= (alienY) && shotY <= (alienY + ALIEN_HEIGHT)) {
						ImageIcon ii = new ImageIcon(explosion);
						alien.setImage(ii.getImage());
						alien.setDying(true);
						Aliens.dead();
						deaths++;
						shot.die();
					}
				}
			}
			int y = shot.getY();
			y -= 4;
			if (y < 0) {
				shot.die();
				} else {
					shot.setY(y);
				}
		}
		// aliens
		for (Aliens alien: aliens) {
			int x = alien.getX();
			if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
				direction = -1;
				Iterator<Aliens> i1 = aliens.iterator();
				while (i1.hasNext()) {
					Aliens a2 = i1.next();
					a2.setY(a2.getY() + GO_DOWN);
				}
			}
			if (x <= BORDER_LEFT && direction != 1) {
				direction = 1;
				Iterator<Aliens> i2 = aliens.iterator();
				while (i2.hasNext()) {
					Aliens a = (Aliens) i2.next();
					a.setY(a.getY() + GO_DOWN);
					}
				}
			}
		Iterator<Aliens> it = aliens.iterator();
		while (it.hasNext()) {
			Aliens alien = (Aliens) it.next();
			if (alien.isVisible()) {
				int y = alien.getY();
				if (y > GROUND - ALIEN_HEIGHT) {
					inGame = false;
					msg = "Invasion!";
				}
				alien.act(direction);
			}
		}
		// bombs
		Random generator = new Random();
		for (Aliens alien: aliens) {
			int shot = generator.nextInt(15);
			Bomb b = alien.getBomb();
			if (shot == CHANCE && alien.isVisible() && b.isDestroyed()) {
				b.setDestroyed(false);
				b.setX(alien.getX());
				b.setY(alien.getY());
			}
			int bombX = b.getX(), bombY = b.getY();
			int playerX = player.getX(), playerY = player.getY();
			if (player.isVisible() && !b.isDestroyed()) {
				if (bombX >= (playerX) && bombX <= (playerX + PLAYER_WIDTH) && bombY >= (playerY) && bombY <= (playerY + PLAYER_HEIGHT)) {
					ImageIcon boom = new ImageIcon(explosion);
					player.setImage(boom.getImage());
					player.setDying(true);
					Player.dead();
					b.setDestroyed(true);
				}
			}
			if (!b.isDestroyed()) {
				b.setY(b.getY() + 1);
				if (b.getY() >= GROUND - BOMB_SIZE) {
					b.setDestroyed(true);
					}
				}
			}
		}
	
	private void changeDelay() {
		if (deaths == NUM_ALIENS - 18 && changeDelay  == 0) {
			delay = 12;
			changeDelay++;
			System.out.println("Delay changed to " + delay);
		} else if (deaths == NUM_ALIENS - 15 && changeDelay  == 1) {
			delay = 10;
			changeDelay++;
			System.out.println("Delay changed to " + delay);
		} else if (deaths == NUM_ALIENS - 12 && changeDelay  == 2) {
			delay = 8;
			changeDelay++;
			System.out.println("Delay changed to " + delay);
		} else if (deaths == NUM_ALIENS - 9 && changeDelay  == 3) {
			delay = 6;
			changeDelay++;
			System.out.println("Delay changed to " + delay);
		} else if (deaths == NUM_ALIENS - 6 && changeDelay  == 4) {
			delay = 4;
			changeDelay++;
			System.out.println("Delay changed to " + delay);
		} else if (deaths == NUM_ALIENS - 3 && changeDelay  == 5) {
			delay = 2;
			changeDelay++;
			System.out.println("Delay changed to " + delay);
		}
	}
	
	public void gameOver() {
		Graphics g = this.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		g.setColor(Color.BLUE);
		g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
		g.setColor(Color.WHITE);
		g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);
		g.setColor(Color.WHITE);
		g.setFont(small);
		g.drawString(msg, (BOARD_WIDTH - metr.stringWidth(msg)) / 2, BOARD_WIDTH / 2);
	}
	
	@Override
	public void run() {
		long beforeTime, timeDiff, sleep;
		beforeTime = System.currentTimeMillis();
		while (inGame) {
			repaint();
			animation();
			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = delay - timeDiff;
			if (sleep < 0) {
				sleep = 1;
			}
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.println("interrupted");
			}
			beforeTime = System.currentTimeMillis();
		}
		gameOver();
	}
	
	private class Keyboard extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			player.keyPressed(e);
			int x = player.getX(), y = player.getY();
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (inGame) {
					if (!shot.isVisible()) {
						shot = new Shot(x,y);
					}
				}
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			super.keyReleased(e);
			player.keyReleased(e);
		}
		
	}
}

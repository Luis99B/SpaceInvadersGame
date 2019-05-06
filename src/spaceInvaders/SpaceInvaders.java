//LuisBodart	A01635000
package spaceInvaders;

import java.awt.*;
import javax.swing.*;

public class SpaceInvaders extends JFrame implements Constants {
	
	public SpaceInvaders() {
		add(new Board());
		setTitle("Space Invaders");
		setSize(BOARD_WIDTH, BOARD_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater( () -> {
            SpaceInvaders game = new SpaceInvaders();
            game.setVisible(true);
        } );
	}
}

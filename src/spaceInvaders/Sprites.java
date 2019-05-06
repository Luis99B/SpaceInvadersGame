//LuisBodart	A01635000
package spaceInvaders;

import java.awt.Image;

public class Sprites {
	
	private boolean visible;
    private Image image;
    protected int x, y;
    protected boolean dying;
    protected int dx;
    
    public Sprites() {
    	visible = true;
    }
    
    public void die() {
    	visible = false;
    }
    
    public Image getImage() { return image; }
	public void setImage(Image image) { this.image = image; }

	protected void setVisible(boolean visible) { this.visible = visible; }
	public boolean isVisible() { return visible; }

	public void setDying(boolean dying) { this.dying = dying; }
	public boolean isDying() { return this.dying; }

	public int getX() { return x; }
	public void setX(int x) { this.x = x; }

	public int getY() { return y; }
	public void setY(int y) { this.y = y; }
	
}

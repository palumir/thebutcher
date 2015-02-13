package drawables;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import main.Main;
import units.Player;
import units.Unit;

// One drawable object.
public class Node implements MouseMotionListener, MouseListener {

	// Positioning
	public AffineTransform trans = new AffineTransform();
	protected boolean movesWithPlayer = false;
	protected int zIndex = 0;
	
	// Are we hidden
	protected boolean hidden = false;
	
	// Cosmetics
	private int width;
	private int height;
	protected boolean shapeHidden = false;
	
	// Node specific stuff
	ArrayList<Node> children = new ArrayList<Node>();
	public Node parent = null;
	private String id;

	// Create a node. But where?
	public Node(int w, int h) {
		width = w;
		setHeight(h);
		this.id = "id";
		Canvas.getGameCanvas().addNode(this);
	}
	
	// Add a child node.
	public void addChild(Node child) {
		child.id = this.id + "." + (this.children.size());
		this.children.add(child);
		child.parent = this;
	}
	
	// Get parent node.
	public Node getParent() {
		return this.parent;
	}

	// Animation timer has ticked.
	public void update() {
		this.updateChildren();
	}

	// Update the kids.
	public void updateChildren() {
		if(children != null && children.size() > 0) {
			for(int i = 0; i < children.size(); i++) {
				Node s = children.get(i);
				s.update();
			}
		}
	}

	// Transforms the current node.
	public void transform(AffineTransform t) {
		this.trans.concatenate(t);
	}
	
	// Return a new translated node.

	// Paint the node and it's kids.
	public void paintNode(Graphics2D g2) {
		// Only paint it if the player can see it...
		if(Player.getCurrentPlayer().close(Math.max(Canvas.getDefaultHeight()/2 + 200, Canvas.getDefaultWidth()/2 + 200),this)) {
				// Remember the transform being used when called
				AffineTransform t = g2.getTransform();
				// Maintain aspect ratio.
				AffineTransform currentTransform = this.getFullTransform();
				g2.translate(currentTransform.getTranslateX()*((double)Canvas.getGameCanvas().getWidth()/(double)Canvas.getDefaultWidth()),currentTransform.getTranslateY()*((double)Canvas.getGameCanvas().getHeight()/(double)Canvas.getDefaultHeight()));
				g2.scale(currentTransform.getScaleX()*((double)Canvas.getGameCanvas().getWidth()/(double)Canvas.getDefaultWidth()),currentTransform.getScaleY()*((double)Canvas.getGameCanvas().getHeight()/(double)Canvas.getDefaultHeight()));
				
				// Restore the transform.
				g2.setTransform(t);
		
				// Paint each child
				for (Node c : this.children) {
					c.paintNode(g2);
				}
		
				// Restore the transform.
				g2.setTransform(t);
			}
	}
	
	// Test intersection of two nodes. Does not ask about children. ASSUMES RECTANGLES. D:
	public boolean touching(Node n, String direction, float x, float y) {	
		// Check what direction we're touching things from
		double x1 = this.trans.getTranslateX();
		double y1 = this.trans.getTranslateY();
		double x2 = n.trans.getTranslateX();
		double y2 = n.trans.getTranslateY();
		boolean isTouching = false;
		if(direction.equals("Up")) { 
			isTouching = y1 - getHeight()/6 - y < y2 + n.getHeight()
					&& !(y1 < y2) // Shape is NOT above Y2
					&& (x1 + width/2 > x2) // To the right of X2
					&& (x1 - width/2 < x2 + n.width); // To the left of X2 plus the width
		}
		if(direction.equals("Down")) { 
			isTouching = y2 <= y1 + getHeight()/2 - y // Shape is below top of y2
					&& !(y1 + getHeight()/2 > y2 + n.getHeight()) // Shape is NOT below the bottom of y2
					&& (x1 + width/2 > x2) // To the right of X2
					&& (x1 - width/2 < x2 + n.width); // To the left of X2 plus the width
			if(isTouching) { 
				if(this instanceof Unit) {
					((Unit)this).onGround();
				}
			}
		}
		if(direction.equals("Left")) { 
			isTouching = y1 + getHeight()/2 - 1 > y2
					&& y1 < y2 + n.getHeight()
					&& x1 - width/2 - x < x2 + n.width
					&& !(x1 < x2); 
		}
		if(direction.equals("Right")) { 
			isTouching =  y1 + getHeight()/2 - 1  > y2
					&& y1 < y2 + n.getHeight() 
					&& x1 + width/2 - x > x2
					&& !(x1 > x2);
		}
		return isTouching;
	}

	// Copies the node's transform. That's all.
	public AffineTransform getLocalTransform() {
		return new AffineTransform(this.trans);
	}

	// Returns the entire transform of this node, from node to it's highest parent.
	public AffineTransform getFullTransform() {
		// Start with an identity matrix. Concatenate on the left
		// every local transformation matrix from here to the root.
		AffineTransform at = new AffineTransform();
		Node curNode = this;
		while (curNode != null) {
			at.preConcatenate(curNode.getLocalTransform());
			curNode = curNode.getParent();
		}
		return at;
	}

	// Returns the inverse of the transform.
	public AffineTransform getFullInverseTransform() {
		try {
			AffineTransform t = this.getFullTransform();
			AffineTransform tp = t.createInverse();
			return tp;
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new AffineTransform();
		}
	}
	
	// Delete the node
	public void deleteNode() {
		for (int i = 0; i < Canvas.getGameCanvas().getNodes().size(); i++) {
			Node n = Canvas.getGameCanvas().getNodes().get(i);
			if(n==this) {
				Canvas.getGameCanvas().getNodes().remove(i);
				break;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	// Check if this unit is within a radius of u;
	public boolean close(int radius, Node u) {
		float yDistance = (float) (u.trans.getTranslateY() - this.trans.getTranslateY());
		float xDistance = (float) (u.trans.getTranslateX() - this.trans.getTranslateX());
		float distanceXY = (float) Math.sqrt(yDistance * yDistance
				+ xDistance * xDistance); // It should take this many frames
		return radius > distanceXY;
	}
	
	public float getWidth() {
		return width;
	}
	
	// Node movement basics
	public void instantlyMove(float x, float y) {
		transform(AffineTransform.getTranslateInstance(x, y));
	}

	public boolean isMovesWithPlayer() {
		return movesWithPlayer;
	}

	public void setMovesWithPlayer(boolean movesWithPlayer) {
		this.movesWithPlayer = movesWithPlayer;
	}

	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}


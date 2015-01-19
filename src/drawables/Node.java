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

// One drawable object.
public class Node implements MouseMotionListener, MouseListener {

	public AffineTransform trans = new AffineTransform();
	private Shape shape;
	ArrayList<Node> children = new ArrayList<Node>();
	public Node parent = null;
	private String id;
	private Color color = Color.RED;

	// Create a node. But where?
	public Node(Shape s, Color color) {
		this.id = "id";
		this.setShape(s);
		this.color = color;
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
	
	// Does the node contain a point?
	public boolean containsPoint(Point2D p) {
		AffineTransform inverseTransform = this.getFullInverseTransform();
		Point2D pPrime = inverseTransform.transform(p, null);
		return this.getShape().contains(pPrime);
	}

	// Gets the node hit at p. Prefers children.
	public Node hitNode(Point2D p) {
		for (Node c : this.children) {
			Node hit = c.hitNode(p);
			if (hit != null)
				return hit;
		}
		if (this.containsPoint(p)) {
			return this;
		} else {
			return null;
		}
	}

	// Transforms the current node.
	public void transform(AffineTransform t) {
		this.trans.concatenate(t);
	}

	// Paint the node and it's kids.
	public void paintNode(Graphics2D g2) {
		// Remember the transform being used when called
		AffineTransform t = g2.getTransform();
		g2.transform(this.getFullTransform());
		g2.setColor(this.color);
		g2.fill(this.getShape());
		
		// Restore the transform.
		g2.setTransform(t);

		// Paint each child
		for (Node c : this.children) {
			c.paintNode(g2);
		}

		// Restore the transform.
		g2.setTransform(t);
	}
	
	// Test intersection of two nodes. Does not ask about children. ASSUMES RECTANGLES. D:
	public boolean touching(Node n, String direction) {
		double x1 = this.trans.getTranslateX();
		double y1 = this.trans.getTranslateY();
		double x2 = n.trans.getTranslateX();
		double y2 = n.trans.getTranslateY();
		boolean isTouching = false;
		if(direction.equals("Up")) isTouching = false;
		if(direction.equals("Down")) isTouching = (y2 - (y1 + ((Rectangle2D)this.getShape()).getHeight()/2) <= 0) // Shape is below top of y2
												&& !(y1 + ((Rectangle2D)this.getShape()).getHeight()/2 > y2 + ((Rectangle2D)n.getShape()).getHeight()) // Shape is NOT below the bottom of y2
												&& (x1 + ((Rectangle2D)this.getShape()).getWidth()/2 >= x2) // To the right of X2
												&& (x1 + ((Rectangle2D)this.getShape()).getWidth()/2 <= x2 + ((Rectangle2D)n.getShape()).getWidth()); // To the left of X2 plus the width
		if(direction.equals("Left")) isTouching = false;
		if(direction.equals("Right")) isTouching = false;
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
	
	// Node movement basics
	public void instantlyMove(int x, int y) {
		transform(AffineTransform.getTranslateInstance(x, y));
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

}


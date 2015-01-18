package drawables;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
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
		this.shape = s;
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
		return this.shape.contains(pPrime);
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
		g2.fill(this.shape);
		
		// Restore the transform.
		g2.setTransform(t);

		// Paint each child
		for (Node c : this.children) {
			c.paintNode(g2);
		}

		// Restore the transform.
		g2.setTransform(t);
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

}


package drawables;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.Timer;

import main.Main;
import terrain.TerrainChunk;
import units.Player;
import units.Unit;

// The actual canvas the game is drawn on.
public class Canvas extends JComponent {

	// List of all nodes to be drawn (pretty much everything)
	private ArrayList<Node> nodes = new ArrayList<Node>();
	
	// The game canvas. Only one.
	private static Canvas gameCanvas;
	private static int maxFPS = 50; // How often we update the animation.
	private Timer timer; // The timer to actually cause the animation updates.
	private static long gameTime = 0; // The clock for the current canvas. In
										// milliseconds.
	// Screen information
	private static int defaultWidth = 600;
	private static int defaultHeight = 600;

	// Initialize the game canvas.
	public static void initCanvas() {
		setGameCanvas(new Canvas());
	}

	// The thing that performs the tasks every time the timer ticks.
	ActionListener taskPerformer = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			updateNodes();
			Unit.updateUnits();
			repaint();
		}
	};

	// Run through each node and update it.
	void updateNodes() {
		for (int i = 0; i < getNodes().size(); i++) {
			Node n = getNodes().get(i);
			n.update();
		}
	}

	// Move all nodes except for...
	public void moveAllButWithNoClip(Node notMove, float x, float y) {
		for(int i = 0; i < getNodes().size(); i++) {
			Node n = getNodes().get(i);
			if(n!=notMove && !n.isMovesWithPlayer()) {
				if(n instanceof Unit && !(n instanceof Player)) {
					((Unit) n).setX((float) (n.trans.getTranslateX() + x));
					((Unit) n).setY((float) (n.trans.getTranslateY() + y));
				}
				n.instantlyMove(x,y);
			}
		}
	}
	
	// Move a unit
	public void moveUnit(Unit move, float x, float y) {
		
		// Are we landing on something?
		if(TerrainChunk.touchingTerrain(move, "Down", -x, -y)) if(y>0) { 
			y = 0;
			((Unit)move).setFallSpeed(Unit.getDefaultFallSpeed());
		}
		// Are we landing on something?
		if(TerrainChunk.touchingTerrain(move, "Left", -x, -y)) if(x<0) { 
			x = 0;
		}
		// Are we landing on something?
		if(TerrainChunk.touchingTerrain(move, "Right", -x, -y)) if(x>0) { 
			x = 0;
		}
		if(move instanceof Unit && !(move instanceof Player)) {
			((Unit)move).setX((float) ((Unit) move).getX() + x);
			((Unit)move).setY((float) ((Unit) move).getY() + y);
		}
		move.instantlyMove(x, y);
	}
	
	// Move all nodes except for...
	public void moveAllBut(Unit notMove, float x, float y) {
		
		// Are we hitting the roof?
		if(TerrainChunk.touchingTerrain(notMove, "Up", x, y)) if(y>0) { 
			y = 0;
			((Unit)notMove).setFallSpeed(0);
		}
		
		// Are we landing on something?
		if(TerrainChunk.touchingTerrain(notMove, "Down", x, y)) if(y<0) { 
			y = 0;
			((Unit)notMove).setFallSpeed(Unit.getDefaultFallSpeed());
		}
	
		// Are we moving Left?
		if(TerrainChunk.touchingTerrain(notMove, "Left", x, y)) if(x>0) { 
			x = 0;
		}
		
		// Are we moving Right?
		if(TerrainChunk.touchingTerrain(notMove, "Right", x, y)) if(x<0) { 
			x = 0;
		}
		
		for(int i = 0; i < getNodes().size(); i++) {
			Node n = getNodes().get(i);
			if (n != notMove && !n.isMovesWithPlayer()) {
				n.instantlyMove(x, y);
			}
		}
		
		if(notMove instanceof Player) {
			// Tell the terrain system "where" our unit is
			((Player) notMove).setY(((Player) notMove).getY() - y);
			((Player) notMove).setX(((Player) notMove).getX() - x);
		}
	}

	// Constructor. Pretty basic.
	public Canvas() {

		// Start the game timer.
		this.setOpaque(true); // we paint every pixel; Java can optimize
		timer = new Timer(1000/maxFPS, taskPerformer);
		timer.setInitialDelay(190);
		timer.start();
		this.setFocusable(true);
		requestFocus(); 
		
		this.addComponentListener(new ComponentAdapter() {
	        public void componentResized(ComponentEvent evt) {
	            Component c = (Component)evt.getSource();
	            repaint();
	        }
		});
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent k) {
				Player.keyPressed(k);
			}

			@Override
			public void keyReleased(KeyEvent k) {
				Player.keyReleased(k);
			}

			@Override
			public void keyTyped(KeyEvent k) {
				// Do nothing. We don't care yet.
			}
		});
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println(gameCanvas.getWidth());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				/*if (e.getButton() == MouseEvent.BUTTON1) {
					mouseDown = true;
					initThread();
				}*/
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				/*if (e.getButton() == MouseEvent.BUTTON1) {
					mouseDown = false;
				}*/
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		/*this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// swingRecognize();
				// boolean swingStarted = true;

				// System.out.println("Dragging");
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});*/
	}

	/*private synchronized boolean checkAndMark() {
		if (swinging)
			return false;
		swinging = true;
		return true;
	}

	private void initThread() {
		if (checkAndMark()) {
			new Thread() {
				public void run() {
					do {

						Point mousePosition = MouseInfo.getPointerInfo()
								.getLocation();

						int i = 0;

						long curTime = Main.getGameTime();

						while (i < 100 && mouseDown) {

							long nextTime = Main.getGameTime();

							if (nextTime - curTime > 500) {

								System.out.println((1 / 2.0)
										* Math.sin(i - (Math.PI / 2.0))
										+ (1 / 2.0));
								i++;
								// System.out.println(mousePosition);
								swingRecognize(mousePosition.x, mousePosition.y);
								curTime = Main.getGameTime();
							}
						}
					} while (mouseDown);
					swinging = false;
				}
			}.start();
		}
	}*/

	// Paint each node. Easy.
	public void paintNodes(Graphics g) {
		int paintedNodes=0;
		for(int i = 0; i < getNodes().size(); i++) {
			for (Node n : getNodes()) {
				if(n.zIndex == i) { 
					n.paintNode((Graphics2D) g);
					paintedNodes++;
				}
			}
			if(paintedNodes>=getNodes().size()) break;
		}
	}

	// Paint the background. Just black for now.
	public void paintBackground(Graphics2D g2) {
		Background.paintBackground(g2, this.getWidth(), this.getHeight());
	}

	// Paint the game canvas.
	public void paintComponent(Graphics g) {

		// Set up the g2 object and paint itself.
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);

		// Paint the background.
		paintBackground(g2);

		// Paint all drawable nodes.
		paintNodes(g);
	}

	// Add a new node to be drawn on the canvas.
	public void addNode(Node n) {
		this.getNodes().add(n);
	}

	public static Canvas getGameCanvas() {
		return gameCanvas;
	}

	public static void setGameCanvas(Canvas gameCanvas) {
		Canvas.gameCanvas = gameCanvas;
	}

	/*public void swingRecognize(int initialXPos, int initialYPos) {

		Point pos1 = new Point(initialXPos, initialYPos);
		int thrustXThreshold = 15;
		int slashXThreshold = 15;
		int slashYThreshold = 30;
		int chopXThreshold = 15;
		int chopYThreshold = -30;

		Point pos2 = MouseInfo.getPointerInfo().getLocation();

		// System.out.println(pos2);

		int absXChange = pos2.x - pos1.x;
		int absYChange = pos2.y - pos1.x;

		int percentXChange = (int) ((absXChange * 1.0)
				/ (getWidth() * 1.0) * 100.0);
		int percentYChange = (int) (((absYChange * 1.0)
				/ (getHeight() * 1.0) * 100.0) * -1.0);

		// System.out.println(percentXChange);
		// System.out.println(percentYChange);

		if (percentXChange >= thrustXThreshold
				&& percentYChange < slashYThreshold
				&& percentYChange > chopYThreshold) {

			// thrust
			System.out.println("Thrust");
			mouseDown = false;
		}

		else if (percentXChange >= slashXThreshold
				&& percentYChange >= slashYThreshold) {

			// chop
			System.out.println("Slash");
			mouseDown = false;
		}

		else if (percentXChange >= chopXThreshold
				&& percentYChange <= chopYThreshold) {

			// slash
			System.out.println("Chop");
			mouseDown = false;
		}

		else {

			// no action
			System.out.println("Fail");
		}
	}*/

	public static int getDefaultWidth() {
		return defaultWidth;
	}

	public static void setDefaultWidth(int defaultWidth) {
		Canvas.defaultWidth = defaultWidth;
	}

	public static int getDefaultHeight() {
		return defaultHeight;
	}

	public static void setDefaultHeight(int defaultHeight) {
		Canvas.defaultHeight = defaultHeight;
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public static int getFPS() {
		return maxFPS;
	}

	public static void setFPS(int fPS) {
		maxFPS = fPS;
	}

}

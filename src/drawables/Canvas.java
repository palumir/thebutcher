package drawables;

import items.Weapon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
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
	private static int FPS = 60; // How often we update the animation.
	private Timer timer; // The timer to actually cause the animation updates.
	private static long gameTime = 0; // The clock for the current canvas. In
										// milliseconds.
<<<<<<< HEAD
	// Screen information
	private static int defaultWidth = 600;
	private static int defaultHeight = 600;
	
	// Combat physics/Mouse Movements
=======
	
//	private Weapon currentlyEquipped; This might be information obtained from the Player instead.
>>>>>>> 349e13eb763cf30ad8f2003a6def75d8bb3dcc40
	private boolean mouseDown = false;
	private boolean swinging = false;

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
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			n.update();
		}
	}

	// Move all nodes except for...
	public void moveAllBut(Node notMove, int x, int y) {
		if (TerrainChunk.touchingTerrain(notMove, "Down"))
			if (y < 0)
				y = 0;
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if (n != notMove) {
				n.instantlyMove(x, y);
			}
		}
	}

	// Constructor. Pretty basic.
	public Canvas() {

		// Start the game timer.
		this.setOpaque(true); // we paint every pixel; Java can optimize
		timer = new Timer(20, taskPerformer);
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
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mouseDown = true;
					initThread();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mouseDown = false;
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		this.addMouseMotionListener(new MouseMotionListener() {

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

		});
	}

	private synchronized boolean checkAndMark() {
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
	}

	// Paint each node. Easy.
	public void paintNodes(Graphics g) {
		for (Node n : nodes)
			n.paintNode((Graphics2D) g);
	}

	// Paint the background. Just black for now.
	public void paintBackground(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
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
		this.nodes.add(n);
	}

	// Get the node at p.
	public Node getNode(Point2D p) {
		Node hit = null;
		int i = 0;
		while (hit == null && i < nodes.size()) {
			hit = nodes.get(i).hitNode(p);
			i++;
		}
		return hit;
	}

	public static Canvas getGameCanvas() {
		return gameCanvas;
	}

	public static void setGameCanvas(Canvas gameCanvas) {
		Canvas.gameCanvas = gameCanvas;
	}

	public void swingRecognize(int initialXPos, int initialYPos) {

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
	}

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

}

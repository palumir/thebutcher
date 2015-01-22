package drawables;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.lang.Math;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import main.Main;
import player.Player;
import terrain.TerrainChunk;
import units.Unit;

// The actual canvas the game is drawn on.
public class Canvas extends JComponent {

	private ArrayList<Node> nodes = new ArrayList<Node>();
	private static Canvas gameCanvas;
	private static int FPS = 20; // How often we update the animation.
	private Timer timer; // The timer to actually cause the animation updates.
	private static long gameTime = 0; // The clock for the current canvas. In
										// milliseconds.
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

		// Deal with key presses for the game.
		this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "up");
		this.getActionMap().put("up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Climb/Enter door?
			}
		});
		this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down");
		this.getActionMap().put("down", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Crouch?
			}
		});
		this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
		this.getActionMap().put("left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Move left.
				Canvas.getGameCanvas().moveAllBut(Player.getSelectedUnit(), 3,
						0);
			}
		});
		this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
		this.getActionMap().put("right", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Move right.
				Canvas.getGameCanvas().moveAllBut(Player.getSelectedUnit(), -3,
						0);
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
		int chopXThreshold = 15;
		int chopYThreshold = 30;
		int slashXThreshold = 15;
		int slashYThreshold = -30;

		Point pos2 = MouseInfo.getPointerInfo().getLocation();

		// System.out.println(pos2);

		int absXChange = pos2.x - pos1.x;
		int absYChange = pos2.y - pos1.x;

		int percentXChange = (int) ((absXChange * 1.0)
				/ (Main.getScreenWidth() * 1.0) * 100.0);
		int percentYChange = (int) (((absYChange * 1.0)
				/ (Main.getScreenHeight() * 1.0) * 100.0) * -1.0);

		// System.out.println(percentXChange);
		// System.out.println(percentYChange);

		if (percentXChange >= thrustXThreshold
				&& percentYChange < chopYThreshold
				&& percentYChange > slashYThreshold) {

			// thrust
			System.out.println("Thrust");
			mouseDown = false;
		}

		else if (percentXChange >= chopXThreshold
				&& percentYChange >= chopYThreshold) {

			// chop
			System.out.println("Chop");
			mouseDown = false;
		}

		else if (percentXChange >= slashXThreshold
				&& percentYChange <= slashYThreshold) {

			// slash
			System.out.println("Slash");
			mouseDown = false;
		}

		else {

			// no action
			System.out.println("Fail");
		}
	}
}

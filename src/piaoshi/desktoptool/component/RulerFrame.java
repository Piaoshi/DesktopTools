package piaoshi.desktoptool.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Ruler Frame to display on the screen Now: just horizontal and vertical ruler,
 * and can not used as a real ruler Now: rotate with any angel is not realized.
 * 
 * @author Piaoshi(jiayalei8@gmail.com)
 *
 */
public class RulerFrame extends JFrame {

	/**
	 * which direction of ruler is shown now 0---vertical ruler 1 and
	 * others---horizontal ruler
	 */
	private int direction = 0;

	/**
	 * point where mouse clicked
	 */
	private Point offset = new Point();
	/**
	 * button to close the ruler
	 */
	private JButton exitButton = new JButton("X");
	/**
	 * button to change the direction of ruler
	 */
	private JButton directionButton = new JButton("/");
	/**
	 * button to rotate the ruler
	 */
	private JButton rotateButton = new JButton("R");
	/**
	 * 
	 */
	double rotate = 0.0, xm0 = 0.0, ym0 = 0.0;
	/**
	 * Label to show the unit of the ruler
	 */
	private JLabel unitLabel = new JLabel();
	/**
	 * Container
	 */
	private JPanel contentPane;

	public RulerFrame(int direction) {
		this.direction = direction;
		setAlwaysOnTop(true);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new RulerPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		setOpacity(0.8f);
		contentPane.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				float opt = getOpacity() - arg0.getWheelRotation() / 10.0f;
				if (opt > 0.99) {
					opt = 0.99f;
				} else if (opt < 0.2) {
					opt = 0.2f;
				}
				// System.out.println(opt);
				setOpacity(opt);
			}
		});
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				offset = e.getPoint();
			}
		});
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (e.getSource().equals(rotateButton)) {
					rotate = (e.getX() - offset.x) / (e.getY() - offset.y);
					repaint();
				} else {
					setLocation(getX() + e.getPoint().x - offset.x, getY() + e.getPoint().y - offset.y);
				}
			}
		});

		getContentPane().setLayout(null);

		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.setMargin(new Insets(0, 0, 0, 0));
		getContentPane().add(exitButton);
		directionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		directionButton.setMargin(new Insets(0, 0, 0, 0));
		getContentPane().add(directionButton);
		rotateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		rotateButton.setMargin(new Insets(0, 0, 0, 0));
		getContentPane().add(rotateButton);
		if (direction == 0) {
			setAllBonds(1000, 100);
		} else {
			setAllBonds(100, 100);
		}

		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		directionButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				changeDirection();
			}
		});
		rotateButton.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				System.out.println("Not Finished");
			}

		});

	}

	/**
	 * change the direction of the ruler
	 */
	private void changeDirection() {
		if (direction != 1) {
			direction = 1;
		} else {
			direction = 0;
		}
		setAllBonds(getX(), getY());
	}

	/**
	 * set the bound and the position of components
	 * 
	 * @param x
	 * @param y
	 */
	private void setAllBonds(int x, int y) {
		if (direction == 0) {
			setBounds(x, y, 100, 700);

			int temp = getWidth();
			exitButton.setBounds(temp - 20, 0, 20, 20);
			directionButton.setBounds(temp - 20, 20, 20, 20);
			rotateButton.setBounds(temp - 20, 40, 20, 20);
		} else {
			setBounds(x, y, 1000, 100);

			int temp = getHeight();
			exitButton.setBounds(0, temp - 20, 20, 20);
			directionButton.setBounds(20, temp - 20, 20, 20);
			rotateButton.setBounds(40, temp - 20, 20, 20);
		}
	}

	// protected void paintComponent(Graphics g) {
	// Graphics2D gg=(Graphics2D)g;
	// gg.translate(getWidth()/2.0, getHeight()/2.0);
	//// gg.translate(getWidth()/2.0,ym0);
	// gg.rotate(rotate);
	// }

	/**
	 * panel of ruler to show
	 * 
	 * @author Piaoshi
	 *
	 */
	class RulerPanel extends JPanel {

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			drawRuler(g, direction);
		}

		/**
		 * draw the line of ruler with direction "direction"
		 * 
		 * @param g
		 * @param direction
		 */
		private void drawRuler(Graphics g, int direction) {

			// use dpi, not exactly?
			// int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
			int pixIncrease = 5;
			int lengthdpi;
			unitLabel.setText("Unit:" + pixIncrease * 10 + " pixel");
			if (direction == 0) {
				lengthdpi = getHeight();
				g.drawString("Unit: " + pixIncrease * 10 + " pixel", lengthdpi - 10, getHeight() - 80);
			} else {
				lengthdpi = getWidth();
				g.drawString("Unit: " + pixIncrease * 10 + " pixel", lengthdpi - 80, getHeight() - 10);
			}
			g.setColor(Color.BLACK);
			int pix;
			int x0, y0, x1, x2, x3, x4, y1, y2, y3, y4;
			for (int i = 0; i < lengthdpi; i = i + pixIncrease) {
				pix = (int) (i);
				if (direction == 0) {
					x0 = 0;
					y0 = pix;
					x1 = 50;
					y1 = pix;
					x2 = 50;
					y2 = pix;
					x3 = 35;
					y3 = pix;
					x4 = 25;
					y4 = pix;
				} else {
					x0 = pix;
					y0 = 0;
					x1 = pix;
					y1 = 50;
					x2 = pix;
					y2 = 50;
					x3 = pix;
					y3 = 35;
					x4 = pix;
					y4 = 25;
				}
				if (pix % (5 * pixIncrease) == 0) {
					if (pix % (10 * pixIncrease) == 0) {
						g.drawLine(x0, y0, x1, y1);
						g.drawString(" " + (pix / (pixIncrease * 10)), x2, y2);
					} else {
						g.drawLine(x0, y0, x3, y3);
					}
				} else {
					g.drawLine(x0, y0, x4, y4);
				}
			}

		}
	}
}

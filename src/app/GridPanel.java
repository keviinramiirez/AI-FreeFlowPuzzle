//package app;
//
//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.Insets;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//public class GridPanel extends JPanel 
//{
//	private int GRID_X;
//	private int GRID_Y;
//	private int CELL_SIZE = 30;
//	private int TOTAL_ROWS;
//	private int TOTAL_COLS;
//	private int BUTTONS_Y_SPACE = 30;
//
//	public GridPanel(int rows, int cols) {
//		this.TOTAL_ROWS = rows;
//		this.TOTAL_COLS = cols;
//		
//		this.GRID_X = this.CELL_SIZE * this.TOTAL_COLS;
//		this.GRID_Y = this.CELL_SIZE * this.TOTAL_ROWS + this.BUTTONS_Y_SPACE;
//		
//		JPanel panel = new JPanel(new GridBagLayout());
//		GridBagConstraints constraints = new GridBagConstraints();
//        constraints.anchor = GridBagConstraints.WEST;
//        constraints.insets = new Insets(10, 10, 10, 10);
//	}
//	
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		
//		// set background color
//		g.setColor(Color.LIGHT_GRAY);
//		g.fillRect(0, 0, GRID_X, GRID_Y);
//		
//		// color grid lines
//		g.setColor(Color.BLACK);
//		for (int y = 0; y <= TOTAL_ROWS; y++) {
//			g.drawLine(0, y * this.CELL_SIZE, this.GRID_X, y * this.CELL_SIZE);
//		}
//		for (int x = 0; x <= TOTAL_COLS; x++) {
//			g.drawLine(x * this.CELL_SIZE, 0, this.GRID_Y - this.BUTTONS_Y_SPACE, 0);
//		}
//	}
//}

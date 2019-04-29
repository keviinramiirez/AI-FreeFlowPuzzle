package app;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.border.LineBorder;

import flowPointer.FlowPointer;
import priorityQueue.HeapPriorityQueue;

import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class FlowFree 
{
	private JFrame frame;
	private final JButton btnNewButton = new JButton("Radomize Colors");
	private final JPanel panel_1 = new JPanel();
	private final JPanel panel_2 = new JPanel();
	private final JComboBox<Integer> pairColorsComboBox = new JComboBox<>();
	private final JComboBox<Integer> columnComboBox = new JComboBox<>(), rowComboBox = new JComboBox<>();
	private JPanel grid = new JPanel();

	static int MINIMUM_DIMENSIONS = 2, MAXIMUM_DIMENSIONS = 12;
	JPanel[][] panels = new JPanel[2][2];
	FlowPointer[][] flowPointers = new FlowPointer[2][2];
	HeapPriorityQueue<Integer, FlowPointer> pq = new HeapPriorityQueue<>();
	LinkedList<String> positions;
	LinkedList<String> initialPositions;
	
	
	
	Iterator<Color> colorIterator = (new ColorManager()).iterator();
	int nCols = MINIMUM_DIMENSIONS, nRows = MINIMUM_DIMENSIONS;
	int nPairFlowPointer = nCols;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FlowFree window = new FlowFree();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FlowFree() {
		initialize();
		
		
	}
	
//	PaintCell
	
	public void clearPanels() {
		initialPositions = new LinkedList<>();
		positions = new LinkedList<>();
		for (int c = 0; c < nCols; c++) {
			for (int r = 0; r < nRows; r++) {
				positions.add(c+","+r);
				panels[c][r].setBackground(new Color(240, 240, 240));
			}
		}
	}
	
	
	
	public void createGrid() {
		grid = new JPanel();
		grid.setLayout(new GridLayout(nCols, nRows));
		frame.getContentPane().remove(grid);
		frame.getContentPane().add(grid, BorderLayout.CENTER);

		this.initialPositions = new LinkedList<>();
		this.panels = new JPanel[nCols][nRows];
		this.flowPointers = new FlowPointer[nCols][nRows];
		
		for (int c = 0; c < nCols; c++) {
			for (int r = 0; r < nRows; r++) {
				JPanel panel = new JPanel();
				panel.setBorder(new LineBorder(new Color(0, 0, 0)));
				grid.add(panel);
				panels[c][r] = panel;
				flowPointers[c][r] = new FlowPointer();
			}
		}
		grid.revalidate();
		grid.repaint();
	}
	
	
	public void paintRandomCell(Color color) {
		Random rand = new Random();
		int randomCol = 0, randomRow = 0;
		
		do {
			randomCol = rand.nextInt(nCols);
			randomRow = rand.nextInt(nRows);
		} while (initialPositions.contains(randomCol+","+randomRow));
		
		this.initialPositions.add(randomCol+","+randomRow);
		this.panels[randomCol][randomRow].setBackground(color);
		this.flowPointers[randomCol][randomRow].color = color;
	}
	

	private void randomizeFlowPointers() {
		this.clearPanels();
		if (nCols*nRows/2 < this.nPairFlowPointer) {
			System.out.println("invalid amount of Pair Flow Pointers");
			return;
		}
		for (int i = 0; i < this.nPairFlowPointer; i++) {
			Color color = colorIterator.next();
			paintRandomCell(color);
			paintRandomCell(color);
		}
		
		
//		for (int c = 0; c < nCols; c++) {
//			for (int r = 0; r < nRows; r++) {
//				int countAdjacents = 0;
//				for (int[] dir : DIRECTIONS)
//					if (this.flowPointers[c + dis[0]][r + dis[1]])
//			}
//		}
//		
//		for (int[] dir : DIRECTIONS)
//			if (solve(currStep.row + dir[0], currStep.col + dir[1]))
//				return true;
	}

	
	private static int[][] DIRECTIONS = 
		{{ 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 }};// right, up, left, down

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(1600, 800, 700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(grid, BorderLayout.CENTER);
		grid.setLayout(new GridLayout(2, 2, 0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		this.createGrid();
		
		for (int i = MINIMUM_DIMENSIONS; i <= MAXIMUM_DIMENSIONS; i++) {
			columnComboBox.addItem(i);
			rowComboBox.addItem(i);
		}
		panel.setLayout(new BorderLayout(0, 0));
		
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.add(rowComboBox);
		panel_1.add(columnComboBox);

		
		columnComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				nCols = (int) columnComboBox.getSelectedItem();
				createGrid();
			}
		});
		
		
		rowComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				nRows = (int) rowComboBox.getSelectedItem();
				createGrid();
			}
		});
		
		panel.add(panel_2, BorderLayout.CENTER);
		pairColorsComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				nPairFlowPointer = (int) pairColorsComboBox.getSelectedItem();
			}
		});
		panel_2.add(pairColorsComboBox);
		for (int i = 2; i <= 4; i++) {
			pairColorsComboBox.addItem(i);
		}
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				randomizeFlowPointers();
				System.out.println();
			}
		});
		panel_2.add(btnNewButton);
	}

}

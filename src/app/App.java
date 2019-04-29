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

import priorityQueue.HeapPriorityQueue;
import util.HardCodeFlowPointers;

import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class App 
{
	private JFrame frame;
	private final JButton btnCreateGrid = new JButton("Create Grid");
	private final JPanel panel_1 = new JPanel();
	private final JPanel panel_2 = new JPanel();
//	private final JComboBox<Integer> pairColorsComboBox = new JComboBox<>();
	private final JComboBox<Integer> columnComboBox = new JComboBox<>(), rowComboBox = new JComboBox<>();
	private JPanel gridComponent = new JPanel();

	static int MINIMUM_DIMENSIONS = 2, MAXIMUM_DIMENSIONS = 12;

	JPanel[][] gridPanel = new JPanel[Grid.ROWS][Grid.COLS];
	Solver solver = new Solver();
	Grid grid = new Grid();
	
//	LinkedList<Pos> positions;
//	LinkedList<Pos> initialPositions;
//
//	Iterator<Color> colorIterator = (new ColorManager()).iterator();
//	int nPairFlowPointer = nCols;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
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
	public App() {
		initialize();
	}
		
	public void clearPanels() {
//		initialPositions = new LinkedList<>();
//		positions = new LinkedList<>();
//		for (int c = 0; c < nCols; c++) {
//			for (int r = 0; r < nRows; r++) {
//				positions.add(new Pos(c, r));
//				matrixPanel[c][r].setBackground(new Color(240, 240, 240));
//			}
//		}
	}

	
	public void createGrid() {
		
		Grid.ROWS = 10;
		Grid.COLS = 10;

		frame.getContentPane().remove(gridComponent);
		gridComponent = new JPanel();
		gridComponent.setLayout(new GridLayout(Grid.ROWS, Grid.COLS));
		frame.getContentPane().add(gridComponent, BorderLayout.CENTER);
		
		
		for (int r = 0; r < Grid.ROWS; r++) {
			for (int c = 0; c < Grid.COLS; c++) {
				gridPanel[r][c] = new JPanel();
//				JPanel panel = new JPanel();
				gridPanel[r][c].setBorder(new LineBorder(new Color(0, 0, 0)));
				gridComponent.add(gridPanel[r][c]);
			}
		}
		// Hard Coded Flow Pointers
		
		HardCodeFlowPointers hardCodePointers = new HardCodeFlowPointers();
		for (GridCell initialPointer : hardCodePointers.generateInitialFlowPointers1()) {
			int row = initialPointer.pos.row;
			int col = initialPointer.pos.col;
			grid.initialFlowPointerPos.add(initialPointer.pos);
			grid.gridCells[row][col] = initialPointer;
			
			// UI gridPanel
			gridPanel[row][col].setBackground(initialPointer.color);
//			gridComponent.add(gridPanel[col][row]);
		}

		gridComponent.revalidate();
		gridComponent.repaint();
	}
	
	
//	public void paintRandomCell(Color color) {
//		Random rand = new Random();
//		int randomCol = 0, randomRow = 0;
//		
//		do {
//			randomCol = rand.nextInt(nCols);
//			randomRow = rand.nextInt(nRows);
//		} while (initialPositions.contains(randomCol+","+randomRow));
//		
//		this.initialPositions.add(new Pos(randomCol, randomRow));
//		this.matrixPanel[randomCol][randomRow].setBackground(color);
//		this.gridsssss.flowPointers[randomCol][randomRow].color = color;
//	}
	

//	private void randomizeFlowPointers() {
//		this.clearPanels();
//		if (grid.COLS*grid.ROWS/2 < this.nPairFlowPointer) {
//			System.out.println("invalid amount of Pair Flow Pointers");
//			return;
//		}
//		for (int i = 0; i < this.nPairFlowPointer; i++) {
//			Color color = colorIterator.next();
//			paintRandomCell(color);
//			paintRandomCell(color);
//		}
//	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(1600, 800, 700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		gridComponent.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		frame.getContentPane().add(gridComponent, BorderLayout.CENTER);
		gridComponent.setLayout(new GridLayout(Grid.ROWS, Grid.COLS, 0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
				
		for (int i = MINIMUM_DIMENSIONS; i <= MAXIMUM_DIMENSIONS; i++) {
			columnComboBox.addItem(i);
			rowComboBox.addItem(i);
		}
		panel.setLayout(new BorderLayout(0, 0));
		
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.add(rowComboBox);
		panel_1.add(columnComboBox);
		
		
		rowComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Grid.ROWS = (int) rowComboBox.getSelectedItem();
			}
		});
		
		columnComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Grid.COLS = (int) columnComboBox.getSelectedItem();
			}
		});
		
		panel.add(panel_2, BorderLayout.CENTER);
		
		
		btnCreateGrid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				createGrid();
			}
		});
		panel_2.add(btnCreateGrid);
	}

}

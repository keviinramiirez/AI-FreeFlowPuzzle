package app;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.border.LineBorder;

import interfaces.Entry;
import util.HardCodedFlowPointers;

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
	private final JComboBox<Integer> columnComboBox = new JComboBox<>(), rowComboBox = new JComboBox<>();
	private JPanel gridComponent = new JPanel();

	static int MINIMUM_DIMENSIONS = 2, MAXIMUM_DIMENSIONS = 12;

	JPanel[][] gridPanel = new JPanel[Grid.ROWS][Grid.COLS];
	public Grid grid = new Grid();
	private final JButton btnNewButton = new JButton("Solve Now");


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
//		this.grid.initialFlowPointers = new HashSet<Pos>();
//		for (int r = 0; r < nRows; r++) {
//			for (int c = 0; c < nCols; c++) {
//				positions.add(new Pos(c, r));
//				matrixPanel[c][r].setBackground(Grid.EMPTY_COLOR);
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
				this.grid.getGridCells()[r][c] = new GridCell(grid, new Pos(r, c));
				gridPanel[r][c] = new JPanel();
				gridPanel[r][c].setBorder(new LineBorder(new Color(0, 0, 0)));
				gridComponent.add(gridPanel[r][c]);
			}
		}
		
		// Hard Coded Flow Pointers
		HardCodedFlowPointers hardCodedPointers = new HardCodedFlowPointers(grid);
		this.initializeInitialPointers(hardCodedPointers.generateInitFlowPointers1());
//		this.initializeInitialPointers(hardCodedPointers.generateStrandedRegion1());
		
		
		// inserts the most constraint Initial Pointers within the priority queue
		this.queueMostConstraintInitialPointers();
	}
	
	
	/** Updates and repaints the grid cells with the given Initial Flow Pointers.
	 *  Also, pairs (pairFlowPointer property) the initial flow pointers that have same colors.
	 */
	private void initializeInitialPointers(ArrayList<GridCell> genInitialFlowPointers) {
		HashMap<Color, GridCell> cellPainted = new HashMap<>();
		this.grid.nEmptyCells -= genInitialFlowPointers.size();

		// paints UI grid and sets each pairFlowPointer property of the Initial Pointers
		for (GridCell currInitPointer : genInitialFlowPointers) {
			int row = currInitPointer.pos.row;
			int col = currInitPointer.pos.col;

			// UI Grid
			this.gridPanel[row][col].setBackground(currInitPointer.color);

			// Grid Class
			this.grid.getInitialFlowPointers().add(currInitPointer);
			this.grid.getGridCells()[row][col] = currInitPointer;

			// process to set pairFlowPointer property of each pair of FlowPointers
			if (cellPainted.containsKey(currInitPointer.color)) {
				GridCell pairPointer = cellPainted.get(currInitPointer.color);
				currInitPointer.pairInitialFlowPointer = pairPointer;
				pairPointer.pairInitialFlowPointer = currInitPointer;
				cellPainted.remove(currInitPointer.color);
			}
			else cellPainted.put(currInitPointer.color, currInitPointer);
		}

//		grid.finishedPaths = new LinkedList[grid.initialFlowPointers.size()];
		
		// repaints the grid panel with the updated cells
		gridComponent.revalidate();
		gridComponent.repaint();
	}
	
	/** Queues the most constraint initial pointer of each pair. <br>
	 *  *Note: The more non-empty adjacent cells, the more constraint it is.
	 */
	private void queueMostConstraintInitialPointers() {
		HashSet<GridCell> visitedCells = new HashSet<>();

		// count amount of adjacent cells of each initial pointer and
		// inserts the most constraint of each pair to the priority queue.
		for (GridCell initGridCell : grid.getInitialFlowPointers()) {
			// Initial Flow Pointers
			GridCell ifp = this.grid.getGridCells()[initGridCell.pos.row][initGridCell.pos.col];
			int adjCount = ifp.getColoredAdjs().size();

			// 
			if (visitedCells.contains(ifp.pairInitialFlowPointer)) {
				int pairPointerAdjCount = ifp.pairInitialFlowPointer.getColoredAdjs().size();

				// inserts the most constraint initial pointer of this pair
				if (adjCount < pairPointerAdjCount)
					this.grid.pq.insert(adjCount, ifp);
				else
					this.grid.pq.insert(pairPointerAdjCount, ifp.pairInitialFlowPointer);
			}
			else
				visitedCells.add(ifp);
		}
		
		for (Entry<Integer, GridCell> entry : this.grid.pq)
			entry.setKey(this.grid.nEmptyCells);
		
		System.out.println();
	}
	
	public void solvePuzzle() {
//		Validation validation = new Validation(grid);
//		System.out.println(validation.isThereStrandedColorOrRegion());
		
		Solver solver = new Solver(grid, gridPanel);
		solver.solve();
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
//	
//
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
		frame.setBounds(1700, 300, 700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		panel_1.add(btnCreateGrid);
		
		
		btnCreateGrid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				createGrid();
			}
		});
		
		
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
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				solvePuzzle();
			}
		});
		panel_2.add(btnNewButton);
	}

}

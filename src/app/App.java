package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
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

public class App {
	private JFrame frame;
	private final JButton btnCreateGrid = new JButton("Create Grid");
	private final JPanel panel_1 = new JPanel();
	private final JPanel panel_2 = new JPanel();
	private final JComboBox<Integer> columnComboBox = new JComboBox<>(), rowComboBox = new JComboBox<>(), numberOfColorsBox = new JComboBox<>();
	private final JComboBox<String>gridComboBox = new JComboBox<>();
	private JPanel gridComponent = new JPanel();	
	private String dimensions = "6x6";
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
//		Grid.ROWS = 10;
//		Grid.COLS = 10;

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
		grid.initializeEdges();

		// Hard Coded Flow Pointers
		HardCodedFlowPointers hardCodedPointers = new HardCodedFlowPointers(grid);
		
		// This doesnt work correctly because of how the Grid class is structure
//		switch(dimensions) {
//			case "5x5":
//				this.initGridFlowPointers(hardCodedPointers.initialPointers2_5x5());
//				break;
//			case "6x6":
//				this.initGridFlowPointers(hardCodedPointers.initialPointers2_6x6());
//				break;
//			case "7x7":
//				this.initGridFlowPointers(hardCodedPointers.initialPointers2_7x7());
//				break;
//			case "10x10":
//				this.initGridFlowPointers(hardCodedPointers.initialPointers1_10x10());
//				break;
//		}
//
//		this.initGridFlowPointers(hardCodedPointers.initialPointers2_5x5());
//		this.initGridFlowPointers(hardCodedPointers.initialPointers2_6x6());
		this.initGridFlowPointers(hardCodedPointers.initialPointers2_7x7());
//		this.initGridFlowPointers(hardCodedPointers.initialPointers1_10x10());

		// inserts the most constraint Initial Pointers within the priority queue
		this.queueMostConstraintInitialPointers();
	}

	/**
	 * initializes the grid with given flow pointer and repaints the grid panel.
	 * Also, connects each pointer with its pair pairPointer property of the flow
	 * pointers that have same colors.
	 */
	private void initGridFlowPointers(ArrayList<GridCell> genInitialFlowPointers) {
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
				currInitPointer.pairFlowPointer = pairPointer;
				pairPointer.pairFlowPointer = currInitPointer;
				cellPainted.remove(currInitPointer.color);
			} else
				cellPainted.put(currInitPointer.color, currInitPointer);
		}

		// repaints the grid panel with the updated cells
		gridComponent.revalidate();
		gridComponent.repaint();
	}

	/**
	 * Queues the most constraint initial pointer of each pair. <br>
	 * *Note: The more non-empty adjacent cells, the more constraint it is.
	 */
	private void queueMostConstraintInitialPointers() {
		LinkedList<GridCell> visitedCells = new LinkedList<>();

		// count amount of adjacent cells of each initial pointer and
		// inserts the most constraint of each pair to the priority queue.
		for (GridCell initGridCell : grid.getInitialFlowPointers()) {
			// Initial Flow Pointers
			GridCell ifp = this.grid.getGridCells()[initGridCell.pos.row][initGridCell.pos.col];
			int emptydjCount = ifp.getEmptyAdjs().size();

			//
			if (visitedCells.contains(ifp.pairFlowPointer)) {
				int pairPointer_emptyAdjCount = ifp.pairFlowPointer.getEmptyAdjs().size();

				// inserts the most constraint initial pointer of this pair
				if (emptydjCount < pairPointer_emptyAdjCount)
					this.grid.pq.insert(emptydjCount, ifp);
				else
					this.grid.pq.insert(pairPointer_emptyAdjCount, ifp.pairFlowPointer);
			} else
				visitedCells.add(ifp);
		}

//		this.grid.pq.removeMin()

//		for (Entry<Integer, GridCell> entry : this.grid.pq)
//			entry.setKey(this.grid.nEmptyCells);		
	}

	public void solvePuzzle() {
		Solver solver = new Solver(grid, gridPanel);
		solver.solve();
		
		// repaints the grid panel with the updated cells
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

	public String toString() {
		return this.grid.toString();
	}

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
			numberOfColorsBox.addItem(i);
			if(i>4&&i!=8&&i!=9 &&i!=11&&i!=12) {
				
				gridComboBox.addItem(Integer.toString(i)+'x'+Integer.toString(i));;
			}
		}
		panel.setLayout(new BorderLayout(0, 0));

		panel.add(panel_1, BorderLayout.NORTH);
//		panel_1.add(rowComboBox);
//		panel_1.add(columnComboBox);
		panel_1.add(numberOfColorsBox);
		panel_1.add(gridComboBox);
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
		gridComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				dimensions = (String) gridComboBox.getSelectedItem();
			}
		});
		numberOfColorsBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int c = (int) numberOfColorsBox.getSelectedItem();
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

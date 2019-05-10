package main;

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

import appClasses.Grid;
import appClasses.GridCell;
import appClasses.Pos;
import appClasses.Solver;
import util.HardCodedFlowPointers;
import util.PuzzleCreator;

import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Font;
import javax.swing.border.EmptyBorder;

public class App 
{
	private JFrame frame;
	private final JButton btnCreateGrid = new JButton("Create Random Grid");
	private final JPanel panel_1 = new JPanel();
	private final JComboBox<String> dimensionsBox = new JComboBox<>();
	private JPanel gridComponent = new JPanel();
	private String nDimensions = "5x5"; // starts with a 5x5 puzzle

	public static JPanel[][] gridPanel;
	public Grid grid = new Grid();
	private final JButton solveNowButton = new JButton("Solve Now");

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
		gridPanel = new JPanel[grid.ROWS][grid.COLS];
		createGrid();
	}

	
	public void createGrid() { createGrid(grid.ROWS, grid.COLS); }
	/** initializes the puzzle (the grid) with empty cells and then generates random flow pointer. */
	public void createGrid(int rows, int cols) {
		frame.getContentPane().remove(gridComponent);
		gridComponent  = new JPanel();
		gridComponent.setLayout(new GridLayout(rows, cols, 0, 0));
		frame.getContentPane().add(gridComponent, BorderLayout.CENTER);

		gridPanel = new JPanel[rows][rows];
		this.grid = new Grid(rows, cols);
		
		gridComponent.repaint();
		frame.getContentPane().repaint();


		// initialize the grid with empty cells
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				this.grid.getGridCells()[r][c] = new GridCell(grid, new Pos(r, c));
				gridPanel[r][c] = new JPanel();
				gridPanel[r][c].setBorder(new LineBorder(new Color(0, 0, 0)));
				gridComponent.add(gridPanel[r][c]);
			}
		}
		
		// store the four edges
		grid.storeEdges();

		PuzzleCreator pzl = new PuzzleCreator();
		
		// initialize respective flow pointer

		this.initGridFlowPointers(pzl.getRamdomPuzzle(grid, nDimensions));

		// insert the most constraint Initial Pointers within the priority queue
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
			gridPanel[row][col].setBackground(currInitPointer.color);

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

			if (visitedCells.contains(ifp.pairFlowPointer)) {
				// 
				int pairPointer_emptyAdjCount = ifp.pairFlowPointer.getEmptyAdjs().size();
				ifp.heuristic = emptydjCount;
				ifp.pairFlowPointer.heuristic = pairPointer_emptyAdjCount;
				
				GridCell iPointerToQueue = 
						(emptydjCount < pairPointer_emptyAdjCount) ? ifp : ifp.pairFlowPointer;
				
				// inserts the most constraint initial pointer of this pair
				this.grid.pq.add(iPointerToQueue);
			} else
				visitedCells.add(ifp);
		}
	}

	/** begins the process of solving the puzzle. Paints the puzzle when finished. */
	public void solvePuzzle() {
		Solver solver = new Solver(grid);
		solver.solve();

		// repainting cells, making sure all cells are painted.
		for (int r = 0; r < grid.ROWS; r++)
			for (int c = 0; c < grid.COLS; c++)
				gridPanel[r][c].setBackground(this.grid.gridCells[r][c].color);;
		
		// repaints the grid panel with the updated cells
		gridComponent.revalidate();
		gridComponent.repaint();
	}
	
	public String toString() {
		return this.grid.toString();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(300, 300, 800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(gridComponent, BorderLayout.CENTER);
//		gridComponent.setLayout(new GridLayout(grid.ROWS, grid.COLS, 0, 0));

		JPanel buttonsPanel = new JPanel();
		frame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

		for (int i = 5; i <= 8; i++)
			dimensionsBox.addItem(Integer.toString(i)+'x'+Integer.toString(i));
		
		buttonsPanel.setLayout(new BorderLayout(0, 0));
		panel_1.setBorder(new EmptyBorder(20, 0, 20, 0));

		buttonsPanel.add(panel_1, BorderLayout.NORTH);
		dimensionsBox.setFont(new Font("Tahoma", Font.PLAIN, 22));
		panel_1.add(dimensionsBox);
		btnCreateGrid.setFont(new Font("Tahoma", Font.PLAIN, 22));
		panel_1.add(btnCreateGrid);
		solveNowButton.setFont(new Font("Tahoma", Font.PLAIN, 22));
		solveNowButton.setForeground(Color.WHITE);
		solveNowButton.setBackground(new Color(0, 204, 102));
		panel_1.add(solveNowButton);
		solveNowButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (solveNowButton.isEnabled())
					solvePuzzle();
				solveNowButton.setEnabled(false);
			}
		});

		btnCreateGrid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				solveNowButton.setEnabled(true);
				createGrid();
			}
		});

		dimensionsBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				nDimensions = (String) dimensionsBox.getSelectedItem();
				grid.ROWS = Integer.parseInt(nDimensions.substring(0, 1));
				grid.COLS = Integer.parseInt(nDimensions.substring(nDimensions.length()-1));
				solveNowButton.setEnabled(true);
				createGrid();
			}
		});
	}
	
	public boolean disableSolveButton = false;
}

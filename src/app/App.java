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
import util.PuzzleCreator;

import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Font;

public class App {
	private JFrame frame;
	private final JButton btnCreateGrid = new JButton("Create Grid");
	private final JPanel panel_1 = new JPanel();
	private final JComboBox<String> dimensionsBox = new JComboBox<>();
	private JPanel gridComponent = new JPanel();	
	private String nDimensions = "5x5";

	JPanel[][] gridPanel;
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
		gridPanel = new JPanel[grid.ROWS][grid.COLS];
	}
	
	public void createGrid()  {
		frame.getContentPane().remove(gridComponent);
		gridComponent  = new JPanel();
		gridComponent.setLayout(new GridLayout(grid.ROWS, grid.COLS, 0, 0));
		frame.getContentPane().add(gridComponent, BorderLayout.CENTER);

		gridPanel = new JPanel[grid.ROWS][grid.COLS];
		this.grid = new Grid(grid.ROWS, grid.COLS);
		
		gridComponent.repaint();
		frame.getContentPane().repaint();


		for (int r = 0; r < grid.ROWS; r++) {
			for (int c = 0; c < grid.COLS; c++) {
				this.grid.getGridCells()[r][c] = new GridCell(grid, new Pos(r, c));
				gridPanel[r][c] = new JPanel();
				gridPanel[r][c].setBorder(new LineBorder(new Color(0, 0, 0)));
				gridComponent.add(gridPanel[r][c]);
			}
		}
		
		grid.initializeEdges();
		
		PuzzleCreator pzl = new PuzzleCreator();
//		pzl.getPuzzle(grid);
		
		// Hard Coded Flow Pointers
		HardCodedFlowPointers hardCodedPointers = new HardCodedFlowPointers(grid);
//
//		this.initGridFlowPointers(hardCodedPointers.initialPointers_5x5());
//		this.initGridFlowPointers(hardCodedPointers.initialPointers_6x6());
//		this.initGridFlowPointers(hardCodedPointers.initialPointers_7x7());
//		this.initGridFlowPointers(hardCodedPointers.initialPointers_7x7_V2());
//		this.initGridFlowPointers(hardCodedPointers.initialPointers_8x8());
//		this.initGridFlowPointers(hardCodedPointers.initialPointers_8x8_V2());
		
		// initialize flow pointer
		this.initGridFlowPointers(pzl.getPuzzle(grid, nDimensions));

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
				ifp.heuristic = emptydjCount;
				ifp.pairFlowPointer.heuristic = pairPointer_emptyAdjCount;
				// inserts the most constraint initial pointer of this pair
				if (emptydjCount < pairPointer_emptyAdjCount)
					this.grid.pq.add(ifp);
				else
					this.grid.pq.add(ifp.pairFlowPointer);
			} else
				visitedCells.add(ifp);
		}

//		this.grid.pq.removeMin();
		System.out.println();
//		for (Entry<Integer, GridCell> entry : this.grid.pq)
//			entry.setKey(this.grid.nEmptyCells);		
	}

	public void solvePuzzle() {
		Solver solver = new Solver(grid, gridPanel);
		solver.solve();
		
		// make sure its all repainted
		for (int r = 0; r < grid.ROWS; r++) {
			for (int c = 0; c < grid.COLS; c++) {
				this.gridPanel[r][c].setBackground(this.grid.gridCells[r][c].color);;
			}
		}
		
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
		frame.setBounds(1700, 300, 700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(gridComponent, BorderLayout.CENTER);
//		gridComponent.setLayout(new GridLayout(grid.ROWS, grid.COLS, 0, 0));

		JPanel buttonsPanel = new JPanel();
		frame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

//		for (int i = MINIMUM_DIMENSIONS; i <= MAXIMUM_DIMENSIONS; i++) {
//			if(i>4&&i!=8&&i!=9 &&i!=11&&i!=12) {
//				dimensionsBox.addItem(Integer.toString(i)+'x'+Integer.toString(i));;
//			}
//		}
		for (int i = 5; i <= 7; i++) {
			dimensionsBox.addItem(Integer.toString(i)+'x'+Integer.toString(i));;

		}
		buttonsPanel.setLayout(new BorderLayout(0, 0));

		buttonsPanel.add(panel_1, BorderLayout.NORTH);
		dimensionsBox.setFont(new Font("Tahoma", Font.PLAIN, 22));
		panel_1.add(dimensionsBox);
		btnCreateGrid.setFont(new Font("Tahoma", Font.PLAIN, 22));
		panel_1.add(btnCreateGrid);
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 22));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(new Color(0, 204, 102));
		panel_1.add(btnNewButton);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				solvePuzzle();
			}
		});

		btnCreateGrid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				createGrid();
			}
		});

		dimensionsBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				nDimensions = (String) dimensionsBox.getSelectedItem();
				grid.ROWS = Integer.parseInt(nDimensions.substring(0, 1));
				grid.COLS = Integer.parseInt(nDimensions.substring(nDimensions.length()-1));
			}
		});
	}

}

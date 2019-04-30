package app;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.border.LineBorder;

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
				gridPanel[r][c].setBorder(new LineBorder(new Color(0, 0, 0)));
				gridComponent.add(gridPanel[r][c]);
			}
		}
		
		// Hard Coded Flow Pointers
		HardCodeFlowPointers hardCodePointers = new HardCodeFlowPointers(grid);
		HashMap<Color, GridCell> cellPainted = new HashMap<>();

		// paints UI grid and sets each pairFlowPointer property of the Initial Pointers
		for (GridCell currInitPointer : hardCodePointers.generateInitialFlowPointers1()) {
			int row = currInitPointer.pos.row;
			int col = currInitPointer.pos.col;

			// UI Grid
			this.gridPanel[row][col].setBackground(currInitPointer.color);

			// Grid Class
			grid.getInitialFlowPointers().add(currInitPointer.pos);
			this.grid.getGridCells()[row][col] = currInitPointer;

			// process to set pairFlowPointer property of each pair of FlowPointers
			if (cellPainted.containsKey(currInitPointer.color)) {
				GridCell pairPointer = cellPainted.get(currInitPointer.color);
				currInitPointer.pairFlowPointer = pairPointer;
				pairPointer.pairFlowPointer = currInitPointer;
				cellPainted.remove(currInitPointer.color);
			}
			else cellPainted.put(currInitPointer.color, currInitPointer);
		}
		gridComponent.revalidate();
		gridComponent.repaint();
		
		// inserts the most constraint Initial Pointers within the priority queue
		this.queueMostConstraintPointers();
	}
	
	private void queueMostConstraintPointers() {
		// inserts the most constraint InitialPointers
		HashSet<GridCell> visitedCells = new HashSet<>();
		for (Pos initPos : grid.getInitialFlowPointers()) {
			// Initial Flow Pointers
			GridCell ifp = this.grid.getGridCells()[initPos.row][initPos.col];
			int adjCount = ifp.countActiveAdjacent();
			if (visitedCells.contains(ifp.pairFlowPointer)) {
				int pairPointerAdjCount = ifp.pairFlowPointer.countActiveAdjacent();
				
				if (adjCount >= pairPointerAdjCount)
					this.grid.pq.insert(adjCount, ifp);
				else
					this.grid.pq.insert(pairPointerAdjCount, ifp.pairFlowPointer);
			}
			else
				visitedCells.add(ifp);
		}
	}
	
	public void solve() {
		Solver solver = new Solver(grid);
		
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
		frame.setBounds(1600, 800, 700, 700);
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
				solve();
			}
		});
		panel_2.add(btnNewButton);
	}

}

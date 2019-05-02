package app;

import java.awt.Color;
import java.util.LinkedList;

/** This class represents each Grid Cell.  Can either be an Empty Cell 
 *  or colored Cell.
 *  
 *  *Note: a Flow Pointer is the peek (or the initial point) of a flow of colored cells
 */
public class GridCell 
{
	/** Contains the grid properties such as the 2d array of grid cells, 
	 *  set of all initialFlowPointers, and priority queue of initialPointers 
	 *  to move first.
	 */
	private Grid grid;
	
	/** This Grid Cell's position within the grid. */
	public Pos pos;
	
	/** This Grid Cell's color. */
	public Color color;
	
	/** This Grid Cell's heuristic value. */
	public int heuristic = 0;
	
	/** The previous pointer to backtrack to. */
	public GridCell previousCell;
	
	/** This Grid Cell's Pair Initial Flow Pointer. */
	public GridCell pairInitialFlowPointer;
	
	/** Adjacent cells to consider moving towards to. */
	public LinkedList<GridCell> nextAdjCells;
	
	/** This Grid Cell had one cell to move into. */
	public boolean hasForcedMove;
		
	
	public GridCell(Grid grid, Pos pos) {
		this(grid, pos, Grid.EMPTY_COLOR, 0, null, null, new LinkedList<GridCell>());
	}
	public GridCell(Grid grid, Pos pos, Color color, int heuristic) {
		this(grid, pos, color, heuristic, null, null, new LinkedList<GridCell>());
	}
	/** 
	 *  @param pos position of this cell
	 *  @param color this Grid Cell's color.
	 *  @param heuristic
	 *  @param pairFlowPointer this cell's pair color pointer. Null if this cell is non empty.
	 *  @param previousPointer reference to the previous pointer, tracked to move backwards to it.
	 *  @param nextAdjCells
	 *  @param hasForcedMove tells us if this grid cell was forced to move to this position.
	 */
	public GridCell(Grid grid, Pos pos, Color color, int heuristic, GridCell pairFlowPointer, 
			GridCell previousPointer, LinkedList<GridCell> nextAdjCells) {
		this.grid = grid;
		this.pos = pos;
		this.color = color;
		this.heuristic = heuristic;

		this.previousCell = previousPointer;
		this.pairInitialFlowPointer = pairFlowPointer;
		this.nextAdjCells = nextAdjCells;
		this.hasForcedMove = false;
	}
	
	/** Resets this color, nextAdjCells, and previousPointer properties */
	public void clearBacktrackingCell(javax.swing.JPanel[][] gridPanel) {
		this.color = Grid.EMPTY_COLOR;
		this.nextAdjCells = new LinkedList<GridCell>();
		this.previousCell = null;
		gridPanel[pos.row][pos.col].setBackground(Grid.EMPTY_COLOR);
	}
	
	/** Returns true if this cell is a non empty cell. */
	public boolean isColoredCell() {
		return !color.equals(Grid.EMPTY_COLOR);
	}
	
	/** Returns true if this is an INTITIAL Flow Pointer. */
	public boolean isInitialFlowPointer() {
		return this.isColoredCell() && grid.initialFlowPointers.contains(this);
	}

	/** Return true if given cell is the pair of this FlowPointer. */
	public boolean isPairPointer(GridCell cell) {
		return cell.isColoredCell() && cell == this.pairInitialFlowPointer;
	}
	
	/** Returns true if the given cell is the previous cell of this FlowPointer. */
	public boolean isCellPreviousPointer(GridCell cell) {
		return cell.isColoredCell() && cell == this.previousCell;
	}
	
	/** Returns null if row index or column index is out of bounds of the game matrix.
	 *  Otherwise, returns the valid Grid Cell that is <i>incrRow</i> far horizontally
	 *  and <i>incrCol</i> far vertically.
	 */
	private GridCell getAdjacentCell(int incrRow, int incrCol) {
		return grid.validPosition(pos.row +incrRow, pos.col +incrCol)
				? grid.gridCells[pos.row  + incrRow][pos.col + incrCol] 
				: null;
	}
	
	/** Returns this grid cell's colored adjacent cells. */
	public LinkedList<GridCell> getColoredAdjacents() {
		LinkedList<GridCell> coloredAdjCells = new LinkedList<>();
		
		for (int[] dir : Grid.DIRECTIONS) {
			GridCell adjCell = this.getAdjacentCell(dir[0], dir[1]);
			if (adjCell != null && adjCell.isColoredCell())
				coloredAdjCells.add(adjCell);
		}
		
		return coloredAdjCells;
	}
	
	
	/** Returns this grid cell's empty adjacent cells. */
	public LinkedList<GridCell> getEmptyAdjacents() {
		LinkedList<GridCell> coloredAdjCells = new LinkedList<>();
		
		for (int[] dir : Grid.DIRECTIONS) {
			GridCell adjCell = this.getAdjacentCell(dir[0], dir[1]);
			if (adjCell != null && !adjCell.isColoredCell())
				coloredAdjCells.add(adjCell);
		}
		
		return coloredAdjCells;
	}
	
	
	/** Returns this grid cell's adjacent cells. */
	public LinkedList<GridCell> getAllAdjacents() {
		LinkedList<GridCell> adjacentCells = new LinkedList<>();
		
		for (int[] dir : Grid.DIRECTIONS)
			adjacentCells.add(this.getAdjacentCell(dir[0], dir[1]));
		
		return adjacentCells;
	}
	
	
	public int countOutBoundAdjacents() {
		int count = 0;
		for (int[] dir : Grid.DIRECTIONS)
			if (getAdjacentCell(dir[0], dir[1]) == null)
				count++;
		return count;
	}
	
	public String toString() {
		return "Pos="+ pos;
//		return "Pos="+ pos +" : heur="+ heuristic +" : pairFlowPointerPos="+ pairInitialFlowPointer.pos 
//			+" : previousPointerPos="+ previousPointer.pos +" : forced="+ wasMoveForced;
	}
}
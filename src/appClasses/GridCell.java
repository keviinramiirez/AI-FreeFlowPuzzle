package appClasses;

import java.awt.Color;
import java.util.HashSet;
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
	public int heuristic;
	
	/** The previous pointer to backtrack to. */
	public GridCell previousCell;
	
	/** This Grid Cell's Pair Initial Flow Pointer. */
	public GridCell pairFlowPointer;
	
	/** Adjacent cells to consider moving towards to. */
	public LinkedList<GridCell> nextAdjCells;
	
	private HashSet<String> cellsToRemember;
	
	/** This Grid Cell had one cell to move into. */
	public boolean hasForcedMove;
		
	/** This Grid Cell is part of a finished path. */
	public boolean isFinished;
	
	
	public GridCell(Grid grid, Pos pos) {
		this(grid, pos, Grid.EMPTY_COLOR, 0, null, null, 
				new LinkedList<GridCell>(), new HashSet<String>());
	}
	public GridCell(Grid grid, Pos pos, Color color) {
		this(grid, pos, color, 0, null, null, 
				new LinkedList<GridCell>(), new HashSet<String>());
	}
	/** 
	 *  @param pos position of this cell
	 *  @param color this Grid Cell's color.
	 *  @param heuristic amount of empty adjacent at initialization
	 *  @param pairFlowPointer this cell's pair color pointer. Null if this cell is non empty.
	 *  @param previousPointer reference to the previous pointer, tracked to move backwards to it.
	 *  @param nextAdjCells list of next adjacent cells to consider
	 *  @param hasForcedMove tells us if this grid cell was forced to move to this position.
	 */
	public GridCell(Grid grid, Pos pos, Color color, int heuristic, GridCell pairFlowPointer, 
			GridCell previousPointer, LinkedList<GridCell> nextAdjCells, HashSet<String> cellsToRemember) {
		this.grid = grid;
		this.pos = pos;
		this.color = color;
		this.heuristic = heuristic;

		this.pairFlowPointer = pairFlowPointer;
		this.previousCell = previousPointer;
		this.nextAdjCells = nextAdjCells;
		this.cellsToRemember = cellsToRemember;
		this.hasForcedMove = this.isFinished = false;
	}
	
	/** Resets this color, nextAdjCells, and previousPointer properties */
	public void clearBacktrackingCell(javax.swing.JPanel[][] gridPanel) {
		this.color = Grid.EMPTY_COLOR;
		this.nextAdjCells = new LinkedList<GridCell>();
		this.previousCell = null;
		gridPanel[pos.row][pos.col].setBackground(Grid.EMPTY_COLOR);
	}

	
	/** Returns true if this cell is a colored cell. */
	public boolean isColoredCell() 		{ return !isEmptyCell(); }
	/** Returns true if this cell is an empty cell. */
	public boolean isEmptyCell() 		{ return color.equals(Grid.EMPTY_COLOR); }
	/** Returns true if this cell is a constraint cell. */
	public boolean isConstraintCell() 	{ return color.equals(Grid.NON_CONSTRAINT_COLOR); }
	
	/** Returns true if this is one of the initial flow pointer. */
	public boolean isInitialPointer() {
		return grid.initialFlowPointers.contains(this);
	}

	/** Returns true if this grid cell is the pair of the given flow pointer. */
	public boolean isPairPointerOf(GridCell flowPointer) {
		return this == flowPointer.pairFlowPointer;
	}
	
	/** Returns true if this grid cell is the previous cell of the one given. */
	public boolean isPreviousPointerOf(GridCell cell) {
		return cell.isColoredCell() && this == cell.previousCell;
	}
	
	/** Returns true if this grid cell already moved to the given cell */
	public boolean alreadyMovedTo(GridCell cell) {
		return cell.cellsToRemember.contains(this.toString());
	}
	
	/** Removes the given cell from the hash set of <i>cellsToRemember</i>. */
	public void removeToNotRemember(GridCell cell) {
		this.cellsToRemember.remove(cell.toString());
	}
	
	/** Adds the given cell to the hash set of cells to remember not moving into again. */
	public void rememberIMovedInto(GridCell toMoveInto) {
		toMoveInto.cellsToRemember.add(this.toString());
	}
	
	/** If the grid cell in <b>[</b>row<b>+incrRow</b>, col<b>+incrCol</b><b>]</b> is 
	 *  out of bounds, then it returns null.  Otherwise, returns the mentioned grid cell.
	 */
	private GridCell getDistantCell(int incrRow, int incrCol) {
		return grid.validPosition(pos.row +incrRow, pos.col +incrCol)
				? grid.gridCells[pos.row  + incrRow][pos.col + incrCol] 
				: null;
	}

	/** 
	 *  @return if one of the adjacents is this grid cell's pair 
	 *  		pointer, then returns it. Otherwise returns null.  
	 */
	public GridCell retrievePairAdjPointer() {
		for (int[] dir : Grid.DIRECTIONS) {
			GridCell adjCell = this.getDistantCell(dir[0], dir[1]);
			if (adjCell != null && adjCell.isPairPointerOf(this))
				return adjCell;
		}
		return null;
	}
	
	/** Returns this grid cell's colored adjacent cells. */
	public LinkedList<GridCell> getColoredAdjs() {
		LinkedList<GridCell> coloredAdjCells = new LinkedList<>();
		
		for (int[] dir : Grid.DIRECTIONS) {
			GridCell adjCell = this.getDistantCell(dir[0], dir[1]);
			if (adjCell != null && adjCell.isColoredCell())
				coloredAdjCells.add(adjCell);
		}
		
		return coloredAdjCells;
	}
	
	
	/** Returns this grid cell's empty adjacent cells. */
	public LinkedList<GridCell> getEmptyAdjs() {
		LinkedList<GridCell> coloredAdjCells = new LinkedList<>();
		
		for (int[] dir : Grid.DIRECTIONS) {
			GridCell adjCell = this.getDistantCell(dir[0], dir[1]);
			if (adjCell != null && !adjCell.isColoredCell())
				coloredAdjCells.add(adjCell);
		}
		
		return coloredAdjCells;
	}
	
	
	/** Returns this grid cell's adjacent cells. */
	public LinkedList<GridCell> getAllAdjs() {
		LinkedList<GridCell> adjacentCells = new LinkedList<>();
		
		for (int[] dir : Grid.DIRECTIONS) {
			GridCell adjCell = this.getDistantCell(dir[0], dir[1]);
			if (adjCell != null)
				adjacentCells.add(this.getDistantCell(dir[0], dir[1]));
		}

		return adjacentCells;
	}
	
	/** Returns, if any, this grid cell's adjacent initial flow pointer. */
	public LinkedList<GridCell> getInitialPointerAdjs() {
		LinkedList<GridCell> initAdjs = new LinkedList<>();
		
		for (int[] dir : Grid.DIRECTIONS) {
			GridCell adjCell = this.getDistantCell(dir[0], dir[1]);
			if (adjCell != null && grid.initialFlowPointers.contains(adjCell))
				initAdjs.add(adjCell);
		}

		return initAdjs;
	}
	
	/** Return true if this grid cell has any constraint adjacent cell. */
	public boolean hasConstraintAdj() {
		for (int[] dir : Grid.DIRECTIONS) {
			GridCell adjCell = this.getDistantCell(dir[0], dir[1]);
			if (adjCell != null && adjCell.isConstraintCell())
				return true;
		}
		return false;
	}
	
	/** Returns true if this grid cell has reached its pair flow pointer. */
	public boolean hasPairPointer() {
		for (int[] dir : Grid.DIRECTIONS) {
			GridCell adjCell = this.getDistantCell(dir[0], dir[1]);
			if (adjCell != null && adjCell.isPairPointerOf(this))
				return true;
		}
		return false;
	}
	
	/** Returns amount of empty adjacent cells. */
	public int heuristic() {
		int adjacents = 0;
		
		for (int[] dir : Grid.DIRECTIONS) {
			GridCell adj_cell = this.getDistantCell(dir[0], dir[1]);
			if (adj_cell != null && !adj_cell.isColoredCell()) 
				adjacents++;
		}
		
		return adjacents;
	}
	
	/** Creates a clone with this grid cell's references, 
	 *  only distinct reference would be the grid.
	 */
	public GridCell clone(Grid grid) {
		return new GridCell(grid, pos, color, heuristic,
				pairFlowPointer, previousCell, nextAdjCells, cellsToRemember);
	}
	
	public String toString() {
		return "Pos:"+ pos +", "+ color.toString().substring(9);
	}
}
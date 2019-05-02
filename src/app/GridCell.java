package app;

import java.awt.Color;
import java.util.LinkedList;

/** This class represents each Grid Cell.  Can either be an Empty Cell 
 *  or an Active Cell (colored cell). 
 *  
 *  *Note: a Flow Pointer is the peek (or the initial point) of a flow of active cells
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
	public GridCell previousPointer;
	
	/** This Grid Cell's Pair Initial Flow Pointer. */
	public GridCell pairInitialFlowPointer;
	
	/** Adjacent cells to consider moving towards to. */
	public LinkedList<GridCell> nextAdjCells;
	
	/** Tells us if this Grid Cell was forced to move to this position. */
	public boolean wasMoveForced;
		
	
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
	 *  @param wasMoveForced tells us if this grid cell was forced to move to this position.
	 */
	public GridCell(Grid grid, Pos pos, Color color, int heuristic, GridCell pairFlowPointer, 
			GridCell previousPointer, LinkedList<GridCell> nextAdjCells) {
		this.grid = grid;
		this.pos = pos;
		this.color = color;
		this.heuristic = heuristic;

		this.previousPointer = previousPointer;
		this.pairInitialFlowPointer = pairFlowPointer;
		this.nextAdjCells = nextAdjCells;
		this.wasMoveForced = false;
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
		return cell.isColoredCell() && cell == this.previousPointer;
	}
	
	/** Returns null if row index or column index is out of bounds of the game matrix.
	 *  Otherwise, returns the valid Grid Cell that is <i>incrRow</i> far horizontally
	 *  and <i>incrCol</i> far vertically.
	 */
	private GridCell getAdjacentCell(int incrRow, int incrCol) {
		return (pos.row+incrRow >= Grid.ROWS || pos.col+incrCol >= Grid.COLS 
				|| pos.row+incrRow < 0 || pos.col+incrCol < 0)
				? null 
				: grid.gridCells[pos.row + incrRow][pos.col + incrCol];
	}
	
	/** Returns this grid cell's active adjacent cells. */
	public LinkedList<GridCell> getColoredAdjacents() {
		LinkedList<GridCell> activeAdjCells = new LinkedList<>();
		
		for (int[] dir : Grid.DIRECTIONS) {
			GridCell adjCell = this.getAdjacentCell(dir[0], dir[1]);
			if (adjCell != null && adjCell.isColoredCell())
				activeAdjCells.add(adjCell);
		}
		
		return activeAdjCells;
	}
	
	
	/** Returns this grid cell's adjacent cells. */
	public LinkedList<GridCell> getAllAdjacents() {
		LinkedList<GridCell> adjacentCells = new LinkedList<>();
		
		for (int[] dir : Grid.DIRECTIONS)
			adjacentCells.add(this.getAdjacentCell(dir[0], dir[1]));
		
		return adjacentCells;
	}
	
	public String toString() {
		return "Pos="+ pos +" : heur="+ heuristic +" : pairFlowPointerPos="+ pairInitialFlowPointer.pos 
			+" : previousPointerPos="+ previousPointer.pos +" : forced="+ wasMoveForced;
	}
}

















//
//
//
//
//
///** Counts the amount of Active Cells that are adjacent to the given position*/
//public static int countActiveAdjacent(Grid grid, Pos pos) {
//	int count = 0;
//	for (int[] dir : Grid.DIRECTIONS) {
//		if (pos.row + dir[0] >= Grid.ROWS || pos.col + dir[1] >= Grid.COLS)
//			
////			(pos.row+incrRow >= Grid.ROWS || pos.col+incrCol >= Grid.COLS)
//		GridCell currAdjCell = getAdjacentCell(dir[0], dir[1]);
////		grid.gridCells
////				[pos.row + dir[0]][pos.col + dir[1]];
//		if (currAdjCell != null && currAdjCell.isActiveCell())
//			count++;
//	}
//	return count;
//}
//
///** Counts this Grid Cell's amount of Active Adjacent. */
//public int countActiveAdjacent() {
//	return GridCell.countActiveAdjacent(grid, this.pos);
//}
//
///** Returns null if row index or column index is out of bounds of the game matrix.
// *  Otherwise, returns the Grid Cell that is <i>incrRow</i> away horizontally
// *  and <i>incrCol</i> away vertically.
// */
//private GridCell getAdjacentCell(int incrRow, int incrCol) {
//	return (pos.row+incrRow >= Grid.ROWS || pos.col+incrCol >= Grid.COLS)
//			? null 
//			: grid.gridCells[pos.row + incrRow][pos.col + incrCol];
//}
//
//
//public String toString() {
//	return (this == null) ? "null" :
//		"Pos="+ pos +" : heur="+ heuristic +" : pairFlowPointerPos="+ pairInitialFlowPointer.pos 
//		+" : previousPointerPos="+ previousPointer.pos +" : forced="+ wasMoveForced;
//}



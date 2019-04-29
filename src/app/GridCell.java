package app;

import java.awt.Color;

/** This class represents each Grid Cell.  Can either be an Empty Cell 
 *  or an Active Cell (colored cell). 
 *  
 *  *Note: a Flow Pointer is the peek (or the initial point) of a flow of active cells
 */
public class GridCell 
{
	private Grid grid = new Grid();
	public Pos pos;
	public Color color;
	
	// these variables have values (not zero nor null) 
	// only if this Grid Cell is an Active Cell.
	public int heuristic = 0;
	public GridCell previousPointer, pairFlowPointer;
	public boolean wasMoveForced;
	
	public GridCell(Pos pos) {
		this(pos, Grid.EMPTY_COLOR, 0, null, null);
	}
	public GridCell(Pos pos, int heuristic, Color color) {
		this(pos, color, heuristic, null, null);
	}
	/** 
	 *  @param pos position of this cell
	 *  @param color this Grid Cell's color.
	 *  @param heuristic
	 *  @param pairFlowPointer this cell's pair color pointer. Null if this cell is non empty.
	 *  @param previousPointer reference to the previous pointer, tracked to move backwards to it.
	 */
	public GridCell(Pos pos, Color color, int heuristic,
			GridCell pairFlowPointer, GridCell previousPointer) {
		this.pos = pos;
		this.color = color;
		this.heuristic = heuristic;

		this.previousPointer = previousPointer;
		this.pairFlowPointer = pairFlowPointer;
		this.wasMoveForced = false;
	}
	
	/** Returns true if this cell is a non empty cell. */
	public boolean isActiveCell() {
		return !color.equals(Grid.EMPTY_COLOR);
	}
	
	/** Return true if given cell is the pair of this FlowPointer. */
	public boolean isPairFlowPointer(GridCell cell) {
		return cell.isActiveCell() && cell == pairFlowPointer;
	}
	
	public boolean isInitialFlowPointer() {
		return this.isActiveCell() && grid.initialFlowPointerPos.contains(pos);
	}

	public int countActiveAdjacent() {
		int count = 0;
		for (int[] dir : grid.DIRECTIONS)
			if (grid.gridCells[pos.row + dir[0]][pos.col+ dir[1]].isActiveCell())
				count++;
		return count;
	}
}

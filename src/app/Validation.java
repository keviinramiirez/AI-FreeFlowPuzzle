package app;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import interfaces.Entry;
import queue.ArrayQueue;
import queue.SLLQueue;

public class Validation 
{
	Grid grid;
	
	public Validation(Grid grid) {
		this.grid = grid;
	}

	boolean causesStrandedColorOrRegion() {	
		for (Entry<Integer, GridCell> entry : grid.pq) {
			// Stranded Color
			if (pathToPairExists(entry.getValue()))
				return true;
			// Stranded Region
			return strandedRegion();
		}
		
		return true;
	}
	
	public boolean strandedRegion() {
		int r = 0, c = 0;
		// iterates until finding first empty cell
		outerloop:
		for (; r < Grid.ROWS; r++)
			for (; c < Grid.COLS; c++)
				if (!this.grid.gridCells[r][c].isColoredCell())
					break outerloop;
		
		ArrayQueue<GridCell> queue = new ArrayQueue<GridCell>();
		HashSet<Pos> visitedPos = new HashSet<>(); 
		queue.enqueue(this.grid.gridCells[r][c]);
		
		
		int nRegionsEmptyCells = 0;
		while (!queue.isEmpty()) {
			GridCell currEmptyCell = queue.dequeue();
			for (GridCell adjCell : currEmptyCell.getAllAdjacents()) {
				// don't queue the already visited empty cells
				if (adjCell != null && !visitedPos.contains(adjCell.pos) && !adjCell.isColoredCell())
				{
					nRegionsEmptyCells++;
					queue.enqueue(adjCell);
					visitedPos.add(adjCell.pos);
				}
			}
		}
		
		return nRegionsEmptyCells < grid.nEmptyCells;
	}
	
	/** Iterates through all possible paths and returns true if there exist 
	 *  a path that leads to the given flow pointer's pair pointer.
     */
	public boolean pathToPairExists(GridCell flowPointer) {
		ArrayQueue<GridCell> queue = new ArrayQueue<GridCell>();
		HashSet<Pos> visitedPos = new HashSet<>(); 
		queue.enqueue(flowPointer);
		
		while (!queue.isEmpty()) {
			GridCell currFlowPointer = queue.dequeue();
			for (GridCell adjCell : currFlowPointer.getAllAdjacents()) {
				if (!visitedPos.contains(adjCell.pos))// don't queue the already visited grid cells
				{
					if (flowPointer.isPairPointer(adjCell)) 
						return true;
					visitedPos.add(adjCell.pos);
					queue.enqueue(adjCell);
				}
			}
		}
		
		return false;
	}
	
	
	boolean puzzleIsSolved() {
		return grid.nEmptyCells == 0;
	}
	
	
	/** 
	 *  The following logic returns true:<br>
	 *  <ul>
	 *  	<li>if <i>currFlowPointer</i> doesn't have any adjacent, or</li>
	 *  	<li>If the given flow pointer constraints an adjacent <b>initial</b> pointer which
	 *  	isn't its own pair initial pointer, then add its previous active cell to given list.</li>
	 *  	<li>Else, if there is only one adjacent cell (forced move), then only add that cell to given list.</li>
	 *  	<li>Else, if there is no constraint and one of the active adjacent was its pair 
	 *  	pointer, then only add its pair to given list.</li>
	 *  </ul>
	 *  Returns false is there isn't any constraint adjacent cells, 
	 *  and adds its active adjacent cells to the given list.
	 *  @param currFlowPointer current flow pointer to analyze.
	 *  @param cellsToConsider cells to consider move toward to.
	 */
	public boolean constraintsPointerOrAdjacents(GridCell currFlowPointer, LinkedList<GridCell> cellsToConsider) {
		int currRow = currFlowPointer.pos.row;
		int currCol = currFlowPointer.pos.col;
		GridCell pairPointerFound = null;
		
		// no adjacent
		if (currFlowPointer.getColoredAdjacents().size() == 0)
			return true;
		
		for (int[] dir : Grid.DIRECTIONS) {
			// current ADJACENT cell of the current flow pointer
			GridCell currAdjCell = grid.gridCells
					[currRow + dir[0]][currCol + dir[1]];

			
			LinkedList<GridCell> activeAdjacentCells = currAdjCell.getColoredAdjacents();
			
			// is current adjacent cell its pair pointer?
			if (currFlowPointer.isPairPointer(currAdjCell))
				pairPointerFound = currAdjCell;
			
			// check if it constraints an initial pointer which isn't its pair pointer
			if (currAdjCell.isColoredCell() && currAdjCell.isInitialFlowPointer()
					&& pairPointerFound == null && activeAdjacentCells.size() == 0)
				return cellsToConsider.add(currFlowPointer.previousPointer); // shall backtrack to previous cell
			
			// is there only one move to consider?
			else if (activeAdjacentCells.size() == 1)
				return cellsToConsider.add(activeAdjacentCells.getFirst());
		}
		
		// if one of the active adjacent was its pair flow pointer
		if (pairPointerFound != null)
			return cellsToConsider.add(pairPointerFound);
		
		// add each found active cell that isn't constraint
		for (GridCell activeCells : currFlowPointer.getColoredAdjacents())
			cellsToConsider.add(activeCells);
		return false;
	}
}








///** Returns null if the given flow pointer has no forces move. If it has,
// *  then returns the position to which the flow pointer is required to to move. 
// */
//LinkedList<GridCell> cellsToConsider(GridCell flowPointer) {
//	LinkedList<GridCell> cellsToConsider = new LinkedList<>();
//	if (constraintsAdjacents(flowPointer, cellsToConsider))
//		return cellsToConsider;
//
//	return null;
//	// at this point, 
////	LinkedList<GridCell> activeAdjArray = flowPointer.getActiveAdjacents();
////	if (activeAdjArray.size() == 1) return cellsToConsider;
////	if (activeAdjArray.size() == 0) return null;
////
////	return activeAdjArray;
//}


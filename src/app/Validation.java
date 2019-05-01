package app;

import java.util.LinkedList;

public class Validation 
{
	Grid grid = new Grid();

	boolean causesStrandedRegions(GridCell flowPointer) {
//		for (int r = 0; r < Grid.ROWS; r++) {
//			for (int c = 0; c < Grid.COLS; c++) {
//				
//			}
//		}
		
		
		return true;
	}
	
	boolean puzzleIsSolved() {
		return false;
	}
	
	/** Returns null if the given flow pointer has no forces move. If it has,
	 *  then returns the position to which the flow pointer is required to to move. 
	 */
	LinkedList<GridCell> cellsToConsider(GridCell flowPointer) {
		LinkedList<GridCell> cellsToConsider = new LinkedList<>();
		if (constraintsAdjacents(flowPointer, cellsToConsider))
			return cellsToConsider; //backtrack to previous node

		// at this point, 
		LinkedList<GridCell> activeAdjArray = flowPointer.getActiveAdjacents();
		if (activeAdjArray.size() == 1) return cellsToConsider;
		if (activeAdjArray.size() == 0) return null;

		return activeAdjArray;
	}
	
	/** 
	 *  If the given flow pointer constraints an adjacent initial flow pointer which
	 *  isn't its own pair initial pointer, then only consider its previous active cell.
	 *  Else, if there is only one adjacent cell, then only consider that cell.
	 *  Else, if there is no constraint and one of the active adjacent was its pair 
	 *  pointer, then only consider its pair.
	 *  @param flowPointer current flow pointer to analyze.
	 *  @param cellsToConsider cells to consider move toward to.
	 */
	boolean constraintsAdjacents(GridCell flowPointer, LinkedList<GridCell> cellsToConsider) {
		int currRow = flowPointer.pos.row;
		int currCol = flowPointer.pos.col;
		LinkedList<GridCell> activeAdjacentCells = new LinkedList<>();
		GridCell pairPointerFound = null;
		
		for (int[] dir : Grid.DIRECTIONS) {
			GridCell currAdjCell = grid.gridCells
					[currRow + dir[0]][currCol + dir[1]];

			if (flowPointer.isPairFlowPointer(currAdjCell))
				pairPointerFound = currAdjCell; 
			
			activeAdjacentCells = currAdjCell.getActiveAdjacents();

			// check if it constrains an initial pointer which isn't its pair pointer
			if (currAdjCell.isActiveCell() && currAdjCell.isInitialFlowPointer()
					&& pairPointerFound == null && activeAdjacentCells.size() == 0)
				return cellsToConsider.add(flowPointer.previousPointer); // shall backtrack to previous cell
			
			// is there only one move to consider?
			else if (activeAdjacentCells.size() == 1)
				return cellsToConsider.add(activeAdjacentCells.getFirst());
		}
		
		// if one of the active adjacent was its pair flow pointer
		if (pairPointerFound != null)
			return cellsToConsider.add(pairPointerFound);
		
		// add each found active cell that isn't constrant
		for (GridCell activeCells : activeAdjacentCells)
			cellsToConsider.add(activeCells);
		return false;
	}
}

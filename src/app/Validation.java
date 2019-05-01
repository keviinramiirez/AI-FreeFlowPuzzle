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
	
//	/** Returns null if the given flow pointer has no forces move. If it has,
//	 *  then returns the position to which the flow pointer is required to to move. 
//	 */
//	LinkedList<GridCell> cellsToConsider(GridCell flowPointer) {
//		LinkedList<GridCell> cellsToConsider = new LinkedList<>();
//		if (constraintsAdjacents(flowPointer, cellsToConsider))
//			return cellsToConsider;
//
//		return null;
//		// at this point, 
////		LinkedList<GridCell> activeAdjArray = flowPointer.getActiveAdjacents();
////		if (activeAdjArray.size() == 1) return cellsToConsider;
////		if (activeAdjArray.size() == 0) return null;
////
////		return activeAdjArray;
//	}
	
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
	boolean constraintsPointerOrAdjacents(GridCell currFlowPointer, LinkedList<GridCell> cellsToConsider) {
		int currRow = currFlowPointer.pos.row;
		int currCol = currFlowPointer.pos.col;
		GridCell pairPointerFound = null;
		
		if (currFlowPointer.getActiveAdjacents().size() == 0)
			return true;
		
		for (int[] dir : Grid.DIRECTIONS) {
			// current ADJACENT cell of the current flow pointer
			GridCell currAdjCell = grid.gridCells
					[currRow + dir[0]][currCol + dir[1]];

			
			LinkedList<GridCell> activeAdjacentCells = currAdjCell.getActiveAdjacents();

			if (currFlowPointer.isCellPairFlowPointer(currAdjCell))
				return cellsToConsider.add(currFlowPointer.previousPointer); // shall backtrack to previous cell
			
			// check if it constrains an initial pointer which isn't its pair pointer
			if (currAdjCell.isActiveCell() && currAdjCell.isInitialFlowPointer()
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
		for (GridCell activeCells : currFlowPointer.getActiveAdjacents())
			cellsToConsider.add(activeCells);
		return false;
	}
}

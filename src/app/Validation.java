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
	
	/** Returns true if the position of the given flow pointer, constraints its adjacent cells.
	 *  @param flowPointer current analyzed flow pointer.
	 *  @param pairPointerFound boolean reference to be used when exiting the method.
	 */
	boolean constraintsAdjacents(GridCell flowPointer, LinkedList<GridCell> cellsToConsider) {
		int currRow = flowPointer.pos.row;
		int currCol = flowPointer.pos.col;

		for (int[] dir : Grid.DIRECTIONS) {
			GridCell currAdjCell = grid.gridCells
					[currRow + dir[0]][currCol + dir[1]];

			int currAdjCount = currAdjCell.getActiveAdjacents().size();

			if (flowPointer.isPairFlowPointer(currAdjCell)){
				cellsToConsider.add(currAdjCell);
				return false;
			}
			
			if (currAdjCell.isActiveCell() && currAdjCell.isInitialFlowPointer()
					&& cellsToConsider.size() == 0 && currAdjCount == 0)
				return true;
			
			else if (currAdjCount == 1)
				return true;			
		}
		
		return false;
	}
}

package app;

public class Validation 
{
	Grid grid = new Grid();
//	boolean validateProcess() {
//		
//	}
	
	
	
	boolean isForceMove(GridCell cell) {
		Boolean pairPointerFound = false; //shall be true if at one of its adjacent is its pair
		
		if (constraintsAdjacents(cell, pairPointerFound))
			return false; //backtrack to previous node
		
		if (pairPointerFound) return true;
		
		int adjCount = cell.countActiveAdjacent();
		if (adjCount == 1) return true;
		if (adjCount == 0) return false;

		return true;
	}
	
	/** Returns true if the position of the given flow pointer, constraints its adjacent cells.
	 *  @param flowPointer current analyzed flow pointer.
	 *  @param pairPointerFound boolean reference to be used when exiting the method.
	 */
	boolean constraintsAdjacents(GridCell flowPointer, Boolean pairPointerFound) {
		int currRow = flowPointer.pos.row;
		int currCol = flowPointer.pos.col;

		for (int[] dir : Grid.DIRECTIONS) {
			GridCell currAdjCell = grid.gridCells
					[currCol + dir[0]][currRow + dir[1]];
			
			int adjCount = currAdjCell.countActiveAdjacent();

			if (flowPointer.isPairFlowPointer(currAdjCell))
				pairPointerFound = true;
			
			if (currAdjCell.isActiveCell() && currAdjCell.isInitialFlowPointer()
					&& !pairPointerFound && adjCount == 0)
				return true;
			if (!currAdjCell.isActiveCell() && adjCount == 1)
				return true;			
		}
		
		return false;
	}
}

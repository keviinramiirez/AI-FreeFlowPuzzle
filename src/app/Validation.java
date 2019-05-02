package app;

import java.util.HashSet;
import java.util.LinkedList;

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
				return false;
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
				if (adjCell != null && !visitedPos.contains(adjCell.pos))// don't queue the already visited grid cells
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
	
	
	public LinkedList<GridCell> cellsToConsiderMovingInto(GridCell currFlowPointer) {
		int currRow = currFlowPointer.pos.row;
		int currCol = currFlowPointer.pos.col;
		LinkedList<GridCell> cellsToConsider = new LinkedList<>();
		GridCell pairPointerFound = null;
		
		LinkedList<GridCell> emptyAdjacents = currFlowPointer.getEmptyAdjacents();
		
		// if no empty adjacent (backtrack)
		if (emptyAdjacents.isEmpty()) {
			cellsToConsider.add(currFlowPointer.previousCell);
			return cellsToConsider;
		}
		
		// if there is only one move to consider (force move)
		if (emptyAdjacents.size() == 1) {
			cellsToConsider.add(emptyAdjacents.getFirst());
			return cellsToConsider;
		}
		
		// analyze each adjacent
		for (int[] dir : Grid.DIRECTIONS) {
			// current ADJACENT cell of the current flow pointer
			GridCell currAdjCell = null;
			if (grid.validPosition(currRow+dir[0], currCol+dir[1]))
				currAdjCell = grid.gridCells[currRow + dir[0]][currCol + dir[1]];
			
			// if current adjacent is not out of bounds
			if (currAdjCell != null) {
				// if current adjacent cell is its pair pointer
				if (currFlowPointer.isPairPointer(currAdjCell))
					pairPointerFound = currAdjCell;
				
				LinkedList<GridCell> coloredAdjacentCells = currAdjCell.getColoredAdjacents();
				
				// if it constraints an initial pointer which isn't its pair pointer (backtrack)
				if (currAdjCell.isColoredCell() && currAdjCell.isInitialFlowPointer()
						&& pairPointerFound == null && coloredAdjacentCells.isEmpty()) {
					cellsToConsider.add(currFlowPointer.previousCell);
					return cellsToConsider;
				}
				
				
				// if it's an empty cell && it has only one move to consider && 
				// at least one of the other three colored cells isn't an initial pointer.
				else if (!currAdjCell.isColoredCell() && 
						(coloredAdjacentCells.size() + currAdjCell.countOutBoundAdjacents() >= 3) 
						&& !this.allInitialPointers(coloredAdjacentCells)) {
					cellsToConsider.add(currFlowPointer.previousCell);
					return cellsToConsider;
				}
			}
		}
		
		// if one of the colored adjacent was its pair flow pointer
		if (pairPointerFound != null)
			cellsToConsider.add(pairPointerFound);
		
		// each empty cell is valid to move into
		for (GridCell emptyAdj : currFlowPointer.getEmptyAdjacents())
			cellsToConsider.add(emptyAdj);
		
		return cellsToConsider;
	}
	
	private boolean allInitialPointers(LinkedList<GridCell> cells) {
		for (GridCell cell : cells)
			if (!cell.isInitialFlowPointer())
				return false;
		return true;
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
		
		// if no empty adjacent 
		if (currFlowPointer.getEmptyAdjacents().size() == 0)
			return true;
		
		for (int[] dir : Grid.DIRECTIONS) {
			// current ADJACENT cell of the current flow pointer
			GridCell currAdjCell = grid.gridCells
					[currRow + dir[0]][currCol + dir[1]];

			
			LinkedList<GridCell> coloredAdjacentCells = currAdjCell.getColoredAdjacents();
			
			// is current adjacent cell its pair pointer?
			if (currFlowPointer.isPairPointer(currAdjCell))
				pairPointerFound = currAdjCell;
			
			// check if it constraints an initial pointer which isn't its pair pointer
			if (currAdjCell.isColoredCell() && currAdjCell.isInitialFlowPointer()
					&& pairPointerFound == null && coloredAdjacentCells.size() == 0)
				return cellsToConsider.add(currFlowPointer.previousCell); // shall backtrack to previous cell
			
			// is there only one move to consider?
			else if (currAdjCell.isColoredCell() && coloredAdjacentCells.size() == 3)
				return cellsToConsider.add(coloredAdjacentCells.getFirst());
		}
		
		// if one of the colored adjacent was its pair flow pointer
		if (pairPointerFound != null)
			return cellsToConsider.add(pairPointerFound);
		
		// add each found colored cell that isn't constraint
		for (GridCell coloredAdj : currFlowPointer.getEmptyAdjacents())
			cellsToConsider.add(coloredAdj);
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


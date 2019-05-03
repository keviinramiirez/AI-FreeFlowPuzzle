package app;

import java.util.HashSet;
import java.util.LinkedList;

import interfaces.Entry;
import queue.SLLQueue;

public class Validation 
{
	Grid grid;
	
	public Validation(Grid grid) {
		this.grid = grid;
	}

	public boolean isThereStrandedColorOrRegion() {	
		for (Entry<Integer, GridCell> entry : grid.pq) {
			// Stranded Color Validation
			if (!pathToPairExists(entry.getValue()))
				return true;
		}
		
		// Stranded Region Validation
		return strandedRegion();
	}
	
	public boolean strandedRegion() {
		int r = 0, c = 0;
		// iterates until finding first empty cell
		outerloop:
		for (; r < Grid.ROWS; r++)
			for (; c < Grid.COLS; c++)
				if (!this.grid.gridCells[r][c].isColoredCell())
					break outerloop;
		
		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
		HashSet<Pos> visitedPos = new HashSet<>(); 
		queue.enqueue(this.grid.gridCells[r][c]);
		
		
		int nRegionsEmptyCells = 0;
		while (!queue.isEmpty()) {
			GridCell currEmptyCell = queue.dequeue();
			for (GridCell adjCell : currEmptyCell.getAllAdjs()) {
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
	private boolean pathToPairExists(GridCell flowPointer) {
		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
		LinkedList<GridCell> visitedPos = new LinkedList<>(); 
		queue.enqueue(flowPointer);
		
		while (!queue.isEmpty()) {
			GridCell currFlowPointer = queue.dequeue();
			for (GridCell adjCell : currFlowPointer.getAllAdjs()) {
				if (adjCell != null && !visitedPos.contains(adjCell.pos))// don't queue the already visited grid cells
				{
					if (flowPointer.isPairPointerOf(adjCell)) 
						return true;
					if (!visitedPos.contains(adjCell)
							&& !adjCell.isColoredCell() || adjCell.color == flowPointer.color)
						queue.enqueue(adjCell);
					visitedPos.add(adjCell);
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
		
		if (currFlowPointer == grid.gridCells[7][6])
			System.out.println();
		
		LinkedList<GridCell> emptyAdjacents = currFlowPointer.getEmptyAdjs();
		
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
			GridCell currAdj = null;
			if (grid.validPosition(currRow+dir[0], currCol+dir[1]))
				currAdj = grid.gridCells[currRow + dir[0]][currCol + dir[1]];
			
			// if current adjacent is not out of bounds
			if (currAdj != null && currAdj != currFlowPointer.previousCell) {
				// if current adjacent cell is its pair pointer
				if (currAdj.isPairPointerOf(currFlowPointer.pairInitialFlowPointer))
					pairPointerFound = currAdj;
				
				LinkedList<GridCell> currAdj_coloredAdjs = currAdj.getColoredAdjs();
//				if (currAdjCell.isColoredCell() && currAdjCell.isInitialFlowPointer()
//						&& pairPointerFound == null && coloredAdjacentCells.isEmpty())
				// if currAdj has a constraint an initial pointer which isn't its pair pointer (backtrack)
				if (currAdj.isColoredCell() && currAdj.isInitialFlowPointer() 
						&& pairPointerFound == null && currAdj_coloredAdjs.isEmpty()) {
					cellsToConsider.add(currFlowPointer.previousCell);
					return cellsToConsider;
				}
				
				// if it's an empty cell && it has only one move to consider && 
				// at least one of the other three colored cells isn't an initial pointer.
				else if (!currAdj.isColoredCell() 
//						&& !this.shallAtLeastOneContraintAdj(currAdj.getInitialPointerAdjs())
						) 
				{
					// include out bound adjs as 'initial pointers' if any
					currAdj_coloredAdjs.addAll(this.getOutBoundAdjacents(currAdj));
					if (currAdj_coloredAdjs.size() == 4 
							|| (currAdj_coloredAdjs.size() == 3 
								&& !this.shallAtLeastOneInitialPointer(currAdj_coloredAdjs))) 
					{
						cellsToConsider.add(currFlowPointer.previousCell);
						return cellsToConsider;
					}
				}
				
//				// if it's an empty cell && it has only one move to consider && 
//				// at least one of the other three colored cells isn't an initial pointer.
//				else if (!currAdj.isColoredCell()) {
//					boolean constrainAdj = !this.contraintsColoredAdjs(currAdj_coloredAdjs);
//					
//					// add out bound 'initial pointers' if any
//					currAdj_coloredAdjs.addAll(this.getOutBoundAdjacents(currAdj));
//					
//					// if one of the initial adjacents contraint
//					if (constrainAdj || (currAdj_coloredAdjs.size() == 3 
//						&& !this.AtLeastOneInitialPointers(currAdj_coloredAdjs)))
//					{
//						cellsToConsider.add(currFlowPointer.previousCell);
//						return cellsToConsider;
//					}
//				}
			}
		}
		
		// if one of the colored adjacent was its pair flow pointer
		if (pairPointerFound != null)
			cellsToConsider.add(pairPointerFound);
		
		// each empty cell is valid to move into
		for (GridCell emptyAdj : currFlowPointer.getEmptyAdjs())
			if (!shallContraintsAdjInitialPointer(emptyAdj))
				cellsToConsider.add(emptyAdj);
		
		return cellsToConsider;
	}
	
	/** method used only within the validation  */
	private LinkedList<GridCell> getOutBoundAdjacents(GridCell cell) {
		// I assign dummy out bound adjs as initial pointer, for the purpose of validation
		LinkedList<GridCell> outBoundAdjs = new LinkedList<>();
		for (int[] dir : Grid.DIRECTIONS)
			if (!grid.validPosition(cell.pos.row + dir[0], cell.pos.col + dir[1]))
				outBoundAdjs.add(grid.initialFlowPointers.getFirst()); // assign whatever initial pointer);
		return outBoundAdjs;
	}
	
	
	private boolean shallAtLeastOneInitialPointer(LinkedList<GridCell> cells) {
		for (GridCell cell : cells)
			if (cell.isInitialFlowPointer())
				return true;
		return false;
	}
	
	
	public boolean shallContraintsAdjInitialPointer(GridCell emptyCell) {
		if (emptyCell.isColoredCell())
			throw new IllegalArgumentException("Argument should be an empty cell");
		
		for (GridCell adjInit : emptyCell.getInitialPointerAdjs())
			if (adjInit.getColoredAdjs().size() >= 3)
				return true;
		return false;
	}
	
//	private boolean shallAtLeastOneContraintAdj(LinkedList<GridCell> adjs) {
//		for (GridCell adj : adjs)
//			if (adj.getColoredAdjs().size() >= 3)
//				return true;
//		return false;
//	}
	
	
	
//	/** 
//	 *  <ul>
//	 *  	<li>if <i>currFlowPointer</i> doesn't have any adjacent, or</li>
//	 *  	<li>If the given flow pointer constraints an adjacent <b>initial</b> pointer which
//	 *  	isn't its own pair initial pointer, then add its previous colored cell to given list.</li>
//	 *  	<li>Else, if there is only one adjacent cell (forced move), then only add that cell to given list.</li>
//	 *  	<li>Else, if there is no constraint and one of the colored adjacent was its pair 
//	 *  	pointer, then only add its pair to given list.</li>
//	 *  </ul>
//	 *  is there isn't any constraint adjacent cells, 
//	 *  and adds its active adjacent cells to the given list.
//	 *  @param currFlowPointer current flow pointer to analyze.
//	 *  @param cellsToConsider cells to consider move toward to.
//	 */
	
	public String toString() {
		return grid.toString();
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


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

	public boolean isThereStrandedColorOrRegion(GridCell currPointer) {
		for (Entry<Integer, GridCell> entry : grid.pq) {
			// Stranded Color Validation
			if (!pathToPairExists(entry.getValue()))
				return true;
		}
		
		// Stranded Region Validation
		return strandedRegion(currPointer);
	}
//	
//	
//	private boolean equalColoredCells(LinkedList<GridCell> cells) {
//		if (cells.size() == 2)
//			return cells.getFirst().color == cells.getLast().color;
////		if (cells.size() == 1)
////			return 
//		return false;
//	}
	
	public boolean validEdgeToMoveInto(GridCell edge) {
		LinkedList<GridCell> coloredAdjs = edge.getColoredAdjs();
		if (coloredAdjs.size() == 2)
			return coloredAdjs.getFirst().color == coloredAdjs.getLast().color;
		return false;
	}
	
	private GridCell edgesWithTwoEqualColoredCells(GridCell currPointer) {
		LinkedList<GridCell> emptyAdjs = currPointer.getEmptyAdjs();
		if (emptyAdjs.size() <= 2) {
			for (GridCell emptyAdj : emptyAdjs)
				if (this.grid.edges.contains(emptyAdj) && validEdgeToMoveInto(emptyAdj))
					return emptyAdj;
		}
		return null;
	}
	
	public boolean strandedRegion(GridCell currPointer) {
		// either valid edge (if currPointer is at the edge) or null if no valid edge exists
		GridCell validEdge = edgesWithTwoEqualColoredCells(currPointer);
		
		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
		LinkedList<GridCell> visitedCells = new LinkedList<>(); 
		int nRegionsEmptyCells = 1;

		if (validEdge != null) {
			visitedCells.add(validEdge);
			nRegionsEmptyCells++;
		}
			
		int r = 0, c = 0;
		// iterates until finding first empty cell
		outerloop:
		for (; r < Grid.ROWS; r++)
			for (; c < Grid.COLS; c++)
				if (!this.grid.gridCells[r][c].isColoredCell() 
						&& !visitedCells.contains(this.grid.gridCells[r][c]))
					break outerloop;
		
		queue.enqueue(this.grid.gridCells[r][c]);
		visitedCells.add(this.grid.gridCells[r][c]);
		
		while (!queue.isEmpty()) {
			GridCell currEmptyCell = queue.dequeue();
			for (GridCell adjCell : currEmptyCell.getEmptyAdjs()) {
				// don't queue the already visited empty cells
				if (!visitedCells.contains(adjCell)) {
					nRegionsEmptyCells++;
					queue.enqueue(adjCell);
					visitedCells.add(adjCell);
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
		GridCell pairPointerFound = currFlowPointer.retrievePairAdjPointer();
		
		if (currFlowPointer == grid.gridCells[2][1])
			System.out.println("for debugging purposes");
		
//		if (pairPointerFound != null) {
//			cellsToConsider.add(pairPointerFound);
//			return cellsToConsider;
//		}
		
		LinkedList<GridCell> emptyAdjacents = currFlowPointer.getEmptyAdjs();
		
		// don't go within this if statement if we've reached the pair pointer
		if (pairPointerFound == null) {
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
		}

		// analyze each adjacent
		for (int[] dir : Grid.DIRECTIONS) {
			// current ADJACENT cell of the current flow pointer
			GridCell currAdj = null;
			if (grid.validPosition(currRow+dir[0], currCol+dir[1]))
				currAdj = grid.gridCells[currRow + dir[0]][currCol + dir[1]];
			
			// if current adjacent is not out of bounds
			if (currAdj != null && !currAdj.isPreviousPointerOf(currFlowPointer)) {
				// if current adjacent cell is its pair pointer
				if (currAdj.isPairPointerOf(currFlowPointer))
					pairPointerFound = currAdj;
				
				LinkedList<GridCell> currAdj_coloredAdjs = currAdj.getColoredAdjs();
				// if currAdj constraints an initial pointer which isn't its pair pointer (backtrack)
				if (currAdj.isColoredCell() && currAdj.isInitialFlowPointer() 
						&& pairPointerFound == null && currAdj_coloredAdjs.isEmpty()) {
					cellsToConsider.add(currFlowPointer.previousCell);
					return cellsToConsider;
				}
				
				// if it's an empty cell && it has only one move to consider && 
				// at least one of the other three colored cells isn't an initial pointer.
				else if (!currAdj.isColoredCell()) 
				{
					// include dummy out bound adjacent cells
					currAdj_coloredAdjs.addAll(this.getOutBoundAdjacents(currAdj));
					if (currAdj_coloredAdjs.size() == 4 && oneAdjIsPairPointerOf(currAdj, currAdj_coloredAdjs)
							|| (currAdj_coloredAdjs.size() == 3 
								&& !this.hasOtherInitialPointerAsAdjs(currAdj_coloredAdjs, currFlowPointer))) 
					{
						cellsToConsider.add(currFlowPointer.previousCell);
						return cellsToConsider;
					}
				}
			}
		}
		
		// if one of the valid colored adjacents was its pair flow pointer
		if (pairPointerFound != null) {
			cellsToConsider.addFirst(pairPointerFound);
			for (GridCell emptyAdj : currFlowPointer.getEmptyAdjs())
				if (!shallContraintsAdjInitialPointer(emptyAdj))
					currFlowPointer.nextAdjCells.add(emptyAdj);
		}
		// at this point consider the empty adjs that don't constraint any initial pointer
		else {
			for (GridCell emptyAdj : currFlowPointer.getEmptyAdjs())
				if (!shallContraintsAdjInitialPointer(emptyAdj))
					cellsToConsider.add(emptyAdj);
		}
		
		return cellsToConsider;
	}
	
	private boolean oneAdjIsPairPointerOf(GridCell currAdj, LinkedList<GridCell> cells) {
		for (GridCell cell : cells) {
			if (cell.isPairPointerOf(currAdj))
				return true;
		}
		return false;
	}
	
	/** method used only within the validation  */
	private LinkedList<GridCell> getOutBoundAdjacents(GridCell cell) {
		LinkedList<GridCell> outBoundAdjs = new LinkedList<>();
		
		for (int[] dir : Grid.DIRECTIONS)
			if (!grid.validPosition(cell.pos.row + dir[0], cell.pos.col + dir[1]))
				outBoundAdjs.add(new GridCell(null, 
						new Pos(cell.pos.row + dir[0], cell.pos.col + dir[1]), Grid.EMPTY_COLOR));
		return outBoundAdjs;
	}
	
//	/** Return true if the given adj cells contains at least one 
//	 *  initial pointer, with the exception the initial pointer of the current path we're traversing.
//	 */
	/** Return true if the given adj cells contains at least one initialPointer.
	 */
	private boolean hasOtherInitialPointerAsAdjs(LinkedList<GridCell> adjs, GridCell currPointer) {
		for (GridCell cell : adjs)
			if (cell.isInitialFlowPointer())
//					&& cell.color != currPointer.color && cell == currPointer.pairFlowPointer)
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








//public LinkedList<GridCell> cellsToConsiderMovingInto(GridCell currFlowPointer) {
//	int currRow = currFlowPointer.pos.row;
//	int currCol = currFlowPointer.pos.col;
//	LinkedList<GridCell> cellsToConsider = new LinkedList<>();
//	GridCell pairPointerFound = currFlowPointer.getPairAdjPointer();
//	
//	if (currFlowPointer == grid.gridCells[3][9])
//		System.out.println("for debugging purposes");
//	
////	if (pairPointerFound != null) {
////		cellsToConsider.add(pairPointerFound);
////		return cellsToConsider;
////	}
//	
//	LinkedList<GridCell> emptyAdjacents = currFlowPointer.getEmptyAdjs();
//	
//	// consider these if an adjacent 
////	if (pairPointerFound != null) {
//		// if no empty adjacent (backtrack)
//		if (emptyAdjacents.isEmpty()) {
//			cellsToConsider.add(currFlowPointer.previousCell);
//			return cellsToConsider;
//		}
//
//		// if there is only one move to consider (force move)
//		if (emptyAdjacents.size() == 1) {
//			cellsToConsider.add(emptyAdjacents.getFirst());
//			return cellsToConsider;
//		}
////	}
//
//	// analyze each adjacent
//	for (int[] dir : Grid.DIRECTIONS) {
//		// current ADJACENT cell of the current flow pointer
//		GridCell currAdj = null;
//		if (grid.validPosition(currRow+dir[0], currCol+dir[1]))
//			currAdj = grid.gridCells[currRow + dir[0]][currCol + dir[1]];
//		
//		// if current adjacent is not out of bounds
//		if (currAdj != null && currAdj != currFlowPointer.previousCell) {
//			// if current adjacent cell is its pair pointer
//			if (currAdj.isPairPointerOf(currFlowPointer.pairInitialFlowPointer))
//				pairPointerFound = currAdj;
//			
//			LinkedList<GridCell> currAdj_coloredAdjs = currAdj.getColoredAdjs();
////			if (currAdjCell.isColoredCell() && currAdjCell.isInitialFlowPointer()
////					&& pairPointerFound == null && coloredAdjacentCells.isEmpty())
//			// if currAdj has a constraint an initial pointer which isn't its pair pointer (backtrack)
//			if (currAdj.isColoredCell() && currAdj.isInitialFlowPointer() 
//					&& pairPointerFound == null && currAdj_coloredAdjs.isEmpty()) {
//				cellsToConsider.add(currFlowPointer.previousCell);
//				return cellsToConsider;
//			}
//			
//			// if it's an empty cell && it has only one move to consider && 
//			// at least one of the other three colored cells isn't an initial pointer.
//			else if (!currAdj.isColoredCell()) 
//			{
//				// include out bound adjs as 'initial pointers' if any
//				currAdj_coloredAdjs.addAll(this.getOutBoundAdjacents(currAdj));
//				if (currAdj_coloredAdjs.size() == 4 
//						|| (currAdj_coloredAdjs.size() == 3 
//							&& !this.shallAtLeastOneInitialPointer(currAdj_coloredAdjs))) 
//				{
//					cellsToConsider.add(currFlowPointer.previousCell);
//					return cellsToConsider;
//				}
//			}
//		}
//	}
//	
//	// if one of the colored adjacent was its pair flow pointer
//	if (pairPointerFound != null)
//		cellsToConsider.add(pairPointerFound);
//	else {
//		// at this point consider the empty adjs that don't constraint any initial pointer
//		for (GridCell emptyAdj : currFlowPointer.getEmptyAdjs())
//			if (!shallContraintsAdjInitialPointer(emptyAdj))
//				cellsToConsider.add(emptyAdj);
//	}
//	
//	return cellsToConsider;
//}

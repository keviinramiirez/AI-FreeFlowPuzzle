package app;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;

import interfaces.Entry;
import queue.SLLQueue;
import util.Util;

public class Validation 
{
	Grid grid;
	Grid savedInstanceGrid;
	
	public Validation(Grid grid) {
		this.grid = grid;
		this.savedInstanceGrid = grid;
	}

	public boolean isThereStrandedColorOrRegion(GridCell currPointer) {
		// only want to check paths once
//		if (this.grid == this.savedInstanceGrid)
//			for (Entry<Integer, GridCell> entry : grid.pq) {
//				// Stranded Color Validation
//				if (!pathToPairExists(entry.getValue()))
//					return true;
//			}
		
		// Stranded Region Validation
		return strandedRegion(currPointer);
	}
	
	
	private boolean validEdgeToMoveInto(GridCell edge) {
		LinkedList<GridCell> coloredAdjs = edge.getColoredAdjs();
		if (coloredAdjs.size() == 2)
			return coloredAdjs.getFirst().color == coloredAdjs.getLast().color;
		return false;
	}
	
	
	private GridCell emptyEdges_withEqualColoredAdjCells(GridCell currPointer) {
		LinkedList<GridCell> emptyAdjs = currPointer.getEmptyAdjs();
		if (emptyAdjs.size() <= 2) {
			for (GridCell emptyAdj : emptyAdjs)
				if (this.grid.edges.contains(emptyAdj) && validEdgeToMoveInto(emptyAdj))
					return emptyAdj;
		}
		return null;
	}
	
	
//	private GridCell getAdjContainingPairPointer(LinkedList<GridCell> emptyAdjs) {
//		for (GridCell emptyAdj : emptyAdjs)
//			if (emptyAdj.retrievePairAdjPointer() != null) // emptyAdj has pariPointer
//				return emptyAdj;
//		return null;
	
	/** Return true if the given adj cells contains at least one initialPointer.
	 */
	private boolean hasOtherInitialPointerAsAdjs(LinkedList<GridCell> adjs, GridCell currPointer) {
		for (GridCell adj : adjs)
			if (adj.isInitialFlowPointer())
//					&& cell.color != currPointer.color && cell == currPointer.pairFlowPointer)
				return true;
		return false;
	}
	
	private LinkedList<GridCell> getAdjsContainingPairPointer(LinkedList<GridCell> emptyAdjs, GridCell currPointer) {
		LinkedList<GridCell> adjsContainingPair = new LinkedList<GridCell>();
		for (GridCell emptyAdj : emptyAdjs) {
			emptyAdj.pairFlowPointer = currPointer.pairFlowPointer; // simulating he moved to that position
			if (emptyAdj.hasPairPointer())
				adjsContainingPair.add(emptyAdj);
			emptyAdj.pairFlowPointer = null;
		}
		return adjsContainingPair;
	}
	
	public boolean strandedRegion(GridCell headPointer) {
		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
		LinkedList<GridCell> visitedCells = new LinkedList<>(); 
		int nRegionEmptyCells = this.grid.nEmptyCells - 1;

		if (headPointer != null) {
			// contains either valid edge (if currPointer is at the edge) or null if no valid edge exists
			GridCell validEdge = emptyEdges_withEqualColoredAdjCells(headPointer);//  NEEDS validity for when has only one colored cell
	
			if (validEdge != null) {
				visitedCells.add(validEdge);
				nRegionEmptyCells--;
			}
			
			// contains 
			LinkedList<GridCell> emptyAdjsContainingPairPointer = this.getAdjsContainingPairPointer(
					headPointer.getEmptyAdjs(), headPointer);
			
			for (GridCell emptyAdj : emptyAdjsContainingPairPointer) {
				if (emptyAdj.getEmptyAdjs().size() == 0 && !visitedCells.contains(emptyAdj)) {
					visitedCells.add(emptyAdj);
					nRegionEmptyCells--;
				}
			}
		}
		
		int r = 0, c = 0;
		// iterates until finding first empty cell
		outerloop:
		for (; r < grid.ROWS; r++)
			for (c = 0; c < grid.COLS; c++)
				if (!this.grid.gridCells[r][c].isColoredCell() 
						&& !visitedCells.contains(this.grid.gridCells[r][c]))
					break outerloop;
		
		queue.enqueue(this.grid.gridCells[r][c]);
		visitedCells.add(this.grid.gridCells[r][c]);
		
		if (headPointer == grid.gridCells[2][1])
			System.out.println();
		while (!queue.isEmpty()) {
			GridCell currEmptyCell = queue.dequeue();
			// considers all adjacent cells
			for (GridCell adjCell : currEmptyCell.getAllAdjs()) {
				// don't queue the already visited empty cells
				if (!visitedCells.contains(adjCell)) {
					if (!adjCell.isColoredCell()) {
						nRegionEmptyCells--;
						queue.enqueue(adjCell);
						visitedCells.add(adjCell);
					}
					else if (adjCell.isInitialFlowPointer() && adjCell.isPairPointerOf(headPointer)) {
if (!adjCell.isFinished && !this.pathToPairExists(adjCell)) {
							// resetting the instance grid to the original
							this.grid = this.savedInstanceGrid;
							return true;
						}
						visitedCells.add(adjCell); // may already been added previously
						visitedCells.add(adjCell.pairFlowPointer);
					}
				}
			}
		}
		
		if (nRegionEmptyCells > 0) {
			this.savedInstanceGrid = grid;
			this.grid = grid.clone();
			this.grid.initialFlowPointers = new LinkedList<GridCell>();

			// fill the emptyCells 
			for (r = 0; r < grid.ROWS; r++)
				for (c = 0; c < grid.COLS; c++) {
					GridCell test = savedInstanceGrid.gridCells[r][c].clone(grid);
					grid.gridCells[r][c] = savedInstanceGrid.gridCells[r][c].clone(grid);
					
					// store clone initial pointer to the cloned grid
					if (savedInstanceGrid.initialFlowPointers.contains(savedInstanceGrid.gridCells[r][c]))
						this.grid.initialFlowPointers.add(grid.gridCells[r][c]);
					
					int innerIndex = visitedCells.indexOf(savedInstanceGrid.gridCells[r][c]);
					if (innerIndex != -1 && visitedCells.get(innerIndex).isEmptyCell()) {
//						cloneGrid.gridCells[r][c] = visitedCells.remove(
//								visitedCells.indexOf(grid.gridCells[r][c]));
						this.grid.gridCells[r][c].color = Grid.NON_CONSTRAINT_COLOR;
					}					
				}
			this.grid.nEmptyCells = nRegionEmptyCells + 1;
			if (headPointer == grid.gridCells[3][4])
				System.out.println();
			return this.strandedRegion(null);
		}
		else {
			// resetting the instance grid to the original
			this.grid = this.savedInstanceGrid;
			return nRegionEmptyCells > 0;
		}
	}
	
	/** Iterates through all possible paths and returns true if there exist 
	 *  a path that leads to the given flow pointer's pair pointer.
     */
	private boolean pathToPairExists(GridCell flowPointer) {
		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
		LinkedList<GridCell> visitedCell = new LinkedList<>(); 
		queue.enqueue(flowPointer);
		int count = 0;
		while (!queue.isEmpty()) {
			GridCell currFlowPointer = queue.dequeue();
			for (GridCell adjCell : currFlowPointer.getAllAdjs()) {
				if (adjCell != null && !visitedCell.contains(adjCell))// don't queue the already visited grid cells
				{
					if (adjCell.isPairPointerOf(flowPointer)) 
						return true;
					if (!visitedCell.contains(adjCell)
							&& (adjCell.isEmptyCell() || adjCell.isConstraintCell() || adjCell.color != flowPointer.color)) {
						queue.enqueue(adjCell);
						count++;
					}
					visitedCell.add(adjCell);
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
				
		// don't go within this if statement if we've reached the pair pointer
		if (pairPointerFound == null) {
			LinkedList<GridCell> emptyAdjacents = currFlowPointer.getEmptyAdjs();

			// if no empty adjacent (backtrack)
			if (emptyAdjacents.isEmpty()) {
				cellsToConsider.add(currFlowPointer.previousCell);
				return cellsToConsider;
			}

			// if there is only one move to consider (force move)
			if (emptyAdjacents.size() == 1) {
				
				for (GridCell initPointer : emptyAdjacents.getFirst().getInitialPointerAdjs())
					// backtrack if initial pointer isn't the previous cell && 
					// moving into empty cell would other initial pointers
					if (currFlowPointer != initPointer && !initPointer.isFinished 
							&& initPointer.getEmptyAdjs().size() == 1
							&& grid.nEmptyCells != 1) 
					{
						cellsToConsider.add(currFlowPointer.previousCell);
						return cellsToConsider;
					}

				cellsToConsider.add(emptyAdjacents.removeFirst());
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
					int outBoundAdjs = this.getOutBoundAdjacents(currAdj).size();
//					currAdj_coloredAdjs.addAll(this.getOutBoundAdjacents(currAdj));
					if (currAdj_coloredAdjs.size()+outBoundAdjs == 4 && oneAdjIsPairPointerOf(currAdj, currAdj_coloredAdjs)
							|| (currAdj_coloredAdjs.size()+outBoundAdjs == 3 
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
	
	
	
	
	
	
	
	
	
	
	public boolean strandedRegion2(GridCell currPointer) {		
		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
		LinkedList<GridCell> visitedCells = new LinkedList<>(); 
		int nRegionsEmptyCells = grid.nEmptyCells - 1;
		
		// contains either valid edge (if currPointer is at the edge) or null if no valid edge exists
		GridCell validEdge = emptyEdges_withEqualColoredAdjCells(currPointer);

		if (validEdge != null) {
			visitedCells.add(validEdge);
			nRegionsEmptyCells--;
		}
		
		// contains 
		LinkedList<GridCell> emptyAdjsContainingPairPointer = this.getAdjsContainingPairPointer(
				currPointer.getEmptyAdjs(), currPointer);
		
		for (GridCell emptyAdj : emptyAdjsContainingPairPointer) {
			if (emptyAdj.getEmptyAdjs().size() == 0 && !visitedCells.contains(emptyAdj)) {
				visitedCells.add(emptyAdj);
				nRegionsEmptyCells--;
			}
		}
		
		int r = 0, c = 0;
		// iterates until finding first empty cell
		outerloop:
		for (; r < grid.ROWS; r++)
			for (c = 0; c < grid.COLS; c++)
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
					nRegionsEmptyCells--;
					queue.enqueue(adjCell);
					visitedCells.add(adjCell);
				}
			}
		}
		
		return nRegionsEmptyCells != 0;
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
	
//	/** Return true if the given adj cells contains at least one initialPointer.
//	 */
//	private boolean hasOtherInitialPointerAsAdjs(LinkedList<GridCell> adjs, GridCell currPointer) {
//		for (GridCell adj : adjs)
//			if (adj.isInitialFlowPointer())
////					&& cell.color != currPointer.color && cell == currPointer.pairFlowPointer)
//				return true;
//		return false;
//	}
	
	
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













//		// include dummy out bound adjacent cells
//		currAdj_coloredAdjs.addAll(this.getOutBoundAdjacents(currAdj));
//		if (currAdj_coloredAdjs.size() == 4 && oneAdjIsPairPointerOf(currAdj, currAdj_coloredAdjs)
//				|| (currAdj_coloredAdjs.size() == 3 
//					&& !this.hasOtherInitialPointerAsAdjs(currAdj_coloredAdjs, currFlowPointer))) 
//		{
//			cellsToConsider.add(currFlowPointer.previousCell);
//			return cellsToConsider;
//		}















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

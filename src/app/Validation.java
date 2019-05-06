package app;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import interfaces.Entry;
import queue.SLLQueue;
import util.Util;

public class Validation 
{
	Grid grid;
//	Stack<Grid> gridStack = new Stack<>();
	
	public Validation(Grid grid) {
		this.grid = grid;
	}
	public boolean isThereStrandedColorOrRegion(GridCell currPointer) {
		return strandedRegion(grid, null, currPointer, new LinkedList<>());
	}
	
	public boolean strandedRegion(Grid cloneGrid, Grid previousGrid, 
			GridCell headPointer, LinkedList<GridCell> visitedCells) {
		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
		int nRegionEmptyCells = cloneGrid.nEmptyCells - 1;

		if (headPointer != null) {
			// contains either valid edge (if currPointer is at the edge) or null if no valid edge exists
			GridCell validConstraintEdge = emptyEdges_withEqualColoredAdjCells(headPointer);//  NEEDS validity for when has only one colored cell
	
			if (validConstraintEdge != null) {
				visitedCells.add(validConstraintEdge);
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
		else nRegionEmptyCells--;

		
		int r = 0, c = 0;
		// iterates until finding first empty cell
		outerloop:
		for (; r < cloneGrid.ROWS; r++)
			for (c = 0; c < cloneGrid.COLS; c++)
				if (cloneGrid.gridCells[r][c].isEmptyCell() 
						&& !visitedCells.contains(this.grid.gridCells[r][c]))
					break outerloop;
		int outBountCount = 0;
		try {
		outBountCount = this.getOutBoundAdjacents(cloneGrid.gridCells[r][c]).size();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println();
		}
		// if head pointer is surrounded by colored cells
		if (headPointer == null 
				&& cloneGrid.gridCells[r][c].getColoredAdjs().size()+outBountCount == 4) {
			cloneGrid = previousGrid; // reset instance grid to saved one
			return true;
		}
		
		queue.enqueue(cloneGrid.gridCells[r][c]);
		visitedCells.add(cloneGrid.gridCells[r][c]);
		
		// queue each empty cell and analyze its adjacents cells. 
		while (!queue.isEmpty()) {
			GridCell emptyAdj = queue.dequeue();
			
			// CONSIDER ALL ADJACENTS
			for (GridCell cAdj : emptyAdj.getAllAdjs()) 
			{   // won't analyze visited adjacent
				if (!visitedCells.contains(cAdj)) {
					if (cAdj == cloneGrid.gridCells[0][3])
						System.out.println();
					
					if (cAdj.isEmptyCell()) {
						nRegionEmptyCells--;
						queue.enqueue(cAdj);
						visitedCells.add(cAdj);
					}
					// analyze path of cAdj == initial cell
					else if (cAdj.isInitialPointer()) {
						// 
						if (!cAdj.isFinished && !this.pathToPairExists(cAdj)) {
							cloneGrid = previousGrid; // reset instance grid to saved one
							return true;
						}
						
						// store adj and its pair as visited
						visitedCells.add(cAdj); // may already been added previously
						visitedCells.add(cAdj.pairFlowPointer);
					}
				}
			}
		}
		
		if (nRegionEmptyCells > 0) {
			nRegionEmptyCells = this.grid.nEmptyCells - 1;
			
			// save instant grid and create a clone to analyze
			previousGrid = cloneGrid;
			cloneGrid = cloneGrid.clone();
			cloneGrid.initialFlowPointers = new LinkedList<GridCell>();

			
			// fill the emptyCells 
			for (r = 0; r < cloneGrid.ROWS; r++)
				for (c = 0; c < cloneGrid.COLS; c++) {
					// current instance and clone cell
					GridCell previousCell = previousGrid.gridCells[r][c];
					GridCell cloneCell = previousCell.clone(cloneGrid);
					
					// reference clone grid cell within clone grid
					cloneGrid.gridCells[r][c] = cloneCell;
					
					// reference pair pointers
					if (previousCell.isInitialPointer())
						cloneCell.pairFlowPointer = cloneGrid.gridCells
						[previousCell.pairFlowPointer.pos.row]
								[previousCell.pairFlowPointer.pos.col];
					
					// store clone init pointer to cloned grid
					if (previousGrid.initialFlowPointers.contains(previousCell))
						cloneGrid.initialFlowPointers.add(cloneCell);

					// row index of clone cell, if any
					int rowIndex = visitedCells.indexOf(previousCell);
					
					// if 
					if (rowIndex != -1 && visitedCells.get(rowIndex).isEmptyCell()) {
						cloneCell.color = Grid.NON_CONSTRAINT_COLOR;
						nRegionEmptyCells--;
					}
				}
			
			
			cloneGrid.nEmptyCells = nRegionEmptyCells + 1;
			
			int counter = visitedCells.size();
			// add 
			while (counter > 0) {
				GridCell prevVisited = visitedCells.removeFirst();
				int row = prevVisited.pos.row, col = prevVisited.pos.col;
				visitedCells.add(cloneGrid.gridCells[row][col]);
				counter--;
			}
			if (headPointer != null && headPointer == grid.gridCells[4][2])
				System.out.println();
			
			return this.strandedRegion(cloneGrid, null, null, visitedCells);
		}
		else {
			// resetting the instance grid to the original
//			this.grid = this.gridStack.pop();
			
			// there are no constrain color nor region 
			return false;
		}
	}
	
	
	
	/** Iterates through all possible paths and returns true if there exist 
	 *  a path that leads to the given flow pointer's pair pointer.
     */
	private boolean pathToPairExists(GridCell initPointer) {
		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
		LinkedList<GridCell> visitedCell = new LinkedList<>(); 
		queue.enqueue(initPointer);
		
		if (initPointer.pos.row == 0 && initPointer.pos.col == 4)
			System.out.println();

		while (!queue.isEmpty()) {
			GridCell currEmpty = queue.dequeue();
			
//			if (currEmpty.pos.row == 0 && currEmpty.pos.col == 4)
//				System.out.println();
			
			// consider ALL adjacents
			for (GridCell cAdj : currEmpty.getAllAdjs()) {
				if (currEmpty.isInitialPointer() && cAdj.isConstraintCell())
					return true;
				// if have not visited cAdj
				if (cAdj != null && cAdj != initPointer && !visitedCell.contains(cAdj)) {
					if (initPointer.isPairPointerOf(cAdj)) 
						return true;
					
					// cAdj empty || same color cell
					if (cAdj.isEmptyCell() || cAdj.color == initPointer.color) {
						queue.enqueue(cAdj);
					}
					
					visitedCell.add(cAdj);
				}
			}
		}
		
		return false;
	}
	
	
	
//	/** Iterates through all possible paths and returns true if there exist 
//	 *  a path that leads to the given flow pointer's pair pointer.
//     */
//	private boolean pathToPairExists(GridCell initPointer) {
//		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
//		LinkedList<GridCell> visitedCell = new LinkedList<>(); 
//		queue.enqueue(initPointer);
//		
//		if (initPointer.pos.row == 0 && initPointer.pos.col == 4)
//			System.out.println();
//
//		while (!queue.isEmpty()) {
//			GridCell currEmpty = queue.dequeue();
//			
////			if (currEmpty.pos.row == 0 && currEmpty.pos.col == 4)
////				System.out.println();
//			
//			// consider ALL adjacents
//			for (GridCell cAdj : currEmpty.getAllAdjs()) {
//				// if have not visited cAdj
//				if (cAdj != null && cAdj != initPointer && !visitedCell.contains(cAdj)) {
//					if (initPointer.isPairPointerOf(cAdj)) 
//						return true;
//					
//					// cAdj empty || constraint || same color cell
//					if (cAdj.isEmptyCell() || cAdj.isConstraintCell() || cAdj.color == initPointer.color) {
//						queue.enqueue(cAdj);
//					}
//					
//					visitedCell.add(cAdj);
//				}
//			}
//		}
//		
//		return false;
//	}
	
	

	
	
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
	
	/** Return true if the given adj cells don't contain any
	 *  initial constraint initial pointer.
	 */
	public boolean anyConstraintAdjInitialPointer(java.util.List<GridCell> coloredAdjs) {
		for (GridCell adj : coloredAdjs)
			if (adj.isInitialPointer() && adj.getEmptyAdjs().size() == 1)
				return true;
		return false;
	}
	
//	/** Return true if the given adj cells don't contain any
//	 *  initial constraint initial pointer.
//	 */
//	private LinkedList<GridCell> initialPointer_asAdjOfAdj(java.util.List<GridCell> coloredAdjs) {
//		LinkedList<GridCell> adjInitPointers = new LinkedList<GridCell>();
//		for (GridCell adj : coloredAdjs)
//			if (adj.isInitialPointer())
//				adjInitPointers.add(adj);
//		return adjInitPointers;
//	}
	
	/** Return true if the given adj cells don't contain any
	 *  initial constraint initial pointer.
	 */
	private boolean initialPointer_asAdjOfAdj(java.util.List<GridCell> coloredAdjs) {
		for (GridCell adj : coloredAdjs)
			if (adj.isInitialPointer())
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
	
	
	
	public boolean initPointerAdjConstraint(LinkedList<GridCell> emptyAdjs) {
		for (GridCell emptyAdj : emptyAdjs) {
			// if empty adj has two init pointers, then it isn't constraint
			if (emptyAdj.getEmptyAdjs().size() == 1 
					&& emptyAdj.getInitialPointerAdjs().size() == 1) {
				return true;
			}
		}
		return false;
	}
	
	
	
	public LinkedList<GridCell> cellsToConsiderMovingInto(GridCell currFlowPointer) {
		int currRow = currFlowPointer.pos.row;
		int currCol = currFlowPointer.pos.col;
		LinkedList<GridCell> cellsToConsider = new LinkedList<>();
		GridCell pairPointerFound = currFlowPointer.retrievePairAdjPointer();
		
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
					// moving into empty cell would constraint other initial pointers
					if (currFlowPointer != initPointer && !initPointer.isFinished 
							&& initPointer.getEmptyAdjs().isEmpty()
							&& grid.nEmptyCells != 1) // for when I'm in the last empty cell
					{
						cellsToConsider.add(currFlowPointer.previousCell);
						return cellsToConsider;
					}
				if (currFlowPointer.isInitialPointer() || !currFlowPointer.alreadyMovedTo(emptyAdjacents.getFirst()))
					cellsToConsider.add(emptyAdjacents.removeFirst());
				return cellsToConsider;
			}
		}

		// analyze each adjacent
		for (int[] dir : Grid.DIRECTIONS) {
			// current ADJACENT cell of the current flow pointer
			GridCell cAdj = null;
			if (grid.validPosition(currRow+dir[0], currCol+dir[1]))
				cAdj = grid.gridCells[currRow + dir[0]][currCol + dir[1]];
			
			// if current adjacent is not out of bounds
			if (cAdj != null && !cAdj.isPreviousPointerOf(currFlowPointer)
					&& !cAdj.isInitialPointer() 
					&& !currFlowPointer.alreadyMovedTo(cAdj)) 
			{
				// if current adjacent cell is its pair pointer
				if (cAdj.isPairPointerOf(currFlowPointer))
					pairPointerFound = cAdj;
				
				LinkedList<GridCell> currAdj_coloredAdjs = cAdj.getColoredAdjs();
				// if currAdj constraints an initial pointer which isn't its pair pointer (backtrack)
				if (cAdj.isColoredCell() && cAdj.isInitialPointer() 
						&& pairPointerFound == null && currAdj_coloredAdjs.isEmpty()) {
					cellsToConsider.add(currFlowPointer.previousCell);
					return cellsToConsider;
				}
				
				// if it's an empty cell && it has only one move to consider && 
				// at least one of the other three colored cells isn't an initial pointer.
				else if (!cAdj.isColoredCell()) 
				{
					// counts dummy out bound adjacent cells
					int outBoundAdjs = this.getOutBoundAdjacents(cAdj).size();
					
					if (currAdj_coloredAdjs.size()+outBoundAdjs == 4 && oneAdjIsPairPointerOf(cAdj, currAdj_coloredAdjs)
							|| (currAdj_coloredAdjs.size()+outBoundAdjs == 3) // only 1 move to consider
								&& !initialPointer_asAdjOfAdj(currAdj_coloredAdjs)
								&& outBoundAdjs != 2) 
					{
						cellsToConsider.add(currFlowPointer.previousCell);
						return cellsToConsider;
					}	

//					if (currAdj_coloredAdjs.size()+outBoundAdjs == 4 
//							&& oneAdjIsPairPointerOf(cAdj, currAdj_coloredAdjs)) {
//						cellsToConsider.add(currFlowPointer.previousCell);
//						return cellsToConsider;
//					}
//					if (currAdj_coloredAdjs.size()+outBoundAdjs == 3) { // only 1 move to consider
//						LinkedList<GridCell> adjInitialPointers = 
//								initialPointer_asAdjOfAdj(currAdj_coloredAdjs);
//						
//						// is there are no adj initial pointer, 
//						// and current analyzed adj is not on the edge
//						if (adjInitialPointers.size() == 0 && outBoundAdjs != 2) {
//							cellsToConsider.add(currFlowPointer.previousCell);
//							return cellsToConsider;
//						}
//
//						for (GridCell adjInitPointer : adjInitialPointers)
//							if (adjInitPointer.getEmptyAdjs().size() <= 1)
//								currFlowPointer.rememberIMovedInto(adjInitPointer);
//					}	
					
				}
			}
		}
		
		// if one of the valid colored adjacents was its pair flow pointer
		if (pairPointerFound != null) {
			boolean hasInitPointerEmptyAdjContraint = 
					initPointerAdjConstraint(pairPointerFound.getEmptyAdjs());	
			
			// an empty adj is constraint
			if (hasInitPointerEmptyAdjContraint) {
				cellsToConsider.add(currFlowPointer.previousCell);
				return cellsToConsider;
			}
			
			cellsToConsider.addFirst(pairPointerFound);
			for (GridCell emptyAdj : currFlowPointer.getEmptyAdjs())
				if (!shallContraintsAdjInitialPointer(emptyAdj)
						&& !currFlowPointer.alreadyMovedTo(emptyAdj))
					currFlowPointer.nextAdjCells.add(emptyAdj);
		}
		// at this point consider the empty adjs that don't constraint any initial pointer
		else {
			for (GridCell emptyAdj : currFlowPointer.getEmptyAdjs())
				if (!shallContraintsAdjInitialPointer(emptyAdj) 
						&& !currFlowPointer.alreadyMovedTo(emptyAdj))
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
	
	
	public boolean shallContraintsAdjInitialPointer(GridCell emptyCell) {
		if (emptyCell.isColoredCell())
			throw new IllegalArgumentException("Argument should be an empty cell");
		
		for (GridCell adjInitPointer : emptyCell.getInitialPointerAdjs())
//			if (adjInit.getColoredAdjs().size() >= 3)
			if (!adjInitPointer.isFinished && adjInitPointer.getEmptyAdjs().size() == 1)
				return true;
		return false;
	}
	
	boolean puzzleIsSolved() {
		return grid.nEmptyCells == 0;
	}
	
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












//package app;
//
//import java.awt.Color;
//import java.util.HashSet;
//import java.util.LinkedList;
//
//import interfaces.Entry;
//import queue.SLLQueue;
//import util.Util;
//
//public class Validation 
//{
//	Grid grid;
//	Grid savedInstanceGrid;
//	
//	public Validation(Grid grid) {
//		this.grid = grid;
//		this.savedInstanceGrid = grid;
//	}
////	public boolean isThereStrandedColorOrRegion(GridCell currPointer) {
////		return strandedRegion(currPointer);
////	}
//	
//	public boolean strandedRegion(GridCell headPointer) {
//		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
//		LinkedList<GridCell> visitedCells = new LinkedList<>(); 
//		int nRegionEmptyCells = this.grid.nEmptyCells - 1;
//
//		if (headPointer != null) {
//			// contains either valid edge (if currPointer is at the edge) or null if no valid edge exists
//			GridCell validEdge = emptyEdges_withEqualColoredAdjCells(headPointer);//  NEEDS validity for when has only one colored cell
//	
//			if (validEdge != null) {
//				visitedCells.add(validEdge);
//				nRegionEmptyCells--;
//			}
//			
//			// contains 
//			LinkedList<GridCell> emptyAdjsContainingPairPointer = this.getAdjsContainingPairPointer(
//					headPointer.getEmptyAdjs(), headPointer);
//			
//			for (GridCell emptyAdj : emptyAdjsContainingPairPointer) {
//				if (emptyAdj.getEmptyAdjs().size() == 0 && !visitedCells.contains(emptyAdj)) {
//					visitedCells.add(emptyAdj);
//					nRegionEmptyCells--;
//				}
//			}
//		}
//		
//		int r = 0, c = 0;
//		// iterates until finding first empty cell
//		outerloop:
//		for (; r < grid.ROWS; r++)
//			for (c = 0; c < grid.COLS; c++)
//				if (!this.grid.gridCells[r][c].isColoredCell() 
//						&& !visitedCells.contains(this.grid.gridCells[r][c]))
//					break outerloop;
//		
//		if (grid.gridCells[r][c] == grid.gridCells[0][3])
//			System.out.println();
//		
//		try {
//		queue.enqueue(this.grid.gridCells[r][c]);
//		visitedCells.add(this.grid.gridCells[r][c]);} 
//		catch (ArrayIndexOutOfBoundsException e) {
//			System.out.println();
//		}
//		
//		if (headPointer == grid.gridCells[2][0])
//			System.out.println();
//		
//		// queue each empty cell and analyze its adjacents cells. 
//		while (!queue.isEmpty()) {
//			GridCell emptyAdj = queue.dequeue();
//			
//			// CONSIDER ALL ADJACENTS
//			for (GridCell cAdj : emptyAdj.getAllAdjs()) 
//			{   // won't analyze visited adjacent
//				if (!visitedCells.contains(cAdj)) {
//					if (cAdj == grid.gridCells[0][3])
//						System.out.println();try {
//					
//					if (cAdj.isEmptyCell()) {
//						nRegionEmptyCells--;
//						queue.enqueue(cAdj);
//						visitedCells.add(cAdj);
//					}
//					// analyze path of cAdj == initial cell
////					else if (cAdj.isPairPointerOf(grid.gridCells[cAdj.pos.row][cAdj.pos.col])) {
//					else if (cAdj.isInitialFlowPointer()) {  //this COMPLEtES THE 7X7, not 5X5
////							&& !cAdj.isPairPointerOf(grid.gridCells[cAdj.pos.row][currAdj.pos.col])) {
////						if (cAdj == grid.gridCells[4][5])
////							System.out.println();
//						
//						if (!cAdj.isFinished && !this.pathToPairExists(cAdj)) {
//							this.grid = this.savedInstanceGrid; // reset instance grid to saved one
//							return true;
//						}
//						
//						// store adj and its pair as visited
//						visitedCells.add(cAdj); // may already been added previously
//						visitedCells.add(cAdj.pairFlowPointer);
//					}
//					} catch (NullPointerException e) {
//						System.out.println();
//					}
//				}
//			}
//		}
//		
//		if (nRegionEmptyCells > 0) {
//			// -------------------------------------------------------------------------------
//			// save instant grid and create a clone to analyze
//			this.savedInstanceGrid = grid;
//			this.grid = grid.clone();
//			this.grid.initialFlowPointers = new LinkedList<GridCell>();
//
//			// fill the emptyCells 
//			for (r = 0; r < grid.ROWS; r++)
//				for (c = 0; c < grid.COLS; c++) {
//					// current instance and clone cell
//					GridCell savedInstanceCell = savedInstanceGrid.gridCells[r][c];
//					GridCell cloneCell = savedInstanceCell.clone(grid);
//					
//					// reference clone grid cell within clone grid
//					grid.gridCells[r][c] = cloneCell;
//					
//					// store clone init pointer to cloned grid
//					if (savedInstanceGrid.initialFlowPointers.contains(savedInstanceCell))
//						this.grid.initialFlowPointers.add(savedInstanceCell);
//					
//					// row index of clone cell, if any
//					int rowIndex = visitedCells.indexOf(cloneCell);
//					
//					// if 
//					if (rowIndex != -1 && visitedCells.get(rowIndex).isEmptyCell())
//						cloneCell.color = Grid.NON_CONSTRAINT_COLOR;
//				}
//			
////			if (headPointer == savedInstanceGrid.gridCells[4][6])
////				System.out.println();
//			
//			this.grid.nEmptyCells = nRegionEmptyCells + 1;
//			
//			
//			return this.strandedRegion(null);
//		}
//		else {
//			// resetting the instance grid to the original
//			this.grid = this.savedInstanceGrid;
//			return nRegionEmptyCells > 0;
//		}
//	}
//	
//	/** Iterates through all possible paths and returns true if there exist 
//	 *  a path that leads to the given flow pointer's pair pointer.
//     */
//	private boolean pathToPairExists(GridCell initPointer) {
//		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
//		LinkedList<GridCell> visitedCell = new LinkedList<>(); 
//		queue.enqueue(initPointer);
//		int count = 0;
//		
//		while (!queue.isEmpty()) {
//			GridCell currEmpty = queue.dequeue();
//			
//			// consider ALL adjacents
//			for (GridCell cAdj : currEmpty.getAllAdjs()) {
//				// if have not visited cAdj
//				if (cAdj != null && cAdj != initPointer && !visitedCell.contains(cAdj)) {
//					if (initPointer.isPairPointerOf(cAdj)) 
//						return true;
//					
//					// queue empty || constraint || same color cell
//					if (cAdj.isEmptyCell() || cAdj.isConstraintCell() || cAdj.color == initPointer.color) {
//						queue.enqueue(cAdj);
//						count++;
//					}
//					visitedCell.add(cAdj);
//				}
//			}
//		}
//		
//		return false;
//	}
//	
//	
//
//	
//	
//	private boolean validEdgeToMoveInto(GridCell edge) {
//		LinkedList<GridCell> coloredAdjs = edge.getColoredAdjs();
//		if (coloredAdjs.size() == 2)
//			return coloredAdjs.getFirst().color == coloredAdjs.getLast().color;
//		return false;
//	}
//	
//	
//	private GridCell emptyEdges_withEqualColoredAdjCells(GridCell currPointer) {
//		LinkedList<GridCell> emptyAdjs = currPointer.getEmptyAdjs();
//		if (emptyAdjs.size() <= 2) {
//			for (GridCell emptyAdj : emptyAdjs)
//				if (this.grid.edges.contains(emptyAdj) && validEdgeToMoveInto(emptyAdj))
//					return emptyAdj;
//		}
//		return null;
//	}
//	
//	
////	private GridCell getAdjContainingPairPointer(LinkedList<GridCell> emptyAdjs) {
////		for (GridCell emptyAdj : emptyAdjs)
////			if (emptyAdj.retrievePairAdjPointer() != null) // emptyAdj has pariPointer
////				return emptyAdj;
////		return null;
//	
//	/** Return true if the given adj cells contains at least one initialPointer.
//	 */
//	private boolean hasOtherInitialPointerAsAdjs(LinkedList<GridCell> adjs, GridCell currPointer) {
//		for (GridCell adj : adjs)
//			if (adj.isInitialFlowPointer())
////					&& cell.color != currPointer.color && cell == currPointer.pairFlowPointer)
//				return true;
//		return false;
//	}
//	
//	private LinkedList<GridCell> getAdjsContainingPairPointer(LinkedList<GridCell> emptyAdjs, GridCell currPointer) {
//		LinkedList<GridCell> adjsContainingPair = new LinkedList<GridCell>();
//		for (GridCell emptyAdj : emptyAdjs) {
//			emptyAdj.pairFlowPointer = currPointer.pairFlowPointer; // simulating he moved to that position
//			if (emptyAdj.hasPairPointer())
//				adjsContainingPair.add(emptyAdj);
//			emptyAdj.pairFlowPointer = null;
//		}
//		return adjsContainingPair;
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	public LinkedList<GridCell> cellsToConsiderMovingInto(GridCell currFlowPointer) {
//		int currRow = currFlowPointer.pos.row;
//		int currCol = currFlowPointer.pos.col;
//		LinkedList<GridCell> cellsToConsider = new LinkedList<>();
//		GridCell pairPointerFound = currFlowPointer.retrievePairAdjPointer();
//		
//		if (currFlowPointer == grid.gridCells[2][1])
//			System.out.println("for debugging purposes");
//		
////		if (pairPointerFound != null) {
////			cellsToConsider.add(pairPointerFound);
////			return cellsToConsider;
//				
//		// don't go within this if statement if we've reached the pair pointer
//		if (pairPointerFound == null) {
//			LinkedList<GridCell> emptyAdjacents = currFlowPointer.getEmptyAdjs();
//
//			// if no empty adjacent (backtrack)
//			if (emptyAdjacents.isEmpty()) {
//				cellsToConsider.add(currFlowPointer.previousCell);
//				return cellsToConsider;
//			}
//
//			// if there is only one move to consider (force move)
//			if (emptyAdjacents.size() == 1) {
//				
//				for (GridCell initPointer : emptyAdjacents.getFirst().getInitialPointerAdjs())
//					// backtrack if initial pointer isn't the previous cell && 
//					// moving into empty cell would other initial pointers
//					if (currFlowPointer != initPointer && !initPointer.isFinished 
//							&& initPointer.getEmptyAdjs().size() == 1
//							&& grid.nEmptyCells != 1) 
//					{
//						cellsToConsider.add(currFlowPointer.previousCell);
//						return cellsToConsider;
//					}
//
//				cellsToConsider.add(emptyAdjacents.removeFirst());
//				return cellsToConsider;
//			}
//		}
//
//		// analyze each adjacent
//		for (int[] dir : Grid.DIRECTIONS) {
//			// current ADJACENT cell of the current flow pointer
//			GridCell currAdj = null;
//			if (grid.validPosition(currRow+dir[0], currCol+dir[1]))
//				currAdj = grid.gridCells[currRow + dir[0]][currCol + dir[1]];
//			
//			// if current adjacent is not out of bounds
//			if (currAdj != null && !currAdj.isPreviousPointerOf(currFlowPointer)) {
//				// if current adjacent cell is its pair pointer
//				if (currAdj.isPairPointerOf(currFlowPointer))
//					pairPointerFound = currAdj;
//				
//				LinkedList<GridCell> currAdj_coloredAdjs = currAdj.getColoredAdjs();
//				// if currAdj constraints an initial pointer which isn't its pair pointer (backtrack)
//				if (currAdj.isColoredCell() && currAdj.isInitialFlowPointer() 
//						&& pairPointerFound == null && currAdj_coloredAdjs.isEmpty()) {
//					cellsToConsider.add(currFlowPointer.previousCell);
//					return cellsToConsider;
//				}
//				
//				// if it's an empty cell && it has only one move to consider && 
//				// at least one of the other three colored cells isn't an initial pointer.
//				else if (!currAdj.isColoredCell()) 
//				{
//					// include dummy out bound adjacent cells
//					int outBoundAdjs = this.getOutBoundAdjacents(currAdj).size();
////					currAdj_coloredAdjs.addAll(this.getOutBoundAdjacents(currAdj));
//					if (currAdj_coloredAdjs.size()+outBoundAdjs == 4 && oneAdjIsPairPointerOf(currAdj, currAdj_coloredAdjs)
//							|| (currAdj_coloredAdjs.size()+outBoundAdjs == 3 
//								&& !this.hasOtherInitialPointerAsAdjs(currAdj_coloredAdjs, currFlowPointer))) 
//					{
//						cellsToConsider.add(currFlowPointer.previousCell);
//						return cellsToConsider;
//					}
//				}
//			}
//		}
//		
//		// if one of the valid colored adjacents was its pair flow pointer
//		if (pairPointerFound != null) {
//			cellsToConsider.addFirst(pairPointerFound);
//			for (GridCell emptyAdj : currFlowPointer.getEmptyAdjs())
//				if (!shallContraintsAdjInitialPointer(emptyAdj))
//					currFlowPointer.nextAdjCells.add(emptyAdj);
//		}
//		// at this point consider the empty adjs that don't constraint any initial pointer
//		else {
//			for (GridCell emptyAdj : currFlowPointer.getEmptyAdjs())
//				if (!shallContraintsAdjInitialPointer(emptyAdj))
//					cellsToConsider.add(emptyAdj);
//		}
//		
//		return cellsToConsider;
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	public boolean strandedRegion2(GridCell currPointer) {		
//		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
//		LinkedList<GridCell> visitedCells = new LinkedList<>(); 
//		int nRegionsEmptyCells = grid.nEmptyCells - 1;
//		
//		// contains either valid edge (if currPointer is at the edge) or null if no valid edge exists
//		GridCell validEdge = emptyEdges_withEqualColoredAdjCells(currPointer);
//
//		if (validEdge != null) {
//			visitedCells.add(validEdge);
//			nRegionsEmptyCells--;
//		}
//		
//		// contains 
//		LinkedList<GridCell> emptyAdjsContainingPairPointer = this.getAdjsContainingPairPointer(
//				currPointer.getEmptyAdjs(), currPointer);
//		
//		for (GridCell emptyAdj : emptyAdjsContainingPairPointer) {
//			if (emptyAdj.getEmptyAdjs().size() == 0 && !visitedCells.contains(emptyAdj)) {
//				visitedCells.add(emptyAdj);
//				nRegionsEmptyCells--;
//			}
//		}
//		
//		int r = 0, c = 0;
//		// iterates until finding first empty cell
//		outerloop:
//		for (; r < grid.ROWS; r++)
//			for (c = 0; c < grid.COLS; c++)
//				if (!this.grid.gridCells[r][c].isColoredCell() 
//						&& !visitedCells.contains(this.grid.gridCells[r][c]))
//					break outerloop;
//		
//		queue.enqueue(this.grid.gridCells[r][c]);
//		visitedCells.add(this.grid.gridCells[r][c]);
//		
//		while (!queue.isEmpty()) {
//			GridCell currEmptyCell = queue.dequeue();
//			for (GridCell adjCell : currEmptyCell.getEmptyAdjs()) {
//				// don't queue the already visited empty cells
//				if (!visitedCells.contains(adjCell)) {
//					nRegionsEmptyCells--;
//					queue.enqueue(adjCell);
//					visitedCells.add(adjCell);
//				}
//			}
//		}
//		
//		return nRegionsEmptyCells != 0;
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	private boolean oneAdjIsPairPointerOf(GridCell currAdj, LinkedList<GridCell> cells) {
//		for (GridCell cell : cells) {
//			if (cell.isPairPointerOf(currAdj))
//				return true;
//		}
//		return false;
//	}
//	
//	/** method used only within the validation  */
//	private LinkedList<GridCell> getOutBoundAdjacents(GridCell cell) {
//		LinkedList<GridCell> outBoundAdjs = new LinkedList<>();
//		
//		for (int[] dir : Grid.DIRECTIONS)
//			if (!grid.validPosition(cell.pos.row + dir[0], cell.pos.col + dir[1]))
//				outBoundAdjs.add(new GridCell(null, 
//						new Pos(cell.pos.row + dir[0], cell.pos.col + dir[1]), Grid.EMPTY_COLOR));
//		return outBoundAdjs;
//	}
//	
//	
//	public boolean shallContraintsAdjInitialPointer(GridCell emptyCell) {
//		if (emptyCell.isColoredCell())
//			throw new IllegalArgumentException("Argument should be an empty cell");
//		
//		for (GridCell adjInit : emptyCell.getInitialPointerAdjs())
//			if (adjInit.getColoredAdjs().size() >= 3)
//				return true;
//		return false;
//	}
//	
//	boolean puzzleIsSolved() {
//		return grid.nEmptyCells == 0;
//	}
//	
//	public String toString() {
//		return grid.toString();
//	}
//}
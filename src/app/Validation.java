package app;

import java.util.HashSet;
import java.util.LinkedList;

import queue.SLLQueue;

public class Validation 
{
	Grid grid;
	
	public Validation(Grid grid) {
		this.grid = grid;
	}
	
	boolean puzzleIsSolved() { return grid.nEmptyCells == 0; }

	/** Returns true if there is either a stranded color or stranded region. */
	public boolean isThereStrandedColorOrRegion(GridCell currPointer) {
		return strandedColorOrRegion(grid, null, currPointer, new LinkedList<>());
	}

	public boolean strandedColorOrRegion(Grid cloneGrid, Grid previousGrid, 
			GridCell headPointer, LinkedList<GridCell> visitedCells) 
	{
		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
		int nRegionEmptyCells = cloneGrid.nEmptyCells - 1;
		
		if (headPointer != null) {
			// contains either valid edge (if currPointer is at the edge) or null if no valid edge exists
			GridCell validConstraintEdge = emptyEdges_withEqualColoredAdjCells(headPointer);// MAYBE needs validity for when has only one colored cell
	
			if (validConstraintEdge != null) {
				visitedCells.add(validConstraintEdge);
				nRegionEmptyCells--;
			}
			
			
			LinkedList<GridCell> emptyAdjsContainingPairPointer = this.getAdjsContainingPairPointer(
					headPointer.getEmptyAdjs(), headPointer);
			
			for (GridCell emptyAdj : emptyAdjsContainingPairPointer) {
				if (emptyAdj.getEmptyAdjs().size() == 0 
						&& !visitedCells.contains(emptyAdj) && grid.nEmptyCells != 1) {
					visitedCells.add(emptyAdj);
					nRegionEmptyCells--;
				}
			}
		}

		// iterates until finding first empty cell
		int r = 0, c = 0;
		outerloop:
		for (; r < cloneGrid.ROWS; r++)
			for (c = 0; c < cloneGrid.COLS; c++)
				if (cloneGrid.gridCells[r][c].isEmptyCell() 
						&& !visitedCells.contains(this.grid.gridCells[r][c]))
					break outerloop;
		
		// colored get adjacents and count of out bound adjacents
		LinkedList<GridCell> coloredAdjs =  cloneGrid.gridCells[r][c].getColoredAdjs();
		int outBountCount  = this.getOutBoundAdjacents(cloneGrid.gridCells[r][c]).size();
		
		// if head pointer is surrounded by colored cells
		if (headPointer == null 
				&& coloredAdjs.size()+outBountCount == 4
				&& !this.hasAdjMatchingPair(coloredAdjs)) {
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
			{   
				
				// won't analyze visited adjacent
				if (!visitedCells.contains(cAdj)) {					
					if (cAdj.isEmptyCell()) {
						nRegionEmptyCells--;
						queue.enqueue(cAdj);
						visitedCells.add(cAdj);
					}
					// analyze path of cAdj == initial cell, only if it hasn't been analyzed
					else if (cAdj.isInitialPointer() && !cAdj.hasConstraintAdj()) {
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
			nRegionEmptyCells = cloneGrid.nEmptyCells;
			
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

			for (GridCell prevInitialPointer : previousGrid.initialFlowPointers) {
				int row = prevInitialPointer.pos.row;
				int col = prevInitialPointer.pos.col;
				int pairRow = prevInitialPointer.pairFlowPointer.pos.row;
				int pairCol = prevInitialPointer.pairFlowPointer.pos.col;
				
				cloneGrid.gridCells[row][col].pairFlowPointer = 
						cloneGrid.gridCells[pairRow][pairCol];		
			}
			
			
			cloneGrid.nEmptyCells = nRegionEmptyCells;
			
			int counter = visitedCells.size();
			while (counter > 0) {
				GridCell prevVisited = visitedCells.removeFirst();
				int row = prevVisited.pos.row, col = prevVisited.pos.col;
				visitedCells.add(cloneGrid.gridCells[row][col]);
				counter--;
			}
			if (headPointer != null && headPointer == grid.gridCells[4][2])
				System.out.println();
			
			return this.strandedColorOrRegion(cloneGrid, null, null, visitedCells);
		}
		// there are no constrain color nor region 
		else return false;
	}
	
	
	/** Returns true if the given cell has a pair of pointer which are their own pair. */
	public boolean hasAdjMatchingPair(LinkedList<GridCell> coloredAdjs) {
		HashSet<GridCell> set = new HashSet<>();
		for (GridCell coloredAdj : coloredAdjs) {
			if (set.contains(coloredAdj.pairFlowPointer))
				return true;
			set.add(coloredAdj);
		}
		return false;
	}
	
	/** Iterates through all possible paths and returns true if there exist 
	 *  a path that leads to the given flow pointer's pair pointer.
     */
	private boolean pathToPairExists(GridCell initPointer) {
		SLLQueue<GridCell> queue = new SLLQueue<GridCell>();
		LinkedList<GridCell> visitedCell = new LinkedList<>(); 
		queue.enqueue(initPointer);

		while (!queue.isEmpty()) {
			GridCell currEmpty = queue.dequeue();
			
			// consider ALL adjacents
			for (GridCell cAdj : currEmpty.getAllAdjs()) {
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
	
	
	private boolean validEdgeToMoveInto(GridCell edge) {
		LinkedList<GridCell> coloredAdjs = edge.getColoredAdjs();
		if (coloredAdjs.size() == 2) {
			// valid if both adjacents are initial pair pointers
			GridCell edge1 = coloredAdjs.getFirst(), edge2 = coloredAdjs.getLast();
			return edge1.isInitialPointer() && edge2.isInitialPointer()
					&& edge1.color == edge2.color;
		}
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
	
	
	/** Return true if the given adj cells don't contain any
	 *  initial constraint initial pointer.
	 */
	public boolean anyConstraintAdjInitialPointer(java.util.List<GridCell> coloredAdjs) {
		for (GridCell adj : coloredAdjs)
			if (adj.isInitialPointer() && adj.getEmptyAdjs().size() == 1)
				return true;
		return false;
	}
	
	
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
		for (GridCell emptyAdj : emptyAdjs)
			// if empty adj has two init pointers, then it isn't constraint
			if (emptyAdj.getEmptyAdjs().size() == 1 
					&& emptyAdj.getInitialPointerAdjs().size() == 1)
				return true;
			
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
					// counts amount of out of bound adjacent cells
					int outBoundAdjs = this.getOutBoundAdjacents(cAdj).size();
					
					if (currAdj_coloredAdjs.size()+outBoundAdjs == 4 && oneAdjIsPairPointerOf(cAdj, currAdj_coloredAdjs)
							|| (currAdj_coloredAdjs.size()+outBoundAdjs == 3) // only 1 move to consider
								&& !initialPointer_asAdjOfAdj(currAdj_coloredAdjs)
								&& outBoundAdjs != 2) 
					{
						cellsToConsider.add(currFlowPointer.previousCell);
						return cellsToConsider;
					}
				}
			}
		}
		
		// if one of the valid colored adjacents is its pair flow pointer
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
				if (!shallContraintsAdjInitialPointer(emptyAdj, currFlowPointer.pairFlowPointer)
						&& !currFlowPointer.alreadyMovedTo(emptyAdj))
					currFlowPointer.nextAdjCells.add(emptyAdj);
		}
		// at this point consider the empty adjs that don't constraint any initial pointer
		else {
			for (GridCell emptyAdj : currFlowPointer.getEmptyAdjs())
				if (!shallContraintsAdjInitialPointer(emptyAdj, currFlowPointer.pairFlowPointer)  
						&& !currFlowPointer.alreadyMovedTo(emptyAdj))
					cellsToConsider.add(emptyAdj);
		}
		
		return cellsToConsider;
	}
	
	
	
	
	
	
	
	
	private boolean oneAdjIsPairPointerOf(GridCell currAdj, LinkedList<GridCell> cells) {
		for (GridCell cell : cells)
			if (cell.isPairPointerOf(currAdj))
				return true;
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
	
	
	public boolean shallContraintsAdjInitialPointer(GridCell emptyCell, GridCell goalPointer) {
		if (emptyCell.isColoredCell())
			throw new IllegalArgumentException("Argument should be an empty cell");
		
		for (GridCell adjInitPointer : emptyCell.getInitialPointerAdjs())
			if (!adjInitPointer.isFinished && adjInitPointer.getEmptyAdjs().size() == 1
					&& adjInitPointer != goalPointer)
				return true;
		return false;
	}
		
	public String toString() { return grid.toString();}
}
package appClasses;

import java.util.LinkedList;
import java.util.Stack;

import main.App;

/**  */
public class Solver
{
	Grid grid;
	Validation validation;

	public Solver(Grid grid) {
		this.grid = grid;
		this.validation = new Validation(grid);
	}
	
	Stack<LinkedList<GridCell>> finishedPaths = new Stack<>();
	
	
	/** 
	 *  Runs the agent's process of finding each initial flow pointer's path.
	 *  
	 */
	public void solve() {
		GridCell currFlowPointer = this.grid.pq.remove();

		GridCell goalPairPointer = currFlowPointer.pairFlowPointer;

		while (!validation.puzzleIsSolved()) {
			/* 
			   If 'nextAdjCells' is empty, it means that we have backtracked and
			   we are now considering the valid cells that we haven't moved into yet.
			   'cellsToConsider' may contain the force-move cell, 
			   the previous cell, or the various cells to move into.
			 */
			LinkedList<GridCell> cellsToConsider = (currFlowPointer.nextAdjCells.isEmpty())
					? validation.cellsToConsiderMovingInto(currFlowPointer)
							: currFlowPointer.nextAdjCells;

			// paint current flow pointer
			currFlowPointer.color = goalPairPointer.color;

			// dead-end
			// if no cells to consider (backtrack)
			if (cellsToConsider.isEmpty())
				currFlowPointer = this.backtrackToPrevious(currFlowPointer);

			// if only one move to consider
			else if (cellsToConsider.size() == 1) {
				GridCell cellToMoveInto = cellsToConsider.getFirst();
				
				// constraint: if current Flow Pointer is at an invalid position (backtrack)
				if (cellToMoveInto.isPreviousPointerOf(currFlowPointer))
					currFlowPointer = this.backtrackToPrevious(currFlowPointer);

				// if arrived at goal pair pointer
				else if (cellToMoveInto.isPairPointerOf(currFlowPointer)) {
					if (validation.isThereStrandedColorOrRegion(currFlowPointer))
						currFlowPointer = this.backtrackToPrevious(currFlowPointer);
					else {
						System.out.println("arrived at goal");
						
						// sets previousPointer property of goal pointer to reference currFlowPoiter
						currFlowPointer.pairFlowPointer.previousCell = currFlowPointer;
						
						// pushes the list of pointers leading to goal pointer (path) to the stack
						this.storePath(currFlowPointer.pairFlowPointer);
						
						// now, let's find a path for the next initial flow pointer
						// we equal this to go within the final if statement
						goalPairPointer = currFlowPointer.pairFlowPointer;
					}
				}
				// only one valid move to consider moving into (force move)
				else {		
					if (currFlowPointer.isInitialPointer() 
							|| !currFlowPointer.alreadyMovedTo(cellsToConsider.getFirst())) 
					{
						if (validation.isThereStrandedColorOrRegion(currFlowPointer))
							currFlowPointer = this.backtrackToPrevious(currFlowPointer);
						else {
							// don't consider the first move to be a force move, cause then 
							// it would cause error when backtracking to the initial pointer
							if (currFlowPointer.pairFlowPointer != goalPairPointer)
//								if (!currFlowPointer.isInitialFlowPointer())
								currFlowPointer.hasForcedMove = true;
							
							// move to next cell
							currFlowPointer = this.moveTowardsCell(currFlowPointer, 
									cellsToConsider.removeFirst());
						}
					}
				}
			}
			// validate for stranded colors or regions (backtrack)
			else if (validation.isThereStrandedColorOrRegion(currFlowPointer))
				currFlowPointer = this.backtrackToPrevious(currFlowPointer);
			
			// If 'nextAdjCells' property is empty, then add the cells to consider
			else {
				// if it's first time considering next adjacents
				if (currFlowPointer.nextAdjCells.isEmpty())
					for (GridCell nextCell : cellsToConsider)
						if (!currFlowPointer.alreadyMovedTo(nextCell))
							currFlowPointer.nextAdjCells.add(nextCell);
				
				// move into next cell
				currFlowPointer = this.moveTowardsCell(currFlowPointer, 
						currFlowPointer.nextAdjCells.removeFirst());
			}
			
			// this happens when we've backtrack to initial flow pointer
			// and don't have any adjacents to consider
			if (currFlowPointer == null)
				currFlowPointer = goalPairPointer.pairFlowPointer;
			
			// if we found a path.
			if (currFlowPointer.isFinished) {
				currFlowPointer = this.grid.pq.remove();
				goalPairPointer = currFlowPointer.pairFlowPointer;
			}
			// if arrived to initial pointer after backtracking (no path found)
			else if (currFlowPointer == goalPairPointer.pairFlowPointer) {
				// if there are more adjacents to consider
				if (!currFlowPointer.nextAdjCells.isEmpty()) {
					// don't remove adjacent cells of the pointer initial flow pointer
					if (currFlowPointer != goalPairPointer.pairFlowPointer)
						currFlowPointer = currFlowPointer.nextAdjCells.removeFirst();
					currFlowPointer.pairFlowPointer = goalPairPointer;
				}
				// restore the previous finished path and find another path
				else {
					this.grid.pq.add(currFlowPointer);
					
					// restore finished path
					if (!this.finishedPaths.isEmpty())
						currFlowPointer = this.restorePrevFinishedPath();
					
					// 
					if (!currFlowPointer.nextAdjCells.isEmpty())
						currFlowPointer.nextAdjCells.removeFirst();
					else 
						currFlowPointer = this.backtrackToPrevious(currFlowPointer);
					
						try {
					// 
					goalPairPointer = currFlowPointer.pairFlowPointer;
						} catch (NullPointerException e) {
							System.out.println("NO SOLUTION WAS FOUND");
						}
				}
			}
		}		
	}
	
	/** Stores the path ending with the given flow pointer all the way to 
	 *  the beginning of flow the given pointer has stepped to.
	 */
	public void storePath(GridCell flowPointer) {
		GridCell currFlowPointer = flowPointer;
		currFlowPointer.isFinished = true;
		LinkedList<GridCell> path = new LinkedList<>();
		
		while (!currFlowPointer.isPairPointerOf(flowPointer)) {
			path.addFirst(currFlowPointer);
			
			currFlowPointer = (currFlowPointer.previousCell != null)
					? currFlowPointer.previousCell : flowPointer.pairFlowPointer;

			currFlowPointer.isFinished = true;
		}
		path.addFirst(currFlowPointer);
		finishedPaths.push(path);
	}
	
	/** Restores previous finished path and returns the starting pointer of the path */
	public GridCell restorePrevFinishedPath() {
		LinkedList<GridCell> path = this.finishedPaths.lastElement();
		
		// sets every cell of the path to not be finished
		for (GridCell cell : path)
			cell.isFinished = false;
		
		// get the previous to last grid cell within path
		GridCell prevLastGridCell = path.get(path.size()-2);
		
		// remove path from list
		this.finishedPaths.remove(finishedPaths.remove(finishedPaths.size()-1));
		
		return backtrackToPrevious(prevLastGridCell);
	}

	/** Moves from <i>currFlowPointer</i> to  <i>nextToMoveInto</i>, and connects 
	 *  the properties of <i>currFlowPointer</i> and <i>nextToMoveInto</i>. 
	 */
	public GridCell moveTowardsCell(GridCell currFlowPointer, GridCell nextToMoveInto) {
		// remember that we have moved to this cell
		if (!currFlowPointer.isInitialPointer())
			currFlowPointer.rememberIMovedInto(nextToMoveInto);
		
		// move towards next cell
		// set next to move to cell the same color as current cell to follow color flow
		nextToMoveInto.color = currFlowPointer.color;
		
		// set the next cell's parent as current cell to save reference
		nextToMoveInto.previousCell = currFlowPointer;

		// set pair flow pointer same as current cell to know which cell it needs to get to
		nextToMoveInto.pairFlowPointer = currFlowPointer.pairFlowPointer;
		
		// change cell's color in panel
		App.gridPanel[nextToMoveInto.pos.row][nextToMoveInto.pos.col]
				.setBackground(nextToMoveInto.color);
		
		// decrease number of empty cells
		this.grid.nEmptyCells--;
		
		return nextToMoveInto;
	}
	
	
	/** Backtracks to the previous non-forced moved cell, resetting each backtracked cell.
	 *  @return the last non forced flow pointer's previous to the given flow pointer.  */
	public GridCell backtrackToPrevious(GridCell flowPointer) {
		// un_reference flow pointer to the adjacent cells
		for (GridCell emptyAdj : flowPointer.getEmptyAdjs())
			emptyAdj.removeToNotRemember(flowPointer);

		flowPointer.nextAdjCells = new LinkedList<>();
		GridCell currCell = flowPointer;
		GridCell prevCell = flowPointer.previousCell;
		do {
			currCell.color = Grid.EMPTY_COLOR;
			currCell.isFinished = false;
			currCell.pairFlowPointer = null;
			currCell.hasForcedMove = false;
			App.gridPanel[currCell.pos.row][currCell.pos.col]
					.setBackground(Grid.EMPTY_COLOR);
			
			prevCell = currCell.previousCell;
			currCell.previousCell = null;
			currCell = prevCell;
			this.grid.nEmptyCells++;
		}
		while (currCell != null && currCell.hasForcedMove);

		if (prevCell != null)
			prevCell.isFinished = false;
		return prevCell;
	}

	public String toString() {
		return grid.toString();
	}
}
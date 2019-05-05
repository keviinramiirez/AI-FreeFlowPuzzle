package app;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.JPanel;


public class Solver
{
	Grid grid;
	JPanel[][] gridPanel;
	Validation validation;

	public Solver(Grid grid, JPanel[][] gridPanel) {
		this.grid = grid;
		this.validation = new Validation(grid);
		this.gridPanel = gridPanel;
	}
	
	Stack<LinkedList<GridCell>> finishedPaths = new Stack<>();
	boolean backtrackedToInitial = false;

	public void solve() {
		GridCell currFlowPointer = this.grid.pq.removeMin().getValue();

		GridCell initialFlowPointer = currFlowPointer;

		while (!validation.puzzleIsSolved()) {			
			
			if (currFlowPointer == grid.gridCells[2][0])
				System.out.println();
			/* 
			   If 'nextAdjCells' is empty, it means that we have backtracked and
			   we are now considering the valid cells that we haven't moved into yet.
			   'cellsToConsider' may contain the force-move cell, 
			   the previous cell, or the various cells to move into.
			 */
			LinkedList<GridCell> cellsToConsider = (currFlowPointer.nextAdjCells.isEmpty())
					? validation.cellsToConsiderMovingInto(currFlowPointer)
							: currFlowPointer.nextAdjCells;
			
			// dead-end
			// if no cells to consider (backtrack)
			if (cellsToConsider.isEmpty())
				currFlowPointer = this.backtrackToPrevious(currFlowPointer);

			if (cellsToConsider.size() == 1) {
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
						initialFlowPointer = currFlowPointer;
					}
				}
				// only one valid move to consider moving into (force move)
				else {
					currFlowPointer.hasForcedMove = true;
					currFlowPointer = this.moveTowardsCell(currFlowPointer, 
							cellsToConsider.removeFirst());
				}
			}
			
			// validate for stranded colors or regions (backtrack)
			else if (validation.isThereStrandedColorOrRegion(currFlowPointer))
				currFlowPointer = this.backtrackToPrevious(currFlowPointer);
			
			// If 'nextAdjCells' property is empty, then add the cells to consider
			else {
				if (currFlowPointer.nextAdjCells.isEmpty())
					for (GridCell nextCell : cellsToConsider)
						currFlowPointer.nextAdjCells.add(nextCell);

				currFlowPointer = this.moveTowardsCell(currFlowPointer, 
						currFlowPointer.nextAdjCells.removeFirst());
				
//				this.updatePQ(currFlowPointer);
				
//				currFlowPointer = this.grid.pq.min().getValue();
			}
			
			
			// is true, it means we've arrived to initial pointer after 
			// backtracking (no path found) or we found a path.
			if (currFlowPointer == initialFlowPointer) {
				System.out.println("have backtracked to initial Pointer");
				if (currFlowPointer.nextAdjCells.isEmpty()) {
					currFlowPointer = this.grid.pq.removeMin().getValue();
					initialFlowPointer = currFlowPointer;
				}
				else {
					currFlowPointer = this.moveTowardsCell(currFlowPointer, 
							currFlowPointer.nextAdjCells.removeFirst());
				}
			}
		}		
	}
	
	public void storePath(GridCell flowPointer) {
		GridCell currFlowPointer = flowPointer;
		currFlowPointer.isFinished = true;
		LinkedList<GridCell> path = new LinkedList<>();
		
		while (!currFlowPointer.isPairPointerOf(flowPointer)) {
			path.addFirst(currFlowPointer);
			currFlowPointer.nextAdjCells = new LinkedList<>();
			currFlowPointer = currFlowPointer.previousCell;
			currFlowPointer.isFinished = true;
		}
		path.addFirst(currFlowPointer);
		finishedPaths.push(path);
	}

	/** Moves from <i>currFlowPointer</i> to  <i>nextToMoveInto</i>, and connects 
	 *  the properties of <i>currFlowPointer</i> and <i>nextToMoveInto</i>. 
	 */
	public GridCell moveTowardsCell(GridCell currFlowPointer, GridCell nextToMoveInto) {
		// move towards next cell
		// set next to move to cell the same color as current cell to follow color flow
		nextToMoveInto.color = currFlowPointer.color;
		
		// set the next cell's parent as current cell to save reference
		nextToMoveInto.previousCell = currFlowPointer;

		// set pair flow pointer same as current cell to know which cell it needs to get to
		nextToMoveInto.pairFlowPointer = currFlowPointer.pairFlowPointer;
		
		// change cell's color in panel
		this.gridPanel[nextToMoveInto.pos.row][nextToMoveInto.pos.col]
				.setBackground(nextToMoveInto.color);
		
		// decrease number of empty cells
		this.grid.nEmptyCells--;
		
		return nextToMoveInto;
	}
	
	
	/** Backtracks to the previous non force-moved cell, resetting each backtracked cell.
	 *  @return the given flow pointer's previous cell */
	public GridCell backtrackToPrevious(GridCell flowPointer) {
		flowPointer.nextAdjCells = new LinkedList<>();
		GridCell currCell = flowPointer;
		GridCell prevCell = flowPointer.previousCell;
		do {
			currCell.color = Grid.EMPTY_COLOR;
			currCell.pairFlowPointer = null;
			currCell.hasForcedMove = false;
			this.gridPanel[currCell.pos.row][currCell.pos.col]
					.setBackground(Grid.EMPTY_COLOR);
			
			if (prevCell.isInitialFlowPointer())
				backtrackedToInitial = true;
			
			prevCell = currCell.previousCell;
			currCell.previousCell = null;
			currCell = prevCell;
			this.grid.nEmptyCells++;
		}
		while (currCell.hasForcedMove);

		
		return prevCell;
	}
	
	public void updatePQ(GridCell curr_flow_pointer) {
		int newHeuristic = curr_flow_pointer.heuristic();
		// insert new flow pointer to pq
		this.grid.pq.insert(newHeuristic, curr_flow_pointer);
//		this.grid.pq.changeKey(curr_flow_pointer, newHeuristic);
		// search for the adjacent flow pointers of new flow pointer, if any
		for (GridCell adj : curr_flow_pointer.getColoredAdjs()) {
			int new_heuristic = adj.heuristic();
			this.grid.pq.changeKey(adj, new_heuristic);
		}
	}

	
	public String toString() {
		return grid.toString();
	}
}





//if (cellsToConsider.size() == 1 && cellsToConsider.getFirst() == currFlowPointer.previousPointer)
//visitedCells.add(cellsToConsider.getFirst());
//
////constraint: if there is only one move to consider (force move)
//else if (cellsToConsider.size() == 1 
//	&& cellsToConsider.getFirst() != initialFlowPointer.pairInitialFlowPointer)
//System.out.println("");

////if no constraints on current or its adjacent cells, then skip to other conditions.
////if there are constrain colors/regions then only add previous pointer to list
//else if (!validation.constraintsPointerOrAdjacents(currFlowPointer, cellsToConsider) 
//	&& validation.causesStrandedColorOrRegion()) {
//cellsToConsider.removeAll(cellsToConsider);
//cellsToConsider.add(currFlowPointer.previousPointer);
//}

////if arrived at goal pair pointer
//else if (cellsToConsider.getFirst() == initialFlowPointer.pairInitialFlowPointer) {
//// create list of cells leading to goal pointer (path)
//LinkedList<GridCell> path = new LinkedList<GridCell>();
//while (currFlowPointer != initialFlowPointer) {
//	path.addLast(currFlowPointer);
//	currFlowPointer = currFlowPointer.previousPointer;
//}
//// add path to list of paths
//grid.finishedPaths.add(path);
//
//// now, consider next initial pointer within priority queue
//currFlowPointer = pq.removeMin().getValue();
//initialFlowPointer = currFlowPointer;
//
//}

package app;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.JPanel;

import priorityQueue.HeapPriorityQueue;

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
	
	
	
	public void solve() {
		HashSet<GridCell> visitedCells = new HashSet<GridCell>();
		HeapPriorityQueue<Integer, GridCell> pq = this.grid.pq;
//		GridCell currFlowPointer = pq.min().getValue();
		GridCell currFlowPointer = this.grid.gridCells[3][3];

		GridCell initialFlowPointer = currFlowPointer;
		boolean alreadyAnalysedInitial = true;
		
		while (!validation.puzzleIsSolved()) {
			// if arrived to initial pointer, after backtracking because of invalid path
			if (!alreadyAnalysedInitial && currFlowPointer == initialFlowPointer) {
				currFlowPointer = currFlowPointer.nextAdjCells.removeFirst();
				System.out.println("Bring back the last path, and start over with its initial Pointer");
			}
			alreadyAnalysedInitial = false;
			
			
			/* 
			   If 'nextAdjCells' isn't empty, it means that we have backtracked and
			   we are now considering the valid cells that we haven't moved into yet.
			   'cellsToConsider' may contain the force-move cell, 
			   the previous cell, or the various cells to move into.
			 */
			LinkedList<GridCell> cellsToConsider = (currFlowPointer.nextAdjCells.isEmpty())
					? validation.cellsToConsiderMovingInto(currFlowPointer)
							: currFlowPointer.nextAdjCells;
			
			if (cellsToConsider.size() == 1) {
				GridCell cellToMoveInto = cellsToConsider.getFirst();
				
				// constraint: if current Flow Pointer is at an invalid position (backtrack)
				if (cellToMoveInto == currFlowPointer.previousCell)
					currFlowPointer = this.backtrackToPrevious(currFlowPointer);
				
				// if arrived at goal pair pointer
				else if (cellToMoveInto == currFlowPointer.pairInitialFlowPointer) {
					this.grid.pq.removeMin();
					System.out.println("arrived at goal");
				}
				// only one valid move to consider moving into (force move)
				else {
					currFlowPointer.hasForcedMove = true;
					currFlowPointer = this.moveTowardsCell(currFlowPointer, 
							cellsToConsider.removeFirst());
				}
			}
			else if (validation.isThereStrandedColorOrRegion())
				currFlowPointer = this.backtrackToPrevious(currFlowPointer);
			else {
				for (GridCell nextCell : cellsToConsider)
					currFlowPointer.nextAdjCells.add(nextCell);
				
				currFlowPointer = this.moveTowardsCell(currFlowPointer, 
						currFlowPointer.nextAdjCells.removeFirst());
			}
		}		
	}
	
	/** Connects the properties of <i>currFlowPointer</i> and <i>nextToMoveInto</i>   */
	public GridCell moveTowardsCell(GridCell currFlowPointer, GridCell nextToMoveInto) {
		// move towards next cell
		nextToMoveInto.color = currFlowPointer.color;
		nextToMoveInto.previousCell = currFlowPointer;
		nextToMoveInto.pairInitialFlowPointer = currFlowPointer.pairInitialFlowPointer;				
		currFlowPointer = nextToMoveInto;
		this.gridPanel[nextToMoveInto.pos.row][nextToMoveInto.pos.col]
				.setBackground(nextToMoveInto.color);

		this.grid.nEmptyCells--;
		return nextToMoveInto;
	}
	
	
	/** Backtracks to the previous non force-moved cell, resetting each backtracked cell.
	 *  @return the given flow pointer's previous cell */
	public GridCell backtrackToPrevious(GridCell flowPointer) {
		
		GridCell currCell = flowPointer;
		GridCell prevCell = flowPointer.previousCell;
		do {
			currCell.color = Grid.EMPTY_COLOR;
			currCell.pairInitialFlowPointer = null;
			currCell.hasForcedMove = false;
			this.gridPanel[currCell.pos.row][currCell.pos.col]
					.setBackground(Grid.EMPTY_COLOR);
			
			prevCell = currCell.previousCell;
			currCell.previousCell = null;
			currCell = prevCell;
			this.grid.nEmptyCells++;
		}
		while (currCell.hasForcedMove);

		return prevCell;
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

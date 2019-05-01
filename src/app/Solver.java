package app;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import priorityQueue.HeapPriorityQueue;

public class Solver 
{
	Grid grid;
	Validation validation;

	public Solver(Grid grid) {
		this.grid = grid;
		this.validation = new Validation(grid);
	}
	
	public void solve() {
		HeapPriorityQueue<Integer, GridCell> pq = this.grid.pq;
		GridCell[][] gridCells = this.grid.gridCells;
		HashSet<Pos> initialFlowPointers = this.grid.initialFlowPointers;
		Stack<GridCell> stack = new Stack<>();
		
		while (!validation.puzzleIsSolved() && !stack.isEmpty()) {
			GridCell currFlowPointer = pq.min().getValue();

			// cellsToConsider can contain the forced move or the previous move
			LinkedList<GridCell> cellsToConsider = new LinkedList<>();
			
			// is current pointer or its adjacent cells constraint?
			if (!validation.constraintsPointerOrAdjacents(currFlowPointer, cellsToConsider)) {
				
				System.out.println("backtrack to previous cell");
				return;
			}
			// if no constraints, then cellsToConsider should 
			// contains the valid grid cells to move towards to.


			if (validation.causesStrandedColorOrRegion()) {
				cellsToConsider.removeAll(cellsToConsider);
				cellsToConsider.add(currFlowPointer.previousPointer);
				System.out.println("backtrack to previous cell");
				return;
			}

			for (GridCell nextCell : cellsToConsider)
				stack.push(nextCell);
			
			this.grid.nEmptyCells--;
		}
		
		if (stack.isEmpty())
			System.out.println("Solution is not found");
		
	}

	public boolean validate(GridCell flowPointer) {
//		if (validation.cellsToConsider(flowPointer) || validation.causesStrandedRegions(flowPointer))
//			return false;
		
		return true;
	}
}

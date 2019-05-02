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
		
//		Stack<GridCell> stack = new Stack<>();
//		stack.push(pq.min().getValue());
		
		GridCell currFlowPointer = pq.min().getValue();
		GridCell initialFlowPointer = currFlowPointer;
		
		while (!validation.puzzleIsSolved()) {
			// cellsToConsider can contain the forced move or the previous move
			LinkedList<GridCell> cellsToConsider = new LinkedList<>();
			
			if (currFlowPointer == initialFlowPointer) {
				System.out.println("Bring back the last path, and start over with its initial Pointer");
			}
			
			// is current pointer or its adjacent cells constraint?
			if (!validation.constraintsPointerOrAdjacents(currFlowPointer, cellsToConsider)) {
				System.out.println("backtrack to previous cell");
				this.grid.nEmptyCells++;
			}
			// if no constraints, then cellsToConsider should 
			// contains the valid grid cells to move towards to.

			else if (validation.causesStrandedColorOrRegion()) {
//				cellsToConsider.removeAll(cellsToConsider);
				cellsToConsider.add(currFlowPointer.previousPointer);
				System.out.println("backtrack to previous cell");
				this.grid.nEmptyCells++;
			}
			
			else if (cellsToConsider.getFirst() == initialFlowPointer.pairInitialFlowPointer) {
				LinkedList<GridCell> path = new LinkedList<GridCell>();
				while (currFlowPointer != initialFlowPointer) {
					path.addLast(currFlowPointer);
					currFlowPointer = currFlowPointer.previousPointer;
				}
				grid.finishedPaths.add(path);
				
				currFlowPointer = pq.removeMin().getValue();
				initialFlowPointer = currFlowPointer;
				
			}
			else {
				for (GridCell nextCell : cellsToConsider)
					currFlowPointer.nextAdjCells.add(nextCell);

				currFlowPointer = currFlowPointer.nextAdjCells.removeFirst();
			}
//			stack.push(currFlowPointer.nextAdjCells.removeFirst());

			this.grid.nEmptyCells--;
		}
		
//		if (stack.isEmpty())
//			System.out.println("Solution is not found");
		
	}

	public boolean validate(GridCell flowPointer) {
//		if (validation.cellsToConsider(flowPointer) || validation.causesStrandedRegions(flowPointer))
//			return false;
		
		return true;
	}
}

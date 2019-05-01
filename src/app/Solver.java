package app;

import java.util.HashSet;
import java.util.LinkedList;

import interfaces.Entry;
import priorityQueue.HeapPriorityQueue;

public class Solver 
{
	Grid grid;
	Validation validation = new Validation();

	public Solver(Grid grid) {
		this.grid = grid;
	}
	
	public void solve() {
		HeapPriorityQueue<Integer, GridCell> pq = this.grid.pq;
		GridCell[][] gridCells = this.grid.gridCells;
		HashSet<Pos> initialFlowPointers = this.grid.initialFlowPointers;

		while (validation.puzzleIsSolved()) {
			GridCell currFlowPointer = pq.min().getValue();

			// cellsToConsider can contain the forced move or the previous move
			LinkedList<GridCell> cellsToConsider = new LinkedList<>();
			// is current pointer or its adjacent cells aren't constraint
			if (!validation.constraintsPointerOrAdjacents(currFlowPointer, cellsToConsider)) {
				System.out.println("backtrack to previous cell");
				return;
			}
			
			
			
			
		}
	}

	public boolean validate(GridCell flowPointer) {
//		if (validation.cellsToConsider(flowPointer) || validation.causesStrandedRegions(flowPointer))
//			return false;
		
		return true;
	}
}

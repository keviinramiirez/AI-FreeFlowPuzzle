package app;

import java.util.HashSet;

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
		
		for (Entry<Integer, GridCell> entry : pq) {
			GridCell cell = entry.getValue();
			validate(cell);
		}
	}
	
	public boolean validate(GridCell cell) {
		if (validation.isForceMove(cell) || validation.)
			return false;
		
		return true;
	}
}

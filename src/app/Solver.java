package app;

import priorityQueue.HeapPriorityQueue;

public class Solver 
{
	Grid grid = new Grid();
	Validation validation = new Validation();
	HeapPriorityQueue<Integer, GridCell> pq = new HeapPriorityQueue<>();

	public Solver() {
		
	}
	
	public boolean validate(GridCell cell) {
		validation.isForceMove(cell);
		return true;
	}
}

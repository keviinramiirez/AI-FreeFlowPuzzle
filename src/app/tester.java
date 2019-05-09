package app;

import java.util.Iterator;
import java.util.List;

import util.PuzzleCreator;

public class tester 
{
	static public void main(String[] args) {
		PuzzleCreator puzzleCreator = new PuzzleCreator();
		
		Iterator<List<String>> puzzleIterator = puzzleCreator.puzzleIterator("5x5");

		int counter = 0;

		while (puzzleIterator.hasNext()) {
			Grid grid = new Grid();
			List<GridCell> initialFlowPointer = puzzleCreator.stringToGridCells(grid, puzzleIterator.next());
			Solver solver = new Solver(grid);
			float starting_time = System.currentTimeMillis()/1000;
			solver.solve();
			float ending_time = System.currentTimeMillis()/1000;
			float elapse_time = ending_time - starting_time;
			System.out.println("puzzle "+ (++counter) +" total runtime time: "+ elapse_time);
		}
	}
}

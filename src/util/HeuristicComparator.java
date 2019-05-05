package util;

import java.util.Comparator;

import app.GridCell;

public class HeuristicComparator implements Comparator<GridCell>
{
	@Override
	public int compare(GridCell gc1, GridCell gc2) {
//		int emptyAdjs1 = gc1.getEmptyAdjs().size(), emptyAdjs2 = gc2.getEmptyAdjs().size();
		
		if (gc1.heuristic > gc2.heuristic) return 1;
		if (gc1.heuristic < gc2.heuristic) return -1;
		else return 0;
	}
}

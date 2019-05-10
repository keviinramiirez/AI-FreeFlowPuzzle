package util;

import java.util.Comparator;

import appClasses.GridCell;

public class HeuristicComparator implements Comparator<GridCell>
{
	@Override
	public int compare(GridCell gc1, GridCell gc2) {
		if (gc1.heuristic > gc2.heuristic) return 1;
		else return -1;
	}
}

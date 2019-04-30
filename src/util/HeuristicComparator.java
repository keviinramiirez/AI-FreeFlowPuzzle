package util;

import java.util.Comparator;

import app.Pos;

public class HeuristicComparator implements Comparator<Integer>
{
	@Override
	public int compare(Integer n1, Integer n2) {
		return (n1 < n2) ? 1 : ((n1 > n2) ? -1 : 0);
	}
}

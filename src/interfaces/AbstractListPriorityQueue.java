package interfaces;

import java.util.ArrayList;
import java.util.Comparator;

import priorityQueue.AbstractPriorityQueue;
import interfaces.Entry;


/**
 * Implementation of a PriorityQueue based in an ArrayList<Entry<K, V>>.
 * @author pedroirivera-vega
 *
 * @param <K>
 * @param <V>
 */
public abstract class AbstractListPriorityQueue<K, V> extends AbstractPriorityQueue<K, V> 
{

	protected ArrayList<Entry<K,V>> list; 
	
	protected AbstractListPriorityQueue(Comparator<K> cmp) { 
		super(cmp);
		list = new ArrayList<>(); 
	}
	
	protected AbstractListPriorityQueue() { 
		super(); 
		list = new ArrayList<>(); 
	}
	
	@Override
	public int size() {
		return list.size();
	}

	@Override
	public Entry<K, V> min() {
		if (this.isEmpty()) return null; 
		return list.get(minEntryIndex());
	}

	/**
	 * Internal method to find the index of the element in list
	 * containing an entry having min key. Subclasses will
	 * implement as needed. 
	 * 
	 * @return index of the element having min key in list. 
	 */
	protected abstract int minEntryIndex();

	/**
	 * This method is mainly for testing purposes. Elements (entries)
	 * are displayed as they are inside the internal list, from first
	 * to last; hence, in this case, they they are not seen in any
	 * particular order (increasing or decreasing), but just as they
	 * are in the list.... 
	 */
	public void display() { 
		if (this.isEmpty()) 
			System.out.println("EMPTY"); 
		else
			for (Entry<K,V> e : list) 
				System.out.println(e); 		
	}
	
	@Override
	public Entry<K, V> removeMin() {
		if (!list.isEmpty())
			return list.remove(minEntryIndex());
		return null;
	}

}

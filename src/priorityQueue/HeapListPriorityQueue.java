package priorityQueue;

import heap.CompleteBinaryTree;
import interfaces.AbstractListPriorityQueue;
import interfaces.Entry;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Implementation of a PriorityQueue based in an ArrayList<Entry<K, V>>, 
 * but treated as a heap binary tree.
 * 
 * @author pedroirivera-vega
 *
 * @param <K>
 * @param <V>
 */
public class HeapListPriorityQueue<K, V> extends AbstractListPriorityQueue<K, V> implements Iterable<Entry<K, V>> 
{
	public HeapListPriorityQueue(Comparator<K> cmp) { 
		super(cmp);
	}
	
	public HeapListPriorityQueue() { 
		super(); 
	}
	

	// internal useful methods
	private int left(int r) { return 2*r+1; } 
	private int right(int r) { return 2*r+2; } 
	private int parent(int r) { return (r-1)/2; } 
	private boolean hasLeft(int r) { return left(r) < list.size(); } 
	private boolean hasRight(int r) { return right(r) < list.size(); } 
	
	@Override
	public Entry<K,V> insert(K key, V value) throws IllegalArgumentException {
		super.validate(key); 
		Entry<K,V> newest = new PQEntry<K,V>(key, value);
		list.add(newest); 
		upHeap(list.size()-1); 
		return newest; 
	}

	@Override
	public Entry<K,V> removeMin() { 
		if (list.isEmpty()) return null; 
		int di = list.size()-1; 
		Entry<K,V> etr = list.set(0, list.get(di)); 
		list.remove(di); 
		
		downHeap(0); 
		return etr; 
	}
	
	public void changeKey(V value, K newKey) {
		for (Entry<K,V> entry : this.list) {
			if (entry.getValue().equals(value)) {
				this.list.remove(entry);
//				entry.setKey(newKey);
				this.insert(newKey, entry.getValue());
				break;
			}
		}
	}
	
	@Override
	protected int minEntryIndex() {
		return 0;
	}
	
	private void downHeap(int r) {
		while (hasLeft(r)) { 
			int mcIndex = left(r); 
			if (hasRight(r)) 
				if (compare(list.get(right(r)), list.get(mcIndex)) < 0)
					mcIndex = right(r); 
			if (compare(list.get(mcIndex), list.get(r)) < 0) { 
				swapListElements(mcIndex, r); 
				r = mcIndex; 
			} 
			else        // heap property is already met
				return;    
		}
	}

    private void upHeap(int r) { 
    	while (r != 0  && compare(list.get(parent(r)), list.get(r)) > 0) { 
    		swapListElements(parent(r), r); 
    		r = parent(r); 
    	}
    }
	
	private void swapListElements(int i, int j) {
		list.set(i, list.set(j, list.get(i))); 
	}
	
	public void display() { 
		CompleteBinaryTree<Entry<K,V>> t = new CompleteBinaryTree<>(); 
		for (Entry<K,V> e : list) 
			t.add(e); 
		t.display();
	}

	public Iterator<Entry<K, V>> iterator() {
		return list.iterator();
	}
}


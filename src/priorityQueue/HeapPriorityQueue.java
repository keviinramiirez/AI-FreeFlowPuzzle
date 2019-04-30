package priorityQueue;

import java.util.Comparator;

import heap.Heap;
import interfaces.Entry;

/**
 * Implementation of a priority queue that uses a previous implementation of
 * the heap ADT as an instance field. The elements in the queue are stored
 * in the internal heap. 
 * @author pedroirivera-vega
 *
 * @param <K>
 * @param <V>
 */

public class HeapPriorityQueue<K, V> extends AbstractPriorityQueue<K, V> {
	
	// The elements of the priority queue will be stored in a heap. 
	// Since a Heap is a particular case of a binary tree, we are going
	// to be able to apply operations of the binary tree; in particular
	// the display() method to visualize the priority queue content. 
	private Heap<Entry<K, V>> heap; 

	/**
	 * Creates a new HeapPriorityQueue using the given comparator. 
	 * @param cmp
	 */
	public HeapPriorityQueue(Comparator<K> cmp) {
		super(cmp); 
		heap = new Heap<Entry<K, V>>(super.entryCmp); 		
	}
	
	/**
	 * Creates a new HeapPriorityQueue using the default comparator. 
	 */
	public HeapPriorityQueue() { 
		super(); 
		heap = new Heap<Entry<K, V>>(super.entryCmp); 		
	}
	
	@Override
	public int size() {
		return heap.size();
	}

	@Override
	public Entry<K, V> min() {
		return heap.min();
	}

	@Override
	public Entry<K, V> removeMin() {
		return heap.removeMin();
	}

	@Override
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
		if (!validate(key)) 
			throw new IllegalArgumentException("Invalid key."); 
		PQEntry<K, V> entry = new PQEntry<K, V>(key, value); 
		heap.add(entry);
		
		return entry;
	}
	
	public Entry<K, V> remove(K key) throws IllegalArgumentException {
		if (!validate(key)) 
			throw new IllegalArgumentException("Invalid key."); 
		return remove(key);
	}
	
	public void display() { 
		heap.display(); 
	}
}

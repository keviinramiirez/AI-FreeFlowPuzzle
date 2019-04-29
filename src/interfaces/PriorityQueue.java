package interfaces;

import interfaces.Entry;

public interface PriorityQueue<K, V> {
	int size(); 
	boolean isEmpty(); 
	Entry<K, V> min(); 
	Entry<K, V> removeMin(); 
    Entry<K, V> insert(K key, V value) throws IllegalArgumentException; 
}

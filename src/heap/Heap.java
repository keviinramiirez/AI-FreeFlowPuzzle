package heap;

import java.util.Comparator;

import interfaces.Position;

/**
 * Heap data structure implemented as a subclass of AbstractBinaryTree
 * and using an ArrayList<Position<E>> to store its elements or positions.
 * Since the tree is a complete binary tree; if it has n nodes, then those
 * nodes are stored as the first n elements in the internal ArrayList. 
 * 
 * root is located at position 0. 
 * If a node v is located at location i, if it has a left child, then
 * that child shall be stored at location 2*i+1. It's right child, if
 * any, will be stored at location 2*i+2 in the ArrayList. If node v
 * is not the root, and if it is stored at location i in the internal
 * ArrayList, then its parent shall be located at location (i-1)/2
 * in the internal ArrayList. 
 * 
 * @author pedroirivera-vega
 *
 * @param <E> The generic type of the elements. 
 */
public class Heap<E> extends CompleteBinaryTree<E> {

	private Comparator<E> cmp;        // heap insertion is based on this comparator
	
	public Heap(Comparator<E> cmp) { 
		this.cmp = cmp; 
	}
		

	
	/**
	 * Method to add a new element to the heap. 
	 * Adds a new element to the heap. The heap is assumed to be a min-heap. 
	 * Always add the new element at the first position available in the tree. 
	 * Just add to the position list.size() in the array list. After the new 
	 * element is added, apply upheap to guarantee that the current content
	 * (which is a heap), plus the new element, continue satisfying the heap
	 * property. 
	 * @param element
	 */
	public Position<E> add(E element) {
		CBTPosition<E> p = (CBTPosition<E>) super.add(element);    // append to the list (at super)
		upHeap(p);      // do up-heap as necessary from that position.
		return p; 
	}
	
	/**
	 * Returns minimum element in the heap. That element is at location 0
	 * in the array or arraylist. 
	 * @return reference to the minimum element. 
	 */
	public E min() { 
		if (list.isEmpty())
			return null; 
		return list.get(0).getElement();    // min element is at position 0 
	}
	

	/** 
	 * Same effect as min() but it also removes that element from the pq. 
	 * @return
	 */
	public E removeMin() { 
		if (list.isEmpty())
			return null; 
		
		CBTPosition<E> ptr = (CBTPosition<E>) list.get(0);
		
		if (list.size() > 1) {
		   list.set(0, list.remove(list.size()-1)); 
		   ((CBTPosition<E>) list.get(0)).setIndex(0);
		   downHeap((CBTPosition<E>) list.get(0)); 
		}
		else 
			list.remove(0);
		
		return ptr.getElement(); 
	}
	
	
	public E remove(E e) {
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).equals(e))
				return list.remove(i).getElement();
		return null;
	}
	
	/**
	 * Does downheap of r in subtree having root r
	 * @param r
	 */
	private void downHeap(CBTPosition<E> r) { 
		if (this.hasLeft(r)) { 
			CBTPosition<E> minChild = (CBTPosition<E>) this.left(r); 
			if (this.hasRight(r)) {
				CBTPosition<E> rChild = (CBTPosition<E>) this.right(r); 
				// is +0 if minChild > right(r)
				if (cmp.compare(minChild.getElement(), this.right(r).getElement()) > 0)
					minChild = rChild; 
			}

			// CODE IS MISSING HERE ... ADD THE CORRECT CODE
			// is -0 if minChild < r
			if (cmp.compare(minChild.getElement(), r.getElement()) < 0) {
				swapPositionsInList(r, minChild);
				downHeap(r);
			}
		}
	}

	/**
	 * Moves upward, as needed, in the path from p to root, the element at p
	 * with the goal of maintaining the heep property after the new element
	 * is added. Potentially, this goes upward, all the way from p to root, 
	 * or until the value initially in p reached a level in which the key
	 * at its parent is less than the key initially at p. 
	 * @param p
	 */
	private void upHeap(CBTPosition<E> p) { 
		if (!this.isRoot(p)) { 
			CBTPosition<E> parent = (CBTPosition<E>) this.parent(p); 
			if (cmp.compare(p.getElement(), parent.getElement()) < 0) { 
				swapPositionsInList(p, parent);    // p is now parent of parent....
				upHeap(p); 
			}
		}
	}
	
	/** 
	 * Interchanges two position in the array. 
	 * 
	 * @param r one of the position
	 * @param c the other position
	 */
	private void swapPositionsInList(CBTPosition<E> r, CBTPosition<E> c) {
		int ir = r.getIndex(); 
		int ic = c.getIndex(); 
		r.setIndex(ic);         // since position change location, indexes are changed too
		c.setIndex(ir);
		
		// swap content of location ir and ic in the arraylist
		list.set(ir, list.set(ic, r));   // swaps elements at positions ir and ic in list
		   
	}
}

package heap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import interfaces.Position;
import treeClasses.AbstractBinaryTree;
import treeClasses.LinkedBinaryTree;

/**
 * Complete BinaryTree is a data structure implemented as a subclass of 
 * AbstractBinaryTree and using an ArrayList<Position<E>> to store its 
 * elements or positions.
 * 
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
public class CompleteBinaryTree<E> extends AbstractBinaryTree<E> {

	protected ArrayList<CBTPosition<E>> list;   // hold the positions in the tree
	
	public CompleteBinaryTree() { 
		list = new ArrayList<>(); 
	}
	
	protected CBTPosition<E> validate(Position<E> p) throws IllegalArgumentException { 
		if (!(p instanceof CBTPosition<?>))
			throw new IllegalArgumentException("Invalid position - not of type HeapPosition."); 
		CBTPosition<E> hp = (CBTPosition<E>) p; 
		if (hp.getIndex() < 0 || hp.getIndex() >= list.size())
			throw new IllegalArgumentException("Position does not belong to this heap."); 
		return hp; 	
	}
	
	// The following three methods are used to determine the index of the 
	// left child, the right child, and the parent of a node whose index is
	// given (parameter i). Notice that the names of each of these methods
	// are overloaded....... with corresponding methods having parameter of
	// type Position<>.
	private int left(int i) { 
		return 2*i+1; 
	}
	
	private int right(int i) { 
		return 2*i+2; 
	}
	
	private int parent(int i) { 
		return ((i-1)/2);
	}
	///////////////////////////////////////////////////////////////////////
	
	@Override 
	/**
	 * left method of BinaryTree ADT
	 */
	public Position<E> left(Position<E> p) throws IllegalArgumentException {
		CBTPosition<E> hp = validate(p); 
		
		int leftIndex = left(hp.getIndex());  // index of left child location in the array
		
		if (leftIndex < list.size())          // check of p has left child
			return list.get(leftIndex); 
		
		return null;
	}

	@Override
	/**
	 * right method of BinaryTree ADT
	 */
	public Position<E> right(Position<E> p) throws IllegalArgumentException {
		CBTPosition<E> hp = validate(p); 
		
		int rightIndex = right(hp.getIndex()); // index of right child location in the array
		
		if (rightIndex < list.size())          // checking if p has right child
			return list.get(rightIndex); 
		
		return null;
	}

	@Override
	/**
	 * root() method of BinaryTree
	 */
	public Position<E> root() {
		if (list.isEmpty()) 
			return null; 
		return list.get(0); 
	}

	@Override
	/**
	 * parent method of BinaryTree
	 */
	public Position<E> parent(Position<E> p) throws IllegalArgumentException {
		CBTPosition<E> hp = validate(p); 
		
		if (hp.getIndex() == 0)    // if p is the root, return null (no parent)
			return null;
		
		return list.get(parent(hp.getIndex()));  // parent is at location hp.getIndex()
	}

	@Override
	/**
	 * size method of BinaryTree
	 */
	public int size() {
		return list.size();
	}

	@Override
	/**
	 * iterator method of BinaryTree - based on breadth-first-search traversal
	 */
	public Iterator<E> iterator() {
		// this is really a bfs iterator
		ArrayList<E> iterList = new ArrayList<>(); 
		for (Position<E> p : list) 
			iterList.add(p.getElement()); 
		
		return iterList.iterator();

	}

	@Override
	/** 
	 * positions method of BinaryTree - based on breadth-first-search traversal
	 */
	public Iterable<Position<E>> positions() {
		ArrayList<Position<E>> iterList = new ArrayList<>(); 
		for (Position<E> p : list) 
			iterList.add(p); 
		
		return iterList;
	}
	
	/**
	 * Method to add a new element to the complete binary tree. 
	 * The new element will be added as a new leaf (the next possible
	 * for the tree to continue satisfying the complete binary tree
	 * property). In this implementation, that translates to add 
	 * the new position at the end of the internal list. 
	 * @param element
	 */
	public Position<E> add(E element) {
		CBTPosition<E> hp = new CBTPosition<>(element, list.size()); 
		list.add(hp);    // add the new position to location list.size() in array
		return hp; 
	}
	
	// Private class implementing the type of positions being used in this 
	// implementation. This type of position is specialized for implementations
	// in which the positions are stored in an array or arraylist.
	protected static class CBTPosition<E> implements Position<E> {

		private E element;   // the element at this position
		private int index;   // its position in the array list
		// the previous index is needed because no operation of Tree
		// uses indexes; those that refer to a tree location, use a position. 
		// Hence to implement such methods, the implementation needs to 
		// know what location in the arraylist is where that position is stored. 
		
		public CBTPosition(E element, int index) { 
			this.element = element; 
			this.index = index; 
		}
		
		@Override
		public E getElement() {
			return element; 
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public void setElement(E element) {
			this.element = element;
		} 	
	}
}

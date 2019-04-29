package treeClasses;

import java.util.ArrayList;
import java.util.Iterator;

import interfaces.Position;

public class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {
    // class Node<E> is included at the end of this class
	
	private Node<E> root;   // the root of the tree
	int size;               // the size of the tree

	public LinkedBinaryTree() { 
		root = null; 
		size = 0; 
	}
	
	private Node<E> validate(Position<E> p) throws IllegalArgumentException { 
		if (!(p instanceof Node<?>)) 
			throw new IllegalArgumentException("Position is not of righ data type."); 
		
		Node<E> ptn = (Node<E>) p; 
		if (ptn.getParent() == p) 
			throw new IllegalArgumentException("Invalid position --- not a tree position."); 
		
		return ptn; 
	}
	
	@Override
	public Position<E> left(Position<E> p) throws IllegalArgumentException {
		Node<E> ptn = validate(p); 
		return ptn.getLeft();
	}

	@Override
	public Position<E> right(Position<E> p) throws IllegalArgumentException {
		Node<E> ptn = validate(p); 
		return ptn.getRight();
	}

	@Override
	public Position<E> root() {
		return root;
	}

	@Override
	public Position<E> parent(Position<E> p) throws IllegalArgumentException {
		Node<E> ptn = validate(p); 
		return ptn.getParent();
	}

	@Override
	public int size() {
		return size;
	}

	
	////////////////////////////////////////////////////////////////////////////////
	// OTHER methods as in textbook: addRoot, addLeft, addRight, attach, and remove
	////////////////////////////////////////////////////////////////////////////////
	
	public Position<E> addRoot(E e) throws IllegalStateException { 
		if (!this.isEmpty()) 
			throw new IllegalStateException("Non-empty tree: Can not add a root to a non-empty tree."); 
		root = createNode(e, null, null, null); 
		size = 1; 
		return root; 
	}
	
	public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException { 
		Node<E> np = validate(p); 
		if (np.getLeft() != null) 
			throw new IllegalArgumentException("Given position already has left child."); 
		Node<E> newNode = createNode(e, np, null, null); 
		np.setLeft(newNode); 
		size++; 
		return newNode;
	}
	
	public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException { 
		Node<E> np = validate(p); 
		if (np.getRight() != null) 
			throw new IllegalArgumentException("Given position already has right child."); 
		Node<E> newNode = createNode(e, np, null, null); 
		np.setRight(newNode); 
		size++; 
		return newNode;
	}
	
	public void Attach(Position<E> p, LinkedBinaryTree<E> t1, LinkedBinaryTree<E> t2) 
							throws IllegalArgumentException
	{ 
		Node<E> np = validate(p); 
		if (isInternal(np))
			throw new IllegalArgumentException("Position is not an external node in the tree."); 
		size += t1.size + t2.size;   // determine new size of this tree
		if (!t1.isEmpty()) { 
			np.setLeft(t1.root);
			t1.root.setParent(np); 
			
			// make t1 empty
			t1.root = null; 
			t1.size = 0; 
		}
		if (!t2.isEmpty()) { 
			np.setRight(t2.root);
			t2.root.setParent(np); 
			
			// make t2 empty
			t2.root = null; 
			t2.size = 0; 
		}
	}
	
	public E remove(Position<E> p) throws IllegalArgumentException { 
		Node<E> ntd = validate(p); 
		if (numChildren(ntd) == 2)
			throw new IllegalArgumentException("Position to delete has two children.");
		E etr = ntd.getElement(); 
		Node<E> child = (ntd.getLeft() == null ? ntd.getRight() : ntd.getLeft()); 
		Node<E> parent = ntd.getParent(); 
		if (parent == null) 
			root = child; 
		else if (parent.getLeft() == ntd)
			parent.setLeft(child); 
		else 
			parent.setRight(child);
		if (child != null)
			child.setParent(parent);
		size--; 
		
		// discard deleted node
		ntd.discard();
		
		return etr; 
	}

	
	////////////////////////////////////////////////////////
	// Inner class Node<E> and method to create new node  //
	////////////////////////////////////////////////////////	
	/**
	 * Inner class Node<E>
	 * @author pedroirivera-vega
	 *
	 * @param <E> Data type of element in Node
	 */
	private static class Node<E> implements Position<E> { 
		private E element; 
		private Node<E> parent, left, right; 
		public Node() {}
		public Node(E element, Node<E> parent, Node<E> left, Node<E> right) { 
			this.element = element; 
			this.parent = parent; 
			this.left = left; 
			this.right = right; 
		}
		public E getElement() { 
			return element; 
		}
		public Node<E> getParent() {
			return parent;
		}
		public void setParent(Node<E> parent) {
			this.parent = parent;
		}
		public Node<E> getLeft() {
			return left;
		}
		public void setLeft(Node<E> left) {
			this.left = left;
		}
		public Node<E> getRight() {
			return right;
		}
		public void setRight(Node<E> right) {
			this.right = right;
		}
		public void setElement(E element) {
			this.element = element;
		}
		
		public void discard() { 
			element = null; 
			left = right = null;
			parent = this; 
		}
		
	} // end Node<E>
	
	/**
	 * Method to create a new Node
	 * @param e the element in the new node
	 * @param p the parent of the new node
	 * @param l the left child of the new node
	 * @param r the right child of the new node
	 * @return
	 */
	protected Node<E> createNode(E e, Node<E> p, Node<E> l, Node<E> r) { 
		return new Node<E>(e, p, l, r); 
	}
}

package interfaces;

import interfaces.Position;

/**
 * BinaryTree ADT - see textbook for explanation of each operation....
 * 
 * @author pedroirivera-vega
 *
 * @param <E>
 */
public interface BinaryTree<E> extends Tree<E> {
	boolean hasLeft(Position<E> p) throws IllegalArgumentException; 
	boolean hasRight(Position<E> p) throws IllegalArgumentException; 
	Position<E> left(Position<E> p) throws IllegalArgumentException; 
	Position<E> right(Position<E> p) throws IllegalArgumentException; 
	Position<E> sibling(Position<E> p) throws IllegalArgumentException; 
}

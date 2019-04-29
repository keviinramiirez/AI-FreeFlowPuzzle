package treeClasses;

import java.util.ArrayList;
import java.util.Iterator;

import interfaces.Position;
import interfaces.Tree;

public abstract class AbstractTree<E> implements Tree<E> {

	@Override
	public boolean isInternal(Position<E> p) throws IllegalArgumentException {
		return this.numChildren(p) > 0;
	}

	@Override
	public boolean isExternal(Position<E> p) throws IllegalArgumentException {
		return this.numChildren(p) == 0;
	}

	@Override
	public boolean isRoot(Position<E> p) throws IllegalArgumentException {
		return this.parent(p) == null; 
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	@Override
	/**
	 * Returns Iterator object of elements in the tree,
	 * and based on inorder traversal. Notice that this is
	 * based on the Iterable<Position<E>> object produced by
	 * method positions()
	 */
	public Iterator<E> iterator() {		
		ArrayList<Position<E>> pList = new ArrayList<>(); 
		fillIterable(root(), pList); 
		
		ArrayList<E> eList = new ArrayList<>(); 
		for (Position<E> p : pList)
			eList.add(p.getElement()); 
		return eList.iterator();
	}

	@Override
	/**
	 * Produces an iterable of positions in the tree based on
	 * postorder traversal. 
	 */
	public Iterable<Position<E>> positions() {
		ArrayList<Position<E>> pList = new ArrayList<Position<E>>(); 
		fillIterable(root(), pList); 
		return pList;
	}
		
    /**
     * Method to fill the Iterable<Position<E>> object by properly traversing
     * the positions in the tree. Final version is decided at the particular 
     * type of tree - general, binary, etc.
     * 
     * The default method, as implemented here, generates an Iterable<Position<E>>
     * object based on PREORDER. 
     * @param r
     * @param pList
     */
	protected void fillIterable(Position<E> r, ArrayList<Position<E>> pList) {
		if (r != null) { 
			pList.add(r); 
			for (Position<E> p : children(r))
				fillIterable(p, pList); 
		}	
	}

	
	///////////////////////////////////////////////////////////////////////
	// The following are miscellaneous methods to display the content of //
	// the tree ....                                                     //
	///////////////////////////////////////////////////////////////////////
	public void display() {                                              //
		final int MAXHEIGHT = 100;                                       //
		int[] control = new int[MAXHEIGHT];                              //
		control[0]=0;                                                    //
		if (!this.isEmpty())                                             //
			recDisplay(this.root(), control, 0);                         //
		else                                                             //
			System.out.println("Tree is empty.");                        //
	}                                                                    //
                                                                         //
	// Auxiliary Method to support display                               //
	protected void recDisplay(Position<E> root,                          //
			int[] control, int level)                                    //
	{                                                                    //
		printPrefix(level, control);                                     //
		System.out.println();                                            //
		printPrefix(level, control);                                     //
		System.out.println("__("+root.getElement()+")");                 //
		control[level]--;                                                //
		int nc = this.numChildren(root);                                 //
		control[level+1] = nc;                                           //
		for (Position<E>  p : this.children(root)) {                     //
			recDisplay(p, control, level+1);                             //
		}                                                                //
	}                                                                    //
                                                                         //
	// Auxiliary method to support display                               //
	protected static void printPrefix(int level, int[] control) {        //
		for (int i=0; i<=level; i++)                                     //
			if (control[i] <= 0)                                         //
				System.out.print("    ");                                //
			else                                                         //
				System.out.print("   |");                                //
	}                                                                    //
    ///////////////////////////////////////////////////////////////////////
}

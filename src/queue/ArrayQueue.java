package queue;

import java.util.ArrayList;

public class ArrayQueue<E> implements Queue<E> 
{
	private final static int INITCAP = 4; 
	private E[] elements; 
	private int first, size;

	@SuppressWarnings("unchecked")
	public ArrayQueue() {
		elements = (E[]) new Object[INITCAP]; 
		first = 0; 
		size = 0; 
	}

	public boolean isEmpty(){ return size == 0; }
	public int size()		{ return size; }


	public E first() {
		if (isEmpty()) return null; 
		return elements[first]; 
	}

	public E dequeue() {
		if (isEmpty()) return null;
		E etr = elements[first];	

		// if number of available positions in the array exceed 3/4 of its 
		// total length and if the current capacity is not less than 2*INITCAP,
		// then shrink the capacity of the internal array to (current length)/2. 
		if (elements.length >= 2*INITCAP && size < elements.length/4)
			changeCapacity(elements.length/2);

		elements[first] = null;
		first = (first + 1) % elements.length;

		size--; 
		return etr;
	}

	public void enqueue(E e) {
		if (size == elements.length)   // check capacity, double it if needed
			changeCapacity(2*size);
		elements[(first+size) % elements.length] = e;
		size++;
	}

//	private void changeCapacity(int newCapacity) { 
//		@SuppressWarnings("unchecked")
//		E[] newArray = (E[]) new Object[newCapacity];		
//		boolean dequeing = newCapacity < elements.length;
//		int indexNew = 0, j = 0;
//		
//		for (int i = 0; i < size; i++) {
//			if (dequeing) {
//				indexNew = i;
//				j = first + i;
//				newArray[indexNew] = elements[j]; 
//			}
//			else  {
//				indexNew = first + size + i;
//				if (indexNew >= newArray.length)   
//					indexNew = indexNew % newArray.length;
//				j = first + size + i;
//				newArray[indexNew] = elements[j % elements.length]; 
//			}	
//		}
//		if (dequeing)
//			first = 0;
//		else
//			first += size;
//		elements = newArray; 
//	}
	
	private void changeCapacity(int newCapacity) { 
		@SuppressWarnings("unchecked")
		E[] newArray = (E[]) new Object[newCapacity];		
		boolean dequeing = newCapacity < elements.length;
		int indexNew = 0, j = 0;
		
		for (int i = 0; i < size; i++) {
			j = first + i;
			indexNew = i;
			if (dequeing)
				newArray[indexNew] = elements[j]; 
			else  {
				indexNew += first + size;
				if (indexNew >= newArray.length)   
					indexNew = indexNew % newArray.length;
				j += size;
				newArray[indexNew] = elements[j % elements.length]; 
			}	
		}
		
		if (dequeing) first = 0;
		else first += size;
		
		elements = newArray; 
	}

	public String toString() {
		/* the commented code is the correct implementation. 
		 * but for the testers, let's show the null elements. */
		//		if (isEmpty()) return "[]";
		if (isEmpty()) return elements.length +" NULLS";
		String result = "[";
		//		for (int i = 0; i < size; i++) {
		for (int i = 0; i < elements.length; i++) {
			result += elements[i];
			//			if (i == size-1)
			if (i == elements.length-1)
				result += "]";
			else
				result += ", ";
		}
		return result;
	}
	
	//Extra Method
	public static<E> ArrayQueue<E> toArrayQueue(ArrayList<E> list) {
		ArrayQueue<E> queue = new ArrayQueue<E>();
		for (E e : list)
			queue.enqueue(e);
		return queue;
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(3);

		System.out.println("ArrayList = "+ list.toString());
		
		//toArrayQueue()
		ArrayQueue<Integer> queue = ArrayQueue.toArrayQueue(list);
		System.out.println("ArrayQueue = "+ queue.toString());
	}
}

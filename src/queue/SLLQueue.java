package queue;

public class SLLQueue<E> implements Queue<E> 
{
	@SuppressWarnings("hiding")
	class Node<E>  
	{
		private E element; 
		private Node<E> next; 
		
		public Node() { 
			element = null; 
			next = null; 
		}
		public Node(E data)  { 
			this.element = data; 
			next = null; 
		}
		public Node(E data, Node<E> next) { 
			this.element = data; 
			this.next = next; 
		}
		
		public void setNext(Node<E> next) 	{ this.next = next; }
		public void setElement(E data) 		{ this.element = data; }
		public Node<E> getNext() 			{ return next; }
		public E getElement() 				{ return element; }
		
		public void clean() { 
			element = null; 
			next = null; 
		}
	}
	
	//Instance Variables
	private Node<E> first, last;   // references to first and last node
	private int size; 
	
	public SLLQueue() {           // initializes instance as empty queue
		first = last = null; 
		size = 0; 
	}
	
	public boolean isEmpty(){ return size == 0; }
	public int size() 		{ return size; }
	
	public E first() {
		return isEmpty() ? null : first.getElement();
	}
	
	public E last() {
		return isEmpty() ? null : last.getElement();
	}
	
	public E dequeue() {
		if (isEmpty()) return null;
		E etr = first.getElement();
		first = first.getNext();
		if (size-- == 1) 
			last = null;
		return etr;
	}
	
	public void enqueue(E e) {
		if (size++ == 0) 
			first = last = new Node<>(e); 
		else {
			Node<E> newNode = new Node<E>(e);
			last = newNode;
			Node<E> curr = first;
			while (curr.getNext() != null)
				curr = curr.getNext();
			curr.setNext(newNode);
		}
	}
	
	public String toString() {
		if (isEmpty()) return "[]";
		String result = "[";
		Node<E> curr = first;
		for (int i = 0; i < size; i++) {
			result += curr.getElement().toString();
			if (i == size-1)
				result += "]";
			else
				result += ", ";
			curr = curr.getNext();
		}
		return result;
	}
	
	/** that takes all elements of Q2 and appends them to the end of the original queue */
	public void concatenate(SLLQueue<E> q) {
		Node<E> curr = last;
		while (!q.isEmpty()) {
			curr.setNext(new Node<E>(q.dequeue()));
			curr = curr.getNext();
			size++;
		}
	}
	
	public static void main(String[] args) {
		SLLQueue<Integer> q = new SLLQueue<Integer>();
		q.enqueue(0);
		q.enqueue(1);
		q.enqueue(2);
		q.dequeue();
		q.dequeue();
		q.dequeue();
		q.dequeue();
		System.out.println(q.toString());
	}
}

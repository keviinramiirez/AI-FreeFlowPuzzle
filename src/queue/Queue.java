package queue;

public interface Queue<E> {
	   /** Accessor Method. Returns the size of the current instance **/
	   int size();

	   /** Accessor Method. Returns true if the current instance is empty; false, if not.  
	   **/
	   boolean isEmpty();

	   /** Accessor Method.
		Accesses element in the current instance of the queue.
		The affected element is the one that has been in the queue
         for the longest time among all its current elements.
		Returns reference to the element being accessed.
		Returns null if queue is empty. 
         (Note that we named it front() in lectures....)
	   **/
	   E first();

	   /** Mutator Method.
		Adds a new element to the queue.  
	   **/
	   void enqueue(E element);

	   /** Mutator Method.
		Similar to the first() method, but this time, the queue is
         altered since the accessed element is also removed from
		the queue. Returns null if the queue is empty.  
	   **/
	   E dequeue();
	}

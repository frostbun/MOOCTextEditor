package textgen;

import java.util.AbstractList;


/** A class that implements a doubly linked list
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	int size;

	/** Create a new empty LinkedList */
	public MyLinkedList() {
		this.head = new LLNode<>(null, null, tail);
		this.tail = new LLNode<>(null, head, null);
	}

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element) 
	{
		if(element == null) {
			throw new NullPointerException("ngu");
		}
		
		LLNode<E> newnode = new LLNode<>(element, this.tail.prev, tail);
		newnode.prev.next = newnode.next.prev = newnode;
		
		++this.size;
		
		return true;
	}

	/** Get the element at position index 
	 * @throws IndexOutOfBoundsException if the index is out of bounds. */
	public E get(int index) 
	{
		return getNode(index).data;
	}

	/**
	 * Add an element to the list at the specified index
	 * @param index The index where the element should be added
	 * @param element The element to add
	 */
	public void add(int index, E element ) 
	{
		if(index == this.size) {
			this.add(element);
			return;
		}
		
		if(element == null) {
			throw new NullPointerException("ngu");
		}

		LLNode<E> current = getNode(index);

		LLNode<E> newnode = new LLNode<>(element, current.prev, current);
		newnode.prev.next = newnode.next.prev = newnode;

		++this.size;
	}


	/** Return the size of the list */
	public int size() 
	{
		return this.size;
	}

	/** Remove a node at the specified index and return its data element.
	 * @param index The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 * 
	 */
	public E remove(int index) 
	{
		LLNode<E> current = getNode(index);
		current.prev.next = current.next;
		current.next.prev = current.prev;

		--this.size;

		return current.data;
	}

	/**
	 * Set an index position in the list to a new element
	 * @param index The index of the element to change
	 * @param element The new element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public E set(int index, E element) 
	{
		this.add(index, element);
		return this.remove(index+1);
	}   

	private LLNode<E> getNode(int index) {
		if(index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException("ngu");
		}

		LLNode<E> current = this.head;
		for(int i=0; i<=index; ++i) {
			current = current.next;
		}
		return current;
	}
}

class LLNode<E> 
{
	LLNode<E> prev;
	LLNode<E> next;
	E data;

	public LLNode(E e, LLNode<E> prev, LLNode<E> next) 
	{
		this.data = e;
		this.prev = prev;
		this.next = next;
	}
}

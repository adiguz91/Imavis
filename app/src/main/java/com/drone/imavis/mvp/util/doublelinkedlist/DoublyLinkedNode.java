package com.drone.imavis.mvp.util.doublelinkedlist;

/**
 * Created by Adrian on 26.11.2016.
 */

/**
 * A class supporting a doubly linked list element.  Each element
 * contains a value and maintains references to the previous and next
 * nodes in the list.
 * <p>
 *
 * @version $Id: DoublyLinkedNode.java 31 2007-08-06 17:19:56Z bailey $
 * @author, 2001 duane a. bailey
 * @see DoublyLinkedList
 */
public class DoublyLinkedNode<E> {
    /**
     * The actual value stored within element; provided by user.
     */
    protected E data;
    /**
     * The reference of element following.
     */
    protected DoublyLinkedNode<E> nextElement;
    /**
     * The reference to element preceding.
     */
    protected DoublyLinkedNode<E> previousElement;

    /**
     * Construct a doubly linked list element.
     *
     * @param v        The value associated with the element.
     * @param next     The reference to the next element.
     * @param previous The reference to the previous element.
     */
    public DoublyLinkedNode(E v,
                            DoublyLinkedNode<E> next,
                            DoublyLinkedNode<E> previous) {
        data = v;
        nextElement = next;
        if (nextElement != null)
            nextElement.previousElement = this;
        previousElement = previous;
        if (previousElement != null)
            previousElement.nextElement = this;
    }

    /**
     * Construct a doubly linked list element containing a value.
     * Not part of any list (references are null).
     *
     * @param v The value referenced by this element.
     * @post constructs a single element
     */
    public DoublyLinkedNode(E v) {
        this(v, null, null);
    }

    /**
     * Access the reference to the next value.
     *
     * @return Reference to the next element of the list.
     * @post returns the element that follows this
     */
    public DoublyLinkedNode<E> next() {
        return nextElement;
    }

    /**
     * Get a reference to the previous element of the list.
     *
     * @return Reference to the previous element.
     * @post returns element that precedes this
     */
    public DoublyLinkedNode<E> previous() {
        return previousElement;
    }

    /**
     * Get value stored within the element.
     *
     * @return The reference to the value stored.
     * @post returns value stored here
     */
    public E value() {
        return data;
    }

    /**
     * Set reference to the next element.
     *
     * @param next The reference to the new next element.
     * @post sets value associated with this element
     */
    public void setNext(DoublyLinkedNode<E> next) {
        nextElement = next;
    }

    /**
     * Set the reference to the previous element.
     *
     * @param previous The new previous element.
     * @post establishes a new reference to a previous value
     */
    public void setPrevious(DoublyLinkedNode<E> previous) {
        previousElement = previous;
    }

    /**
     * Set the value of the element.
     *
     * @param value The new value associated with the element.
     * @post sets a new value for this object
     */
    public void setValue(E value) {
        data = value;
    }

    /**
     * Determine if this element equal to another.
     *
     * @param other The other doubly linked list element.
     * @return True iff the values within elements are the same.
     * @post returns true if this object and other are equal
     */
    public boolean equals(Object other) {
        DoublyLinkedNode that = (DoublyLinkedNode) other;
        if (that == null) return false;
        if (that.value() == null || value() == null) {
            return value() == that.value();
        } else {
            return value().equals(that.value());
        }
    }

    /**
     * Generate hash code associated with the element.
     *
     * @return The hash code associated with the value in element.
     * @post generates hash code for element
     */
    public int hashCode() {
        if (value() == null) return super.hashCode();
        else return value().hashCode();
    }

    /**
     * Construct a string representation of the element.
     *
     * @return The string representing element.
     * @post returns string representation of element
     */
    public String toString() {
        return "<DoublyLinkedNode: " + value() + ">";
    }
}

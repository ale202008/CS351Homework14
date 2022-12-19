package edu.uwm.cs351;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * An implementation of a min-heap that is agnostic
 * on the tree implementation technique, 
 * and use any comparator of the values.
 * @param E type of elements on the heap.
 */
public class MinHeap<E> {
	private CompleteTree<E> tree;
	private Comparator<E> comparator;

	/// Invariant checks:
	
	private static Consumer<String> reporter = (s) -> { System.err.println("Invariant error: " + s); };
	
	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}
	
	/**
	 * Check the subtree that all elements are within the given bound,
	 * and that the elements form a min-heap
	 * @param l subtree to check, may be null
	 * @param bound inclusive lower bound
	 * @return if a problem is found, in which case it has been reported
	 */
	private boolean checkSubtree(CompleteTree.Location<E> l, E bound) {
		if (l.left() != null && comparator.compare(l.left().get(), bound) > 0) return false;
		if (l.right() != null && comparator.compare(l.right().get(), bound) > 0) return false;
		
		if (l.left() != null) return checkSubtree(l.left(), l.left().get());
		if (l.right() != null) return checkSubtree(l.left(), l.left().get());
	
		return true; // TODO
	}
	
	private boolean wellFormed() {
		// TODO: check that fields aren't null
		// and that the tree obeys the min-heap property.
		// (No need to check that the tree is "complete";
		// that's done by the CompleteTree implementation.)
		
		//Invariant 1
		if (tree == null) return report("tree is null");
		
		//Invariant 2
		if (comparator == null) return report("tree is null");
		
		//Invariant 3
		if (tree.root() != null && !checkSubtree(tree.root(), tree.root().get())) return report("not within bounds");

		return true;
	}
	
	/**
	 * Create a min-heap with the given tree (which must not be null)
	 * and comparator (if null, using natural ordering)
	 * @param tree complete tree implementation, must be empty
	 * @param comparator ordering to use, many be null (natural ordering)
	 */
	@SuppressWarnings("unchecked")
	public MinHeap(CompleteTree<E> tree, Comparator<E> comparator) {
		if (!tree.isEmpty()) throw new IllegalArgumentException("tree must be empty");
		if (comparator == null) {
			comparator = (o1, o2) -> ((Comparable<E>)o1).compareTo(o2);;
		}
		this.tree = tree;
		this.comparator = comparator;
		assert wellFormed() : "invariant broken in constructor";
	}
	
	private MinHeap(boolean ignored) {} // do not change this constructor
	
	
	/**
	 * Return the number of elements in the heap.
	 */
	public int size() {
		assert wellFormed() : "invariant broken in size";
		return tree.size(); // TODO (easy)
	}
	
	/**
	 * Add a new value to the min heap.
	 * @param value to add, must be acceptable to the comparator (usually not null)
	 */
	public void add(E value) {
		assert wellFormed() : "invariant broken in add";
		// TODO: put code here (between the two asserts)
		assert wellFormed() : "invariant broken by add";
	}
	
	/**
	 * Return the minimum value in the min-heap.
	 * This is a constant-time operation.
	 * @return minimum element
	 * @exception NoSuchElementException if the heap is empty.
	 */
	public E min() {
		assert wellFormed() : "invariant broken in min";
		if (tree.isEmpty()) throw new NoSuchElementException();
		return null; // TODO
	}
	
	/**
	 * Remove and return the minimum value from this heap.
	 * @return the former minimum value.
	 * @exception NoSuchElementException if the heap is empty.
	 */
	public E removeMin() {
		assert wellFormed() : "invariant broken in removeMin";
		E result = null;
		// TODO
		assert wellFormed() : "invariant broken by removeMin";
		return result;
	}
	
	public static class Spy {
		/**
		 * Return the sink for invariant error messages
		 * @return
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}
		
		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}
		
		/**
		 * Create a min heap with the given data structure.
		 * This method does not check the invariant.
		 * nor does it use the regular constructor that checks the parameters.
		 * @param tree complete tree implementation
		 * @param comp comparator to use
		 * @return new main heap object.
		 */
		public <T> MinHeap<T> createMinHeap(CompleteTree<T> tree, Comparator<T> comp) {
			MinHeap<T> result = new MinHeap<>(false);
			result.comparator = comp;
			result.tree = tree;
			return result;
		}
		
		/**
		 * Return the results of running the invariant checker
		 * on this object.  Errors will likely be reported
		 * to the error reporter.
		 * @param h min heap to check
		 * @return whether the invariant evaluates to true
		 */
		public boolean wellFormed(MinHeap<?> h) {
			return h.wellFormed();
		}
	}
}

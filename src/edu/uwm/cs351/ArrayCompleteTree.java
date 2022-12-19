package edu.uwm.cs351;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Andrew Le
 * Homework 14 COMPSCI 351
 */

/**
 * A dynamic-array implementation of the CompleteTree interface.
 */
public class ArrayCompleteTree<E> implements CompleteTree<E> {
	private static final int INITIAL_CAPACITY = 10;
	
	private E[] data;
	private int manyItems;
	
	private static Consumer<String> reporter = (s) -> { System.err.println("Invariant error: " + s); };

	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}
	
	private boolean wellFormed() {
		if (data == null) return report("array is null");
		if (manyItems < 0) return report("manyItems is negative");
		if (manyItems > data.length) return report("manyItems is too many: " + manyItems + " > capacity = " + data.length);
		return true;
	}
	
	/**
	 * Create a new array of the element type.
	 * @param cap number of elements to create array
	 * @return array of the required size (that pretends to be
	 * of the required type -- do not let this array escape the scope
	 * of this class).
	 */
	@SuppressWarnings("unchecked")
	private E[] makeArray(int cap) {
		return (E[]) new Object[cap];
	}
	
	/**
	 * Ensure that the underlying array has at least the given 
	 * capacity.  If it's necessary to allocate an array,
	 * we allocate one that is at least twice as long.
	 * @param minimumCapacity minimum number of elements needed
	 */
	private void ensureCapacity(int minimumCapacity) {
		if (data.length >= minimumCapacity) return;
		int newCap = data.length*2;
		if (newCap < minimumCapacity) newCap = minimumCapacity;
		E[] newData = makeArray(newCap);
		for (int i=0; i < manyItems; ++i) {
			newData[i] = data[i];
		}
		data = newData;
	}

	/**
	 * Create an empty complete tree.
	 */
	public ArrayCompleteTree() {
		data = makeArray(INITIAL_CAPACITY);
		assert wellFormed(): "invariant broken by constructor";
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return manyItems;
	}


	@Override
	public Location root() {
		// TODO Auto-generated method stub
		if (!isEmpty()) return new Location(1);
		return null;
	}


	@Override
	public Location last() {
		// TODO Auto-generated method stub
		if (!isEmpty()) return new Location(manyItems);
		return null;
	}


	@Override
	public Location add(E value) {
		// TODO Auto-generated method stub
		ensureCapacity(manyItems+1);
		data[manyItems] = value;
		manyItems++;
		return last();
	}

	@Override
	public E remove() {
		// TODO Auto-generated method stub
		if (manyItems == 0) throw new NoSuchElementException();
		E answer = data[manyItems-1];
		data[manyItems-1] = null;
		manyItems--;

		return answer;
	}
	
	// TODO
	// We don't use identity on locations; you can create a new Location
	// whenever you need to return a location.

	private /* non-static */ class Location implements CompleteTree.Location<E> {
		private final int index; // 1-based index into tree

		Location(int index) {
			if (index < 1) throw new IllegalArgumentException("cannot use a negative index");
			this.index = index;
		}
		
		// TODO: implement required Location methods
		// You will need to figure out the (simple) pattern
		// connecting parents and children location numbers.

		@Override // implementation
		public String toString() {
			return "Location(" + index + ")";
		}
		
		@Override // implementation
		public int hashCode() {
			return index;
		}

		@Override // implementation
		public boolean equals(Object obj) {
			if (!(obj instanceof ArrayCompleteTree<?>.Location)) return false;
			ArrayCompleteTree<?>.Location loc = (ArrayCompleteTree<?>.Location)obj;
			return loc.index == index;
		}

		@Override
		public E get() {
			// TODO Auto-generated method stub
			return data[index-1];
		}

		@Override
		public void set(E val) {
			// TODO Auto-generated method stub
			data[index-1] = val;
			
		}

		@Override
		public Location parent() {
			// TODO Auto-generated method stub
			if (index == 1) return null;
			return new Location((index/2));
		}

		@Override
		public Location child(boolean right) {
			// TODO Auto-generated method stub
			
			if (right && (index*2) + 1 <= manyItems) {
				return new Location((index*2)+1);
			}
			else if (!right && index * 2 <= manyItems) {
				return new Location(index * 2);
			}
			
			return null;
		}
		
	}

}

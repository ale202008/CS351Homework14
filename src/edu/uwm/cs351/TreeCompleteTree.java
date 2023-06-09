package edu.uwm.cs351;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

import edu.uwm.cs.util.Pair;
import edu.uwm.cs.util.PowersOfTwo;

/**
 * Andrew Le
 * Homework 14 COMPSCI 351
 */

public class TreeCompleteTree<E> implements CompleteTree<E> {
	private static class Node<T> implements Location<T> {
		Node<T> parent, left, right;
		T data;
		
		Node(T data) {
			this.data = data;
		}
		
		// The following are for the purposes of clients.
		// We don't bother using them.
		@Override // required
		public T get() {
			return data;
		}
		
		@Override // required
		public void set(T val) {
			data = val;
		}
		
		@Override // required
		public Location<T> parent() {
			return parent;
		}
		
		@Override // required
		public Location<T> child(boolean right) {
			return right? this.right : left;
		}
	}
	
	private Node<E> root;
	private int manyNodes;
	// NO MORE FIELDS!
	
	private static Consumer<String> reporter = (s) -> { System.err.println("Invariant error: " + s); };

	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}
	
	private boolean checkSubtree(Node<E> r, Node<E> p, int size) {
		if (r == null) {
			if (size == 0) return true;
			return report("found null tree of supposed size " + size);
		}
		if (r.parent != p) return report("Found bad parent on node with " + r.data);
		if (size <= 0) return report("a non-empty subtree cannot have size = " + size);
		int power = PowersOfTwo.next(size/2);
		int prev = power/2;
		int left = (power+prev > size) ? (size - prev) : power-1;
		return checkSubtree(r.left, r, left) && checkSubtree(r.right, r, size - left - 1);
	}
	
	private boolean wellFormed() {
		return checkSubtree(root, null, manyNodes);
	}

	/**
	 * Create an empty complete tree.
	 */
	public TreeCompleteTree() {
		root = null;
		manyNodes = 0;
		assert wellFormed(): "invariant broken by constructor";
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return manyNodes;
	}

	@Override
	public Location<E> root() {
		// TODO Auto-generated method stub
		if (!isEmpty()) return root;
		return null;
	}

	@Override
	public Location<E> last() {
		// TODO Auto-generated method stub
		if (!isEmpty()) return find(manyNodes).fst();
		return null;
	}

	@Override
	public Location<E> add(E value) {
		// TODO Auto-generated method stub
		assert wellFormed() : "Broken in start of add";
		if (manyNodes == 0) {
			root = new Node<E>(value);
			manyNodes++;
		}
		else {
			Node<E> tempParent = find(manyNodes+1).snd();
			Node<E> newNode = new Node<E>(value);
			newNode.parent = tempParent;
			if (tempParent.left == null) {
				tempParent.left = newNode;
			}
			else {
				tempParent.right = newNode;
			}
			manyNodes++;
		}
		assert wellFormed() : "Broken in end of add";
		return last();
	}

	@Override
	public E remove() {
		// TODO Auto-generated method stub
		if (isEmpty()) throw new NoSuchElementException();
		E temp = null;

		if (manyNodes == 1) {
			temp = root.data;
			root = null;
			manyNodes--;

		}
		else {
			Node<E> removeParent = find(manyNodes).snd();
			Node<E> remove = find(manyNodes).fst();
			if (removeParent.left == remove) {
				removeParent.left = null;
			}
			else
				removeParent.right = null;
			remove.parent = null;
			manyNodes--;
			temp = remove.data;
		}
		
		assert wellFormed() : "Broken in end of remove";
		return temp;
	}
	
	/**
	 * Locate a node in the tree using the algorithm
	 * explained in Activity 14.  It will start with the root and then
	 * go left or right at each step depending on what the algorithm says.
	 * It will return the "lag" (parent) pointer too, which will make it 
	 * easier to handle additions and removals.
	 * <p>
	 * If the node is not in the tree yet, the node may be null.
	 * If the node is at the root, the parent may be null.
	 * <p>
	 * This is useful for accessing the last element, including before we add 
	 * or remove it.
	 * @param n number of node to locate, one-based.
	 * It must be positive and must be no more than one more than 
	 * manyNodes.
	 * @return two nodes, either of which could be null,
	 * the first is the node itself, and the second is its parent
	 */
	private Pair<Node<E>, Node<E>> find(int n) {
		if (n <= 0 || n > manyNodes+1) throw new IllegalArgumentException("bad index " + n);
		Node<E> lag = null;
		Node<E> answer = root;
		int nextPower = PowersOfTwo.next(n/2);
		int positions = (int) ((Math.log(nextPower) / Math.log(2))) - 1;
		
		while (positions >= 0) {
			lag = answer;
            if ((n & (1 << positions)) == 0) {
                answer = answer.left;
            }
            else {
                answer = answer.right;
            }
			positions--;
		}
		



		
		return new Pair<>(answer,lag); // TODO
		// You will need to use PowersOfTwo to find the power
		// of two that represents the first bit in the number.
		// This is the largest power of two which is *less*
		// than the number.  You can compute this as the *next* power of two after
		// half the number.
	}
	
	// TODO: Implement everything
	// Our solution uses "find" three separate times
	
	public static class Spy {
		public static <E> Pair<Location<E>,Location<E>> find(CompleteTree<E> tree, int n) {
			Pair<Node<E>, Node<E>> pair = ((TreeCompleteTree<E>)tree).find(n);
			return new Pair<>(pair.fst(), pair.snd());
		}
	}

}

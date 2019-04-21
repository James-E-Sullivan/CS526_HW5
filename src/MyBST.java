import net.datastructures.*;
import java.util.Comparator;
import java.util.Random;

// generic binary findNode tree
public class MyBST<E> extends LinkedBinaryTree<E> {

	private Comparator<E> comp;
	private int size = 0;

	public MyBST(Comparator<E> c) {comp = c;} // compare by non-naturing ordering
	public MyBST(){ this(new DefaultComparator<E>()); } // compare by natural ordering

	public int size() { return size; }
	public boolean isEmpty() { return size() == 0; }

	/**
	 * Adds new node to tree, or uses node as root of new tree
	 * @param p: The position of the root of the tree (or subtree) to which a new node is added
	 * @param e: The element of the new node to be added
	 * @return the position of the new node that was added. Returns null if there's already node w/ e in tree
	 */
	public Position<E> add(Position<E> p, E e){

		if (p == null){		// this is an empty tree
			addRoot(e);		// Creates root w/ element e, sets size to 1, returns new position
			size++;
			return root;
		}

		Node<E> child = validate(p);
		Node<E> parent = child;		// parent will stay one step behind child as we traverse tree
		while (child != null){										// loop breaks when it reaches a null position
			if (child.getElement() == e)
				return null;										// already an element in tree
			else if (comp.compare(child.getElement(), e) > 0){		// if element of x > e
				parent = child;
				child = child.getLeft();							// x set to its left child
			}
			else{													// if element of x < e
				parent = child;
				child = child.getRight();							// x set to its right child
			}
		}	// end of while. y will be most recent non-null position

		Node<E> temp = createNode(e, null, null, null);		// new node with element e
		temp.setParent(parent);													// node's parent set to y

		if (comp.compare(parent.getElement(), e) > 0)				// if temp < parent
			parent.setLeft(temp);									// set parent's left child to temp
		else
			parent.setRight(temp);									// else set parent's right to temp

		size++;
		return temp;
	}

	// print a binary tree horizontally using indentation
	public void print(Position<E> p, int depth){

		  Node<E> n = validate(p);
	      int i;

	      for (i = 1; i <= depth; i++)
	         System.out.print("    ");
	      System.out.println(n.getElement());

	      if (n.getLeft() != null)
	         print(n.getLeft(), depth+1);
	      else if (n.getRight() != null)
	      {
	         for (i = 1; i <= depth+1; i++)
	            System.out.print("    ");
	         System.out.println("--");
	      }

	      if (n.getRight() != null)
	         print(n.getRight(), depth+1);
	      else if (n.getLeft() != null)
	      {
	         for (i = 1; i <= depth+1; i++)
	            System.out.print("    ");
	         System.out.println("--");
	      }
	   }

	// print a binary tree using inorder tree traversal
	public void inorderPrint(Position<E> p){
		if (p == null) return;
		Node<E> n = validate(p);
		inorderPrint(n.getLeft());
		System.out.print(n.getElement() + "  ");
		inorderPrint(n.getRight());
	}


    /**
     * Creates pseudo-random int (e) between 0 and 1000000
     * @return int e
     */
	private static int getRandomInt(Random r){
        int e;
        e = r.nextInt(1000000);			// random int w/ 1000000 upper bound
        return e;
    }

	/**
	 * Returns the position of the "successor" of p.
	 * Successor is node which has the smallest element that is larger than element
	 * e of p.
	 * @param p: Position of the node whose successor is searched
	 * @return successorPosition: Position of successor of p (if none, return null)
	 */
	private Position<E> successor(Position<E> p){
		Node<E> n = validate(p);
		if (n.getRight() != null)				// if right child exists
			return treeMinimum(n.getRight());	// return node w/ min value in right child subtree
		Node<E> y = n.getParent();

		// return successor or return null
		while (y != null && n == y.getRight()){
			n = y;
			y = y.getParent();
		}
		return y;
	}

	/**
	 * Returns position of the "predecessor" of p.
	 * Predecessor is node which has the largest element smaller than
	 * element e of p.
	 * @param p: Position of node whose predecessor is searched
	 * @return predPosition: Position of p's predecessor (if none, return null)
	 */
	private Position<E> predecessor(Position<E> p){
		Node<E> n = validate(p);
		if (n.getLeft() != null)				// if left child exists
			return treeMaximum(n.getLeft());	// return nod w/ max value in left child subtree
		Node<E> y = n.getParent();

		// return predecessor or return null
		while (y != null && n == y.getLeft()){
			n = y;
			y = y.getParent();
		}
		return y;
	}

	/**
	 * Deletes node w/ element e from tree (or subtree) with root Position p.
	 * @param p: Position of the root of tree (or subtree) containing
	 *            node w/ element e (to be deleted)
	 * @param e: Element of node to be deleted
	 * @return element of deleted node (otherwise null).
	 */
	public E delete(Position<E> p, E e){

	    // search for node and test if it is null
	    Position<E> foundPosition = findNode(p, e);
	    if (foundPosition == null)
	        return null;

		Node<E> toBeDeleted = validate(foundPosition);		// toBeDeleted set to position w/ element e

		if (numChildren(toBeDeleted) <= 1)		// if number of children is 0 or 1
			return remove(toBeDeleted);			// remove node and return its element

		// get predecessor node of toBeDeleted
		Node<E> predecessorNode = validate(predecessor(toBeDeleted));

		// swap elements of toBeDeleted and its predecessor
		E tempElement = toBeDeleted.getElement();
		toBeDeleted.setElement(predecessorNode.getElement());
		predecessorNode.setElement(tempElement);

		// remove predecessor (with former toBeDeleted value), return its element
		return remove(predecessorNode);
	}

	/**
	 * Searches binary findNode tree and returns position with element e (or null)
	 * @param root: root position of tree or sub-tree to be searched
	 * @param e: element for which you are searching
	 * @return Node n with element e, or null:
	 */
	private Position<E> findNode(Position<E> root, E e){
		Node<E> n = validate(root);		// n starts as root of tree or subtree to be searched
		while (n != null){
			if (comp.compare(n.getElement(), e) == 0){return n;}	// Position found
			else if (comp.compare(n.getElement(), e) < 0){n = n.getRight();}	// element at p<e
			else if (comp.compare(n.getElement(), e) > 0){n = n.getLeft();}		// element at p>e
		}
		return null;		// Position with element not found
	}


	/**
	 * Finds node w/ maximum value in tree/subtree with root p
	 * @param p: root of tree or subtree
	 * @return n: the node with tree's maximum value
	 */
	private Position<E> treeMaximum(Position<E> p){
		Node<E> n = validate(p);
		while (n.getRight() != null)
			n = n.getRight();
		return n;
	}

	/**
	 * Finds node w/ minimum value in tree/subtree with root p
	 * @param p: root of tree or subtree
	 * @return n: the node with tree's minimum value
	 */
	private Position<E> treeMinimum(Position<E> p){
		Node<E> n = validate(p);
		while (n.getLeft() != null)
			n = n.getLeft();
		return n;
	}

	/**
	 * Creates a test MyBST using elements in testArray.
	 * Prints values of testTree in order, and prints a visual of tree layout.
	 * Prints the predecessor and successor of every tree element.
     * Tests if deleting a non-existent value returns null.
	 * Continuously deletes root of tree until tree empty (prints each step).
	 * @param testArray: array of elements to be inserted into a test tree
	 */
	private static void testMethods(int[] testArray){
		MyBST<Integer> testTree = new MyBST<>();

		// create MyBST testTree with values from testArray
		for (int e : testArray)
			testTree.add(testTree.root, e);

		// Print out testTree elements and a visual of the tree
		System.out.println("Values of testTree, in order: ");
		testTree.inorderPrint(testTree.root);
		System.out.println("\n\nHorizontal visual of testTree: \n");
		testTree.print(testTree.root, 0);

		// test predecessor and successor methods
		for (int e : testArray){
			Position<Integer> testPredecessor = testTree.predecessor(
					testTree.findNode(testTree.root, e));
			Position<Integer> testSuccessor = testTree.successor(
					testTree.findNode(testTree.root, e));

			System.out.println("\nElement: " + e);

			if (testPredecessor == null)
				System.out.println("Predecessor: null");
			else
				System.out.println("Predecessor: " + testPredecessor.getElement());

			if (testSuccessor == null)
				System.out.println("Successor: null");
			else
				System.out.println("Successor: " + testSuccessor.getElement());
		}

		// test delete method on non-existent position; assumes - 999999 not in tree
        Integer testDelete = testTree.delete(testTree.root, -999999);
		if (testDelete == null)
		    System.out.println("\ndelete method: Non-existent position correctly returns null.");
		else
		    System.out.println("\ndelete method: Non-existent position falsely returned value.");

		// test delete method by deleting the root of the tree
		System.out.println("\nDeleting root node until tree is empty: ");
		System.out.println("----------------------------------------");
		while(testTree.root != null){
			testTree.print(testTree.root, 0);
			testTree.delete(testTree.root, testTree.root.getElement());
			System.out.println("----------------------------------------");
		}
		System.out.println();
	}

	public static void main(String[] args) {

		int[] testArray1 = new int[] {100, 50, 150, 35, 70, 130, 120, 140};
		testMethods(testArray1);

		int[] testArray2 = new int[] {44, 17, 8, 32, 28, 21, 29, 88, 65,
				54, 82, 76, 68, 80, 97, 93};
		testMethods(testArray2);

	}

}

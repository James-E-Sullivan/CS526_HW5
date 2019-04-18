import net.datastructures.*;

import java.util.Comparator;
import java.util.Random;

// generic binary search tree
public class MyBST<E> extends LinkedBinaryTree<E> {

	private Comparator<E> comp;
	private int size = 0;
	
	public MyBST(Comparator<E> c) {comp = c;}; // compare by non-naturing ordering
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
	
	public static void main(String[] args) {
		
		MyBST<Integer> t =   new MyBST<>();

		// test add method
		t.add(t.root, 100);
		t.add(t.root, 50);
		t.add(t.root, 150);
		t.add(t.root, 70);
		t.add(t.root, 30);
		t.add(t.root, 130);
		t.add(t.root, 140);
		t.add(t.root, 120);
		
		System.out.println("Number of nodes is: " + t.size);
		
		System.out.println("Print tree horizontally using indentation: ");
		t.print(t.root, 0);
		System.out.println("\n");
		
		System.out.println("Print tree by inorder traversal: ");
		t.inorderPrint(t.root);

		System.out.println("\n");


		Random r = new Random();					// instantiate new Random object
		r.setSeed(System.currentTimeMillis());		// set seed based on current system time


        int heightSum = 0;						// int used to sum the height of all trees
        int i;
        for (i=1; i <= 100; i++){				// create 100 trees
            int j;
            MyBST<Integer> avgHeightTree = new MyBST<>();
            for (j=0; j < 1000; j++){									// fill each tree with 1000 nodes
                Integer randomNumber = getRandomInt(r);					// get random number
                avgHeightTree.add(avgHeightTree.root, randomNumber);	// each node element is pseudo-random number
            }

            int maxHeight = avgHeightTree.height(avgHeightTree.root);	// height is max depth of Tree, from root

            System.out.println("Height = " + maxHeight + ", " + "Size = " + avgHeightTree.size);
            heightSum += maxHeight;		// add height of tree to the previous sum of all tree heights
        }

        double avgHeight = (double) heightSum / i;						// average height of the 100 trees
        System.out.println("\nAverage height = " + avgHeight +
                " // this is the average of " + (i-1) + " heights");
	}

}

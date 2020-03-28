
package axm170039;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
/**
 *
 * SP7
 * Author: axm170039 (Arun Babu Madhavan), rxs180125 (Rahul Sharma)
 * Created as part of Short project 7 - Implementation of Data Structures (Spring 2020)
 * 
 * Implementation of Binary Search Tree
 *
 *
 **/

public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {

    /***
     * Entry for the Binary Search tree
     * @param <T> Generic type, T extends comparable.
     */
    static class Entry<T> {
        T element;
        Entry<T> left, right;

        /***
         * Constructor
         * @param x element to be stored in the binary search tree
         * @param left entry to the left of the bst
         * @param right entry to the right of the bst
         */
        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
	        this.left = left;
	        this.right = right;
        }
        /***
         * Contructor
         * @param x element to be stored in the binary search tree
         */
        public Entry(T x) {
           this(x,null,null);
        }
    }
    
    Entry<T> root;
    int size;
    //Stack that stores the parent of the current node during traversal
    Stack<Entry<T>> findPath;

    /**
     * Constructor
     */
    public BinarySearchTree() {
	    root = null;
	    size = 0;
    }

    /***
     * Find the element x in the bst
     * @param x element to find
     * @return the entry of the element if exists in the BST, otherwise null
     */
    private Entry<T> find(T x){
       findPath  = new Stack<>();
       findPath.push(null);
       return find(root,x);
    }

    /***
     * Find the Entry x in the subtree at root t
     * @param t root node of the subtree
     * @param x element to find
     * @return the entry of the x if exists in the BST, otherwise null 
     */
    private Entry<T> find(Entry<T> t, T x){
        if(t == null || t.element.compareTo(x) == 0)
            return t;
        while(true){
            if(x.compareTo(t.element) < 0){
                if(t.left == null) break;
                findPath.push(t); t = t.left;
            }
            else if(x.compareTo(t.element) == 0) break;
            else {
                if(t.right == null) break;
                findPath.push(t); t = t.right;
            }
        }
        return t;
    }


    /**
     * Checks if the element x exists in the tree
     * @param x element to search
     * @return true if the tree contains x, otherwise false.
     */
    public boolean contains(T x) {
        Entry<T> t = find(x);
	    return t != null && t.element.equals(x);
    }

    /***
     * Searches for the element and returns the same element that is equal to x in the tree
     * @param x element to search
     * @return x if tree contains x, otherwise null;
     */
    public T get(T x) {
        Entry<T> t = find(x);
        if(t != null && t.element.equals(x))
            return x;
        return null;
    }


  
    public boolean add(Entry<T> entry) {
        if(size ==0){
            root = entry;
            size++; return true;
        }
        else{
            Entry<T> t = find(entry.element);
            if(t.element.compareTo(entry.element) == 0) return false;
            if(entry.element.compareTo(t.element)<0)
                t.left = entry;
            else
                t.right = entry;
            size++;
            findPath.push(t);
            return true;
        }
    }
    /***
     * Adds x to the tree
     * @param x element to be added.
     * @return true if x is added to the tree, false if x already exists
     */
    public boolean add(T x) {
      return  add(new Entry<>(x));
    }

 
    /***
     * Remove x from the tree
     * @param x element to be removed
     * @return x if it exists in the tree and removed successfully, otherwise null
     */
    public T remove(T x) {
       if(size == 0) return null;
       Entry<T> t = find(x);
       if(t.element.compareTo(x) != 0) return null;
      
       if(t.left == null || t.right == null)
        splice(t);
       else
       {
           findPath.push(t);
           Entry<T> minRight = find(t.right,x);
           t.element = minRight.element;
           splice(minRight);
       } 
       size--;
       return x;    
    }

    protected void leftRotate(Entry<T> x){
        Entry<T> rightChild = x.right;
        x.right = rightChild.left;
        if(findPath.isEmpty()){
            root = rightChild;
            return;
        }
        Entry<T> parent = findPath.peek();
        
        if(isLeftChild(x)){
            parent.left = rightChild;
        }
        else{
            parent.right = rightChild;
        }
        // findPath.push(parent);

    }

    protected void rightRotate(Entry<T> x){
        Entry<T> leftChild = x.left;
        x.left = leftChild.right;
        Entry<T> parent = findPath.peek();
        if(findPath.isEmpty()){
            root = leftChild;
            return;
        }
        if(isLeftChild(x)){
            parent.left = leftChild;
        }
        else{
            parent.right = leftChild;
        }
        // findPath.push(parent);
    }


    protected Boolean isLeftChild(Entry<T> x){
        Entry<T> parent = findPath.peek();
        return parent.element.compareTo(x.element) > 0;
    }

   

    protected Boolean isRightChild(Entry<T> x){
        Entry<T> parent = findPath.peek();
        return parent.element.compareTo(x.element) < 0;
    }


    protected Entry<T> uncle(Entry<T> x){
        Entry<T> parent = findPath.pop();
        Entry<T> result;
        if(isLeftChild(parent)){
            result = findPath.peek().right;
        }
        else{
            result = findPath.peek().left;
        }
        findPath.push(parent);
        return result;
    }

    /***
     * Splice the subtree at root t such that t is replaced with child of t.
     * Precondition: t should have only one child
     * @param t root of the subtree, the entry to be reomved
     */
    public void splice(Entry<T> t){
        Entry<T> parent = findPath.peek();
        Entry<T> child = t.left == null ? t.right : t.left;
        if(parent == null)
            root = child;
        else if(parent.left == t)
            parent.left = child;
        else 
            parent.right = child;
    }

    /***
     * Return the elment  with minimum value in the tree
     * @return the element with minimum value in the BST, null if the tree has no element.
     */
    public T min() {
        if(size == 0)
            return null;
        Entry<T> t = root;
        while(t.left != null)
            t = t.left;
        return t.element;
    }
   /***
     * Return the elment  with ,maximum value in the tree
     * @return the element with maximum value in the BST, null if the tree has no element.
     */
    public T max() {
        if(size == 0)
        return null;
        Entry<T> t = root;
        while(t.right != null)
            t = t.right;
        return t.element;
    }

    /***
     * Creates an array with elements in in-order traversal of the tree 
     * @return the array of elments from the tree in in-order (ascending order) traversal
     */
    public Comparable[] toArray() {
        if(size == 0)
             return null;
        Comparable[] arr = new Comparable[size];
        List<T> lst = new ArrayList<>();
        Entry<T> t = root;
        inOrder(t, lst);
	    return lst.toArray(arr);
    }

    /***
     * Performs in-order traversal of the tree and stores the path in t
     * @param t root of the subtree
     * @param lst list where the inorder traversal is stored
     */
    public void inOrder(Entry<T> t, List<T> lst){
        if(t!=null) {
            inOrder(t.left, lst);
            lst.add(t.element);
            inOrder(t.right, lst);
        }
    }


// Start of Optional problem 2

    /** Optional problem 2: Iterate elements in sorted order of keys
	Solve this problem without creating an array using in-order traversal (toArray()).
     */
    public Iterator<T> iterator() {
	    return new BinarySearchTreeIterator<T>(root);
    }

    public static class BinarySearchTreeIterator<T> implements Iterator<T> {
        Stack<Entry<T>> stack;
        int state = 0;
        public BinarySearchTreeIterator(Entry<T> root){
            stack = new Stack<>();
            pushLeftChildrenToStack(root);
        }
        @Override
        public boolean hasNext() {
           return !stack.empty();
        }

        @Override
		public T next() {
            Entry<T> t = stack.pop();
            T result = t.element;
            if(t.right!=null){
                pushLeftChildrenToStack(t.right);
            }
            return result;
        }

        private void pushLeftChildrenToStack(Entry<T> root){
            Entry<T> t = root;
            while(t !=null){
                stack.push(t);
                t = t.left;
            }
        }
    }

    /***
     * Main method for testing merge topological sort
     * @param args optional arguments
     * @throws Exception
     */
    public static void main(String[] args) {
	BinarySearchTree<Integer> t = new BinarySearchTree<>();
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
                System.out.println("Min: " + t.min() +", Max: " + t.max());

            } else if(x < 0) {
                if(t.remove(-x) ==null)
                     System.out.print("Element does not exist in the tree");
                System.out.print("Remove " + x + " : ");
                t.printTree();
                
                System.out.println("Min: " + t.min() +", Max: " + t.max());
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for(int i=0; i<t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                System.out.println("Min: " + t.min() +", Max: " + t.max());
                return;
            }           
        }
    }


    /***
     * Prints the size and inorder of the tree from the root
     */
    public void printTree() {
	    System.out.print("[" + size + "]");
	    printTree(root);
	    System.out.println();
    }

    /***
     * Performs in order traversal of the subtree and prints the tree in ascendeing order of the value
     * @param node root of the sub tree
     */
    void printTree(Entry<T> node) {
	    if(node != null) {
	         printTree(node.left);
	         System.out.print(" " + node.element);
	         printTree(node.right);
        }
    }
}

/*
Sample input:
1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0

Output:
Add 1 : [1] 1
Add 3 : [2] 1 3
Add 5 : [3] 1 3 5
Add 7 : [4] 1 3 5 7
Add 9 : [5] 1 3 5 7 9
Add 2 : [6] 1 2 3 5 7 9
Add 4 : [7] 1 2 3 4 5 7 9
Add 6 : [8] 1 2 3 4 5 6 7 9
Add 8 : [9] 1 2 3 4 5 6 7 8 9
Add 10 : [10] 1 2 3 4 5 6 7 8 9 10
Remove -3 : [9] 1 2 4 5 6 7 8 9 10
Remove -6 : [8] 1 2 4 5 7 8 9 10
Remove -3 : [8] 1 2 4 5 7 8 9 10
Final: 1 2 4 5 7 8 9 10 

*/

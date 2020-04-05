/**
 * 
 * Red Black Tree Implementation
 * 
 * @authors (sxr170016)	Srikumar Ramaswamy, (axm170039) Arun Babu Madhavan, (axk180031)	Andry Thomas Kozhikkadan, (sxs180036) Sravya Singireddy
 * Created as part of Long Project 3 - Implementation of Data Structures (Spring 2020)
 * 
 */
package axm170039;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class RedBlackTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    

     /***
     * Entry for the Red Black tree
     * @param <T> Generic type, T extends Entry of the BST.
     */
    static class Entry<T> extends BinarySearchTree.Entry<T> {
        boolean color;

        /***
         * Constructor
         * @param x element to be stored in the red black tree
         * @param left entry to the left of the rbt
         * @param right entry to the right of the rbt
         */
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            color = RED;
        }

        /**
         * Check if the color of the node is Red
         * @return true of the node is red else false
         */
        boolean isRed() {
	        return color == RED;
        }

         /**
         * Check if the color of the node is Black
         * @return true of the node is black else false
         */
        boolean isBlack() {
	        return color == BLACK;
        }

        /**
         * to string method to print the element with its color in RBT
         */
        public String toString(){
            if(element!=null)
                return (color? "RED ":"BLACK " )+  element.toString();
            else
                return "NIL";
        }

    }

    // NIL node which forms the leaf node in RBT
    private Entry<T> NIL;

    /**
     * Constructor
     */
    public RedBlackTree() {
        super();
        NIL = new Entry(null,null,null);
        NIL.color = BLACK;
    }

    /***
     * Get the parent of the given node
     * @param x child node
     * @return parent node of the child, null if x is root
     */
    private Entry<T> parent(Entry<T> x){
        return (Entry<T>)super.parent(x);
    }

    /***
     * Get the Uncle of the given node
     * @param x node
     * @return the child of the x's grand parent which is not the parent(x)
     */
    private Entry<T> uncle(Entry<T> x){
        return (Entry<T>)super.uncle(x);
    }

    /***
     * return the sibling of given node x
     * @param x node
     * @return the child of the x's parent which is not equal to x
     */
    private Entry<T> sib(Entry<T> x){
        if(x == root)
            return null;
        if(x.isLeftChild()) 
           { 
               return (Entry<T>)parent(x).right;
           }
        else
            return (Entry<T>)parent(x).left;
    }
    
    
    /***
     * Verify the properties of Red black tree 
     * @return true of all the properties of Red Black tree is satisfied
     */
    public boolean verifyRBT(){
        boolean prop1,  // The root is black
                prop2,  // All leaf nodes are black, All leaf nodes are black nil nodes
                prop3,  // The parent of a red node is black or red parent has black children
                prop4;  // The number of black nodes on each path from the root to a leaf node is the same
        HashSet<Entry<T>> leaves = new HashSet<>();

        prop1 = ((Entry<T>)(root)).isBlack();
        
        Queue<Entry<T>> bfs = new LinkedList<>(); 
        prop3 = true; //Initialize

        bfs.add((Entry<T>)root);
        while(!bfs.isEmpty() && prop3){
            Entry<T> rt =  bfs.remove();
            if(rt.isRed()) {
                prop3 = ((Entry<T>)rt.left).isBlack();
                prop3 = prop3 && ((Entry<T>)rt.right).isBlack();
            }
            
            if(rt.left != null)
            { 
                bfs.add((Entry<T>)rt.left);
            }
            if(rt.right != null)
            {
                bfs.add((Entry<T>)rt.right);
            }
            if(rt.left == null && rt.right == null){
                leaves.add(rt);
            }
        }
       prop2 = leaves.size() == 1;

       prop4 = getBlackHeight((Entry<T>)root) != -1;

       return prop1 && prop2 && prop3 && prop4;
    }

    /***
     * Recursive function to compute number of black nodes in the subtree from the node to the leaf
     * @param node root node of the sub tree
     * @return
     */
    int getBlackHeight(Entry<T> node){
        if(node == null)
            return 0;
        
        int leftHeight = getBlackHeight((Entry<T>)node.left);
        int rightHeight = getBlackHeight((Entry<T>)node.right);
        
        if(leftHeight == -1 || rightHeight == -1 || leftHeight!=rightHeight )
            return -1;
        
        return rightHeight + (node.isBlack()? 1:0);

    }

     /***
     * Copies element value from source node to destination node
     * @param dest destination node
     * @param src source node
     */
    protected void copy(BinarySearchTree.Entry<T> dest, BinarySearchTree.Entry<T> src){
        super.copy(dest,src);
        ((Entry<T>)dest).color = ((Entry<T>)src).color;
    }


    /***
     * Remove the entry t from the tree
     * @param t entry to be removed
     * @return element stored in entry t if it exists, otherwise null
     */
    public T remove(T x)
    {
        if(size == 0) return null;
        Entry<T> t = (Entry<T>)find(x);
        if(t.element.compareTo(x) != 0) return null;
        super.remove(t);
        boolean splicedEntryColor = ((Entry<T>)splicedEntry).color; 
        Entry<T> cursor = (Entry<T>)splicedChild;

        if(splicedEntryColor == BLACK) {
            fixUp(cursor);
        }
        return x;
    }

    /**
     * Fix up fixes the RBT property after deletiion of a black node such that black height of children each node is the tree remains constant
     * @param cursor the child of the node spliced during delete
     */
    private void fixUp(Entry<T> cursor){
        Entry<T> sibling, parent, sibling_left, sibling_right;
        while(cursor != root && cursor.isBlack()){
            if(cursor.isLeftChild())
            {
                sibling = sib(cursor);
                if(sibling.isRed()) // Case 1
                { 
                    sibling.color = BLACK;
                    parent = parent(cursor);
                    parent.color = RED;
                    leftRotate(parent);
                    sibling = sib(cursor);
                }
                if(sibling == NIL) break;
                sibling_left = (Entry<T>)sibling.left;
                sibling_right = (Entry<T>)sibling.right;

                if(sibling_left.isBlack() && sibling_right.isBlack()) //  Case 2
                {
                    sibling.color = RED;
                    cursor = parent(cursor);
                }
                else
                {
                    if(sibling_left != NIL && sibling_right.isBlack())  // Case 3
                    {
                        sibling_left.color = BLACK;
                        sibling.color = RED;
                        rightRotate(sibling);
                        sibling = sib(cursor);
                    }

                    if(sibling == NIL) break;
                    sibling_left = (Entry<T>)sibling.left;
                    sibling_right = (Entry<T>)sibling.right;

                    sibling_right.color = BLACK; // Case 4
                    parent = parent(cursor);
                    sibling.color = parent.color;
                    parent.color = BLACK;
                    leftRotate(parent);
                    cursor = (Entry<T>)root;
                }
            }
            else
            {
                sibling = sib(cursor);
             
                if(sibling.isRed()) // Case 1
                {
                    sibling.color = BLACK;
                    parent = parent(cursor);
                    parent.color = RED;
                    rightRotate(parent);
                    sibling = sib(cursor);
                }
                if(sibling == NIL) break;
                sibling_left = (Entry<T>)sibling.left;
                sibling_right = (Entry<T>)sibling.right;

                if(sibling_left.isBlack() && sibling_right.isBlack()) //  Case 2
                {
                    sibling.color = RED;
                    cursor = parent(cursor);
                }
                else
                {
                    if(sibling_right != NIL && sibling_left.isBlack())  // Case 3
                    {
                        sibling_right.color = BLACK;
                        sibling.color = RED;
                        leftRotate(sibling);
                        sibling = sib(cursor);
                    }
                    if(sibling == NIL) break;
                    sibling_left = (Entry<T>)sibling.left;
                    sibling_right = (Entry<T>)sibling.right;

                    sibling_left.color = BLACK; // Case 4
                    parent = parent(cursor);
                    sibling.color = parent.color;
                    parent.color = BLACK;
                    rightRotate(parent);
                    cursor = (Entry<T>)root;
                }
            }
        }
    }

     /***
     * Adds x to the tree
     * @param x element to be added.
     * @return true if x is added to the tree, false if x already exists
     */
    public boolean add(T x){
        Entry<T> cursor = new Entry<T>(x,NIL,NIL);
        if(!super.add(cursor))
            return false;
        Entry<T> parent = parent(cursor);
        Entry<T> uncle, grandParent;
        while(cursor!=root && size > 3 && parent.color != BLACK)
        {
            if(parent.isLeftChild())
            {
                uncle = uncle(cursor);
                if(uncle.isRed())
                {
                    parent.color = uncle.color = BLACK;
                    cursor = parent(parent);
                    cursor.color = RED;
                }
                else 
                {
                    if(cursor.isRightChild())
                    {
                        cursor = parent(cursor);
                        leftRotate(cursor);
                    }
                    parent = parent(cursor);
                    parent.color = BLACK;
                    grandParent = parent(parent);
                    if(grandParent != null){
                        grandParent.color = RED;
                        rightRotate(grandParent);
                    }
                }
            }
            else 
            {
                uncle = uncle(cursor);
                if(uncle.isRed())
                {
                    parent.color = uncle.color = BLACK;
                    cursor = parent(parent);
                    cursor.color = RED;
                }
                else
                {
                    if(cursor.isLeftChild())
                    {
                        cursor = parent(cursor);
                        rightRotate(cursor);
                    }
                    parent = parent(cursor);
                    parent.color = BLACK;
                    grandParent = parent(parent);
                    if(grandParent != null){
                        grandParent.color = RED;
                        leftRotate(grandParent);
                    }
                }
            }
            parent = parent(cursor);
        }
        ((Entry<T>)root).color = BLACK;
        return true;
    }
}


/** Starter code for Red-Black Tree
 */
package axm170039;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class RedBlackTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    

    static class Entry<T> extends BinarySearchTree.Entry<T> {
        boolean color;
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            color = RED;
        }

        boolean isRed() {
	        return color == RED;
        }

        boolean isBlack() {
	        return color == BLACK;
        }

        public String toString(){
            if(element!=null)
                return (color? "RED ":"BLACK " )+  element.toString();
            else
                return "NIL";
        }

    }

    private Entry<T> NIL;

    RedBlackTree() {
        super();
        NIL = new Entry(null,null,null);
        NIL.color = BLACK;
    }

    private Entry<T> parent(Entry<T> x){
        return (Entry<T>)super.parent(x);
    }

    private Entry<T> uncle(Entry<T> x){
        return (Entry<T>)super.uncle(x);
    }

    private Entry<T> sib(Entry<T> x){
        if(x == root)
            return null;
        if(x.isLeftChild()) 
            return (Entry<T>)parent(x).right;
        else
            return (Entry<T>)parent(x).left;
    }
    
    
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

    int getBlackHeight(Entry<T> cur){
        if(cur == null)
            return 0;
        
        int leftHeight = getBlackHeight((Entry<T>)cur.left);
        int rightHeight = getBlackHeight((Entry<T>)cur.right);
        
        if(leftHeight == -1 || rightHeight == -1 || leftHeight!=rightHeight )
            return -1;
        
        return rightHeight + (cur.isBlack()? 1:0);

    }

    public T remove(T x)
    {
        if(size == 0) return null;
        Entry<T> t = (Entry<T>)find(x);
        if(t.element.compareTo(x) != 0) return null;
        super.remove(t);
        boolean splicedEntryColor = ((Entry<T>)splicedEntry).color; 
        
        Entry<T> cursor = (Entry<T>)splicedChild;

        if(splicedEntryColor == BLACK){
            fixUp(cursor);
        }
        return x;
    }

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
                    if(sibling_right.isBlack())  // Case 3
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
                try{
                sibling = sib(cursor);
                }
                catch(Exception e){
                    throw e;
                }

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
                    if(sibling_left.isBlack())  // Case 3
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

    public static void main(String args[]){
        RedBlackTree<Integer> rbt = new RedBlackTree<>();

      //  int[] input = new int[] {7,3,10,22,8,11,26,63,2,23,21,18,20,27,1,5,19};
      int[] input = new int[] {47, 32, 71, 65,  87, 51, 82, 93};
        System.out.println();
        for(int i=0;i<input.length;i++){
            rbt.add(input[i]);
        }
        rbt.remove(87);
        //  rbt.remove(63);
        // rbt.remove(8);
        // rbt.remove(27);
        // rbt.remove(11);






        
        for(int i=0;i<input.length;i++){
            if(!rbt.contains(input[i])){
                System.out.println("Contains fail : " + input[i]);
            }
        }

        System.out.println("VerifyRBT : " +  rbt.verifyRBT());

    }
}


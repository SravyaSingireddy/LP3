/** Starter code for Red-Black Tree
 */
package axm170039;

import java.util.Comparator;
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
          /***
         * Contructor
         * @param x element to be stored in the binary search tree
         */
        public Entry(T x) {
            this(x,null,null);
         }

        boolean isRed() {
	        return color == RED;
        }

        boolean isBlack() {
	        return color == BLACK;
        }

    }

    private Entry<T> NIL;

    RedBlackTree() {
        super();
        NIL = new Entry(null,null,null);
        NIL.color = BLACK;
    }

    protected Entry<T> grandParent(){
        findPath.pop();
        return (Entry<T>)findPath.pop();
    }
    
    public boolean verifyRBT(){
        boolean prop1, prop2 = true, prop3 = true, prop4, prop5 = true;
        
        prop1 = ((Entry<T>)(root)).isBlack();
        
        Queue<Entry<T>> bfs = new LinkedList<>(); 
        bfs.add((Entry<T>)root);
        while(!bfs.isEmpty() && prop3){
            Entry<T> rt =  bfs.remove();
            if(rt.isRed()) {
                prop3 = rt.left == null || ((Entry<T>)rt.left).isBlack();
                prop3 = prop3 && (rt.right == null || ((Entry<T>)rt.right).isBlack());
            }
            if(rt.left!=null)
              { 
                   bfs.add((Entry<T>)rt.left);
              }
            if(rt.right!=null)
               {
                    bfs.add((Entry<T>)rt.right);
                }
        }

       prop4 = getBlackHeight((Entry<T>)root) != -1;
      

        return prop1 && prop2 && prop3 && prop4 && prop5;
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

   

    public boolean add(T x){
        Entry<T> cursor = new Entry<T>(x);
        if(!super.add(cursor))
            return false;
        while(cursor!=root && size > 3 && ((Entry<T>)findPath.peek()).isRed())
        {
            Entry<T> parent = (Entry<T>)findPath.pop();
            Entry<T> grandParent;
            if(isLeftChild(parent)){
                findPath.push(parent);
                Entry<T> uncle = (Entry<T>)uncle(cursor);
                if(uncle.isRed()){
                    parent.color = uncle.color = BLACK;
                    cursor = grandParent();
                    cursor.color = RED;
                }
                else{
                     findPath.push(parent);
                     if(isRightChild(cursor)){
                         cursor = (Entry<T>)findPath.pop();
                         leftRotate(cursor);
                     }
                     if(parent!=null)
                     {
                     parent = (Entry<T>)findPath.pop();
                     parent.color = BLACK;
                     grandParent = (Entry<T>)findPath.pop();
                     if(grandParent != null){
                     grandParent.color = RED;
                     rightRotate(grandParent);
                     findPath.push(grandParent);

                     }
                     else
                        leftRotate(parent);
                     }
                     findPath.push(parent);

                }
            }
            else {
                findPath.push(parent);
                Entry<T> uncle = (Entry<T>)uncle(cursor);
                if(uncle.isRed()) {
                    parent.color = uncle.color = BLACK;
                    cursor = grandParent();
                    cursor.color = RED;
                }
                else{
                     if(isLeftChild(cursor)){
                         cursor = (Entry<T>)findPath.pop();
                         rightRotate(cursor);
                     }
                     parent = (Entry<T>)findPath.pop();
                     if(parent!=null)
                     {
                     parent.color = BLACK;

                     grandParent = (Entry<T>)findPath.pop();
                     if(grandParent != null){
                        grandParent.color = RED;
                        leftRotate(grandParent);
                        findPath.push(grandParent);
                    }
                    else
                        leftRotate(parent);
                        findPath.push(parent);
                }
                }
            }
        }
        ((Entry<T>)root).color = BLACK;
        return true;
    }

    public static void main(String args[]){
        RedBlackTree<Integer> rbt = new RedBlackTree<>();
        rbt.add(39);
        rbt.add(35);
        rbt.add(70);
        rbt.add(20);
        rbt.add(38);
        rbt.add(50);
        rbt.add(75);
      
        rbt.add(40);
        rbt.add(65);
        rbt.add(80);
        rbt.add(60);
        rbt.add(30);







        
    }
}


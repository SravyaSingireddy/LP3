/**
 * 
 * SkipList: Skiplist Implementation
 * 
 * @authors (sxr170016)	Srikumar Ramaswamy, (axm170039) Arun Babu Madhavan, (axk180031)	Andry Thomas Kozhikkadan, (sxs180036) Sravya Singireddy
 * Created as part of Long Project 3 - Implementation of Data Structures (Spring 2020)
 * 
 */
package axm170039;

import java.util.Iterator;
import java.util.Random;

public class SkipList<T extends Comparable<? super T>> {
    static final int maxLevel = 32;
    Entry<T> head, tail;
    Entry<T>[] pred;
    int size;
    Random random;
    int[] span;

    /***
     * Entry for SkipList
     * @param <E> Generic type, E extends comparable.
     */
    static class Entry<E> {
	    E element;
	    Entry<E>[] next;
        Entry<E> prev;
        int[] width;

        /***
         * Constructor
         * @param x element to be stored in the skip list
         * @param lev no of levels the element should be stored
         */
	    public Entry(final E x, final int lev) {
	        element = x;
            next = new Entry[lev];
            width = new int[lev];
            for(int i=0;i<lev;i++)
                width[i] = 1;
	    }

        /***
         * Get property for this.element
         * @return the element stored in the entry
         */
	    public E getElement() {
	        return element;
        }

        /***
         * returns the length of the next array which is same as the height of the entry in the skip list
         * @return the height of the current entry
         */
        int height(){
            return next.length;
        }
    }

    /***
     * Constrtuctor
     */
    public SkipList() {
        head = new Entry<>(null, maxLevel + 1);
        tail = new Entry<>(null, maxLevel + 1);
        pred = new Entry[maxLevel + 1];
        span = new int[maxLevel + 1];
        random = new Random();
        for(int i=0;i<head.next.length;i++)
           {
               head.next[i] = tail;
               head.width[i] = 1;
           }
        tail.prev = head;
    }

    /***
     * Find the predecessors for x the path of the traversal is soted in the pred array
     * @param x value to be found
     */
    private void findPred(final T x){
        Entry<T> p = head;
        for(int i=p.height()-1;i>=0;i--){
            span[i] = 0;
            while(p.next[i]!=null && p.next[i]!= tail && p.next[i].getElement().compareTo(x) < 0)
            {   
                span[i] += p.width[i];
                p = p.next[i];
            }
            pred[i] = p;
        }
    }

    /***
     * Add x to list. If x already exists, reject it. Returns true if new node is added to list
     * @param x entry to be added
     * @return true of new node is added otherwise false
     */
    public boolean add(final T x) {
        if(contains(x)) return false;
       
        final int height = chooseHeight();
        Entry<T> entry= new Entry<>(x,height);
        int dst = span[0];

        if(pred[0].next[0] == tail){
            tail.prev = entry;
        }

        for(int i=0;i<height;i++)
        { 
            entry.next[i] = pred[i].next[i];
            pred[i].next[i] = entry;

            if(i>0) {
                span[i] = dst + span[i];
                if(span[i]!=0)
                     dst = span[i];
                entry.width[i] = Math.max(pred[i].width[i] - span[i-1],1); //subtract span of the pred with previous level span
                pred[i].width[i]  = span[i-1] + 1; //previous level span
            } 
        }
        for(int i= height;i<maxLevel+1;i++){
            pred[i].width[i]++;
        }

        size++;
        return true;

    }

    /***
     * Choose a random height for the node
     */
    private int chooseHeight(){
      //  random = new Random();
        int height = 1 + Integer.numberOfTrailingZeros(random.nextInt());
        return Math.min(height, maxLevel);
    }

    /***
     * Find smallest element that is greater or equal to x
     * @param x value
     * @return the smallest element that is greater or equal to x
     */
    public T ceiling(final T x) {
        if(size == 0)
            return null;
	    findPred(x);
        return pred[0].next[0].getElement();
    }

    /**
     * Contains method to check if the list contains x
     * @param x value
     * @return true if the list contains x, otherwise false
     */
    public boolean contains(final T x) {
        findPred(x);
        return pred[0].next[0].getElement() != null && pred[0].next[0].getElement().compareTo(x) == 0;
    }

    /***
     * Return first element of list
     * @return the first element in the list, null if the size of list is zero
     */
    public T first() {
        if(size == 0)
            return null;
        return head.next[0].getElement();
    }

    /***
     * Find largest element that is less than or equal to x
     * @param x value
     * @return the largest element that is less than or equal to x
     */
    public T floor(final T x) {
        if(size == 0)
            return null;
        findPred(x);
        if(pred[0].next[0].getElement().compareTo(x) == 0)
            return x;
        else
            return pred[0].getElement();
    }

    /***
     * Get the element at index n of list.  First element is at index 0.
     * @param n index
     * @return the element at index n
     */
    public T get(final int n) {
        if(n < 33)
            return getLinear(n);
        else  
           return getLog(n);
    }

    
    /***
     * O(n) algorithm for get(n)
     * @param n
     * @return the element at index n
     */
    public T getLinear(final int n) {
        if(n > size - 1) throw new IllegalArgumentException("Invalid index:"+ n);
        Entry<T> p = head;
        for(int i=0;i<n;i++)
            p = p.next[0];
        return p.next[0].getElement();
    }

    /***
     * O(Log(n)) algorithm for get(n)
     * @param n index
     * @return the element at index n
     */
    public T getLog(final int n) {
        if(n > size - 1) throw new IllegalArgumentException("Invalid index:"+ n);
        Entry<T> p = head;
        int idx = n + 1;
        for(int i=p.height() - 1 ;i>=0 && idx > 0;i--){
            while(p.next[i]!=null && p.next[i] !=tail && idx >= p.width[i] && idx > 0){
                idx = idx - p.width[i];
                p = p.next[i];
            }
        }
        return p.getElement();
    }

    /***
     * To check if the list is empty
     * @return true of the list is empty otherwise false
     */
    public boolean isEmpty() {
	    return size == 0;
    }

    // Iterate through the elements of list in sorted order (optional)
    public Iterator<T> iterator() {
	    return null;
    }

    /**
     * Return last element of list
     * @return the last elment in the list
     */
    public T last() {
        if(size == 0)
            return null;
	    return tail.prev.getElement();
    }

 
    // Not a standard operation in skip lists.  (optional)
    public void rebuild() {
	
    }

    /***
     * Remove x from list.  
     * @param x element to be removed
     * @return Removed element is returned. Return null if x not in list
     */
    public T remove(final T x) {
        if(!contains(x)) return null;
        Entry<T> entry = pred[0].next[0];
        int height = entry.height();
        if(entry.next[0] == tail){
            tail.prev = pred[0];
        }
        for(int i=0;i<height;i++)
        { 
            pred[i].next[i] = entry.next[i];
            pred[i].width[i] = pred[i].width[i] + entry.width[i] -1 ;
        }

        for(int i=height;i<=maxLevel;i++)
        {
            pred[i].width[i]--;
        }
        size--;

        return entry.getElement();
    }

    /***
     * Return the number of elements in the list
     * @return
     */
    public int size() {
	    return size;
    }

}

/* Starter code for LP3 */

// Change this to netid of any member of team
package axm170039;

import java.util.Iterator;
import java.util.Random;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> {
    static final int maxLevel = 32;
    Entry<T> head, tail;
    Entry<T>[] pred;
    int size;
    Random random;
    int[] span;


    static class Entry<E> {
	E element;
	Entry<E>[] next;
    Entry prev;
    int[] width;

	public Entry(final E x, final int lev) {
	    element = x;
        next = new Entry[lev];
        width = new int[lev];
        for(int i=0;i<lev;i++)
            width[i] = 1;
	}

	public E getElement() {
	    return element;
    }

    int height(){
        return next.length;
    }

    }

    // Constructor
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
    }

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

    // Add x to list. If x already exists, reject it. Returns true if new node is added to list
    public boolean add(final T x) {
        if(contains(x)) return false;
        final int height = chooseHeight();
        Entry<T> entry= new Entry<>(x,height);
        int dst = span[0];

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

    private int chooseHeight(){
        return 1 + random.nextInt(maxLevel);
    }
    // Find smallest element that is greater or equal to x
    public T ceiling(final T x) {
        if(size == 0)
            return null;
	    findPred(x);
        return pred[0].next[0].getElement();
    }

    // Does list contain x?
    public boolean contains(final T x) {
        findPred(x);
        return pred[0].next[0].getElement() != null && pred[0].next[0].getElement().compareTo(x) == 0;
    }

    // Return first element of list
    public T first() {
        if(size == 0)
            return null;
        return head.next[0].getElement();
    }

    // Find largest element that is less than or equal to x
    public T floor(final T x) {
        if(size == 0)
            return null;
        findPred(x);
        if(pred[0].next[0].getElement().compareTo(x) == 0)
            return x;
        else
            return pred[0].getElement();
    }

    // Return element at index n of list.  First element is at index 0.
    public T get(final int n) {
        if(n < 33)
            return getLinear(n);
        else  
           return getLog(n);
    }

    // O(n) algorithm for get(n)
    public T getLinear(final int n) {
        if(n > size - 1) throw new IllegalArgumentException("Invalid index:"+ n);
        Entry<T> p = head;
        for(int i=0;i<n;i++)
            p = p.next[0];
        return p.next[0].getElement();
    }

    // Optional operation: Eligible for EC.
    // O(log n) expected time for get(n).
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

    // Is the list empty?
    public boolean isEmpty() {
	    return size == 0;
    }

    // Iterate through the elements of list in sorted order (optional)
    public Iterator<T> iterator() {
	    return null;
    }

    // Return last element of list (optional)
    public T last() {
	    return get(size - 1);
    }

 
    // Not a standard operation in skip lists.  (optional)
    public void rebuild() {
	
    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(final T x) {
        if(!contains(x)) return null;
        Entry<T> entry = pred[0].next[0];
        int height = entry.height();
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

    // Return the number of elements in the list
    public int size() {
	    return size;
    }

    public static void main(String[] args) 
	{
        SkipList<Integer> skiplist = new SkipList<>();
        skiplist.add(13);
        skiplist.add(25);
        skiplist.remove(13);
          
        skiplist.add(19);
        skiplist.add(17);
        skiplist.remove(25);

        skiplist.add(6);

      //  skiplist.remove(19);


      

    System.out.println();
        for(int i=0;i<skiplist.size();i++)
             System.out.println(skiplist.get(i));
             System.out.println();
            //  for(int i=0;i<skiplist.size();i++)
            //  System.out.println(skiplist.get(i));

    }

}

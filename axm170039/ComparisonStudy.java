/**
 * 
 * ComparisonStudy: Comparison Study between SkipList, RedBlackTree and Java Tree set
 * 
 * This class runs add, remove and contains operation for n different inputs among the three and gives a report on  memory usage and time taken
 * 
 * @authors (sxr170016)	Srikumar Ramaswamy, (axm170039) Arun Babu Madhavan, (axk180031)	Andry Thomas Kozhikkadan, (sxs180036) Sravya Singireddy
 * Created as part of Long Project 3 - Implementation of Data Structures (Spring 2020)
 * 
 */
package axm170039;

import java.util.Random;
import java.util.TreeSet;

public class ComparisonStudy {
    public static Random random = new Random();
    public static int numTrials = 20;
    static int progressPrint = 200000; 
    /***
     * 
     * @param args input arguments
     *          args[0] - n, input size
     *          args[1] - choice of operation, 1 - add, 2 - remove, 3 - contains
     *          args[2] - datatstructure choice 1- rbt, 2- skiplist, 3 - treeset
     */
    public static void main(String args[])
    {
        int n = 16 * 1000000; 
        int choice = 3; // 1 - add, 2 - remove, 3 - contains
        int methodChoice = 1; // 1- rbt, 2- skiplist, 3 - treeset
        
	    //Overwrite n and choice if arguments are passed
        if(args.length > 0) { n = Integer.parseInt(args[0]); }
        if(args.length > 1) { choice = Integer.parseInt(args[1]); }
        if(args.length > 2) { methodChoice = Integer.parseInt(args[2]); }


        Long[] arr = new Long[n];
        for(int i=0; i<n; i++) {
            arr[i] = Long.valueOf(i);
        }
                
        System.out.println();

        switch(methodChoice){
            case 1:
                testRBT(arr,choice);
                break;
            case 2:
                testSkipList(arr,choice);
                break;
            case 3:
                testTreeSet(arr,choice);
                break;
            default:
                break;

        }
    }

    /***
     * Test tree set over the given array, the array values are shuffles and an operation is tested based on the given choice
     * @param arr input array
     * @param choice choice of operation  1 - add, 2 - remove, 3 - contains
     */
    public static void testTreeSet(Long[] arr, int choice){
        TreeSet<Long> treeSet = new TreeSet<>();
        Timer timer;
        switch(choice){
            case 1: // Add
            timer = new Timer();
            for(int i=0; i<numTrials; i++) {
                treeSet = new TreeSet<>();
                Shuffle.shuffle(arr);
                for(int j=0;j<arr.length;j++){
                    if(!treeSet.add(arr[j])){
                        System.out.println("Error: Add failed: " + arr[j]);
                        return;
                    }
                    // if(j%progressPrint == 0){
                    //     System.out.println("Adding " + j + "th element!");
                    // }
                }
            }
            timer.end();
		    timer.scale(numTrials);
            System.out.println("Add - TreeSet Tree");
            System.out.println("------------------------------------------------------------");
            System.out.println("n: " + arr.length);
            System.out.println(timer);
            break;
        case 2: // Remove
           long timeElapsed = 0; 
            timer = new Timer();
            for(int i=0; i<numTrials; i++) 
            {
                treeSet = new TreeSet<>();
                Shuffle.shuffle(arr);
                for(int j=0;j<arr.length;j++){
                    if(!treeSet.add(arr[j])){
                        System.out.println("Error: Add failed: " + arr[j]);
                        return;
                    }
                    // if(j%progressPrint == 0){
                    //     System.out.println("Adding " + j + "th element!");
                    // }
                }
                timer = new Timer();
                Shuffle.shuffle(arr);
                for(int j=0;j<arr.length;j++){
                    if(!treeSet.remove(arr[j])){
                        System.out.println("Error: Remove failed: " + arr[j]);
                        return;
                    } 
                    // if(j%progressPrint == 0){
                    //     System.out.println("Removing " + j + "th element!");
                    // }
                }
                timer.end();
                timeElapsed = timeElapsed + timer.elapsedTime;
            }
            timeElapsed = timeElapsed/numTrials;
            timer.setElapsedTime(timeElapsed);
            System.out.println("Remove - TreeSet Tree");
            System.out.println("------------------------------------------------------------");
            System.out.println("n: " + arr.length);
            System.out.println(timer);
            break;
        case 3: // Contains
            Shuffle.shuffle(arr);
            treeSet = new TreeSet<>();
            for(int j=0;j<arr.length;j++){
                if(!treeSet.add(arr[j])){
                    System.out.println("Error: Add failed: " + arr[j]);
                    return;
                }
                // if(j%progressPrint == 0){
                //     System.out.println("Adding " + j + "th element!");
                // }
            }
            timer = new Timer();
            for(int i=0; i<numTrials; i++) {
                Shuffle.shuffle(arr);
                for(int j=0;j<arr.length;j++){
                    if(!treeSet.contains(arr[j])){
                        System.out.println("Contains failed: " + arr[j]);
                        return;
                    }
                    // if(j%progressPrint == 0){
                    //     System.out.println("Checking " + j + "th element!");
                    // }
                }
            }
            timer.end();
		    timer.scale(numTrials);
            System.out.println("Contains - TreeSet Tree");
            System.out.println("------------------------------------------------------------");
            System.out.println("n: " + arr.length);
            System.out.println(timer);
            break;
        default:
            break;
        }

    }



     /***
     * Test SkipList over the given array, the array values are shuffles and an operation is tested based on the given choice
     * @param arr input array
     * @param choice choice of operation  1 - add, 2 - remove, 3 - contains
     */
    public static void testSkipList(Long[] arr, int choice){
        SkipList<Long> skipList = new SkipList<>();
        Timer timer;
        switch(choice){
            case 1: // Add
            timer = new Timer();
            for(int i=0; i<numTrials; i++) {
                skipList = new SkipList<>();
                Shuffle.shuffle(arr);
                for(int j=0;j<arr.length;j++){
                    if(!skipList.add(arr[j])){
                        System.out.println("Error: Add failed: " + arr[j]);
                        return;
                    }
                    // if(j%progressPrint == 0){
                    //     System.out.println("Adding " + j + "th element!");
                  //  }
                }
            }
            timer.end();
		    timer.scale(numTrials);
            System.out.println("Add - SkipList Tree");
            System.out.println("------------------------------------------------------------");
            System.out.println("n: " + arr.length);
            System.out.println(timer);
            break;
        case 2: // Remove
           long timeElapsed = 0; 
            timer = new Timer();
            for(int i=0; i<numTrials; i++) 
            {
                skipList = new SkipList<>();
                Shuffle.shuffle(arr);
                for(int j=0;j<arr.length;j++){
                    if(!skipList.add(arr[j])){
                        System.out.println("Error: Add failed: " + arr[j]);
                        return;
                    }
                    // if(j%progressPrint == 0){
                    //     System.out.println("Adding " + j + "th element!");
                    // }
                }
                timer = new Timer();
                Shuffle.shuffle(arr);
                for(int j=0;j<arr.length;j++){
                    if(skipList.remove(arr[j]) == null){
                        System.out.println("Error: Remove failed: " + arr[j]);
                        return;
                    } 
                    // if(j%progressPrint == 0){
                    //     System.out.println("Removing " + j + "th element!");
                    // }
                }
                timer.end();
                timeElapsed = timeElapsed + timer.elapsedTime;
            }
            timeElapsed = timeElapsed/numTrials;
            timer.setElapsedTime(timeElapsed);
            System.out.println("Remove - SkipList Tree");
            System.out.println("------------------------------------------------------------");
            System.out.println("n: " + arr.length);
            System.out.println(timer);
            break;
        case 3: // Contains
            Shuffle.shuffle(arr);
            for(int j=0;j<arr.length;j++){
                if(!skipList.add(arr[j])){
                    System.out.println("Error: Add failed: " + arr[j]);
                    return;
                }
                // if(j%progressPrint == 0){
                //     System.out.println("Adding " + j + "th element!");
                // }
            }
            timer = new Timer();
            for(int i=0; i<numTrials; i++) {
                Shuffle.shuffle(arr);
                for(int j=0;j<arr.length;j++){
                    if(!skipList.contains(arr[j])){
                        System.out.println("Contains failed: " + arr[j]);
                        return;
                    }
                    // if(j%progressPrint == 0){
                    //     System.out.println("Checking " + j + "th element!");
                    // }
                }
            }
            timer.end();
		    timer.scale(numTrials);
            System.out.println("Contains - SkipList Tree");
            System.out.println("------------------------------------------------------------");
            System.out.println("n: " + arr.length);
            System.out.println(timer);
            break;
        default:
            break;
        }

    }

   /***
     * Test Red Black Tree over the given array, the array values are shuffles and an operation is tested based on the given choice
     * @param arr input array
     * @param choice choice of operation  1 - add, 2 - remove, 3 - contains
     */
    public static void testRBT(Long[] arr, int choice){
        RedBlackTree<Long> rbt = new RedBlackTree<>();
        Timer timer;
        switch(choice){
            case 1: // Add
            timer = new Timer();
            for(int i=0; i<numTrials; i++) {
                Shuffle.shuffle(arr);
                rbt = new RedBlackTree<>();
                for(int j=0;j<arr.length;j++){
                    if(!rbt.add(arr[j])){
                        System.out.println("Error: Add failed: " + arr[j]);
                        return;
                    }
                    // if(j%progressPrint == 0){
                    //     System.out.println("Adding " + j + "th element!");
                    // }
                }
            }
            timer.end();
		    timer.scale(numTrials);
            System.out.println("Add - RedBlack Tree");
            System.out.println("------------------------------------------------------------");
            System.out.println("n: " + arr.length);
            System.out.println(timer);
            break;
        case 2: // Remove
           long timeElapsed = 0; 
            timer = new Timer();
            for(int i=0; i<numTrials; i++) 
            {
                Shuffle.shuffle(arr);
                for(int j=0;j<arr.length;j++){
                    if(!rbt.add(arr[j])){
                        System.out.println("Error: Add failed: " + arr[j]);
                        return;
                    }
                    // if(j%progressPrint == 0){
                    //     System.out.println("Adding " + j + "th element!");
                    // }
                }

                timer = new Timer();
                Shuffle.shuffle(arr);
                for(int j=0;j<arr.length;j++){
                    if(rbt.remove(arr[j]) == null){
                        System.out.println("Error: Remove failed: " + arr[j]);
                        return;
                    } 
                    // if(j%progressPrint == 0){
                    //     System.out.println("Removing " + j + "th element!");
                    // }
                }
                timer.end();
                timeElapsed = timeElapsed + timer.elapsedTime;
            }
            timeElapsed = timeElapsed/numTrials;
            timer.setElapsedTime(timeElapsed);
            System.out.println("Remove - RedBlack Tree");
            System.out.println("------------------------------------------------------------");
            System.out.println("n: " + arr.length);
            System.out.println(timer);
            break;
        case 3: // Contains
            Shuffle.shuffle(arr);
            for(int j=0;j<arr.length;j++){
                if(!rbt.add(arr[j])){
                    System.out.println("Error: Add failed: " + arr[j]);
                    return;
                }
                // if(j%progressPrint == 0){
                //     System.out.println("Adding " + j + "th element!");
                // }
            }
            timer = new Timer();
            for(int i=0; i<numTrials; i++) {
                Shuffle.shuffle(arr);
                for(int j=0;j<arr.length;j++){
                    if(!rbt.contains(arr[j])){
                        System.out.println("Contains failed: " + arr[j]);
                        return;
                    }
                    // if(j%progressPrint == 0){
                    //     System.out.println("Checking " + j + "th element!");
                    // }
                }
            }
            timer.end();
		    timer.scale(numTrials);
            System.out.println("Contains - RedBlack Tree");
            System.out.println("------------------------------------------------------------");
            System.out.println("n: " + arr.length);
            System.out.println(timer);
            break;
        default:
            break;
        }

    }


     /** @author rbk : based on algorithm described in a book
     */

    /* Shuffle the elements of an array arr[from..to] randomly */
    public static class Shuffle {
	
        /***
         * Shuffles the given array of integer
         * @param arr array of integers to shuffle
         */
        public static void shuffle(int[] arr) {
            shuffle(arr, 0, arr.length-1);
        }
    
        /***
         * Overloaded method to shuffle a generic array of elements
         * @param arr array to shuffle
         */
        public static<T> void shuffle(T[] arr) {
            shuffle(arr, 0, arr.length-1);
        }
    
        /***
         * Shuffles a partition of elements in a integer array 
         * @param arr array of integers to shuffle
         * @param from starting index of partition 
         * @param to ending index of the partition
         */
        public static void shuffle(int[] arr, int from, int to) {
            int n = to - from  + 1;
            for(int i=1; i<n; i++) {
            int j = random.nextInt(i);
            swap(arr, i+from, j+from);
            }
        }
        
        /***
         * Shuffles a partition of elements in a generic array 
         * @param arr array to shuffle
         * @param from starting index of partition 
         * @param to ending index of the partition
         */
        public static<T> void shuffle(T[] arr, int from, int to) {
            int n = to - from  + 1;
            Random random = new Random();
            for(int i=1; i<n; i++) {
            int j = random.nextInt(i);
            swap(arr, i+from, j+from);
            }
        }
        
        /***
         * Swap elements between two indices in an Integer array such that arr[x] <- arr[y] and arr[y] <-- arr[x] 
         * @param arr array of integers
         * @param x index of the first element
         * @param y index of the second element
         */
        static void swap(int[] arr, int x, int y) {
            int tmp = arr[x];
            arr[x] = arr[y];
            arr[y] = tmp;
        }
        
        /***
         * Swap elements between two indices in an Integer array such that arr[x] <- arr[y] and arr[y] <-- arr[x] 
         * @param arr array of integers
         * @param x index of the first element
         * @param y index of the second element
         */
        static<T> void swap(T[] arr, int x, int y) {
            T tmp = arr[x];
            arr[x] = arr[y];
            arr[y] = tmp;
        }
        
        /***
         * Prints all the elements of array with message
         * @param arr array of elements
         * @param message message to be prefixed before the elements
         */
        public static<T> void printArray(T[] arr, String message) {
            printArray(arr, 0, arr.length-1, message);
        }
    
        /***
         * Prints a segement of elements in the array
         * @param arr array of elements
         * @param from starting index of segement 
         * @param to ending index of segement 
         * @param message message to be prefixed before the elements
         */
        public static<T> void printArray(T[] arr, int from, int to, String message) {
            System.out.print(message);
            for(int i=from; i<=to; i++) {
            System.out.print(" " + arr[i]);
            }
         
            System.out.println();
        }
    }
}

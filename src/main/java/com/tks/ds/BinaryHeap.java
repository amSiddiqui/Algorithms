package com.tks.ds;

import java.util.Arrays;

/**
 * Class to produce Binary Heaps or PriorityQueue
 * Supported operations:
 * Addition
 * Deletion
 * Replacement
 * @param <T> Type of data stored in Binary Heap Must be comparable
 */
public class BinaryHeap<T extends Comparable<T>> {
    private T heap[];
    private static final boolean DEBUG = false;
    // Initial Array Size
    private static final int SIZE = 16;
    // Increment in size after overflow
    private static final int FILL = 10;
    // Pointer to low index
    private int low;
    // Pointer to high index
    private int high;
    
    /**
     * Creates Binary Heap Of default Size 16
     */
    public BinaryHeap() {
        this(SIZE);
    }

    /**
     * Create binary heap of desired capacity
     * @param capacity Capacity of initial binary heap
     */
    @SuppressWarnings("unchecked")
    public BinaryHeap(int capacity) {
        this.heap = (T[]) new Comparable[capacity];
        this.low = 0;
        this.high = 0;
    }
    
    /**
     * Add an element to the binary heap.
     * Time complexity: log(n)
     * @param e Value to be added in binary heap. Must be type compatible. 
     */
    public void add(T e){
        // Increase the heap capacity by FILL when overflow
        if (high == this.heap.length) {
            this.increaseCapacity();
        }
        this.heap[this.high] = e;
        if (DEBUG)
        System.out.println("Added New Element in "+Arrays.toString(this.heap));
        
        int parent = this.high == 0 ? 0 : (this.high - 1) / 2;
        int child = this.high;
        
        if(DEBUG) {
            System.out.println("Heapifying");
            System.out.println("Init Parent: "+parent);
            System.out.println("Init Child: "+child);
        }
        
        // Keep swapping parent and child till heapified.
        while (parent != child && this.heap[parent].compareTo(this.heap[child]) < 0) {
            swap(parent, child);
            child = parent;
            parent = child == 0 ? 0 : (child - 1) / 2;
        }
        // Increment Heap size by 1
        this.high++;
    }
    
    /**
     * Removes the root element of the binary heap. 
     * Decreases the size of binary heap by 1.
     * @return Root element. Highest or lowest value
     * @throws IndexOutOfBoundsException Exception when value is removed from an empty heap.
     */
    public T remove()throws IndexOutOfBoundsException {
        if (high == low) throw new IndexOutOfBoundsException();
        T max = this.heap[this.low];
        swap(high-1, low);
        this.high--;
        this.heapify(this.low);
        return max;
    }
    
    /**
     * Replaces an already existing vlaue in the heap with a new value. 
     * Time complexity: log(n)
     * Heapifies child and all parents
     * @param val New value to be replced which is type compatible
     * @param index Index At whcih the value is to be replaced
     * @return Original value which is now replaced.
     * @throws IndexOutOfBoundsException Exception when index is out of heap size.
     */
    public T replace(T val, int index)throws IndexOutOfBoundsException {
        if (index == this.high) throw new IndexOutOfBoundsException(); 
        T replaced = this.heap[index];
        this.heap[index] = val;
        // Heapify Child
        this.heapify(index);
        // Heapify parent till all parents are heapified
        int parent = index == 0 ? 0: (index - 1)/2;
        int child = index;
        while (parent != child && this.heap[parent].compareTo(this.heap[child]) < 0) {
            heapify(parent);
            child = parent;
            parent = child == 0 ? 0 : (child - 1)/2;
        }
        return replaced;
    }
    
    /**
     * Checks if heap is empty
     */
    public boolean isEmpty() {
        return this.high == this.low;
    }

    /**
     * Returns the current number of nodes in heap
     * @return Size of the heap.
     */
    public int size() {
        return this.high - this.low;
    }

    /**
     * Static factory funtion to contruct a BinaryHeap from an array
     * @param <E> Type of BinaryHeap elements which are Comparable
     * @param array Array from which BinaryHeap will be formed
     * @return Returns the replaced value
     */
    public static <E extends Comparable<E>> BinaryHeap<E> fromArray(E array[]) {
        BinaryHeap<E> bh = new BinaryHeap<>(array.length);
        for (int i = 0; i < array.length; i++) {
            bh.heap[i] = array[i];
        }
        bh.high = bh.heap.length;
        for (int i = (bh.heap.length / 2)-1; i >= 0; i--) {
            bh.heapify(i);
        }
        return bh;
    }

    @Override
    public String toString() {
        String str = "[";
        for (int i = low; i < high-1; i++) {
            str = str+this.heap[i]+", ";
        }
        str = str+this.heap[this.high-1]+"]";
        return str;
    }

    private void heapify (int i) {
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        int n = (this.high - this.low);
        int large = l;
        if (!isHeap(i)) {
            if (l >= n) large = r;
            else if (r >= n) large = l;
            else large = this.heap[r].compareTo(this.heap[l]) > 0 ? r : l;
            swap (large, i);
        }
        if (!isHeap(large)) {
            heapify(large);
        }
    }

    private boolean isHeap(int index) {
        int l = 2 * index + 1;
        int r = 2 * index + 2;
        int n = (this.high - this.low);
        int leafIndexStart = (n / 2);
        if (index >= leafIndexStart) {
            return true;
        }
        if (l>=n) {
            return this.heap[index].compareTo(this.heap[r]) >= 0;
        }
        else if (r >= n) {
            return this.heap[index].compareTo(this.heap[l]) >= 0;
        }
        else{
            return this.heap[index].compareTo(this.heap[l]) >= 0 && this.heap[index].compareTo(this.heap[r]) >= 0;
        }

    }

    /**
     * For Testing. 
     * Validity check that the current heap is a heap or not
     */
    public boolean isHeap() {
        for (int i = this.low; i < this.high; i++) {
            if (!isHeap(i)) {
                if (DEBUG)
                System.out.println("Heap failed at index: "+i);
                return false;
            }
        }
        return true;
    }

    private void swap (int i, int j) {
        T temp = this.heap[i];
        this.heap[i] = this.heap[j];
        this.heap[j] = temp;
    }
    
    @SuppressWarnings("unchecked")
    private void increaseCapacity() {
        int originalCapacity = this.heap.length;
        int newLength = originalCapacity + FILL;
        T temp[] = (T[]) new Comparable[newLength];
        for (int i = 0; i < this.heap.length; i++) {
            temp[i] = heap[i];
        }
        if (DEBUG) {
            System.out.println("Increasing Capacity");
            System.out.println("Original Array: ");
            System.out.println(Arrays.toString(this.heap));
        }
        this.heap = temp;
        if (DEBUG) {
            System.out.println("New Array: ");
            System.out.println(Arrays.toString(this.heap));
        }
    }
}

// Task 1. DisjointSets class (10%)

// hint: you can use the DisjointSets from your textbook

import java.util.ArrayList;

// disjoint sets class, using union by size and path compression.
public class DisjointSets<T> {

  // Constructor
  public DisjointSets(ArrayList<T> data) {
    // init sets storage
    sets = new ArrayList<Set<T>>();

    // init parent storage
    p = new int[data.size()];
    
    // init size storage
    s = new int[data.size()];

    for (int i = 0; i < data.size(); ++i) {

      // fill sets storage
      T element = data.get(i);
      Set<T> newset = new Set<T>(element);
      this.sets.add(newset);

      // fill parent storage
      s[i] = i;

    }
  }

  // Release union by size algo 
  //  Must have O(1) time complexity
  
  public int union(int root1, int root2) {
    
    
    
    return
    
  }

  // Must implement path compression
  public int find(int x) {
    return -1; // TODO: remove and replace this line
  }

  // Get all the data in the same set
  // Must have O(1) time complexity
  public Set<T> get(int root) {
    return null; // TODO: remove and replace this line

  }

  // return the number of disjoint sets remaining
  // must be O(1) time
  public int getNumSets() {
    return sets.size();
  }

  // Data

  // size storage
  private int[] s;
  
  // sets storage
  private ArrayList<Set<T>> sets;

  // parents storage
  private int[] p;
}

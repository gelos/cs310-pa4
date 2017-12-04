
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
    //p = new int[data.size()];
    
    // init parent storage
    s = new int[data.size()];

    for (int i = 0; i < data.size(); ++i) {

      // fill sets storage, one set - one element
      T element = data.get(i);
      Set<T> newset = new Set<T>(element);
      this.sets.add(newset);

      // fill parent storage
      s[i] = i;

    }
    this.setsCount = data.size();
  }

  // Release union by size algo 
  //  Must have O(1) time complexity
  
  public int union(int root1, int root2) {
    
    if (root1 == root2) {
      return root1;
    }
    
    // union small set to big 
    if (this.sets.get(root1).size() > this.sets.get(root2).size()) {
      s[root2] = root1;
      this.sets.get(root1).addAll(this.sets.get(root2));
      this.sets.get(root2).clear();
      this.setsCount--;
      return root1;
    } else {
      s[root1] = root2;
      this.sets.get(root2).addAll(this.sets.get(root1));
      this.sets.get(root1).clear();
      this.setsCount--;
      return root2;
    }
            
  }

  // Must implement path compression
  public int find(int x) {
    //return -1; // TODO: remove and replace this line
    if (s[x] == x) {
      return x;
    } else {
      s[x] = find(s[x]);
    }
    return s[x];
  }

  // Get all the data in the same set
  // Must have O(1) time complexity
  public Set<T> get(int root) {
    return this.sets.get(root);
  }

  // return the number of disjoint sets remaining
  // must be O(1) time
  public int getNumSets() {
    //return sets.size();
    return setsCount;
  }

  // Data

  // sets count
  private int setsCount;
  
  // parent storage
  private int[] s;
  // sets storage
  private ArrayList<Set<T>> sets;
}


// Task 1. DisjointSets class (10%)

// hint: you can use the DisjointSets from your textbook

import java.util.ArrayList;

/**
 * Disjoint sets class, using union by size and path compression.
 */
public class DisjointSets<T> {

  /**
   * Constructor, init internal data structures.
   * Fill disjointset with given data, use one data element as one set.
   * @param data the arraylist of data 
   */
  public DisjointSets(ArrayList<T> data) {

    // init sets storage
    sets = new ArrayList<Set<T>>();
    
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

  /**
   * Union two sets given by their root ids
   * Use union by size, small set add to big
   * @param root1 the first set root id
   * @param root2 the second set root id
   * @return root id of union set
   */
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

  /** 
   * Return root id of x set.
   * Implement path compression
   *      
   * @param x the element
   * @return root id of the x set
   */
  public int find(int x) {
    
    if (s[x] == x) {
      return x;
    } else {
      s[x] = find(s[x]);
    }
    return s[x];
  }

  /**
   * Get all the data in set for given root id
   * @param root the root id
   * @return set of data for given root id
   */
  public Set<T> get(int root) {
    return this.sets.get(root);
  }

  /**
   * Return the number of disjoint sets remaining
   * @return number of disjoint sest remaining
   */
  public int getNumSets() {
    return setsCount;
  }

  //
  // Data
  //
  
  // sets count
  private int setsCount;
  
  // parent storage
  private int[] s;
  
  // sets storage
  private ArrayList<Set<T>> sets;
}

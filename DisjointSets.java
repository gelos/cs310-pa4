import java.util.ArrayList;

/**
 * Disjoint sets class, using union by size and path compression. Internal storage, use ArraList as
 * Set<T> and int[] as parent storage.
 *
 * @param <T> the generic type
 */
public class DisjointSets<T> {

  /**
   * Construct and initialize internal data structures. Fill DisjointSets<T> with given
   * ArrayList<T>, use one ArrayList<T> element as one Set<T>.
   * 
   * @param data the ArrayList<T> of data
   */
  public DisjointSets(ArrayList<T> data) {

    // initialize sets storage
    sets = new ArrayList<Set<T>>();

    // initialize parent storage
    s = new int[data.size()];

    // fill sets storage, one set - one element
    for (int i = 0; i < data.size(); i++) {

      T element = data.get(i);
      Set<T> newset = new Set<T>(element);
      this.sets.add(newset);

      // fill parent storage
      s[i] = i;

    }
    this.setsCount = data.size();
  }

  /**
   * Union two sets given by their root IDs. Use union by size, set with smaller size added to
   * bigger one.
   *
   * @param root1 the first set root id
   * @param root2 the second set root id
   * @return root id of union set
   */
  public int union(int root1, int root2) {

    // do not union if it is same set
    if (root1 == root2) {
      return root1;
    }

    // gets sets
    Set<T> set1 = sets.get(root1);
    Set<T> set2 = sets.get(root2);

    // union small set to big
    if (set1.size() > set2.size()) {
      s[root2] = root1;
      set1.addAll(set2);
      set2.clear();
      this.setsCount--;
      return root1;
    } else {
      s[root1] = root2;
      set2.addAll(set1);
      set1.clear();
      this.setsCount--;
      return root2;
    }
  }

  /**
   * Recursively find root ID of x set. Implement path compression.
   * 
   * @param x the element
   * @return root ID of the x set
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
   * Get all the data in set for given root ID.
   *
   * @param root the root id
   * @return set of data for given root ID
   */
  public Set<T> get(int root) {
    return sets.get(root);
  }

  /**
   * Return the number of disjoint sets remaining.
   *
   * @return number of disjoint sets remaining
   */
  public int getNumSets() {
    return setsCount;
  }

  //
  // Data
  //

  /** The sets count. */
  private int setsCount;

  /** The parent storage. */
  private int[] s;

  /** The sets storage. */
  private ArrayList<Set<T>> sets;
}

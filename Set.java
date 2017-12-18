import java.util.AbstractCollection;
import java.util.Iterator;

/**
 * Class set used in DisjointSets<T> to store actual data in the same sets. Use array as internal
 * storage.
 *
 * @param <T> the generic type
 */
public class Set<T> extends AbstractCollection<T> {

  //
  // Constructors
  //

  /**
   * Instantiate a new empty Set<T> with internal storage size equals DEFAULT_CAPACITY.
   * 
   */
  public Set() {
    internalStorage = (T[]) new Object[DEFAULT_CAPACITY];
    size = 0;
  }

  /**
   * Instantiate a new Set<T> with one element.
   *
   * @param element the element
   */
  public Set(T element) {
    this();
    add(element);
  }

  /**
   * Add item to this Set<T>.
   *
   * @param item the item
   * @return true, if successful
   * @see java.util.AbstractCollection#add(java.lang.Object)
   */
  public boolean add(T item) {
    ensureCapacity(size + 1);
    internalStorage[size++] = item;
    return true;
  }

  /**
   * Adds set of T to this Set<T>.
   *
   * @param other the set of elements T
   * @return true, if successful
   */
  public boolean addAll(Set<T> other) {
    int otherSize = other.size();
    Object otherArray = (T[]) other.toArray();
    ensureCapacity(size() + otherSize);
    System.arraycopy(otherArray, 0, internalStorage, size, otherSize);
    size += otherSize;
    return otherSize != 0;
  }

  /**
   * Clear Set<T>.
   *
   * @see java.util.AbstractCollection#clear()
   */
  public void clear() {
    for (int i = 0; i < size; i++) {
      internalStorage[i] = null;
    }
    size = 0;
  }

  /**
   * Return size of this Set<T>.
   * 
   * @return int, the size of set
   * @see java.util.AbstractCollection#size()
   */
  public int size() {
    return size;
  }

  /**
   * Return Iterator object.
   * 
   * @return the iterator
   * @see java.util.AbstractCollection#iterator()
   */
  public Iterator<T> iterator() {

    return new Iterator<T>() {

      /** Current position */
      int current = 0;

      /** Return next element */
      public T next() {
        if (hasNext()) {
          return internalStorage[current++];
        } else {
          throw new RuntimeException("NoSuchElementException");
        }
      }

      /** Check if next element exists */
      public boolean hasNext() {
        return (current != size());
      }
    };
  }

  /** The data. */
  private T[] internalStorage;

  /** The size. */
  private int size = 0;

  /** Default capacity constant */
  private static final int DEFAULT_CAPACITY = 100;

  /**
   * Internal method.Ensure capacity of at least minCapacity. If required internal storage size
   * grows by approximate 50%.
   * 
   * @param minCapacity the minimal capacity
   */
  private void ensureCapacity(int minCapacity) {
    int oldSize = internalStorage.length;
    if (minCapacity > oldSize) {
      T[] oldStorage = internalStorage;
      int newSize = (oldSize * 3) / 2 + 1;
      if (newSize < minCapacity) {
        newSize = minCapacity;
      }
      // minCapacity is usually close to size, so this is a win:
      internalStorage = (T[]) new Object[newSize];
      System.arraycopy(oldStorage, 0, internalStorage, 0, oldSize);
    }
  }
}

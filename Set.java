
//
// Task 1. Set<T> class (5%)
// This is used in DisjointSets<T> to store actual data in the same sets
//

// You cannot import additonal items
import java.util.AbstractCollection;
import java.util.Iterator;
// You cannot import additonal items

//
// Hint: if you think really hard, you will realize this class Set<T> is in fact just a list
// because DisjointSets<T> ensures that all values stored in Set<T> must be unique,
// but should it be array list or linked list??
//

//http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/util/ArrayList.java#ArrayList


public class Set<T> extends AbstractCollection<T> {

  // Consructor
  
  public Set() {
    this.data = new int[10];
    this.size = 0;
  }
  
  // O(1)
  public boolean add(T item) {

    return data.add(item);
  }

  // O(1)
  public boolean addAll(Set<T> other) {
    return data.addAll(other);
  }

  // O(1)
  public void clear() {
    data.clear();
  }

  // O(1)
  public int size() {
    return data.size();
  }

  public Iterator<T> iterator() {
    return new Iterator<T>() {
      private Iterator<T> iter = data.iterator();

      public T next() {
        // return null;
        return iter.next();
      }

      public boolean hasNext() {
        // return false;
        return iter.hasNext();
      }
    };
  }

  // Data
  private T[] data;
  private int size = 0;

  private void ensureCapacity(int minCapacity) {
    modCount++;
    int oldCapacity = elementData.length;
    if (minCapacity > oldCapacity) {
      Object oldData[] = elementData;
      int newCapacity = (oldCapacity * 3) / 2 + 1;
      if (newCapacity < minCapacity)
        newCapacity = minCapacity;
      // minCapacity is usually close to size, so this is a win:
      elementData = Arrays.copyOf(elementData, newCapacity);
    }
  }
}

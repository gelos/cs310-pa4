
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

	// Constructors

	public Set(T element) {
		this();
		add(element);
	}
	
	public Set() {
		this.data = (T[]) new Object[10];
		this.size = 0;
	}

	// O(1)
	public boolean add(T item) {
		ensureCapacity(this.size + 1);
		this.data[this.size++] = item;
		return true;
	}

	// O(1)
	public boolean addAll(Set<T> other) {
		int otherSize = other.size();
		Object array = (T[]) other.toArray();
		ensureCapacity(this.size() + otherSize);
		System.arraycopy(array, 0, this.data, this.size(), otherSize);
		this.size += otherSize;
		return otherSize != 0;
	}

	// O(1)
	public void clear() {
		this.size = 0;
		this.data = null;
	}

	// O(1)
	public int size() {
		return this.size;
	}

	public Iterator<T> iterator() {

		return new Iterator<T>() {

			int current = 0;

			public T next() {
				// return null;
				if (hasNext()) {
					return data[current++];
				} else {
					throw new RuntimeException("NoSuchElementException");
				}
			}

			public boolean hasNext() {
				return (current != size());
			}
		};
	}

	// Data
	private T[] data;
	private int size = 0;

	private void ensureCapacity(int minCapacity) {
		int oldCapacity = this.data.length;
		if (minCapacity > oldCapacity) {
			T[] oldData = data;
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity;
			}
			// minCapacity is usually close to size, so this is a win:
			// data = Arrays.copyOf(data, newCapacity);
			// this.data = new T[newCapacity];
			this.data = (T[]) new Object[newCapacity];
			System.arraycopy(oldData, 0, this.data, 0, oldCapacity);
		}
	}
}

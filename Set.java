
//
// Task 1. Set<T> class (5%)
// This is used in DisjointSets<T> to store actual data in the same sets
//

//You cannot import additonal items
import java.util.AbstractCollection;
import java.util.Iterator;
//You cannot import additonal items

//
//Hint: if you think really hard, you will realize this class Set<T> is in fact just a list
//      because DisjointSets<T> ensures that all values stored in Set<T> must be unique, 
//      but should it be array list or linked list??
//

public class Set<T> extends AbstractCollection<T>
{
	//O(1)
	public boolean add(T item)
	{
		return false;
	}
	
	//O(1)
	public boolean addAll(Set<T> other)
	{
		return false;
	}
	
	//O(1)
	public void clear()
	{
		
	}
	
	//O(1)
	public int size()
	{
		return -1;
	}
	
	public Iterator<T> iterator()
	{
		return new Iterator<T>()
		{
			public T next()
			{
				return null;
			}
			
			public boolean hasNext()
			{
				return false;
			}
		};
	}
}

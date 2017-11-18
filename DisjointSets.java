
// Task 1. DisjointSets class (10%)

// hint: you can use the DisjointSets from your textbook

import java.util.ArrayList;

// disjoint sets class, using union by size and path compression.
public class DisjointSets<T>
{
    //Constructor
    public DisjointSets( ArrayList<T> data  )
    {
      //your code here
    }
    
    //Must have O(1) time complexity
    public int union( int root1, int root2 )
    {
		return -1; //TODO: remove and replace this line
    }

    //Must implement path compression
    public int find( int x )
    {
      return -1; //TODO: remove and replace this line
    }

    //Get all the data in the same set
    //Must have O(1) time complexity
    public Set<T> get( int root )
    {
      return null; //TODO: remove and replace this line
    }
	
	//return the number of disjoint sets remaining
    // must be O(1) time
	public int getNumSets()
	{
		return -1; //TODO: remove and replace this line
	}

    //Data
    private int [ ] s;
    private ArrayList<Set<T>> sets;
}

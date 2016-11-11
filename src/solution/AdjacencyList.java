package solution;



import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AdjacencyList<T> implements Iterable<T>{

	Set<T> list ;
	
	public AdjacencyList() {
		list = new HashSet<T>();
	}
	
	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	public boolean add(T w) {
		return list.add(w);
	}
	
	
	@Override
	public String toString() {
		
		StringBuilder list = new StringBuilder();
		for (T t : this.list) {
			list.append(t + " ");
		}
		return list.toString();
	}
	
	public int size() {
		return list.size();
	}
	
	public boolean isEmpty() {
		return list.size()==0;
	}

	public boolean contains(T w) {
		return list.contains(w);
	}
	

}

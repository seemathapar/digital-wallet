package solution;

import java.util.*;

public class Node<K, V> implements Iterable<K>{
	
	private Map<K, V> map;
	
	public Node() {		
		map = new HashMap<K, V>();
	}

	@Override
	public Iterator<K> iterator() {
		return map.keySet().iterator();
	}
	
	public void add(K key, V value) {
		map.put(key, value);
	}
	
	public boolean contains(K key) {
		return map.containsKey(key);
	}
	
	

}
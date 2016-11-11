package solution;

import java.util.*;

public class Graph{

	
	private int EDGES = 0;

	private Map<String, AdjacencyList<String>> map;

	public Graph() {

		map = new HashMap<String, AdjacencyList<String>>();

	}

	public void addEdge(String v, String w) {
		if (!map.containsKey(v)) {
			addNode(v);
		}
		if (map.get(v).add(w)){
			EDGES++;
		}
		
		if (!map.containsKey(w)) {
			addNode(w);
		}
		map.get(w).add(v);

	}
	
	public  Iterable<String> vertices() {
		return map.keySet();

	}
	
	public int getNumberOfVertices(){
		return map.keySet().size();
	}
	
	public int getNumberOfEdges(){
		return EDGES;
	}
	
	public int getDegree(String v) {
		return map.get(v) == null ? 0: map.get(v).size();
	}
	
	
	
	public  Iterator<String> edges(String v) {
		return map.get(v).iterator();

	}
	
	public String toString() {
        StringBuilder s = new StringBuilder();
        for (String v : vertices()) {
            s.append(v + ": ");
            for (String w : map.get(v)) {
                s.append(w + " ");
            }
            s.append('\n');
        }
        return s.toString();
    }
	
	public AdjacencyList<String> get(String key){
		return map.get(key);
	}
	
	public void addNode(String key) {
		if (map.get(key) == null){
			map.put(key, new AdjacencyList<String>());
		}
	}
	
	
	
	public static void main(String[] args) {
		Graph g = new Graph();
		g.addEdge("A", "B");
		g.addEdge("A", "C");
		g.addEdge("A", "G");
		g.addEdge("A", "H");
		g.addEdge("B", "A");
		g.addEdge("D", "C");
		g.addEdge("A", "H");
		
		System.out.println(g);
		
		
	}

	
	

	
	
	

}

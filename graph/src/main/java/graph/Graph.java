package graph;

import java.util.*;

public class Graph <V, E> {

    // AF(this) = Map{Parent_vertex -> Map{Child_vertex -> Set of vertices
    // from Parent_vertex to Child_vertex}}

    // RI: graph != null
    // For every parent Vertex v in graph.keySet():
    //      v != null
    //      Map m in graph.get(v): graph.get(v) != null
    //      For every child vertex v_i in map.keySet(): v_i != null and v_i must exist in the graph
    //              Set s in map.get(v_i): s != null
    //              For every label in set s:
    //                      label != null and label != ""

    /**
     *  In a graph, each parent vertex is mapped to a map, in which the child vertex
     *  is mapped to a set of vertices from the parent vertex to the child vertex
     */
    private final Map<V, Map<V, Set<E>>> graph;

    /**
     * A flag to disable slow_check when submitting the project
     */
    private final boolean flag = false;

    /**
     * Create an empty directed graph
     * @spec.effects An empty directed graph is created
     */
    public Graph(){
        graph = new HashMap<>();
        checkRep();
    }

    /**
     * Check if the graph is empty
     * @return true if the graph does not have any vertices
     */
    public boolean isEmpty(){
        checkRep();
        return graph.isEmpty();
    }

    /**
     * Check if the graph contains a vertex
     * @param v the vertex to be checked
     * @throws IllegalArgumentException if v is NULL
     * @return true if the graph contains the vertex v
     */
    public boolean contains(V v){
        checkRep();
        if (v == null)
            throw new IllegalArgumentException("Invalid parameter");
        checkRep();
        return graph.containsKey(v);
    }

    /**
     * Check if the graph contains an edge
     * @param from the parent vertex to be checked
     * @param to the child vertex to be checked
     * @param label the edge label to be checked
     * @throws IllegalArgumentException if from or to or label is null
     * @return true if the graph contains both vertices p and c there exists an edge labeled l
     * from p to c
     */
    public boolean contains(V from, V to, E label){
        checkRep();
        if (from == null || to == null || label == null)
            throw new IllegalArgumentException("Invalid parameter");
        boolean result = false;
        if (graph.containsKey(from) && graph.containsKey(to) && graph.get(from) != null &&
                graph.get(from).get(to)!= null)
            result = graph.get(from).get(to).contains(label);
        checkRep();
        return result;
    }

    /**
     * Return a list of all the nodes in the graph
     * @return A set of all the nodes in the graph
     */
    public Set<V> setNodes(){
        checkRep();
        Set<V> result = new HashSet<>(graph.keySet());
        checkRep();
        return result;
    }

    /**
     * Return a map with each child vertex mapped to a set of edges
     * @param v the parent vertex
     * @return A map of with each child vertex of the parent one mapped to a set of edges,
     * or null when no children is found
     * @throws IllegalArgumentException when v is null
     */
    public Map<V, Set<E>> listChildren(V v){
        checkRep();
        if (v == null)
            throw new IllegalArgumentException("Invalid parameter");
        Map<V, Set<E>> result;
        if (!contains(v))
            result = null;
        else {
            result = new HashMap<>();
            for (V d : graph.get(v).keySet()) {
                if (areConnected(v, d))
                    result.put(d, graph.get(v).get(d));
            }
        }
        checkRep();
        return result;
    }

    /**
     * Return a set of edges between two vertices
     * @param from the parent vertex
     * @param to the child vertex
     * @return a set of edges from vertex from to vertex to
     * @throws IllegalArgumentException when from or to is null or the graph does not contain
     * from or to
     */
    public Set<E> setEdges(V from, V to){
        checkRep();
        if (from == null || to == null || !contains(from) || !contains(to))
            throw new IllegalArgumentException("Invalid parameter");
        Set<E> result;
        if (areConnected(from, to))
            result = new HashSet<>(graph.get(from).get(to));
        else
            result = null;
        checkRep();
        return result;
    }

    /**
     * Check if there is an edge from Vertex from to Vertex to
     * @param from the vertex to be checked if parent
     * @param to the vertex to be checked if child
     * @spec.requires from and to exist in the graph
     * @throws IllegalArgumentException if either from or to is NULL
     * @return true if from is a parent of to
     */
    public boolean areConnected(V from, V to){
        checkRep();
        if (from == null || to == null)
            throw new IllegalArgumentException("Invalid parameter");
        boolean result = false;
        if (contains(from) && contains(to) && graph.get(from) != null &&
                graph.get(from).get(to)!= null)
            result = !graph.get(from).get(to).isEmpty();
        checkRep();
        return result;
    }

    /**
     * Add a vertex to the graph
     * @param v to be added to the graph
     * @spec.modifies this
     * @spec.requires v exists in the graph
     * @spec.effects each vertex v will be mapped to an empty HashMap
     * @throws IllegalArgumentException if v is null
     */
    public void addVertex(V v){
        checkRep();
        if (v == null)
            throw new IllegalArgumentException("Invalid parameter");
        if (!contains(v))
            graph.put(v, new HashMap<>());
        checkRep();
    }

    /**
     * Add an edge to the graph
     * @param from the starting vertex
     * @param to the destination vertex
     * @param label the label of the edge from vertex from to vertex to
     * @spec.modifies this
     * @spec.requires from and to exist in the graph and label has not existed
     * @spec.effects add the edge label to the set of edges from vertex from to vertex to
     * @throws IllegalArgumentException if either from, to or label is null, or label
     * is an empty string
     */
    public void addEdge(V from, V to, E label){
        checkRep();
        if (from == null || to == null || label == null)
            throw new IllegalArgumentException("Invalid parameter");
        if (contains(from) && contains(to) && !contains(from, to, label)) {
            graph.get(from).computeIfAbsent(to, k -> new HashSet<>());
            graph.get(from).get(to).add(label);
        }
        checkRep();
    }

    /**
     * Remove one edge from the graph
     * @param v the vertex to be removed
     * @spec.modifies this
     * @spec.requires v exists in the graph
     * @spec.effects vertex v is removed from the key set of parent vertices, and is removed
     * from the set of children vertices of each parent vertex
     * @throws IllegalArgumentException if v is NULL
     */
    public void removeVertex(V v){
        checkRep();
        if (v == null)
            throw new IllegalArgumentException("Invalid parameter");
        if (contains(v)) {
            graph.remove(v);
            for (V other: graph.keySet()){
                if (graph.get(other).get(v) != null)
                    graph.get(other).remove(v);
            }
        }
        checkRep();
    }

    /**
     * Remove one edge from the graph
     * @param from the outgoing vertex of the edge
     * @param to the edge to be removed
     * @param label the label of the edge to be removed
     * @spec.modifies this
     * @spec.requires there exists an edge between from from to to
     * @spec.effects remove the edge label from the set of edges from vertex from to vertex to.
     * If the set is empty, map vertex from to an empty HashMap.
     * @throws IllegalArgumentException if either from, to or label is null, or label
     * is an empty string
     */
    public void removeEdge(V from, V to, E label){
        checkRep();
        if (from == null || to == null || label == null)
            throw new IllegalArgumentException("Invalid parameter");
        if (contains(from, to, label)) {
            Set<E> to_remove_from = graph.get(from).get(to);
            to_remove_from.remove(label);
            if (to_remove_from.isEmpty())
                graph.put(from, new HashMap<>());
        }
        checkRep();
    }

    /**
     * Get the string representation of the Graph
     * @return A string representation of the Graph
     */
    @Override
    public String toString(){
        checkRep();
        String s = "";

        List<V> list_starting_vertices = new ArrayList<>(graph.keySet());
        List<String> list_starting_vertices_string = new ArrayList<>();
        for (V v: list_starting_vertices)
            list_starting_vertices_string.add(v.toString());

        Collections.sort(list_starting_vertices_string);

        for (int i = 0; i < list_starting_vertices_string.size(); i++){
            s += list_starting_vertices_string.get(i);
            s += "(";
            Map<V, Set<E>> map_i = graph.get(list_starting_vertices_string.get(i));
            List<V> list_destination_vertices = new ArrayList<>(map_i.keySet());
            List<String> list_destination_vertices_string = new ArrayList<>();
            for (V v: list_destination_vertices)
                list_destination_vertices_string.add(v.toString());
            Collections.sort(list_destination_vertices_string);
            for (int j = 0; j < list_destination_vertices_string.size(); j++){
                String destination_vertex = list_destination_vertices_string.get(j);
                s += destination_vertex;
                s += ":{";
                List<E> list_edge = new ArrayList<>(map_i.get(destination_vertex));
                for (int k = 0; k < list_edge.size(); k++) {
                    s += list_edge.get(k);
                    if (k != list_edge.size() - 1)
                        s += ", ";
                }
                s += "}";
                if (j != list_destination_vertices.size() - 1)
                    s += ", ";
            }
            s += ")";
            if (i != list_starting_vertices.size() - 1)
                s += ",\n";
        }
        checkRep();
        return s;
    }

    /**
     * Compare two Graph's.
     * @param o the object to be compared
     * @return true if they have the values in each graph are the same and false if they are different
     * @throws IllegalArgumentException if o is null
     */
    @Override
    public boolean equals(Object o) {
        checkRep();
        if (o == null)
            throw new IllegalArgumentException("Invalid parameter");
        boolean result = false;
        if (o instanceof Graph<?, ?>) {
            Graph<?, ?> other = (Graph<?, ?>) o;
            result = graph.equals(other.graph);
        }
        checkRep();
        return result;
    }

    /**
     * Standard hashCode function.
     * @return an int that all objects equal to this will also
     */
    @Override
    public int hashCode(){
        checkRep();
        return graph.hashCode();
    }

    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep(){
        assert(graph != null): "The graph cannot be null";
        if (flag){
            for (V v : graph.keySet() ) {
                assert (v != null): "Starting vertices cannot be null";
                Map<V, Set<E>> map = graph.get(v);
                assert (map != null): "Each node cannot be mapped to a null value";
                for (V v_i: map.keySet()){
                    assert (v_i != null): "Destination vertices cannot be null";
                    assert (graph.containsKey(v_i)): "Destination vertices must exist in the graph";
                    Set<E> set = map.get(v_i);
                    assert (set != null): "Set of edges cannot be null";
                    for (E label: set) {
                        assert (label != null) : "Labels cannot be null";
                    }
                }
            }
        }
    }
}


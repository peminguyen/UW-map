package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;

import java.util.*;

/**
 * This class contains a method to find the optimal path between two vertices in a graph
 * using Dijkstra's Algorithm
 */
public class Dijkstra {
     // This class is not an ADT

    /**
     * Find the cost-minimum path from start to dest of a graph with edge label type Double
     * @param graph the graph to traverse on
     * @param start the starting vertex
     * @param dest the destination vertex
     * @param <V> the generic type of vertex
     * @throws IllegalArgumentException when graph or start or dest is null, or when the graph
     * does not contain either start or dest
     * @return a path with cost 0 when start and dest are the same, a path with minimum cost
     * otherwise. If no path exists, return null
     */
    public static <V> Path<V> findPath(
            Graph<V, Double> graph, V start, V dest){
        if (graph == null || start == null || dest == null)
            throw new IllegalArgumentException("Invalid parameter");
        if (!graph.contains(start))
            throw new IllegalArgumentException(start + " is not in the graph");
        if (!graph.contains(dest))
            throw new IllegalArgumentException(dest + " is not in the graph");

        // Queue stores all known paths with priority given to the minimum cost one
        PriorityQueue<Path<V>> active = new PriorityQueue<>();

        // set of nodes for which we know the minimum-cost path from start.
        Set<V> finished = new HashSet<V>();
        Path<V> path = new Path<>(start);
        active.add(path);

        if (start.equals(dest))
            return path.extend(start, 0.0);

        while (!active.isEmpty()) {

            // minPath is the lowest-cost path in active and,
            // if minDest isn't already 'finished,' is the
            // minimum-cost path to the node minDest
            Path<V> minPath = active.poll();
            V minDest = minPath.getEnd();

            if (minDest.equals(dest))
                return minPath;

            if (finished.contains(minDest))
                continue;

            Set<V> children = new HashSet<>(graph.listChildren(minDest).keySet());
            for (V child : children) {
                // If we don't know the minimum-cost path from start to child,
                // examine the path we've just found
                if (!finished.contains(child)) {
                    Double newCost = graph.setEdges(minDest, child).iterator().next();
                    Path<V> newPath = minPath.extend(child, newCost);
                    active.add(newPath);
                }
            }
            finished.add(minDest);
        }
        // Return null if no path found
        return null;
    }
}

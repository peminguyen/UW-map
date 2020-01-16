package pathfinder;

/**
 * A class containing methods to build a graph
 */

import graph.Graph;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusPath;

import java.util.List;

/**
 * This class contains the method to build a graph from a list of CampusPath
 */
public class GraphBuilding {
    /**
     * Method to build the graph from a list of CampusPath
     * @param listPath the list of CampusPath
     * @throws IllegalArgumentException if the filename is null
     * @return a single Graph with Double edges (representing the cost) between every two Point's
     */
    public static Graph<Point, Double> constructGraph(List<CampusPath> listPath){
        if (listPath == null)
            throw new IllegalArgumentException("Invalid parameter");
        Graph<Point, Double> graph = new Graph<>();
        for (CampusPath cp : listPath){
            Point p1 = new Point(cp.getX1(), cp.getY1());
            Point p2 = new Point(cp.getX2(), cp.getY2());
            Double distance = cp.getDistance();
            graph.addVertex(p1);
            graph.addVertex(p2);
            graph.addEdge(p1, p2, distance);
            graph.addEdge(p2, p1, distance);
        }
        return graph;
    }
}
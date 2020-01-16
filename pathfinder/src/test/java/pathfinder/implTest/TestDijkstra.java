package pathfinder.implTest;

import graph.Graph;
import org.junit.Test;
import static org.junit.Assert.*;
import pathfinder.Dijkstra;

public class TestDijkstra {
    public static final String A = "A";
    public static final String B = "B";
    public static final String C = "C";
    public static final String D = "D";

    @Test
    public void testTieBreaking(){
        Graph<String, Double> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(A, B, 1.0);
        g.addEdge(A, C, 2.0);
        g.addEdge(B, C, 1.0);
        g.addEdge(C, B, 1.0);
        g.addEdge(B, D, 3.0);
        g.addEdge(C, D, 2.0);
        assertTrue(Dijkstra.findPath(g, A, D).toString().equals("A =(1.000)=> B =(3.000)=> D") ||
                Dijkstra.findPath(g, A, D).toString().equals("A =(2.000)=> C =(2.000)=> D"));
    }
}

package graph.implTest;

import graph.Graph;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.Timeout;

public class TestGraph{
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10);

    public static final String A = "A";
    public static final String B = "B";
    public static final String C = "C";
    public static final String D = "D";

    @Test
    public void testConstructor() {
        Graph g = new Graph();
        assertEquals("", g.toString());
    }


    @Test
    public void testIsEmptyOne() {
        Graph<String, String> g = new Graph<>();
        assertTrue(g.isEmpty());
    }

    @Test
    public void testIsEmptyTwo() {
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        assertFalse(g.isEmpty());
    }

    @Test
    public void testIsEmptyThree(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.removeVertex(B);
        g.removeVertex(A);
        assertTrue(g.isEmpty());
    }

    @Test
    public void testIsEmptyFour(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.removeVertex(B);
        g.removeVertex(A);
        g.addVertex(A);
        g.addEdge(A, A, "1");
        g.removeVertex(A);
        assertTrue(g.isEmpty());
    }


    @Test
    public void testContainsVertexOne(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        assertTrue(g.contains(A));
        assertTrue(g.contains(B));
        assertFalse(g.contains(D));
    }

    @Test
    public void testContainsVertexTwo() {
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(D);
        assertTrue(g.contains(D));
    }

    @Test
    public void testContainsVertexThree() {
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(D);
        g.removeVertex(A);
        assertFalse(g.contains(A));
    }

    @Test
    public void testContainsVertexFour(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(D);
        g.removeVertex(A);
        g.removeVertex(B);
        assertFalse(g.contains(B));
    }

    @Test
    public void testContainsEdgeOne(){
        Graph<String, String> g = new Graph<String, String>();
        g.addVertex(A);
        g.addVertex(B);
        assertFalse(g.contains(A, B, "1"));
    }

    @Test
    public void testContainsEdgeTwo(){
        Graph<String, String> g = new Graph<String, String>();
        g.addVertex(A);
        g.addVertex(B);
        g.addEdge(A, B, "1");
        assertTrue(g.contains(A, B, "1"));
    }

    @Test
    public void testContainsEdgeThree(){
        Graph<String, String> g = new Graph<String, String>();
        g.addVertex(A);
        g.addVertex(B);
        g.addEdge(A, B, "1");
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(C, D, "2");
        g.addEdge(A, C, "3");
        assertTrue(g.contains(C, D, "2"));
        assertTrue(g.contains(A, C, "3"));
        assertFalse(g.contains(C, A, "3"));
    }

    @Test
    public void testContainsEdgeFour(){
        Graph<String, String> g = new Graph<String, String>();
        g.addVertex(A);
        g.addVertex(B);
        g.addEdge(A, B, "1");
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(C, D, "2");
        g.addEdge(A, C, "3");
        g.removeEdge(A, C, "3");
        assertFalse(g.contains(A, C, "3"));
    }

    @Test
    public void testListNodeOne(){
        Graph<String, String> g = new Graph<String, String>();
        g.addVertex(A);
        Set<String> l = new HashSet<>();
        l.add(A);
        assertEquals(g.setNodes(), l);
    }

    @Test
    public void testListNodeTwo(){
        Graph<String, String> g = new Graph<String, String>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        Set<String> l = new HashSet<>();
        l.add(C);
        l.add(B);
        l.add(A);
        assertEquals(g.setNodes(), l);
    }

    @Test
    public void testListNodeThree(){
        Graph<String, String> g = new Graph<String, String>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        Set<String> l = new HashSet<>();
        l.add(A);
        l.add(C);
        l.add(B);
        g.addEdge(A, B, "1");
        assertEquals(g.setNodes(), l);
    }

    @Test
    public void testListNodeFour(){
        Graph<String, String> g = new Graph<String, String>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addEdge(A, B, "1");
        g.removeVertex(A);
        Set<String> l = new HashSet<>();
        l.add(C);
        l.add(B);
        assertEquals(g.setNodes(), l);
    }

    @Test
    public void testListChildrenOne(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(B, C, "2");
        g.addEdge(B, C, "3");
        g.addEdge(B, C, "4");
        Map<String, HashSet<String>> m = new HashMap<>();
        HashSet<String> s = new HashSet<>();
        s.add("2");
        s.add("3");
        s.add("4");
        m.put(C, s);
        assertTrue(g.listChildren(B).equals(m));
    }

    @Test
    public void testListChildrenTwo(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(B, C, "2");
        g.addEdge(B, C, "3");
        g.addEdge(B, C, "4");
        g.removeEdge(B, C, "2");
        Map<String, HashSet<String>> m = new HashMap<>();
        HashSet<String> s = new HashSet<>();
        s.add("3");
        s.add("4");
        m.put(C, s);
        assertTrue(g.listChildren(B).equals(m));
    }

    @Test
    public void testListChildrenThree(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(B, C, "2");
        g.addEdge(B, C, "3");
        g.addEdge(B, C, "4");
        g.removeEdge(B, C, "2");
        g.removeEdge(B, C, "3");
        g.removeEdge(B, C, "4");
        Map<String, HashSet<String>> m = new HashMap<>();
        HashSet<String> s = new HashSet<>();
        m.put(C, s);
        assertTrue(g.listChildren(B).isEmpty());
    }

    @Test
    public void testSetEdgeOne(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(A, B, "1");
        g.addEdge(A, B, "2");
        g.addEdge(A, B, "3");
        Set<String> s = new TreeSet<>();
        s.add("1");
        s.add("3");
        s.add("2");
        assertEquals(s, g.setEdges(A, B));
    }

    @Test
    public void testSetEdgeTwo(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        assertNull(g.setEdges(A, B));
    }

    @Test
    public void testSetEdgeThree(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addEdge(A, B, "1");
        g.removeEdge(A, B, "1");
        assertNull(g.setEdges(A, B));
    }

    @Test
    public void testAreConnectedOne(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        assertFalse(g.areConnected(A, B));
        assertFalse(g.areConnected(A, C));

    }

    @Test
    public void testAreConnectedTwo(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addEdge(A, B, "2");
        assertTrue(g.areConnected(A, B));
    }

    @Test
    public void testAreConnectedThree(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addEdge(A, C, "2");
        g.removeVertex(C);
        assertFalse(g.areConnected(A, C));
    }

    @Test
    public void testAddVertexOne(){
        Graph<String, String> g = new Graph<>();

        g.addVertex(A);
        assertEquals("A()", g.toString());

        g.addVertex(B);
        assertEquals("A(),\n" +
                "B()", g.toString());

        g.addVertex(A);
        assertEquals("A(),\n" +
                "B()", g.toString());

        g.addVertex(C);
        assertEquals("A(),\n" +
                "B(),\n" +
                "C()", g.toString());

        g.addVertex(D);
        assertEquals("A(),\n" +
                "B(),\n" +
                "C(),\n" +
                "D()", g.toString());
    }

    @Test
    public void testAddVertexTwo(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        assertEquals("A(),\n" +
                "B()", g.toString());
    }

    @Test
    public void testAddVertexThree(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(A);
        assertEquals("A(),\n" +
                "B()", g.toString());
    }

    @Test
    public void testAddVertexFour(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(A);
        g.addVertex(C);
        assertEquals("A(),\n" +
                "B(),\n" +
                "C()", g.toString());
    }

    @Test
    public void testAddVertexFive(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(A);
        g.addVertex(C);
        g.addVertex(D);
        assertEquals("A(),\n" +
                "B(),\n" +
                "C(),\n" +
                "D()", g.toString());
    }


    @Test
    public void testAddEdgeOne(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(A, B, "1");
        g.addEdge(B, C, "2");
        assertEquals("A(B:{1}),\n" +
                "B(C:{2}),\n" +
                "C(),\n" +
                "D()", g.toString());

    }

    @Test
    public void testAddEdgeTwo() {
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(A, B, "1");
        g.addEdge(B, C, "2");
        g.addEdge(C, D, "3");
        g.addEdge(D, A, "4");
        assertEquals("A(B:{1}),\n" +
                "B(C:{2}),\n" +
                "C(D:{3}),\n" +
                "D(A:{4})", g.toString());
    }

    @Test
    public void testAddEdgeThree(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(A, B, "1");
        g.addEdge(B, C, "2");
        g.addEdge(C, D, "3");
        g.addEdge(D, A, "4");
        g.addEdge(A, B, "2");
        g.addEdge(A, B, "1");
        assertEquals("A(B:{1, 2}),\n" +
                "B(C:{2}),\n" +
                "C(D:{3}),\n" +
                "D(A:{4})", g.toString());
    }

    @Test
    public void testAddEdgeFour(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(A, B, "1");
        g.addEdge(B, C, "2");
        g.addEdge(C, D, "3");
        g.addEdge(D, A, "4");
        g.addEdge(A, B, "2");
        g.addEdge(A, B, "1");
        g.addEdge(C, C, "7");
        g.addEdge(D, D, "8");
        assertEquals("A(B:{1, 2}),\n" +
                "B(C:{2}),\n" +
                "C(C:{7}, D:{3}),\n" +
                "D(A:{4}, D:{8})", g.toString());
    }

    @Test
    public void testRemoveVertexOne(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addEdge(A, B, "1");
        g.removeVertex(D);
        assertEquals("A(B:{1}),\n" +
                "B()", g.toString());
    }

    @Test
    public void testRemoveVertexTwo(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addEdge(A, B, "1");
        g.removeVertex(D);
        g.removeVertex(B);
        assertEquals("A()", g.toString());
    }

    @Test
    public void testRemoveVertexThree(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addEdge(A, B, "1");
        g.removeVertex(D);
        g.removeVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(A, C, "2");
        g.removeVertex(A);
        assertEquals("C(),\n" +
                "D()", g.toString());

    }

    @Test
    public void testRemoveVertexFour(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addEdge(A, B, "1");
        g.removeVertex(D);
        g.removeVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(A, C, "2");
        g.removeVertex(A);
        g.addEdge(C, C, "1");
        g.addEdge(D, D, "2");
        g.addVertex(B);
        g.addEdge(D, B, "3");
        g.removeVertex(C);
        assertEquals("B(),\n" +
                "D(B:{3}, D:{2})", g.toString());
    }


    @Test
    public void testRemoveEdgeOne(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(A, B, "1");
        g.addEdge(B, C, "2");
        g.removeEdge(A, B, "2");
        assertEquals("A(B:{1}),\n" +
                "B(C:{2}),\n" +
                "C(),\n" +
                "D()", g.toString());
    }

    @Test
    public void testRemoveEdgeTwo(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(A, B, "1");
        g.addEdge(B, C, "2");
        g.removeEdge(A, B, "2");
        g.removeEdge(A, B, "1");
        g.removeEdge(A, B, "1");
        assertEquals("A(),\n" +
                "B(C:{2}),\n" +
                "C(),\n" +
                "D()", g.toString());
    }

    @Test
    public void testRemoveEdgeThree(){
        Graph<String, String> g = new Graph<>();
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(A, B, "1");
        g.addEdge(B, C, "2");
        g.removeEdge(A, B, "2");
        g.removeEdge(A, B, "1");
        g.removeEdge(A, B, "1");
        g.addEdge(C, C, "3");
        g.addEdge(D, A, "4");
        g.removeEdge(D, A, "2");
        assertEquals("A(),\n" +
                "B(C:{2}),\n" +
                "C(C:{3}),\n" +
                "D(A:{4})", g.toString());
    }

    @Test
    public void testRemoveEdgeFour(){
        Graph<String, String> g = new Graph<>();

        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);
        g.addEdge(A, B, "1");
        g.addEdge(B, C, "2");
        g.removeEdge(A, B, "2");
        g.removeEdge(A, B, "1");
        g.removeEdge(A, B, "1");
        g.addEdge(C, C, "3");
        g.addEdge(D, A, "4");
        g.removeEdge(D, A, "2");
        g.addEdge(D, A, "1");
        g.removeEdge(D, A, "2");
        g.removeEdge(C, C, "3");
        assertEquals("A(),\n" +
                "B(C:{2}),\n" +
                "C(),\n" +
                "D(A:{1, 4})", g.toString());
    }
    @Test
    public void testEqualsOne(){
        Graph<String, String> g1 = new Graph<String, String>();
        Graph<String, String> g2 = new Graph<String, String>();
        g1.addVertex(A);
        g2.addVertex(A);
        assertTrue(g1.equals(g2));
    }

    @Test
    public void testEqualsTwo(){
        Graph<String, String> g1 = new Graph<String, String>();
        Graph<String, String> g2 = new Graph<String, String>();
        g1.addVertex(A);
        g2.addVertex(A);
        g1.addVertex(B);
        assertFalse(g1.equals(g2));
    }

    @Test
    public void testEqualsThree(){
        Graph<String, String> g1 = new Graph<String, String>();
        Graph<String, String> g2 = new Graph<String, String>();
        g1.addVertex(A);
        g2.addVertex(A);
        g1.addVertex(B);
        g2.addVertex(B);
        g2.addVertex(B);
        g1.addVertex(C);
        g2.addVertex(C);
        g1.addEdge(A, C, "1");
        g2.addEdge(A, C, "1");
        assertTrue(g1.equals(g2));
    }

    @Test
    public void testEqualsFour(){
        Graph<String, String> g1 = new Graph<String, String>();
        Graph<String, String> g2 = new Graph<String, String>();
        g1.addVertex(A);
        g2.addVertex(A);
        g1.addVertex(B);
        g2.addVertex(B);
        g2.addVertex(B);
        g1.addVertex(C);
        g2.addVertex(C);
        g1.addEdge(A, C, "1");
        g2.addEdge(A, C, "1");
        g1.addEdge(A, B, "2");
        g2.addEdge(A, C, "2");
        assertFalse(g1.equals(g2));
    }

    @Test
    public void testHashCodeOne(){
        Graph<String, String> g1 = new Graph<>();
        Graph<String, String> g2 = new Graph<>();
        assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    public void testHashCodeTwo(){
        Graph<String, String> g1 = new Graph<>();
        Graph<String, String> g2 = new Graph<>();
        g1.addVertex(B);
        g2.addVertex(B);
        assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    public void testHashCodeThree(){
        Graph<String, String> g1 = new Graph<>();
        Graph<String, String> g2 = new Graph<>();
        g1.addVertex(B);
        g2.addVertex(B);
        g1.addVertex(A);
        g1.addVertex(C);
        g2.addVertex(A);
        g2.addVertex(C);
        assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    public void testHashCodeFour(){
        Graph<String, String> g1 = new Graph<>();
        Graph<String, String> g2 = new Graph<>();
        g1.addVertex(B);
        g2.addVertex(B);
        g1.addVertex(A);
        g1.addVertex(C);
        g2.addVertex(A);
        g2.addVertex(C);
        g1.addEdge(A, B, "2");
        g2.addEdge(A, B, "2");
        g1.addEdge(B, B, "3");
        g2.addEdge(B, B, "3");
        g1.addEdge(C, A, "2");
        g2.addEdge(C, A, "2");
        assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    public void testToStringOne(){
        Graph<String, String> g = new Graph<String, String>();
        assertEquals("", g.toString());
    }

    @Test
    public void testToStringTwo(){
        Graph<String, String> g = new Graph<String, String>();
        g.addVertex(B);
        g.addVertex(A);
        assertEquals("A(),\n" +
                "B()", g.toString());
    }

    @Test
    public void testToStringThree(){
        Graph<String, String> g = new Graph<String, String>();
        g.addVertex(B);
        g.addVertex(A);
        g.addVertex(C);
        g.addEdge(A, B, "1");
        assertEquals("A(B:{1}),\n" +
                "B(),\n" +
                "C()", g.toString());
    }

    @Test
    public void testToStringFour(){
        Graph<String, String> g = new Graph<String, String>();
        g.addVertex(B);
        g.addVertex(A);
        g.addVertex(C);
        g.addEdge(A, B, "1");
        g.addEdge(C, A, "3");
        g.addEdge(B, B, "2");
        g.addEdge(B, B, "1");
        g.removeVertex(B);
        assertEquals("A(),\n" +
                "C(A:{3})", g.toString());
    }
}

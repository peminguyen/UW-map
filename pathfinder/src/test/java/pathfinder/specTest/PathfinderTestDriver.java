/*
 * Copyright ©2019 Dan Grossman.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Autumn Quarter 2019 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.specTest;

import java.io.*;

import graph.Graph;
import marvel.MarvelPaths;
import pathfinder.Dijkstra;
import pathfinder.datastructures.Path;

import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */

public class PathfinderTestDriver{
    public static void main(String[] args) {
        try {
            if(args.length > 1) {
                printUsage();
                return;
            }
            PathfinderTestDriver td;

            if(args.length == 0) {
                td = new PathfinderTestDriver(new InputStreamReader(System.in), new OutputStreamWriter(System.out));
                System.out.println("Running in interactive mode.");
                System.out.println("Type a line in the spec testing language to see the output.");
            } else {
                String fileName = args[0];
                File tests = new File(fileName);
                System.out.println("Reading from the provided file.");
                System.out.println("Writing the output from running those tests to standard out.");
                if(tests.exists() || tests.canRead()) {
                    td = new PathfinderTestDriver(new FileReader(tests), new OutputStreamWriter(System.out));
                } else {
                    System.err.println("Cannot read from " + tests.toString());
                    printUsage();
                    return;
                }
            }
            td.runTests();
        }
        catch(IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("  Run the gradle 'build' task");
        System.err.println("  Open a terminal at hw-graph/build/classes/java/test");
        System.err.println("  To read from a file: java graph.specTest.GraphTestDriver <name of input script>");
        System.err.println("  To read from standard in (interactive): java graph.specTest.GraphTestDriver");
    }

    // ***************************
    // ***  JUnit Test Driver  ***
    // ***************************

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/
    private final Map<String, Graph<String, Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @spec.requires r != null && w != null
     * @spec.effects Creates a new GraphTestDriver which reads command from
     * {@code r} and writes results to {@code w}
     **/
    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @throws IOException if the input or output sources encounter an IOException
     * @spec.effects Executes the commands read from the input and writes results to the output
     **/
    public void runTests()
            throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<String>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1)
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);


        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new Graph<>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2)
            throw new CommandException("Bad arguments to AddNode: " + arguments);

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);
        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String, Double> g = graphs.get(graphName);
        g.addVertex(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4)
            throw new CommandException("Bad arguments to AddEdge: " + arguments);

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        Double edgeLabel;

        try {
            edgeLabel = Double.parseDouble(arguments.get(3));
        }
        catch (NumberFormatException nfe) {
            throw new CommandException("The last argument should be of type Double");
        }

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         Double edgeLabel) {
        Graph<String, Double> g = graphs.get(graphName);
        g.addEdge(parentName, childName, edgeLabel);
        output.println(String.format("added edge %.3f", edgeLabel) + " from " + parentName +
                " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }
        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph<String, Double> g = graphs.get(graphName);
        String result = graphName + " contains:";
        Set<String> vertices = new TreeSet<>(g.setNodes());
        for (String vertex : vertices)
            result += " " + vertex;
        output.println(result);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2)
            throw new CommandException("Bad arguments to ListChildren: " + arguments);

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        Graph<String, Double> g = graphs.get(graphName);
        String result = "the children of " + parentName + " in " + graphName + " are:";
        List<String> l1 = new ArrayList<>(g.listChildren(parentName).keySet());
        Collections.sort(l1);
        for (String v: l1) {
            Double edgeCost = g.setEdges(parentName, v).iterator().next();
            result += " " + v + String.format("(%.3f)", edgeCost);
        }
        output.println(result);
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3)
            throw new CommandException("Bad arguments to findPath: " + arguments);

        String graphName = arguments.get(0);
        String start = arguments.get(1);
        String end = arguments.get(2);
        findPath(graphName, start, end);
    }

    private void findPath(String graphName, String start, String end) {
        Graph<String, Double> g = graphs.get(graphName);

        if ((!g.contains(start)) && (!g.contains(end))) {
            output.println("unknown node " + start);
            output.println("unknown node " + end);
        }
        else if (!(g.contains(start)))
            output.println("unknown node " + start);
        else if (!(g.contains(end)))
            output.println("unknown node " + end);
        else {
            String toPrint = "path from " + start + " to " + end + ":";
            Path<String> path = Dijkstra.findPath(g, start, end);
            if (path == null)
                toPrint += "\n" + "no path found";
            else {
                List<Path<String>.Segment> listOfSegments = path.getList();
                for (Path<String>.Segment nextSegment : listOfSegments) {
                    toPrint += "\n" + nextSegment.getStart() + " to " + nextSegment.getEnd() + " with weight " + String.format("%.3f", nextSegment.getCost());
                }
                toPrint += "\n" + "total cost: " + String.format("%.3f", path.getCost());
            }
            output.println(toPrint);
        }
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {
        public CommandException() {
            super();
        }
        public CommandException(String s) {
            super(s);
        }
        public static final long serialVersionUID = 3495;
    }
}

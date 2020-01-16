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

package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
In the pathfinder homework, the text user interface calls these methods to talk
to your model. In the campuspaths homework, your graphical user interface
will ultimately make calls to these methods (through a web server) to
talk to your model the same way.

This is the power of the Model-View-Controller pattern, two completely different
user interfaces can use the same model to display and interact with data in
different ways, without requiring a lot of work to change things over.
*/

/**
 * This class represents the connection between the view/controller and the model
 * for the pathfinder and campus paths applications.
 */
public class ModelConnector {
    // AF(this) = a ModelConnector object that contains the constant list of CampusBuilding on UW campus, a graph representing
    // the connectedness between points on UW campus, and a map to look up the coordinate point from a building's
    // shortname
    // RI: graph != null && coordinateLookUp != null


    /**
     * This field is where we store the list of information of all buildings
     */
    private static final List<CampusBuilding> listCampusBuilding = CampusPathsParser.parseCampusBuildings();

    /**
     * This field is where we store the graph of all coordinates in UW
     */
    private static Graph<Point, Double> graph = new Graph<>();

    /**
     * This field is where we map every building abbreviation to a coordinate point
     */
    private static Map<String, Point> coordinateLookUp = new HashMap<>();

    /**
     * @spec.effects The graph is initialized and the map coordinateLookUp is initialized
     */
    public ModelConnector() {
      /**
       * Creates a new {@link ModelConnector} and initializes it to contain data about
       * pathways and buildings or locations of interest on the campus of the University
       * of Washington, Seattle. When this constructor completes, the dataset is loaded
       * and prepared, and any method may be called on this object to query the data.
       */
      List<CampusPath> listPath = CampusPathsParser.parseCampusPaths();

      graph = GraphBuilding.constructGraph(listPath);

      for (CampusBuilding building : listCampusBuilding){
          double x = building.getX();
          double y = building.getY();
          Point p = new Point(x, y);
          coordinateLookUp.put(building.getShortName(), p);
      }
  }

  /**
   * @param shortName The short name of a building to query.
   * @return {@literal true} iff the short name provided exists in this campus map.
   */
  public boolean shortNameExists(String shortName) {
      for (CampusBuilding building: listCampusBuilding){
          if (building.getShortName().equals(shortName)){
              return true;
          }
      }
      return false;
  }

  /**
   * @param shortName The short name of a building to look up.
   * @return The long name of the building corresponding to the provided short name.
   * @throws IllegalArgumentException if the short name provided does not exist.
   */
  public String longNameForShort(String shortName) {
      checkRep();
      for (CampusBuilding building: listCampusBuilding){
          if (building.getShortName().equals(shortName)){
              return building.getLongName();
          }
      }
      checkRep();
      return null;
  }

  /**
   * @return The mapping from all the buildings' short names to their long names in this campus map.
   */
  public Map<String, String> buildingNames() {
      checkRep();
      Map<String, String> map = new HashMap<>();
      for (CampusBuilding building: listCampusBuilding)
          map.put(building.getShortName(), building.getLongName());
      checkRep();
      return map;
  }


  /**
   * Finds the shortest path, by distance, between the two provided buildings.
   *
   * @param startShortName The short name of the building at the beginning of this path.
   * @param endShortName   The short name of the building at the end of this path.
   * @return A path between {@code startBuilding} and {@code endBuilding}, or {@literal null}
   * if none exists.
   * @throws IllegalArgumentException if {@code startBuilding} or {@code endBuilding} are
   *                                  {@literal null}, or not valid short names of buildings in
   *                                  this campus map.
   */
  public Path<Point> findShortestPath(String startShortName, String endShortName) {
      checkRep();
      if (startShortName == null || endShortName == null ||
              !shortNameExists(startShortName) || !shortNameExists(endShortName))
          throw new IllegalArgumentException("Invalid parameter");
      Point start = coordinateLookUp.get(startShortName);
      Point dest = coordinateLookUp.get(endShortName);
      checkRep();
      return Dijkstra.findPath(graph, start, dest);
  }

    /**
     * Throws an exception if the representation invariant is violated.
     */
  private void checkRep(){
      assert(graph != null);
      assert(coordinateLookUp != null);
  }
}

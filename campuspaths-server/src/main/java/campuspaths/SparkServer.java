package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.ModelConnector;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.textInterface.TextInterfaceView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.ArrayList;
import java.util.List;

public class SparkServer {

  public static void main(String[] args) {
    CORSFilter corsFilter = new CORSFilter();
    corsFilter.apply();
    // The above two lines help set up some settings that allow the
    // React application to make requests to the Spark server, even though it
    // comes from a different server.
    // You should leave these two lines at the very beginning of main().

    // The link to get the path from one building to another
    Spark.get("/findPath", new Route() {
      @Override
      public Object handle(Request request, Response response) throws Exception {
        String startString = request.queryParams("start");
        String endString = request.queryParams("end");
        if(startString == null || endString == null)
          Spark.halt(400, "The link must have start and end");

        ModelConnector mc = new ModelConnector();
        if (!mc.shortNameExists(startString) || !mc.shortNameExists(endString))
          Spark.halt(400, "Invalid building names");

        Path<Point> path = mc.findShortestPath(startString, endString);
        Gson gson = new Gson();
        return gson.toJson(path);
      }
    });

    // The link to get all building information
    // It returns a list of lists, in which each inner list has the form [shortName, longName] (of every building)
    Spark.get("/buildings", new Route() {
      @Override
      public Object handle(Request request, Response response) throws Exception {
        ModelConnector mc = new ModelConnector();
        Gson gson = new Gson();
        List<List<String>> list = new ArrayList<>();
        for (String shortname : mc.buildingNames().keySet()){
          List<String> list_line = new ArrayList<>();
          list_line.add(shortname);
          list_line.add(mc.buildingNames().get(shortname));
          list.add(list_line);
        }
        return gson.toJson(list);
      }
    });

    // The link to get the direction details from one building to another
    Spark.get("/getDirection", new Route() {
      @Override
      public Object handle(Request request, Response response) throws Exception {
        String startString = request.queryParams("start");
        String endString = request.queryParams("end");
        if(startString == null || endString == null)
          Spark.halt(400, "The link must have start and end");

        ModelConnector mc = new ModelConnector();
        if (!mc.shortNameExists(startString) || !mc.shortNameExists(endString))
          Spark.halt(400, "Invalid building names");

        Path<Point> path = mc.findShortestPath(startString, endString);
        TextInterfaceView view = new TextInterfaceView();

        // I modified the showPath method in order to return the direction String
        String s = view.showPath(mc.longNameForShort(startString), mc.longNameForShort(endString), path);

        Gson gson = new Gson();
        return gson.toJson(s);
      }
    });


  }
}

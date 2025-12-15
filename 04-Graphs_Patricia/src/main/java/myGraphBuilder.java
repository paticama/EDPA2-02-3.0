import graphsDS_ESI_UCLM_v2.*;
import java.io.IOException;
import java.util.*;

/**************************************************************************************************
 * 
 * Class Name: myGraphBuilder
 * Author/s name: PC, RCR, LD - Group 02
 * Release/Creation date: 13/12/2025
 * Class description: Main class for assignment 04. Builds a graph from bikeway data
 * 
***************************************************************************************************
*/

public class myGraphBuilder {
    static final String PATH = "C:\\Users\\pcama\\Downloads\\EDPA2-02-3.0-master\\EDPA2-02-3.0-master\\04-Graphs_Patricia\\src\\main\\resources\\bikeways2.csv"; //Path to .csv
    private static final Scanner input = new Scanner(System.in);
    private static int VirtualID = 4001;
    /**************************************************************************************************
     * 
     * Method Name: main
     * Name of the original author: PC, RCR, LD
     * Description of the Method: Entry point of the program. Reads bikeway data from CSV file,
     * builds graph, displays statistics and displays the menu
     * Calling arguments: String[] args 
     * Return value: void
     * Required files: bikeways2.csv 
     * 
    ***************************************************************************************************
    */
    public  static void main(String[] args) throws IOException {
        try {
            SequentialFile<BikewaySegment> file = new SequentialFile<>(PATH, ";");
            BikewaySegment segment = new BikewaySegment();
            Graph g = new TreeMapGraph();
            int docLines = 0;
            int numDegGreat = 0;
            double totalDist = 0;

            file.skipLine(); //Header won't be read

            while (file.checkEOF()) {
                file.read(segment);
                docLines++;

                if(segment.checkValid()) {
                    segment.processCoordinates();
                    addToGraph(segment, g);
                    totalDist += segment.segmentLength;
                }
                segment = new BikewaySegment(); //Needed so that the reader keeps reading something other than the same bikeway
            }
            //Print relevant statistics
            System.out.println("Number of intersections: " + g.getN());
            System.out.println("Number of segments: " + g.getM());
            System.out.println("Selected " + g.getM() + " segments out of " + docLines + " candidates.");
            System.out.println("Average length of " + totalDist/g.getM() + " meters.");
            System.out.println("Number of intersections with degree greater than 2: " + degreeVertex(g, 2) ); //2 is given by the requirements.

            showMenu();
            processOptions(g);
        }
        catch(IOException e){
            System.out.println("Something went wrong. That file does not exist");
        }

    }
    /**************************************************************************************************
     * 
     * Method Name: addToGraph
     * Name of the original author: PC
     * Description of the Method: Adds a bikeway segment to the graph by creating vertices for start
     * and end intersections, creating an edge between them with the segment as decorator
     * Calling arguments: BikewaySegment segment, Graph g 
     * Return value: void
     * Required files: None
     * 
    ***************************************************************************************************
    */
    public static void addToGraph(BikewaySegment segment, Graph g){
        Edge<BikewaySegment> bikeSegment;
        Vertex<Intersection> startPoint, endPoint;

        Intersection sPoint = new Intersection(segment.startPoint);
        Intersection ePoint = new Intersection(segment.endPoint);

        startPoint = g.insertVertex(sPoint);
        endPoint = g.insertVertex(ePoint);

        bikeSegment = g.insertEdge(startPoint, endPoint);
        bikeSegment.setDecorator(new Decorator<BikewaySegment>(segment) {}); //El {} implica que es anónimo

    }
    /**************************************************************************************************
     * 
     * Method Name: processOptions
     * Name of the original author: RCR
     * Description of the Method: Main loop that processes user menu input, handling the options
     * Calling arguments: Graph g 
     * Return value: void
     * Required files: None
     * 
    ***************************************************************************************************
    */
    public static void processOptions(Graph g){
        String entrada;
        boolean finished = false;

        while (!finished) {
            System.out.println("Please, enter an option and the commands: ");
            entrada = input.nextLine();

            if(!entrada.isEmpty()) {
                char option = entrada.charAt(0);
                if (option == 'e') {
                    System.out.println("Ending program...");
                    finished = true;
                } else if (option == 'b' || option == 's' || option == 'd') {
                    String[] split = entrada.trim().split("\\s+");
                    if(split.length != 3) {
                        System.out.println("Invalid number of arguments");
                    }
                    else {
                        try {
                            Intersection coord1 = new Intersection(split[1]);
                            Intersection coord2 = new Intersection(split[2]);
                            manageMenu(g, option, coord1, coord2, finished);
                        } catch (Exception e){
                            System.out.println("Invalid coordinates");
                        }
                    }
                }
                else{
                    System.out.println("That option is not supported ");
                }
            } else{
                System.out.println("Empty input");
            }
        }
    }
    /**************************************************************************************************
     * 
     * Method Name: degreeVertex
     * Name of the original author: LD
     * Description of the Method: Searches all nodes of the graph and counts how many are greater than a certain parameter
     * Calling arguments: Graph g, int limit 
     * Return value: int 
     * Required files: None
     * 
    ***************************************************************************************************
    */
    public static int degreeVertex(Graph g, int limit){
        
        int degGreater = 0;

        Iterator<Vertex> it;
        it = g.getVertices();

        while(it.hasNext()){
            Vertex v = it.next();
            int currentDeg = 0;
            Iterator<Edge> itCurrent = g.incidentEdges(v);
            while (itCurrent.hasNext()){
                itCurrent.next();
                currentDeg++;
            }
            if(currentDeg > limit){
                degGreater++;
            }
        }
        return degGreater;
    }
    /**************************************************************************************************
     * 
     * Method Name: showMenu
     * Name of the original author: RCR
     * Description of the Method: Displays the interactive menu with available path-finding options
     * Calling arguments: None
     * Return value: void
     * Required files: None
     * 
    ***************************************************************************************************
    */
    public static void showMenu(){
        System.out.printf("--OPTIONS--\n");
        System.out.printf("b <lat,long> <lat,long>: Finds shortest path between the given coordinates. \n");
        System.out.printf("s <lat,long> <lat,long>: Finds shortest path between the given coordinates only" +
                " considering paths with snow removal. \n");
        System.out.printf("d <lat,long> <lat,long>: Finds a path between the given coordinates using BFS. " +
                "Only considers segments with a speed limit of 30 km/h.  \n");
        System.out.printf("e: Exits the program. \n");
        System.out.printf("-----------\n");
    }
    /**************************************************************************************************
     * 
     * Method Name: manageMenu
     * Name of the original author: PC
     * Description of the Method: Dispatches menu commands to appropriate path-finding algorithms
     * (BFS standard, BFS with snow removal, or DFS). Creates virtual segments if path not found
     * Calling arguments: Graph g, char option, Intersection coord1, Intersection coord2, boolean finished 
     * Return value: boolean 
     * Required files: None
     * 
    ***************************************************************************************************
    */
    public static boolean manageMenu(Graph g, char option, Intersection coord1, Intersection coord2, boolean finished) {
        switch (option) {
            case 'b':
                List<Vertex> bfs = BFS(g, coord1, coord2, false);
                if (bfs == null || bfs.size() == 1) {
                    System.out.println("Path not found, virtual segments will be created");
                    createVirtualSegments(g, coord1, coord2);
                    //we call the BFS so we don't have to call the "b" option again
                    bfs = BFS(g, coord1, coord2, false);
                    if (bfs != null && bfs.size() > 1) {
                        displayPath(g, bfs);
                    }
                } else {
                    displayPath(g, bfs);
                }
                break;
            case 's':
                List<Vertex> bfsSnow = BFS(g, coord1, coord2, true);
                if (bfsSnow == null || bfsSnow.size() == 1) {
                    System.out.println("Path not found, virtual segments will not be created. ");
                    // As virtual paths are basically blank, we wont create one as it wont have snow removal.
                } else {
                    displayPath(g, bfsSnow);
                }
                break;
            case 'd':
                List<Vertex> dfs = DFS(g, coord1, coord2);
                if (dfs == null || dfs.size() == 1) {
                    System.out.println("Path not found.");
                } else {
                    displayPath(g, dfs);
                }
                break;
            default:
                System.out.println("ERROR, option not available");
                break;
        }
        return finished;
    }
    /**************************************************************************************************
     * 
     * Method Name: DFS
     * Name of the original author: PC, RCR, LD
     * Description of the Method: Performs Depth-First Search to find a path between two intersections.
     * Calling arguments: Graph gp, Intersection start, Intersection finish 
     * Return value: List<Vertex>, null
     * Required files: None
     * 
    ***************************************************************************************************
    */
    public static List DFS(Graph gp, Intersection start, Intersection finish){
        Vertex<Intersection> s = gp.getVertex(start.getID());
        Vertex<Intersection> f = gp.getVertex(finish.getID());
        boolean found = false;
        if(!gp.exists(s) || !gp.exists(f)){
            System.out.println("ERROR: Nodes not in graph.");
            return null;
        }
        Stack<Vertex> st = new Stack<>();
        Map<Vertex, Vertex> parent = new HashMap<>();
        Set<Vertex> visited = new HashSet<>();

        st.push(s);
        visited.add(s);

        while (!st.isEmpty() && !found) {

            Vertex current = st.pop();

            if (current.equals(f))
                break;
            Iterator<Edge<BikewaySegment>> it = gp.incidentEdges(current);
            while (it.hasNext()) {
                Edge<BikewaySegment> e = it.next();
                BikewaySegment segment = e.getDecorator().getData();
                if (segment.speedLimit != 30 || segment.bikewayType.equals("VIRTUAL")) {
                    //As virtual paths are blank, they have no speed limit, but we added the comparison so it is more readeable
                    continue; // Skip invalid segment 
                }
                Vertex<Intersection> neighbor = gp.opposite(current, e);

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    st.push(neighbor);
                }
            }
        }
        // Path reconstruction 
        LinkedList<Vertex<Intersection>> path = new LinkedList<>();

        Vertex p = f;
        while (p != null) {
            path.addFirst(p);
            p = parent.get(p);
        }
        return path;
    }

    /**************************************************************************************************
     * 
     * Method Name: BFS
     * Name of the original author: PC, RCR, LD
     * Description of the Method: Performs Breadth-First Search to find shortest path between two
     * intersections. Can optionally filter for protected bike lanes with snow removal
     * Calling arguments: Graph gp, Intersection start, Intersection finish, Boolean snow 
     * Return value: List<Vertex>, null
     * Required files: None
     * 
    ***************************************************************************************************
    */
    public static List BFS(Graph gp, Intersection start, Intersection finish, Boolean snow){
        Vertex<Intersection> s = gp.getVertex(start.getID());
        Vertex<Intersection> f = gp.getVertex(finish.getID());
        boolean found = false;

        if(!gp.exists(s) || !gp.exists(f)){
            System.out.println("ERROR: Nodes not in graph.");
            return null;
        }

        Queue<Vertex> q = new LinkedList<>();
        Map<Vertex, Vertex> parent = new HashMap<>();
        Set<Vertex> visited = new HashSet<>();

        q.add(s);
        visited.add(s);

        while (!q.isEmpty() && !found) {

            Vertex current = q.poll();

            if (current.equals(f))
                break;

            //Iterator<Vertex> it = gp.incidentEdges(current);
            Iterator<Edge<BikewaySegment>> it = gp.incidentEdges(current);

            while (it.hasNext()) {
                //Vertex neighbor = it.next();
                Edge<BikewaySegment> e = it.next();
                BikewaySegment segment = e.getDecorator().getData();
                if (snow && (!segment.bikewayType.equals("Protected Bike Lanes") || !segment.snowRemoval)) {
                    continue; // Skip invalid segment 
                }

                Vertex<Intersection> neighbor = gp.opposite(current, e);

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    q.add(neighbor);
                }
            }
        }

        
        //LinkedList<Vertex> path = new LinkedList<>();
        LinkedList<Vertex<Intersection>> path = new LinkedList<>();

        Vertex p = f;
        while (p != null) {
            path.addFirst(p);
            p = parent.get(p);
        }

        return path;
    }
    /**************************************************************************************************
     * 
     * Method Name: displayPath
     * Name of the original author: RCR
     * Description of the Method: Displays detailed information about a found path including all
     * segments, distances, and surface types
     * Calling arguments: Graph g, List<Vertex> path
     * Return value: void
     * Required files: None
     * 
    ***************************************************************************************************
    */
    public static void displayPath(Graph g, List<Vertex> path) {
        if (path == null || path.isEmpty()) {
            System.out.println("Path not found");
            return;
        }

        System.out.println("\n=== FOUND PATH === ");
        // -1 because last size is a vertex, not an edge
        System.out.println("Total segment in path: " + (path.size() - 1));
        Set<String> surfaceTypes = new HashSet<>();
        double totalDistance = 0;
        
        System.out.println("Starting point: " + path.get(0).getData());
        
        
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex<Intersection> u = path.get(i);
            Vertex<Intersection> v = path.get(i + 1);
            Edge<BikewaySegment> edge = findEdgeBetween(g, u, v);
            
            if (edge != null) {
                BikewaySegment segment = edge.getDecorator().getData();
                System.out.println("\n ---- Segment " + (i + 1) + " ---- ");
                System.out.println("ID: " + segment.ID);
                System.out.println("Name: " + segment.routeName);
                System.out.println("Distance: " + segment.segmentLength + " m");
                System.out.println("Starting intersection: " + u.getData() + " Finishing intersection: " + v.getData());
                totalDistance += segment.segmentLength;
                surfaceTypes.add(segment.surfaceType);
            }
        }
        
        
        System.out.println("\n=== SUMMARY ===");
        System.out.println("TOTAL DISTANCE: " +  totalDistance + " m");
        System.out.println("Surface types: ");
        for (String surface : surfaceTypes) {
            System.out.println(surface);
        }
    }
    /**************************************************************************************************
     * 
     * Method Name: findEdgeBetween
     * Name of the original author: PC
     * Description of the Method: Finds the edge connecting two vertices in the graph
     * Calling arguments: Graph g, Vertex u, Vertex v 
     * Return value: Edge<BikewaySegment>, null
     * Required files: None
     * 
    ***************************************************************************************************
    */
    private static Edge<BikewaySegment> findEdgeBetween(Graph g, Vertex u, Vertex v) {
        Iterator<Edge<BikewaySegment>> it = g.incidentEdges(u);
        while (it.hasNext()) {
            Edge<BikewaySegment> edge = it.next();
            Vertex opposite = g.opposite(u, edge);
            if (opposite.equals(v)) {
                return edge;
            }
        }
        return null;
    }
    /**************************************************************************************************
     * 
     * Method Name: createVirtualSegments
     * Name of the original author: RCR
     * Description of the Method: Creates virtual segments to connect start and finish points through a midpoint when no direct path exists
     * Calling arguments: Graph g, Intersection start, Intersection finish 
     * Return value: void
     * Required files: None
     * 
    ***************************************************************************************************
    */
    public static void createVirtualSegments(Graph g, Intersection start, Intersection finish) {
        String[] startCoords = start.getID().split(",");
        String[] finishCoords = finish.getID().split(",");

        double startLon = Double.parseDouble(startCoords[0]);
        double startLat = Double.parseDouble(startCoords[1]);
        double finishLon = Double.parseDouble(finishCoords[0]);
        double finishLat = Double.parseDouble(finishCoords[1]);

        double avgLon = (startLon + finishLon) / 2.0;
        double avgLat = (startLat + finishLat) / 2.0;

        String avgCoords = avgLon + "," + avgLat;
        Intersection midPoint = new Intersection(avgCoords);
        
       Vertex<Intersection> existingMid = g.getVertex(midPoint.getID());
        if (existingMid != null) {
            System.out.println("Cannot create an intersection because it already exists");
            return;
        }
        
        Vertex<Intersection> mid = g.insertVertex(midPoint);
        Vertex<Intersection> s = g.getVertex(start.getID());
        Vertex<Intersection> f = g.getVertex(finish.getID());
        
        //we assume 100 m in distance for a virtual distance
        double dist1 = 100.0;
        double dist2 = 100.0;
        
        
        BikewaySegment virtual1 = createVirtualSegment(VirtualID++, dist1);
        BikewaySegment virtual2 = createVirtualSegment(VirtualID++, dist2);
        
        
        Edge<BikewaySegment> e1 = g.insertEdge(s, mid);
        // Without the {} does not work, don't know why  -> {} means its anonymous, as Decorator
        // cannot be instantiated, this is a way to make it work.
        e1.setDecorator(new Decorator<BikewaySegment>(virtual1) {});
        
        Edge<BikewaySegment> e2 = g.insertEdge(mid, f);
        e2.setDecorator(new Decorator<BikewaySegment>(virtual2) {});

        System.out.println("\n=== VIRTUAL SEGMENTS ===");
        System.out.println("Mid Point : " + avgCoords);
        System.out.println("Virtual segment 1: ID " + virtual1.ID + " " + dist1 + " m");
        System.out.println("Virtual segment 2: ID " + virtual2.ID + " " + dist2 + " m");
        System.out.println("Vertex: " + g.getN());
        System.out.println("Edges: " + g.getM());
    }
    /**************************************************************************************************
     * 
     * Method Name: createVirtualSegment
     * Name of the original author: LD
     * Description of the Method: Creates a BikewaySegment object with virtual properties and specified length
     * Calling arguments: int id, double length 
     * Return value: BikewaySegment 
     * Required files: None
     * 
    ***************************************************************************************************
    */
    private static BikewaySegment createVirtualSegment(int id, double length) {
        BikewaySegment virtual = new BikewaySegment();
        virtual.ID = id;
        virtual.routeName = "VIRTUAL";
        virtual.streetName = "VIRTUAL";
        virtual.bikewayType = "VIRTUAL";
        virtual.speedLimit = 0;
        virtual.surfaceType = "VIRTUAL";
        virtual.snowRemoval = false;
        virtual.segmentLength = length;
        virtual.yearOfConstruction = 0;
        virtual.setVirtual(true); // Marcar como virtual
        return virtual;
    }

}

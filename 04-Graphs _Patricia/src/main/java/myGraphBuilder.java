import graphsDS_ESI_UCLM_v2.*;
import java.io.IOException;
import java.util.*;

public class myGraphBuilder {
    static final String PATH = "C:\\Users\\pcama\\Desktop\\Curso25-26\\Primer Cuatrimestre\\EDA\\Assignment05\\assign05\\bikeways.csv"; //Path to .csv
    private static final Scanner input = new Scanner(System.in);

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
            System.out.println("Number of intersections with degree greater than 2: " + gradoVertice(g, 2) ); //2 is given by the requirements.

            showMenu();
            processOptions(g);
        }
        catch(IOException e){
            System.out.println("Something went wrong. That file does not exist");
        }

    }

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

    public static void processOptions(Graph g){
        String entrada;
        boolean finished = false;

        while (!finished) {
            System.out.println("Please, enter an option and the commands: ");
            entrada = input.nextLine();
            char option = entrada.charAt(0);
            if (option == 'e') {
                System.out.println("Ending program...");
                finished = true;
            } else if (option == 'b' || option == 's' || option == 'd') {
                String[] split = entrada.trim().split("\\s+");
                //parse first option and create first coord
                double x1 = Double.parseDouble(split[1]);
                double y1 = Double.parseDouble(split[2]);
                double[] coord1 = {x1, y1};
                //parse second option and create second coord
                double x2 = Double.parseDouble(split[3]);
                double y2 = Double.parseDouble(split[4]);
                double[] coord2 = {x2, y2};
                displayMenu(g, option, coord1, coord2, finished);
            }
            else{
                System.out.println("That option is not supported ");
            }
        }
    }

    public static int gradoVertice(Graph g, int limit){
        //Searches all nodes of the graph and counts how many are greater than a certain parameter
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

    public static void showMenu(){
        System.out.printf("--OPTIONS--");
        System.out.printf("b <lat,long> <lat,long>: Finds shortest path between the given coordinates. ");
        System.out.printf("s <lat,long> <lat,long>: Finds shortest path between the given coordinates only" +
                " considering paths with snow removal. ");
        System.out.printf("d <lat,long> <lat,long>: Finds a path between the given coordinates using BFS. " +
                "Only considers segments with a speed limit of 30 km/h.  ");
        System.out.printf("e: Exits the program. ");


    }

    public static boolean displayMenu(Graph g, char option, double[] coord1, double[] coord2, boolean finished) {
        switch (option) {
            case 'b':
                //List bfs = BFS(g, coord1, coord2); //TODO: APAÑAR LOS PARÁMETROS SOLICITADOS EN METODO BFS!!
                break;
            case 's':

                break;
            case 'd':

                break;
            default:
                System.out.println("ERROR, option not available");
                break;
        }
        return finished;
    }

    public static List BFS(Graph gp, Vertex s, Vertex f){
        Queue<Vertex> q = new LinkedList<>();
        Map<Vertex, Vertex> parent = new HashMap<>();
        Set<Vertex> visited = new HashSet<>();

        q.add(s);
        visited.add(s);

        while (!q.isEmpty()) {

            Vertex current = q.poll();

            if (current.equals(f))
                break;

            //Iterator<Vertex> it = gp.incidentEdges(current);
            Iterator<Edge<BikewaySegment>> it = gp.incidentEdges(current);

            while (it.hasNext()) {
                //Vertex neighbor = it.next();
                Edge<BikewaySegment> e = it.next();
                Vertex<Intersection> neighbor = gp.opposite(current, e);

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    q.add(neighbor);
                }
            }
        }


        if (!parent.containsKey(f)) return null;

        // reconstruir camino
        //LinkedList<Vertex> path = new LinkedList<>();
        LinkedList<Vertex<Intersection>> path = new LinkedList<>();

        Vertex p = f;
        while (p != null) {
            path.addFirst(p);
            p = parent.get(p);
        }

        return path;
    }


}

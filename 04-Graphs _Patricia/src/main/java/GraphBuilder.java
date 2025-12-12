/*
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

import graphsDS_ESI_UCLM_v2.*;


/*********************************************************************
 *
 * Class Name: GraphBuilder
 * Author/s name: PC, RCR, LD - Group 02
 * Release/Creation date: 13/11/2025
 * Class description: Clase encargada de construir el grafo con el CSV y establecer los filtros del enunciado
 *
 **********************************************************************/

/*
public class GraphBuilder {

    private Graph graph;
    private int totalSegments = 0;
    private int validSegments = 0;
    private double totalLength= 0;


    public GraphBuilder(){
        this.graph = new TreeMapGraph();
    }
    // Lee el archivo CSV y filtra los segmentos válidos, añadiéndolos al grafo

    public void loadCsv(String filePath){
        try (Scanner scanner = new Scanner(new File(filePath))){
            scanner.nextLine(); // Saltar cabecera

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] fields = line.split(";");

                try {

                    int ID =  Integer.parseInt(fields[0]);
                    String speedStr = fields[4];
                    String lengthStr = fields[6];
                    String yearStr = fields[7];

                    /* 
                    String geom = fields[0];
                    String speedStr = fields[4];
                    String lengthStr = fields[6];
                    String yearStr = fields[7];
                    */
/*
                    // Validar que los campos requeridos no estén vacíos
                    if (!speedStr.isEmpty() && !lengthStr.isEmpty() && !yearStr.isEmpty()) {
                        double length = Double.parseDouble(lengthStr);
                        int speed = Integer.parseInt(speedStr);
                        int year = Integer.parseInt(yearStr);


                        // Aplicar filtros del enunciado
                        if (speed >= 30 && length > 10 && year >= 1990){
                            validSegments++;
                            totalSegments++;


                            // Procesar coordenadas del segmento
                            String[] points = geom.replace("MULTILINESTRING ((", "").replace("))", "").split(",");
                            String[] start = points[0].trim().split(" ");
                            String[] end = points[points.length - 1].trim().split(" ");
                            String startCoord = roundTo5(start[0]) + "," + roundTo5(start[1]);
                            String endCoord = roundTo5(end[0]) + "," + roundTo5(end[1]);


                            // Añadir vértices y arista al grafo
                            graph.insertVertex(startCoord);
                            graph.insertVertex(endCoord);
                            
                            if (!startCoord.equals(endCoord)) {
                                Vertex<String> vStart = graph.insertVertex(startCoord);
                                Vertex<String> vEnd= graph.insertVertex(endCoord);
                                graph.insertEdge(vStart,vEnd);
                            }


                            totalLength += length;
                        } else {
                            totalSegments++;
                        }
                    } else {
                        totalSegments++;
                    }
                } catch (Exception e) {
                    // Saltar líneas mal hechas
                    totalSegments++;
                }
            }


        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        }
        
        
    }
 // Método para redondear a 5 decimales un valor en formato string
    private String roundTo5(String value) {
        BigDecimal bd = new BigDecimal(value);
        return String.valueOf(bd.setScale(5, RoundingMode.HALF_UP).doubleValue());
    }
    public Graph getGraph() {
    return graph;
    }

    


}
//

 */


import graphsDS_ESI_UCLM_v2.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BikewaySegment implements iface, Identity {
    String routeName, streetName, bikewayType, surfaceType, geom;
    int ID, speedLimit, yearOfConstruction; //Remember, this is km/h and m
    boolean snowRemoval;
    
    double segmentLength;
    String [] points, start, end;
    String startPoint, endPoint;
    private boolean virtual = false;


    @Override
    public void readData(String[] data) {
        this.ID = Integer.parseInt(data[0]);
        this.routeName = data[1];
        this.streetName = data[2];
        this.bikewayType = data[3];
        this.speedLimit = (!data[10].isEmpty()) ? Integer.parseInt(data[10]) : 0;
        this.surfaceType = data[11];
        this.snowRemoval = data[16].equals("Yes");
        this.segmentLength = Double.parseDouble(data[17]);
        this.yearOfConstruction = (!data[18].isEmpty()) ? Integer.parseInt(data[18]) : 0;
        this.geom = data[22];
    }

    // Método para redondear a 5 decimales un valor en formato string
    private String roundTo5(String value) {
        BigDecimal bd = new BigDecimal(value);
        return String.valueOf(bd.setScale(5, RoundingMode.HALF_UP).doubleValue());
    }

    public void  processCoordinates(){
        //points = geom.replace("MULTILINESTRING ((", "").replace("))", "").split(",");
        points = geom.replace("[", "").replace("]", "").split(" ");
        start = points[0].trim().split(",");
        end = points[points.length - 1].trim().split(",");

        startPoint = roundTo5(start[0]) + "," + roundTo5(start[1]);
        endPoint = roundTo5(end[0]) + "," + roundTo5(end[1]);

    }

    public boolean checkValid() {
        return (speedLimit >= 30 && segmentLength > 10  &&  yearOfConstruction >= 1990);
        // Hay un problema en el CSV (por ej. linea 1107) en el que la longitud de segmentos
        // tiene un formato extraño, y puede hacer que varíe el número de segmentos validos
    }
    public void setVirtual(boolean virtual){
        this.virtual = virtual;

    }

    @Override
    public String toString() {
        String str = "ID: " + ID + " RouteName: " + routeName + " Street Name: " + streetName + " Bikeway Type: " +
                " SpeedLimit: " + speedLimit + " Surface Type: " + surfaceType + " SnowRemoval: " + snowRemoval +
                " Segment Length: " + segmentLength + " Year of construction: " + yearOfConstruction + " Geom:" + geom +
                " StartPoint: " + startPoint + " EndPoint: " + endPoint;
        return str;
    }

    @Override
    public String getID() {
        return startPoint + "," + endPoint + "," + streetName + "," + bikewayType;
    }
}




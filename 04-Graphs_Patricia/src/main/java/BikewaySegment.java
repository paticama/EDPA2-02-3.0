import graphsDS_ESI_UCLM_v2.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**************************************************************************************************
 * 
 * Class Name: BikewaySegment
 * Author/s name: PC, RCR, LD - Group 02
 * Release/Creation date: 11/12/2025
 * Class description: Represents a bikeway segment with all its properties 
 * 
***************************************************************************************************
*/
public class BikewaySegment implements iface, Identity {
    String routeName, streetName, bikewayType, surfaceType, geom;
    int ID, speedLimit, yearOfConstruction; //Remember, this is km/h and m
    boolean snowRemoval;
    
    double segmentLength;
    String [] points, start, end;
    String startPoint, endPoint;
    private boolean virtual = false;

    /**************************************************************************************************
     * 
     * Method Name: readData
     * Name of the original author: PC
     * Description of the Method: Reads and parses bikeway segment data from CSV string array
     * Calling arguments: String[] data 
     * Return value: void
     * Required files: None
     * 
    ***************************************************************************************************
    */
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

    /**************************************************************************************************
     * 
     * Method Name: roundTo5
     * Name of the original author: LD
     * Description of the Method: Rounds a coordinate string value to 5 decimal places using HALF_UP rounding mode
     * Calling arguments: String value 
     * Return value: String 
     * Required files: None
     * 
    ***************************************************************************************************
    */
    private String roundTo5(String value) {
        BigDecimal bd = new BigDecimal(value);
        return String.valueOf(bd.setScale(5, RoundingMode.HALF_UP).doubleValue());
    }

    /**************************************************************************************************
     * 
     * Method Name: processCoordinates
     * Name of the original author: LD
     * Description of the Method: Parses the geometry string to extract start and end coordinates,
     * rounding them to 5 decimal places for consistency
     * Calling arguments: None
     * Return value: void
     * Required files: None
     * 
    ***************************************************************************************************
    */
    public void  processCoordinates(){
        points = geom.replace("[", "").replace("]", "").split(" ");
        start = points[0].trim().split(",");
        end = points[points.length - 1].trim().split(",");

        startPoint = roundTo5(start[0]) + "," + roundTo5(start[1]);
        endPoint = roundTo5(end[0]) + "," + roundTo5(end[1]);

    }
    /**************************************************************************************************
     * 
     * Method Name: checkValid
     * Name of the original author: PC
     * Description of the Method: Validates segment based on requirements
     * Calling arguments: None
     * Return value: boolean 
     * Required files: None
     * 
    ***************************************************************************************************
    */
    public boolean checkValid() {
        return (speedLimit >= 30 && segmentLength > 10  &&  yearOfConstruction >= 1990);
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




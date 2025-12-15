import graphsDS_ESI_UCLM_v2.Identity;

/**************************************************************************************************
 * 
 * Class Name: Intersection
 * Author/s name: PC, RCR, LD - Group 02
 * Release/Creation date: 12/12/2025
 * Class description: Represents an intersection in the bikeway network identified by coordinates.
 * 
***************************************************************************************************
*/
public class Intersection implements Identity {
    String coord;

    public Intersection(String c) {
        coord = c;
    }

    public String getID() {
        return coord;
    }

    @Override
    public String toString(){
        return coord;
    }
}

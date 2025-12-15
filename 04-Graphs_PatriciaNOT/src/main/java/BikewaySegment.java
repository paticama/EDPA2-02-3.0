
/*********************************************************************
 *
 * Class Name: BikewaySegment
 * Author/s name: PC, RCR, LD - Group 02
 * Release/Creation date: 13/11/2025
 * Class description: Represents a bikeway segment as read from the CSV.It stores the information that is logically attached to each edge of the graph.
 *
 *********************************************************************/
public class BikewaySegment {

    private int id;
    private int speedLimit;// km/h
    private double length;// meters
    private int year;// year of construction
    private boolean snowRemoval;// snow removal service
    private String startCoord;// "lon,lat" rounded
    private String endCoord;//"lon,lat" rounded

    public BikewaySegment(int id,int speedLimit,double length,int year,boolean snowRemoval,String startCoord,String endCoord){
        this.id = id;
        this.speedLimit = speedLimit;
        this.length = length;
        this.year = year;
        this.snowRemoval = snowRemoval;
        this.startCoord = startCoord;
        this.endCoord = endCoord;
    }

    public BikewaySegment(){}


    // Getters

    public int getId() {
        return id;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public double getLength() {
        return length;
    }

    public int getYear() {
        return year;
    }

    public boolean hasSnowRemoval() {
        return snowRemoval;
    }

    public String getStartCoord() {
        return startCoord;
    }

    public String getEndCoord() {
        return endCoord;
    }

    @Override
    public String toString() {
        return "BikewaySegment{" +"id=" + id +", speedLimit=" + speedLimit +", length=" + length +", year=" + year +", snowRemoval=" + snowRemoval +", startCoord='" + startCoord + '\'' +", endCoord='" + endCoord + '\'' +'}';
    }
    
}


import graphsDS_ESI_UCLM_v2.Decorator;

public class BikewayDecorator extends Decorator {
    private BikewaySegment segment;
     //Constructor: recibe el segmento asociado a la arista y se lo pasa al decorator
    public BikewayDecorator(BikewaySegment segment) {
        // Importante: llamar al constructor correcto de Decorator
        super(segment);
        this.segment = segment;
    }
    public BikewaySegment getSegment() {
        return segment;
    }
    public void setSegment(BikewaySegment segment) {
        this.segment = segment;
    }

    @Override
    public String toString() {
        return "BikewayDecorator{ segment=" + segment + " }";
    }
}

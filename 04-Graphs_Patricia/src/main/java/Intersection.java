import graphsDS_ESI_UCLM_v2.Identity;

//Necesita implementar Identity para que el Grafo pueda reconocer cada ID como la identidad del nodo
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

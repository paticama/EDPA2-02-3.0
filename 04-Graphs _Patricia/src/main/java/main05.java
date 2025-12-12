import java.util.*;

public class main05 {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        String entrada;
        boolean finished = false;

        //Remember to change the path!!
        while (!finished) {
            System.out.println("Please, enter an option and the commands: ");
            entrada = input.nextLine();
            if (entrada.charAt(0) == 'e') {
                System.out.println("Ending program...");
                finished = true;
            } else {
                String[] split = entrada.trim().split("\\s+");
                String option = split[0];
                //parse first option and create first coord
                double x1 = Double.parseDouble(split[1]);
                double y1 = Double.parseDouble(split[2]);
                double[] coord1 = {x1, y1};
                //parse second option and create second coord
                double x2 = Double.parseDouble(split[3]);
                double y2 = Double.parseDouble(split[4]);
                double[] coord2 = {x2, y2};
                displayMenu(option, coord1, coord2, finished);
            }
        }

    }

    public static boolean displayMenu(String option, double[] coord1, double[] coord2, boolean finished) {
        switch (option) {
            case "b":

                break;
            case "s":

                break;
            case "d":

                break;
            default:
                System.out.println("ERROR, option not available");
                break;
        }
        return finished;
    }
}


import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class AutomatonFileParser {

    public static void parseFile(File automatonFile, Vector<Integer> endStates, Vector<Transition> transitions) throws FileNotFoundException, IOException, NumberFormatException {

        String endStateString;
        int endState;

        try (Scanner scanner = new Scanner(automatonFile)) {
            endStateString = scanner.next();

            while (!endStateString.equals(".")) {
                endState = Integer.parseInt(endStateString);
                endStates.add(endState);
                endStateString = scanner.next();
            }

            while (scanner.hasNext()) {

                int transitionBeginState;
                char transitionSymbol;
                int transitionEndState;

                transitionBeginState = Integer.parseInt(scanner.next());
                transitionSymbol = scanner.next("[A-Za-z]").charAt(0);
                transitionEndState = Integer.parseInt(scanner.next());

                transitions.add(new Transition(transitionBeginState,transitionSymbol,transitionEndState));
            }
        }

    }

}
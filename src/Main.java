import java.io.*;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Usage: ./prog automatonFile stringFile");
            return;
        }

        File automatonFile = new File(args[0]);
        File stringFile = new File(args[1]);

        Vector<Integer> endStates = new Vector<>();
        Vector<Transition> transitions = new Vector<>();

        try {
            AutomatonFileParser.parseFile(automatonFile, endStates, transitions);

            int currentState = 1;
            int currentSymbol;

            boolean isFound;
            boolean isEnded = false;

            for (int endState : endStates) {
                if (currentState == endState)
                    isEnded = true;
            }

            if (isEnded) {
                System.out.println(isEnded);
                return;
            }

            try (Reader reader = new FileReader(stringFile)) {
                while ((currentSymbol = reader.read()) != -1) {

                    isFound = false;

                    for (Transition transition : transitions) {
                        if (currentState == transition.getBeginState() && currentSymbol == transition.getTransferSymbol()) {
                            currentState = transition.getEndState();
                            isFound = true;
                            break;
                        }
                    }

                    if (!isFound) {
                        break;
                    }

                    for (int endState : endStates) {
                        if (currentState == endState)
                            isEnded = true;
                    }

                    if (isEnded)
                        break;

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(isEnded);
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NumberFormatException e) {
            System.err.println("Wrong file format");
        }
    }
}

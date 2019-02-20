public class Transition {

    private int beginState;
    private int transferSymbol;
    private int endState;

    public Transition(int beginState, int transferSymbol, int endState) {
        this.beginState = beginState;
        this.transferSymbol = transferSymbol;
        this.endState = endState;
    }

    public int getBeginState() {
        return beginState;
    }

    public int getTransferSymbol() {
        return transferSymbol;
    }

    public int getEndState() {
        return endState;
    }
}

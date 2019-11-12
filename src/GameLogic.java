public class GameLogic implements IRules {

    protected final int WAIT_ON_TURN = 0;
    protected final int TURN_GRANTED = 1;
    protected int playerState = WAIT_ON_TURN;

    protected final int ROUND_START = 0;
    protected final int ROUND_END = 2;
    protected int gameState = ROUND_START;

    public void setStateForPlayer(int playerState) {
        this.playerState = playerState;
    }

    public void setStateForGame(int gameState) {
        this.gameState = gameState;
    }

    //Spelarna ska turas om att få "svara" på frågorna
    @Override
    public Boolean isItPlayersTurn() {
        if (playerState == WAIT_ON_TURN) {
            setStateForPlayer(TURN_GRANTED);
            return true;

        } else if (playerState == TURN_GRANTED) {
            setStateForPlayer(WAIT_ON_TURN);
            return false;
        }
        return isItPlayersTurn();
    }

    @Override
    public void startNewRound() {

        if (gameState == ROUND_START) {
            System.out.println("Round 1 started!");
            setStateForGame(1);

        } else if (gameState == 1) {
            System.out.println("Round 2 started!");
            setStateForGame(2);

        } else if (gameState == ROUND_END) {
            setStateForGame(0);
            startNewGame();
        }
    }

        @Override
        public void startNewGame() {
            //Launch quiz questions within same category
            System.out.println("NEW GAME!");
            startNewRound();
        }

    public static void main(String[] args) {
        GameLogic test = new GameLogic();

        System.out.println(test.isItPlayersTurn());
        System.out.println(test.isItPlayersTurn());
        System.out.println(test.isItPlayersTurn());

        test.startNewGame();
        test.startNewRound();

        test.startNewRound();
        test.startNewRound();
    }
}
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

    //Method to take turn on answering questions
    @Override
    public boolean isItPlayersTurn() {
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
            //Launch quiz questions within a random category

        } else if (gameState == 1) {
            System.out.println("Round 2 started!");
            setStateForGame(2);
            //Launch quiz question within same previous category

        } else if (gameState == ROUND_END) {
            setStateForGame(0);
            startNewGame();
        }
    }

        @Override
        public void startNewGame() {
            System.out.println("NEW GAME!");
            startNewRound();
        }

    public static void main(String[] args) {
        GameLogic testLogic = new GameLogic();

        System.out.println(testLogic.isItPlayersTurn());
        System.out.println(testLogic.isItPlayersTurn());
        System.out.println(testLogic.isItPlayersTurn());

        testLogic.startNewGame();
        testLogic.startNewRound();

        testLogic.startNewRound();
        testLogic.startNewRound();
    }
}
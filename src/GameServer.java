import java.io.*;
import java.net.*;

public class GameServer {

    Protocol protocol = new Protocol();
    private ServerSocket serverSocket;
    private int numberOfPlayers;
    private int port = 51734;
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    private ServerSideConnection player3;
    private boolean maxPlayersForOneGame = false;
    private int turn; //so the server can "count" what turn/question it is on
    private int playerOnePoints; // So the server can send the points to the other player
    private int playerTwopoints; // so the server can send the points to the other player
    
    public GameServer() {
        numberOfPlayers = 0;
        System.out.println("---Game Server Booting Up---");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("IOException from GameServer Constructor");
            ex.printStackTrace();
        }
    }

    //accepts 2 player for quiz competition and starts Threads for the players
    public void acceptConnection() {
        try {
            System.out.println("Waiting for connections...");
            while (numberOfPlayers < 2) {
                Socket socket = serverSocket.accept();
                numberOfPlayers++;
                System.out.println("Player #" + numberOfPlayers + " has connected.");
                ServerSideConnection ssc = new ServerSideConnection(socket, numberOfPlayers);
                if (numberOfPlayers == 1) {
                    player1 = ssc;
                } else {
                    player2 = ssc;
                }
                Thread thread = new Thread(ssc);
                thread.start();
            }
            System.out.println("We now have 2 players.");
            maxPlayersForOneGame = true;
        } catch (IOException ex) {
            System.out.println("IOException from acceptConnection");
            ex.printStackTrace();
        }
    }

    //gives runnable object to both players and differentiate the two players
    private class ServerSideConnection implements Runnable {
        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private int playerID;

        public ServerSideConnection(Socket socket, int id) {
            this.socket = socket;
            this.playerID = id;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream((socket.getOutputStream()));
            } catch (IOException ex) {
                System.out.println("IOException from ServerSideConnection constructor");
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                //state begin
                dataOutputStream.writeInt(playerID);
                dataOutputStream.writeUTF(protocol.getQuestion());
                dataOutputStream.writeUTF(protocol.getAlt1_1());
                dataOutputStream.writeUTF(protocol.getAlt1_2());
                dataOutputStream.writeUTF(protocol.getAlt1_3());
                dataOutputStream.writeUTF(protocol.getAlt1_4());
                dataOutputStream.writeUTF(protocol.getAnswer());

                dataOutputStream.flush();
                while (true) {
                    if(playerID == 1){
                        playerOnePoints += dataInputStream.readInt();
                        System.out.println("spelare 1 har" + playerOnePoints + "poäng");
                    }
                    if (playerID == 2){
                        playerTwopoints += dataInputStream.readInt();
                        System.out.println("spelare 2 har " + playerTwopoints + " poäng");
                    }
                    String question = protocol.question;
                    byte[] questionByte = question.getBytes();
                    dataOutputStream.write(questionByte);
                }
            } catch (IOException ex) {
                System.out.println("IOException from run() SSC");
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        gameServer.acceptConnection();
    }
}

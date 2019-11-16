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
    private int playerOnePoints = 0; // So the server can send the points to the other player
    private int playerTwopoints = 0; // so the server can send the points to the other player

    public GameServer() throws IOException {
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
            while (true) {
                Socket socket = serverSocket.accept();
                numberOfPlayers++;
                System.out.println("Player #" + numberOfPlayers + " has connected.");
                ServerSideConnection ssc = new ServerSideConnection(socket, numberOfPlayers);
                if (numberOfPlayers == 1) {
                    player1 = ssc;
                } else {
                    player2 = ssc;
                }
                if(numberOfPlayers == 2){
                    numberOfPlayers = 0;
                }
                Thread thread = new Thread(ssc);
                thread.start();
            }
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
                dataOutputStream.writeUTF(protocol.getAlt1());
                dataOutputStream.writeUTF(protocol.getAlt2());
                dataOutputStream.writeUTF(protocol.getAlt3());
                dataOutputStream.writeUTF(protocol.getAlt4());
                dataOutputStream.writeUTF(protocol.getAnswer());


                dataOutputStream.flush();
                while (true) {
                    if (playerID == 1) {
                        playerOnePoints = dataInputStream.readInt();
                        System.out.println("spelare 1 har " + playerOnePoints + " poäng");
                        player2.sendPoints(playerOnePoints);
                    }
                    if (playerID == 2){
                        playerTwopoints = dataInputStream.readInt();
                        System.out.println("spelare 2 har " + playerTwopoints + " poäng");
                        player1.sendPoints(playerTwopoints);
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

        public void sendPoints(int points) {
            try {
                dataOutputStream.writeInt(points);
                dataOutputStream.flush();
            } catch (IOException ex) {
                System.out.println("IOException from sendPoints SSC");
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        GameServer gameServer = new GameServer();
        gameServer.acceptConnection();
    }
}

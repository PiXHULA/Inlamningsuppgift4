import java.io.*;
import java.net.*;

public class GameServer {

    private ServerSocket serverSocket;
    private int numberOfPlayers;
    private int port = 51734;
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    private int turn; //so the server can "count" what turn/question it is on
    private int playerOnePoints; // So the server can send the points to the other player
    private int playerTwopoints; // so the server can send the points to the other player


    public GameServer(){
        System.out.println("---Game Server Booting Up---");
        numberOfPlayers = 0;
        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException ex){
            System.out.println("IOException from GameServer Constructor");
            ex.printStackTrace();
        }
    }

    //accepts 2 player for quiz competition and starts Threads for the players
    public void acceptConnection(){
        try{
            System.out.println("Waiting for connections...");
            while(numberOfPlayers < 2){
                Socket socket = serverSocket.accept();
                numberOfPlayers++;
                System.out.println("Player #" + numberOfPlayers + " has connected.");
                ServerSideConnection ssc = new ServerSideConnection(socket, numberOfPlayers);
                if(numberOfPlayers == 1){
                    player1 = ssc;
                }else{
                    player2 = ssc;
                }
                Thread thread = new Thread(ssc);
                thread.start();
            }
            System.out.println("We now have 2 players.");
        }catch (IOException ex){
            System.out.println("IOException from acceptConnection");
            ex.printStackTrace();
        }
    }

    //gives runnable object to both players and differentiate the two players
    private class ServerSideConnection implements Runnable{
        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private int playerID;

        public ServerSideConnection(Socket socket, int id){
            this.socket = socket;
            this.playerID = id;
            try{
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream((socket.getOutputStream()));
            }catch (IOException ex){
                System.out.println("IOException from ServerSideConnection constructor");
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            try{
                dataOutputStream.writeInt(playerID);
                dataOutputStream.flush();
                while(true){
                    //put in more later, so the server can send more stuff etc...
                }
            }catch (IOException ex){
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

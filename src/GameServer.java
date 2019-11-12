import java.io.*;
import java.net.*;

public class GameServer {

    private ServerSocket serverSocket;
    private int numberOfPlayers;
    private int port = 51734;
    private ServerSideConnection player1;
    private ServerSideConnection player2;


    public GameServer(){
        System.out.println("---Game Server Booting Up---");
        numberOfPlayers = 0;
        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException ex){
            System.out.println("IOException from GameServer Constructor");
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
        }catch (IOException ex){
            System.out.println("IOException from acceptConnection");
        }
    }

    //gives runnable object to both players
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
            }
        }

        @Override
        public void run() {
            try{
                dataOutputStream.writeInt(playerID);
                dataOutputStream.flush();
                while(true){

                }
            }catch (IOException ex){
                System.out.println("IOException from run() SSC");
            }
        }
    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        gameServer.acceptConnection();
    }
}

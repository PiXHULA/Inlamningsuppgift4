
import jdk.jfr.StackTrace;

import java.io.*;
import java.net.*;

public class GameServer {

    private ServerSocket serverSocket;
    private int numberOfPlayers;
    private int port = 51734;

    public GameServer(){
        System.out.printf("---Game Server Booting Up---");
        numberOfPlayers = 0;
        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException ex){
            System.out.printf("IOException from GameServer Constructor");
        }
    }

    public void acceptConnection(){
        try{
            System.out.printf("Waiting for connections...");
            while(numberOfPlayers < 2){
                Socket socket = serverSocket.accept();
                numberOfPlayers++;
                System.out.printf("Player #" + numberOfPlayers + " has connected.");
            }
        }catch (IOException ex){
            System.out.printf("IOException from acceptConnection");
        }
    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        gameServer.acceptConnection();
    }
}

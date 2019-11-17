package Testing;

import java.io.*;
import java.net.*;

public class GameServer {

    private ServerSocket serverSocket;
    private int numberOfPlayers;
    private int port = 51735;
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    private int turnsMade;
    private int maxTurns;
    private int[] values;
    private int player1ButtonNum;
    private int player2ButtonNum;

    private String[] question1;
    private String[] question2;
    private String[] question3;
    private String[] question4;

    public GameServer() {
        System.out.println("---GAME SERVER---");
        numberOfPlayers = 0;
        turnsMade = 0;
        maxTurns = 4;

        //String arrays med frågor och svar, layout: fråga, svar1, svar2, svar3, svar4, rättSvar;
        question1 = new String[]{"Vilket år skapades Java?","1995","2000","1972","1984", "1995"};
        question2 = new String[]{"När är Mahmud född?","1980","1968","1973","1976","1973"};
        question3 = new String[]{"Vilket år var bäst för Pedram?", "2003", "2005", "2007", "2001", "2003"};
        question4 = new String[]{"Vem skapade Java?","Ryan Gosling","James Gosling","Bruce Wayne","Peter Parker","James Gosling"};

        System.out.println("Question #1 is " + question1[0]);
        System.out.println("Question #2 is " + question2[0]);
        System.out.println("Question #3 is " + question3[0]);
        System.out.println("Question #4 is " + question4[0]);


        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnection() {
        try {
            System.out.println("Waiting for connections...");

            while (true) { //makes it posible to have multibul gamesessions runing at the same time
                while (numberOfPlayers < 2) { // connects 2 clients together
                    Socket socket = serverSocket.accept();
                    numberOfPlayers++; // control number for how many players in one game
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
                numberOfPlayers = 0; //resets the number of players so other instances of the server can run
            System.out.println("We now have 2 players");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ServerSideConnection implements Runnable {

        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private int playerID;

        public ServerSideConnection(Socket s, int ID) {
            socket = s;
            playerID = ID;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() {
            try {
                dataOutputStream.writeInt(playerID);
                dataOutputStream.writeInt(maxTurns);

                dataOutputStream.writeUTF(question1[0]);
                dataOutputStream.writeUTF(question2[0]);
                dataOutputStream.writeUTF(question3[0]);
                dataOutputStream.writeUTF(question4[0]);
                for(int i = 1; i < question1.length - 1; i++){
                dataOutputStream.writeUTF(question1[i]);
                }
                for(int i = 1; i < question2.length - 1; i++){
                dataOutputStream.writeUTF(question2[i]);
                }
                for(int i = 1; i < question3.length - 1; i++){
                dataOutputStream.writeUTF(question3[i]);
                }
                for(int i = 1; i < question4.length - 1; i++){
                dataOutputStream.writeUTF(question4[i]);
                }

                dataOutputStream.writeUTF(question1[5]);
                dataOutputStream.writeUTF(question2[5]);
                dataOutputStream.writeUTF(question3[5]);
                dataOutputStream.writeUTF(question4[5]);

                dataOutputStream.flush();
                while (true) {
                    if (playerID == 1) {
                        player1ButtonNum = dataInputStream.readInt();
                        System.out.println("Player 1 clicked button #" + player1ButtonNum);
                        player2.sendButtonNum(player1ButtonNum);
                    } else {
                        player2ButtonNum = dataInputStream.readInt();
                        System.out.println("player 2 clicked button #" + player2ButtonNum);
                        player1.sendButtonNum(player2ButtonNum);
                    }
                    turnsMade++;
                    if (turnsMade == maxTurns) {
                        System.out.println("Max Turns have been reached.");
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendButtonNum(int n) {
            try {
                dataOutputStream.writeInt(n);
                dataOutputStream.flush();
            } catch (IOException e) {
                System.out.println("Player #" + playerID + " disconnected.");
                e.printStackTrace();
            }
        }

        /*
        public void closeConnection() {
            try {
                socket.close();
                System.out.println("---Connection Closed---");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
         */

    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        gameServer.acceptConnection();
    }


}

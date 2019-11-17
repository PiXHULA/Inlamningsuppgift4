package Testing;

import java.io.*;
import java.net.*;

public class GameServer {

    Protocol protocol = new Protocol();
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
    private String[] questions = new String[4];
    private String[] alt1 = new String[4];;
    private String[] alt2 = new String[4];;
    private String[] alt3 = new String[4];;
    private String[] alt4 = new String[4];;
    private String[] rightAnswer = new String[4];

    public GameServer() throws IOException {
        System.out.println("---GAME SERVER---");
        numberOfPlayers = 0;
        turnsMade = 0;
        maxTurns = 4;
        values = new int[4];

        questions[0] = "Vilket år skapades Java?";
        questions[1] = "när är Mahmud född?";
        questions[2] = "Vilket år var bäst för Pedram?";
        questions[3] = "Vem skapade Java?";

        alt1[0] = "1995";
        alt1[1] = "2000";
        alt1[2] = "1972";
        alt1[3] = "1984";

        alt2[0] = "1980";
        alt2[1] = "1968";
        alt2[2] = "1973";
        alt2[3] = "1976";

        alt3[0] = "2003";
        alt3[1] = "2005";
        alt3[2] = "2007";
        alt3[3] = "2001";

        alt4[0] = "Ryan Gosling";
        alt4[1] = "James Gosling";
        alt4[2] = "Bruce Wayne";
        alt4[3] = "Peter Parker";

        rightAnswer[0] = "1995";
        rightAnswer[1] = "1973";
        rightAnswer[2] = "2003";
        rightAnswer[3] = "James Gosling";


        for (int i = 0; i < values.length; i++) {
            System.out.println("Question # " + (i + 1) + " is " + questions[i]);
        }

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

                dataOutputStream.writeUTF(protocol.getSortedHistoryQuestions()[0]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryQuestions()[1]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryQuestions()[2]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryQuestions()[3]);

                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts1()[0]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts2()[0]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts3()[0]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts4()[0]);

                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts1()[1]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts2()[1]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts3()[1]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts4()[1]);

                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts1()[2]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts2()[2]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts3()[2]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts4()[2]);

                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts1()[3]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts2()[3]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts3()[3]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAlts4()[3]);

                dataOutputStream.writeUTF(protocol.getSortedHistoryAnswers()[0]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAnswers()[1]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAnswers()[2]);
                dataOutputStream.writeUTF(protocol.getSortedHistoryAnswers()[3]);

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

    public static void main(String[] args) throws IOException {
        GameServer gameServer = new GameServer();
        gameServer.acceptConnection();
    }


}
package Testing;

import java.io.*;
import java.net.*;

public class GameServer {

    private ServerSocket serverSocket;
    private int numberOfPlayers;
    private int port = 51730;
    private ServerSideConnection[] otherPlayer = new ServerSideConnection[100];
    private int turnsMade;
    private int maxTurns;
    private int[] values;
    private int player1Points;
    private int player2Points;
    private String[] questions = new String[4];
    private String[] alt1 = new String[4];;
    private String[] alt2 = new String[4];;
    private String[] alt3 = new String[4];;
    private String[] alt4 = new String[4];;
    private String[] rightAnswer = new String[4];;
    private int playerIDPosition = 0;

    public GameServer() {
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

        System.out.println("Checking Data...");

        for (int i = 0; i < values.length; i++) {
            System.out.println("Question # " + (i + 1) + " is " + questions[i]);
        }

        System.out.println("Data checked!");

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnection() {
        try {
            System.out.println("Waiting for connections...");
            while (true) {
                while (numberOfPlayers < 2) {
                    Socket socket = serverSocket.accept();
                    numberOfPlayers++;
                    System.out.println("Player #" + numberOfPlayers + " has connected.");
                    playerIDPosition++;
                    if (numberOfPlayers == 1) {
                        otherPlayer[playerIDPosition] = new ServerSideConnection(socket, numberOfPlayers, playerIDPosition);
                        System.out.println(playerIDPosition + "acceptConnection player1");
                        otherPlayer[playerIDPosition].start();
                    } else {
                        otherPlayer[playerIDPosition] = new ServerSideConnection(socket, numberOfPlayers, playerIDPosition);
                        System.out.println(playerIDPosition + "acceptConnection player2");
                        otherPlayer[playerIDPosition].start();
                    }
                }
                numberOfPlayers = 0;
                System.out.println("We now have 2 players");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private class ServerSideConnection extends Thread {

        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private int playerID;
        private int playerIDposition;

        public ServerSideConnection(Socket s, int ID, int PlayerIDPosition ) {
            socket = s;
            playerID = ID;
            playerIDposition = PlayerIDPosition;
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
                dataOutputStream.writeInt(playerIDposition);
                dataOutputStream.writeInt(maxTurns);

                //här borde vara möjligheten att fixa en dataInputStream för att usern ska välja kategori

                dataOutputStream.writeUTF(questions[0]);
                dataOutputStream.writeUTF(questions[1]);
                dataOutputStream.writeUTF(questions[2]);
                dataOutputStream.writeUTF(questions[3]);

                dataOutputStream.writeUTF(alt1[0]);
                dataOutputStream.writeUTF(alt1[1]);
                dataOutputStream.writeUTF(alt1[2]);
                dataOutputStream.writeUTF(alt1[3]);

                dataOutputStream.writeUTF(alt2[0]);
                dataOutputStream.writeUTF(alt2[1]);
                dataOutputStream.writeUTF(alt2[2]);
                dataOutputStream.writeUTF(alt2[3]);

                dataOutputStream.writeUTF(alt3[0]);
                dataOutputStream.writeUTF(alt3[1]);
                dataOutputStream.writeUTF(alt3[2]);
                dataOutputStream.writeUTF(alt3[3]);

                dataOutputStream.writeUTF(alt4[0]);
                dataOutputStream.writeUTF(alt4[1]);
                dataOutputStream.writeUTF(alt4[2]);
                dataOutputStream.writeUTF(alt4[3]);

                dataOutputStream.writeUTF(rightAnswer[0]);
                dataOutputStream.writeUTF(rightAnswer[1]);
                dataOutputStream.writeUTF(rightAnswer[2]);
                dataOutputStream.writeUTF(rightAnswer[3]);

                dataOutputStream.flush();

                //här är sammankopplingen av två olika clienter samt poäng skickandet emellan
                while (true) {
                    if (playerID == 1) {
                        int playerIDposition = dataInputStream.readInt();
                        player1Points = dataInputStream.readInt();
                        System.out.println("Player 1 has " + player1Points + "points");
                        otherPlayer[playerIDposition+1].sendPoints(player1Points);
                    } else {
                        int playerIDposition = dataInputStream.readInt();
                        player2Points = dataInputStream.readInt();
                        System.out.println("player 2 has #" + player2Points + "points");
                        otherPlayer[playerIDposition-1].sendPoints(player2Points);
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

        public void sendPoints(int points) {
            try {
                dataOutputStream.writeInt(points);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        try {
            gameServer.acceptConnection();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
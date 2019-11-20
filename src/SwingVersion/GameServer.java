package SwingVersion;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.*;

public class GameServer {

    //Protocol protocol = new Protocol();
    Protocol protocol = new Protocol();
    private ServerSocket serverSocket;
    private int numberOfPlayers;
    private int port = 51730;
    private ServerSideConnection[] otherPlayer = new ServerSideConnection[100];
    private int turnsMade;
    private int maxTurns;
    private int[] values;
    private String[] questions = new String[4];
    private String[] alt1 = new String[4];
    private String[] alt2 = new String[4];
    private String[] alt3 = new String[4];
    private String[] alt4 = new String[4];
    private String[] rightAnswer = new String[4];
    private int playerIDPosition = 0;
    private int categori = 0;

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
                    playerIDPosition++; //denna gör att varje client kan "tracka" sig själv och därmed sin partner
                    if (numberOfPlayers == 1) {
                        otherPlayer[playerIDPosition] = new ServerSideConnection(socket, numberOfPlayers, playerIDPosition);
                        System.out.println(playerIDPosition + " acceptConnection player1");
                        otherPlayer[playerIDPosition].start();
                    } else {
                        otherPlayer[playerIDPosition] = new ServerSideConnection(socket, numberOfPlayers, playerIDPosition);
                        System.out.println(playerIDPosition + " acceptConnection player2");
                        otherPlayer[playerIDPosition].start();
                    }
                }
                numberOfPlayers = 0;
                System.out.println("We now have 2 players");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private class ServerSideConnection extends Thread {

        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private int playerID;
        private int playerIDposition;

        public ServerSideConnection(Socket s, int ID, int PlayerIDPosition) {
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

                playerIDposition = dataInputStream.readInt();
                categori = dataInputStream.readInt();

                //här skickar playerID 1 in data för att välja kategori
                if (playerID == 1) {
                    if (categori != 0) {
                        System.out.println("getting request for <T> fil");
                        sendQuestion(categori, playerIDposition);
                        categori = 0;
                    }
                }

                //här är den konstanta sammankopplingen av två olika clienter samt poäng skickandet emellan
                while (true) {

                    if (playerID == 1) {
                        int player1IDposition = dataInputStream.readInt();
                        int player1Points = dataInputStream.readInt();
                        System.out.println("player id position " + player1IDposition + " has sent data");
                        System.out.println("Player 1 has " + player1Points + " points");
                        otherPlayer[player1IDposition + 1].sendPoints(player1Points);
                    } else {
                        int player2IDposition = dataInputStream.readInt();
                        int player2Points = dataInputStream.readInt();
                        System.out.println("player id position " + player2IDposition + " has sent data");
                        System.out.println("player 2 " + player2IDposition + "has #" + player2Points + " points");
                        otherPlayer[player2IDposition - 1].sendPoints(player2Points);
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

        public void sendQuestion(int categorieNumber, int playerposition) {
            if (categorieNumber == 1) {
                //Sends all history question to 2 specific players and so on and so on...
                try {
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryQuestions()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryQuestions()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryQuestions()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryQuestions()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryQuestions()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryQuestions()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryQuestions()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryQuestions()[3]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts1()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts1()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts2()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts2()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts3()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts3()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts4()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts4()[0]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts1()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts1()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts2()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts2()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts3()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts3()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts4()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts4()[1]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts1()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts1()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts2()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts2()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts3()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts3()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts4()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts4()[2]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts1()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts1()[3]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts2()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts2()[3]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts3()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts3()[3]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts4()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts4()[3]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAnswers()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAnswers()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAnswers()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAnswers()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAnswers()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAnswers()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAnswers()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAnswers()[3]);

                    dataOutputStream.flush();
                    System.out.println("Sending history file...");
                } catch (IOException ex) {
                    System.out.println("IOException from sendQuestion history section");
                    ex.printStackTrace();
                }
            } else if (categorieNumber == 2) {
                //Sends all sport question to 2 specific players and so on and so on...
                try {
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportQuestions()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportQuestions()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportQuestions()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportQuestions()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportQuestions()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportQuestions()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportQuestions()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportQuestions()[3]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts1()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts1()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts2()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts2()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts3()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts3()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts4()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts4()[0]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts1()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts1()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts2()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts2()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts3()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts3()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts4()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts4()[1]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts1()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts1()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts2()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts2()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts3()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts3()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts4()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts4()[2]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts1()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts1()[3]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts2()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts2()[3]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts3()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts3()[3]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts4()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts4()[3]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAnswers()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAnswers()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAnswers()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAnswers()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAnswers()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAnswers()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedSportAnswers()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAnswers()[3]);

                    dataOutputStream.flush();
                    System.out.println("Sending sport file...");
                } catch (IOException ex) {
                    System.out.println("IOException from sendQuestion sport section");
                    ex.printStackTrace();
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException from sendQuestion sport section");
                    e.printStackTrace();
                }
            } else if (categorieNumber == 3) {
                //Sends all film question to 2 specific players and so on and so on...
                try {
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmQuestions()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmQuestions()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmQuestions()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmQuestions()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmQuestions()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmQuestions()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmQuestions()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmQuestions()[3]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts1()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts1()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts2()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts2()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts3()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts3()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts4()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts4()[0]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts1()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts1()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts2()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts2()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts3()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts3()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts4()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts4()[1]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts1()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts1()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts2()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts2()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts3()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts3()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts4()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts4()[2]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts1()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts1()[3]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts2()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts2()[3]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts3()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts3()[3]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts4()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts4()[3]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAnswers()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAnswers()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAnswers()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAnswers()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAnswers()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAnswers()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedFilmAnswers()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAnswers()[3]);

                    dataOutputStream.flush();
                    System.out.println("Sending film file...");
                } catch (IOException ex) {
                    System.out.println("IOException from sendQuestion film section");
                    ex.printStackTrace();
                }
            } else if (categorieNumber == 4) {
                //Sends all gaming question to 2 specific players and so on and so on...
                try {
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingQuestions()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingQuestions()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingQuestions()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingQuestions()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingQuestions()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingQuestions()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingQuestions()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingQuestions()[3]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts1()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts1()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts2()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts2()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts3()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts3()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts4()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts4()[0]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts1()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts1()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts2()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts2()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts3()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts3()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts4()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts4()[1]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts1()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts1()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts2()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts2()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts3()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts3()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts4()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts4()[2]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts1()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts1()[3]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts2()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts2()[3]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts3()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts3()[3]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts4()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts4()[3]);

                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAnswers()[0]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAnswers()[0]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAnswers()[1]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAnswers()[1]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAnswers()[2]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAnswers()[2]);
                    otherPlayer[playerposition].sendLine(protocol.getSortedGamingAnswers()[3]);
                    otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAnswers()[3]);

                    dataOutputStream.flush();
                    System.out.println("Sending gaming file...");
                } catch (IOException ex) {
                    System.out.println("IOException from sendQuestion gaming section");
                    ex.printStackTrace();
                }
            }
        }

        public void sendLine(String sortedQuestion) {
            try {
                dataOutputStream.writeUTF(sortedQuestion);
                dataOutputStream.flush();
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

    public static void main(String[] args) throws IOException {
        GameServer gameServer = new GameServer();
        try {
            gameServer.acceptConnection();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
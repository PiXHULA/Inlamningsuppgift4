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
                    for (int i = 0; i < protocol.getSortedHistoryQuestions().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedHistoryQuestions()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryQuestions()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedHistoryAlts1().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts1()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts1()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedHistoryAlts2().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts2()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts2()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedHistoryAlts3().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts3()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts3()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedHistoryAlts4().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAlts4()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAlts4()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedHistoryAnswers().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedHistoryAnswers()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedHistoryAnswers()[i]);
                    }
                    dataOutputStream.flush();
                    System.out.println("Sending history file...");
                } catch (IOException ex) {
                    System.out.println("IOException from sendQuestion history section");
                    ex.printStackTrace();
                }
            } else if (categorieNumber == 2) {
                //Sends all sport question to 2 specific players and so on and so on...
                try {
                    for (int i = 0; i < protocol.getSortedSportQuestions().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedSportQuestions()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportQuestions()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedSportAlts1().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts1()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts1()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedSportAlts2().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts2()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts2()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedSportAlts3().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts3()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts3()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedSportAlts4().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedSportAlts4()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAlts4()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedSportAnswers().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedSportAnswers()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedSportAnswers()[i]);
                    }
                    dataOutputStream.flush();
                    System.out.println("Sending film file...");
                } catch (IOException ex) {
                    System.out.println("IOException from sendQuestion film section");
                    ex.printStackTrace();
                }

            } else if (categorieNumber == 3) {
                //Sends all film question to 2 specific players and so on and so on...
                try {
                    for (int i = 0; i < protocol.getSortedFilmQuestions().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedFilmQuestions()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmQuestions()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedFilmAlts1().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts1()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts1()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedFilmAlts2().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts2()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts2()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedFilmAlts3().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts3()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts3()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedFilmAlts4().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedFilmAlts4()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAlts4()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedFilmAnswers().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedFilmAnswers()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedFilmAnswers()[i]);
                    }
                    dataOutputStream.flush();
                    System.out.println("Sending film file...");
                } catch (IOException ex) {
                    System.out.println("IOException from sendQuestion film section");
                    ex.printStackTrace();
                }
            } else if (categorieNumber == 4) {
                //Sends all gaming question to 2 specific players and so on and so on...
                try {
                    for (int i = 0; i < protocol.getSortedGamingQuestions().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedGamingQuestions()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingQuestions()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedGamingAlts1().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts1()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts1()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedGamingAlts2().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts2()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts2()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedGamingAlts3().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts3()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts3()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedGamingAlts4().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedGamingAlts4()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAlts4()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedGamingAnswers().length; i++) {
                        otherPlayer[playerposition].sendLine(protocol.getSortedGamingAnswers()[i]);
                        otherPlayer[playerposition + 1].sendLine(protocol.getSortedGamingAnswers()[i]);
                    }
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
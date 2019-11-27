package SwingVersion;

import java.io.*;
import java.net.*;

public class GameServer {

    Protocol protocol = new Protocol();
    private ServerSocket serverSocket;
    private int numberOfPlayers;
    private int port = 51730;
    private ServerSideConnection[] player = new ServerSideConnection[100];
    private int turnsMade;
    private int maxTurns;
    private int playerIDPosition = 0;
    private int categori = 0;

    public GameServer() throws IOException {
        System.out.println("---GAME SERVER---");
        numberOfPlayers = 0;
        turnsMade = 0;
        maxTurns = 4;

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
                        player[playerIDPosition] = new ServerSideConnection(socket, numberOfPlayers, playerIDPosition);
                        System.out.println(playerIDPosition + " acceptConnection player1");
                        player[playerIDPosition].start();
                    } else {
                        player[playerIDPosition] = new ServerSideConnection(socket, numberOfPlayers, playerIDPosition);
                        System.out.println(playerIDPosition + " acceptConnection player2");
                        player[playerIDPosition].start();
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
                        player[player1IDposition + 1].sendPoints(player1Points);
                    } else {
                        int player2IDposition = dataInputStream.readInt();
                        int player2Points = dataInputStream.readInt();
                        System.out.println("player id position " + player2IDposition + " has sent data");
                        System.out.println("player 2 " + player2IDposition + "has #" + player2Points + " points");
                        player[player2IDposition - 1].sendPoints(player2Points);
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
                        player[playerposition].sendLine(protocol.getSortedHistoryQuestions()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedHistoryQuestions()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedHistoryAlts1().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedHistoryAlts1()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedHistoryAlts1()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedHistoryAlts2().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedHistoryAlts2()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedHistoryAlts2()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedHistoryAlts3().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedHistoryAlts3()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedHistoryAlts3()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedHistoryAlts4().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedHistoryAlts4()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedHistoryAlts4()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedHistoryAnswers().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedHistoryAnswers()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedHistoryAnswers()[i]);
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
                        player[playerposition].sendLine(protocol.getSortedSportQuestions()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedSportQuestions()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedSportAlts1().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedSportAlts1()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedSportAlts1()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedSportAlts2().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedSportAlts2()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedSportAlts2()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedSportAlts3().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedSportAlts3()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedSportAlts3()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedSportAlts4().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedSportAlts4()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedSportAlts4()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedSportAnswers().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedSportAnswers()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedSportAnswers()[i]);
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
                        player[playerposition].sendLine(protocol.getSortedFilmQuestions()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedFilmQuestions()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedFilmAlts1().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedFilmAlts1()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedFilmAlts1()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedFilmAlts2().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedFilmAlts2()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedFilmAlts2()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedFilmAlts3().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedFilmAlts3()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedFilmAlts3()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedFilmAlts4().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedFilmAlts4()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedFilmAlts4()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedFilmAnswers().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedFilmAnswers()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedFilmAnswers()[i]);
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
                        player[playerposition].sendLine(protocol.getSortedGamingQuestions()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedGamingQuestions()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedGamingAlts1().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedGamingAlts1()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedGamingAlts1()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedGamingAlts2().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedGamingAlts2()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedGamingAlts2()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedGamingAlts3().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedGamingAlts3()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedGamingAlts3()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedGamingAlts4().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedGamingAlts4()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedGamingAlts4()[i]);
                    }
                    for (int i = 0; i < protocol.getSortedGamingAnswers().length; i++) {
                        player[playerposition].sendLine(protocol.getSortedGamingAnswers()[i]);
                        player[playerposition + 1].sendLine(protocol.getSortedGamingAnswers()[i]);
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
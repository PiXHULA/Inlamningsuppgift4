package SwingVersion;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;

public class Player {
    private int width;
    private int height;
    private Container contentPane;
    private JFrame frame;
    private JTextArea message;
    private JTextArea labelQuestion;
    private JButton b1;
    private JButton b2;
    private JButton b3;
    private JButton b4;
    private int playerID;
    private int otherPlayer;
    private int[] values;
    private int maxTurns;
    private int turnsMade;
    private int myPoints;
    private int enemyPoints;
    private boolean buttonsEnable;
    private String[] questions = new String[4];
    private String[] alt1 = new String[4];
    private String[] alt2 = new String[4];
    private String[] alt3 = new String[4];
    private String[] alt4 = new String[4];
    private String[] rightAnswer = new String[4];
    private int altcounter = 0;
    private int counter = 0;
    private int player1counter = 0;
    private int player2counter = 0;
    private int playerNumber;
    JButton historyButton = new JButton("Historia");
    JButton sportButton = new JButton("Sport");
    JButton filmButton = new JButton("Film");
    JButton gamingButton = new JButton("Gaming");
    private int categori;
    JFrame startFrame;
    private ClientSideConnection csc;

    public Player(int w, int h) {
        width = w;
        height = h;
        contentPane = new Container();
        message = new JTextArea();
        frame = new JFrame();
        labelQuestion = new JTextArea();
        b1 = new JButton("1");
        b2 = new JButton("2");
        b3 = new JButton("3");
        b4 = new JButton("4");
        values = new int[4];
        myPoints = 0;
        enemyPoints = 0;
    }

    public void setUpStartGUI() {
        setUpStartButtons();
        if (playerID == 1) {
            startFrame = new JFrame();
            startFrame.setSize(600, 150);
            startFrame.setTitle("Player #: " + playerID);
            startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            startFrame.setLayout(new BorderLayout());
            Container startContentPane = new Container();
            startContentPane.setLayout(new GridLayout(1, 4));
            JTextArea infoArea = new JTextArea();
            startContentPane.add(historyButton);
            startContentPane.add(sportButton);
            startContentPane.add(filmButton);
            startContentPane.add(gamingButton);
            infoArea.setText("Välj kategori!");
            startFrame.add(infoArea, BorderLayout.CENTER);
            startFrame.add(startContentPane, BorderLayout.SOUTH);
            infoArea.setWrapStyleWord(true);
            infoArea.setLineWrap(true);
            infoArea.setEditable(false);
            startFrame.setVisible(true);
            System.out.println("Choosing categori");
        } else {
            questions[0] = "empty";
            startFrame = new JFrame();
            startFrame.setSize(600, 150);
            startFrame.setTitle("Player #: " + playerID);
            startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            startFrame.setLayout(new BorderLayout());
            JTextArea infoArea = new JTextArea();
            infoArea.setText("Spelare #2 väljer kategori, vänligen vänta!");
            startFrame.add(infoArea, BorderLayout.CENTER);
            infoArea.setWrapStyleWord(true);
            infoArea.setLineWrap(true);
            infoArea.setEditable(false);
            startFrame.setVisible(true);
            System.out.println("Waiting for opponent to choose categori");
            csc.getQuestion();
            while (true) {
                if (!questions[0].equalsIgnoreCase("empty")) {
                    startFrame.dispose();
                    break;
                }
            }
            setUpGUI();
            setUpButtons();
        }
    }

    public void setUpStartButtons() {
        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource().equals(historyButton)) {
                    System.out.println("Categori selected: History");
                    categori = 1;
                    System.out.println(categori);
                    csc.sendCategori(categori, playerNumber);
                    startFrame.dispose();
                    csc.getQuestion();
                    setUpGUI();
                    setUpButtons();
                } else if (ae.getSource().equals(sportButton)) {
                    System.out.println("Categori selected: Sport");
                    categori = 2;
                    System.out.println(categori);
                    csc.sendCategori(categori, playerNumber);
                    startFrame.dispose();
                    csc.getQuestion();
                    setUpGUI();
                    setUpButtons();
                } else if (ae.getSource().equals(filmButton)) {
                    System.out.println("Categori selected: Film");
                    categori = 3;
                    System.out.println(categori);
                    csc.sendCategori(categori, playerNumber);
                    startFrame.dispose();
                    csc.getQuestion();
                    setUpGUI();
                    setUpButtons();
                } else if (ae.getSource().equals(gamingButton)) {
                    System.out.println("Categori selected: Gaming");
                    categori = 4;
                    System.out.println(categori);
                    csc.sendCategori(categori, playerNumber);
                    startFrame.dispose();
                    csc.getQuestion();
                    setUpGUI();
                    setUpButtons();
                }
            }
        };
        historyButton.addActionListener(actionListener);
        sportButton.addActionListener(actionListener);
        filmButton.addActionListener(actionListener);
        gamingButton.addActionListener(actionListener);
    }

    public void setUpGUI() {
        frame.setSize(width, height);
        frame.setTitle("Player #: " + playerID);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        contentPane.setLayout(new GridLayout(1, 5));
        frame.add(labelQuestion, BorderLayout.CENTER);
        contentPane.add(message);
        message.setText("creating a simple turn-based game in java");
        labelQuestion.setText("Here is wherer questions goes!");
        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        message.setEditable(false);
        labelQuestion.setWrapStyleWord(true);
        labelQuestion.setLineWrap(true);
        labelQuestion.setEditable(false);
        contentPane.add(b1);
        contentPane.add(b2);
        contentPane.add(b3);
        contentPane.add(b4);
        frame.add(contentPane, BorderLayout.SOUTH);
        frame.setVisible(true);

        if (playerID == 1) {
            message.setText("You are player #1. You go first!");
            labelQuestion.setText(questions[counter]);
            b1.setText(alt1[altcounter]);
            altcounter++;
            b2.setText(alt1[altcounter]);
            altcounter++;
            b3.setText(alt1[altcounter]);
            altcounter++;
            b4.setText(alt1[altcounter]);
            counter++;
            counter++;
            altcounter = 0;
            otherPlayer = 2;
            buttonsEnable = true;
        } else {
            counter++;
            player2counter++;
            message.setText("You are player #2. Wait for your turn.");
            labelQuestion.setText(questions[counter]);
            b1.setText(alt2[altcounter]);
            altcounter++;
            b2.setText(alt2[altcounter]);
            altcounter++;
            b3.setText(alt2[altcounter]);
            altcounter++;
            b4.setText(alt2[altcounter]);
            counter++;
            counter++;
            altcounter = 0;
            otherPlayer = 1;
            buttonsEnable = false;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    updateTurn();
                }
            });
            t.start();
        }
        toggleButtons();
        frame.setVisible(true);
    }

    public void connectToServer() {
        csc = new ClientSideConnection();
    }

    public void setUpButtons() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JButton b = (JButton) ae.getSource();
                String bNum = b.getText();

                message.setText("You clicked button. now wait for player #" + otherPlayer);
                turnsMade++;
                System.out.println("Turns made: " + turnsMade);

                buttonsEnable = false;
                toggleButtons();

                if (playerID == 1) {
                    labelQuestion.setText(questions[counter]);
                    b1.setText(alt3[altcounter]);
                    altcounter++;
                    b2.setText(alt3[altcounter]);
                    altcounter++;
                    b3.setText(alt3[altcounter]);
                    altcounter++;
                    b4.setText(alt3[altcounter]);
                    altcounter = 0;
                    player1counter++;
                    player1counter++;
                } else {
                    labelQuestion.setText(questions[counter]);
                    b1.setText(alt4[altcounter]);
                    altcounter++;
                    b2.setText(alt4[altcounter]);
                    altcounter++;
                    b3.setText(alt4[altcounter]);
                    altcounter++;
                    b4.setText(alt4[altcounter]);
                    altcounter = 0;
                    player2counter++;
                    player2counter++;
                }

                for (int i = 0; i <= 3; i++) {
                    if (bNum.equalsIgnoreCase(rightAnswer[i])) {
                        myPoints++;
                    }
                }

                System.out.println("My points: " + myPoints);
                csc.sendPoints(myPoints, playerNumber);
                if (playerID == 2 && turnsMade == maxTurns) {
                    checkWinner();
                } else {
                    System.out.println();
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updateTurn();
                        }
                    });
                    t.start();
                }
            }
        };

        b1.addActionListener(al);
        b2.addActionListener(al);
        b3.addActionListener(al);
        b4.addActionListener(al);
    }

    public void toggleButtons() {
        b1.setEnabled(buttonsEnable);
        b2.setEnabled(buttonsEnable);
        b3.setEnabled(buttonsEnable);
        b4.setEnabled(buttonsEnable);
    }

    public void updateTurn() {
        System.out.println("playerID: " + playerID + " is here3 " + "turnsMade " + turnsMade);
        enemyPoints = csc.receiveEnemyPoints();
        System.out.println("Your Enemy has " + enemyPoints + " points.");
        buttonsEnable = true;
        if (playerID == 1 && turnsMade == maxTurns) {
            checkWinner();
        } else {
            buttonsEnable = true;
        }
        toggleButtons();
    }

    private void checkWinner() {
        buttonsEnable = false;
        if (myPoints > enemyPoints) {
            message.setText("YOU WON!\nYOU: " + myPoints + " | Enemy: " + enemyPoints);
        } else if (myPoints < enemyPoints) {
            message.setText("YOU LOST!\nYOU: " + myPoints + " | Enemy: " + enemyPoints);
        } else {
            message.setText("YOU TIED!\nYOU: " + myPoints + " | Enemy: " + enemyPoints);
        }
    }

    //Client connection
    private class ClientSideConnection {

        private Socket socket;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;
        private int port = 51730;


        public ClientSideConnection() {
            System.out.println("---CLIENT CONNECTING---");
            try {
                socket = new Socket("localhost", port);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                playerID = dataInputStream.readInt();
                playerNumber = dataInputStream.readInt();
                maxTurns = dataInputStream.readInt() / 2;
                System.out.println("MaxTurns:" + maxTurns);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void getQuestion() {
            try {
                questions[0] = dataInputStream.readUTF();
                questions[1] = dataInputStream.readUTF();
                questions[2] = dataInputStream.readUTF();
                questions[3] = dataInputStream.readUTF();

                alt1[0] = dataInputStream.readUTF();
                alt1[1] = dataInputStream.readUTF();
                alt1[2] = dataInputStream.readUTF();
                alt1[3] = dataInputStream.readUTF();

                alt2[0] = dataInputStream.readUTF();
                alt2[1] = dataInputStream.readUTF();
                alt2[2] = dataInputStream.readUTF();
                alt2[3] = dataInputStream.readUTF();

                alt3[0] = dataInputStream.readUTF();
                alt3[1] = dataInputStream.readUTF();
                alt3[2] = dataInputStream.readUTF();
                alt3[3] = dataInputStream.readUTF();

                alt4[0] = dataInputStream.readUTF();
                alt4[1] = dataInputStream.readUTF();
                alt4[2] = dataInputStream.readUTF();
                alt4[3] = dataInputStream.readUTF();

                rightAnswer[0] = dataInputStream.readUTF();
                rightAnswer[1] = dataInputStream.readUTF();
                rightAnswer[2] = dataInputStream.readUTF();
                rightAnswer[3] = dataInputStream.readUTF();

                System.out.println("Right answer #1 is : " + rightAnswer[0]);
                System.out.println("Right answer #2 is : " + rightAnswer[1]);
                System.out.println("Right answer #3 is : " + rightAnswer[2]);
                System.out.println("Right answer #4 is : " + rightAnswer[3]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendCategori(int categori, int playerIDPosition) {
            try {
                dataOutputStream.writeInt(playerIDPosition);
                dataOutputStream.writeInt(categori);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void sendPoints(int points, int playeridPosition) {
            try {
                if (playerID == 1) {
                    dataOutputStream.writeInt(playeridPosition);
                    dataOutputStream.writeInt(points);
                    dataOutputStream.flush();
                } else if (playerID == 2) {
                    if (turnsMade == 1) { //anledningen till detta är att pga av att playerID 1 väljer kategori så hamnar det "skräp" i pipen hos playerID2 vilket gör att vi måste skicka dubbelt upp första turnen
                        dataOutputStream.writeInt(playeridPosition);
                        dataOutputStream.writeInt(points);
                        dataOutputStream.writeInt(playeridPosition);
                        dataOutputStream.writeInt(points);
                        dataOutputStream.flush();
                    } else {
                        dataOutputStream.writeInt(playeridPosition);
                        dataOutputStream.writeInt(points);
                        dataOutputStream.flush();
                    }
                }
                System.out.println("points sent " + points + " " + playeridPosition);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public int receiveEnemyPoints() {
            int EnemyPoints = -1; //-1 för att få den att fungera, fattar inte!
            try {
                EnemyPoints = dataInputStream.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return EnemyPoints;
        }

        public void closeConnection() { //ifall vi behöver lägga till "nytt spel" eller liknade... ersätt denna!
            try {
                socket.close();
                System.out.println("---Connection Closed---");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Player p = new Player(800, 200);
        p.connectToServer();
        p.setUpStartGUI();
    }
}

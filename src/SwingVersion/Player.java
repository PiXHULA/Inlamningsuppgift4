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
    private JButton historyButton = new JButton("Historia");
    private JButton sportButton = new JButton("Sport");
    private JButton filmButton = new JButton("Film");
    private JButton gamingButton = new JButton("Gaming");
    private int categori;
    private JFrame startFrame;
    private JPanel panel;
    private JTextArea scoreboard;
    private JLabel infoLabel;
    private JLabel questionLabel;


    private JButton tempButton;

    private ClientSideConnection csc;

    public Player(int w, int h) {
        width = w;
        height = h;
        contentPane = new Container();
        message = new JTextArea();
        frame = new JFrame();
        frame.setLocationRelativeTo(null);
        panel = new JPanel();
        scoreboard = new JTextArea();
        labelQuestion = new JTextArea();
        b1 = new JButton("1");
        b1.setSize(100, 150);
        b2 = new JButton("2");
        b2.setSize(100, 150);
        b3 = new JButton("3");
        b3.setSize(100, 150);
        b4 = new JButton("4");
        b4.setSize(100, 150);
        values = new int[4];
        myPoints = 0;
        enemyPoints = 0;
    }


    //välja kategori GUI
    public void setUpStartGUI() {
        setUpStartButtons();
        if (playerID == 1) {
            startFrame = new JFrame();
            startFrame.setSize(400, 600);
            startFrame.setTitle("Player #" + playerID);
            startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            startFrame.setLocationRelativeTo(null);
            startFrame.setLayout(new BorderLayout());
            Container startContentPane = new Container();
            startContentPane.setLayout(new GridLayout(2, 2));
            JTextArea infoArea = new JTextArea();
            infoArea.setMargin(new Insets(200,160,150,20));
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
            startFrame.setSize(400, 600);
            startFrame.setTitle("Player #" + playerID);
            startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            startFrame.setLocationRelativeTo(null);
            startFrame.setLayout(new BorderLayout());
            JTextArea infoArea = new JTextArea();
            infoArea.setMargin(new Insets(250,90,150,20));
            infoArea.setText("Spelare #1 väljer kategori, vänligen vänta!");
            startFrame.add(infoArea, BorderLayout.CENTER);
            infoArea.setWrapStyleWord(true);
            infoArea.setLineWrap(true);
            infoArea.setEditable(false);
            startFrame.setVisible(true);
            System.out.println("Waiting for opponent to choose categori");
            csc.getQuestion();
            while (true) { //gör så att player2 är i sin "loading" screen tills player 1 har valt kategori
                if (!questions[0].equalsIgnoreCase("empty")) {
                    startFrame.dispose();
                    break;
                }
            }
            setUpGUI();
            setUpButtons();
        }
    }

    //Välja kategori knappars funktioner
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

    //gameView GUI
    public void setUpGUI() {
        frame.setSize(width, height);
        frame.setTitle("Player #" + playerID);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        panel.setLayout(new BorderLayout());
        scoreboard.append("Scoreboard");
        scoreboard.setWrapStyleWord(true);
        scoreboard.setLineWrap(true);
        scoreboard.setEditable(false);
        panel.add(scoreboard, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.EAST);
        contentPane.setLayout(new BorderLayout());
        frame.add(labelQuestion, BorderLayout.CENTER);
        Container buttonPane = new Container();
        buttonPane.setLayout(new GridLayout(2,2));
        message.setText("creating a simple turn-based game in java");
        labelQuestion.setText("Here is wherer questions goes!");
        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        message.setEditable(false);
        labelQuestion.setWrapStyleWord(true);
        labelQuestion.setLineWrap(true);
        labelQuestion.setEditable(false);
        this.b1.setSize(100,150);
        this.b2.setSize(100,150);
        this.b3.setSize(100,150);
        this.b4.setSize(100,150);
        buttonPane.add(b1);
        buttonPane.add(b2);
        buttonPane.add(b3);
        buttonPane.add(b4);
        contentPane.add(buttonPane, BorderLayout.SOUTH);
        contentPane.add(message,BorderLayout.NORTH);
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

    //gameView knappars funktioner
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

                checkCorrectButton(b, bNum, ae);
                changeButtonColor();

                System.out.println("My points: " + myPoints);
                csc.sendPoints(myPoints, playerNumber);
                if (playerID == 2) {
                    scoreboard.append("\nRound: " + turnsMade + "\nMy points: " + myPoints +
                            "\nMy enemy Points: " + enemyPoints);
                }
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

    public void checkCorrectButton(JButton b, String answer, ActionEvent ae) {
        int temp = 0;
        for (int i = 0; i <= 3; i++) {
            if (answer.equalsIgnoreCase(rightAnswer[i]))
                temp++;
        }
        if (temp > 0) {
            b.setBackground(Color.green);
            tempButton = (JButton) ae.getSource();
            myPoints++;
        } else {
            b.setBackground(Color.red);
            tempButton = (JButton) ae.getSource();
        }
    }

    public void changeButtonColor() {
        JButton[] buttons = new JButton[]{b1, b2, b3, b4};
        for (JButton button : buttons)
            if (button.getText().equalsIgnoreCase(rightAnswer[0])
                    || button.getText().equalsIgnoreCase(rightAnswer[1])
                    || button.getText().equalsIgnoreCase(rightAnswer[2])
                    || button.getText().equalsIgnoreCase(rightAnswer[3]))
                button.setBackground(Color.GREEN);
    }

    public void toggleButtons() {
        b1.setEnabled(buttonsEnable);
        b2.setEnabled(buttonsEnable);
        b3.setEnabled(buttonsEnable);
        b4.setEnabled(buttonsEnable);
    }

    public void test() {

            b1.setBackground(null);
            b2.setBackground(null);
            b3.setBackground(null);
            b4.setBackground(null);


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
            player1counter++;// VARFÖR BEHÖVS PLAYER1COUNTER?
            player1counter++;// VARFÖR BEHÖVS PLAYER1COUNTER?
        } else if (playerID == 2 && turnsMade == 1) {
            labelQuestion.setText(questions[counter]);
            b1.setText(alt4[altcounter]);
            altcounter++;
            b2.setText(alt4[altcounter]);
            altcounter++;
            b3.setText(alt4[altcounter]);
            altcounter++;
            b4.setText(alt4[altcounter]);
            altcounter = 0;
            player2counter++; //VARFÖR BEHÖVS PLAYER1COUNTER?
            player2counter++; //VARFÖR BEHÖVS PLAYER1COUNTER?
        }
    }

    public void updateTurn() {
        enemyPoints = csc.receiveEnemyPoints();
        if (playerID == 1) {
            scoreboard.append("\nTurn: " + turnsMade + "\nMy points: " + myPoints + "\nMy enemy Points: " + enemyPoints);
        }
        System.out.println("Your Enemy has " + enemyPoints + " points.");

        try {
            Thread.sleep(2000);
            if (playerID == 1 && turnsMade == 2) {
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        test();
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
            scoreboard.append("YOU WON!\nYOU: " + myPoints + " | Enemy: " + enemyPoints);
        } else if (myPoints < enemyPoints) {
            message.setText("YOU LOST!\nYOU: " + myPoints + " | Enemy: " + enemyPoints);
            scoreboard.append("YOU LOST!\nYOU: " + myPoints + " | Enemy: " + enemyPoints);
        } else {
            message.setText("YOU TIED!\nYOU: " + myPoints + " | Enemy: " + enemyPoints);
            scoreboard.append("YOU TIED!\nYOU: " + myPoints + " | Enemy: " + enemyPoints);
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
                for (int i = 0; i < 4; i++) {
                    questions[i] = dataInputStream.readUTF();
                }
                for (int j = 0; j < 4; j++) {
                    alt1[j] = dataInputStream.readUTF();
                    alt2[j] = dataInputStream.readUTF();
                    alt3[j] = dataInputStream.readUTF();
                    alt4[j] = dataInputStream.readUTF();
                }
                for (int j = 0; j < 4; j++) {
                    rightAnswer[j] = dataInputStream.readUTF();
                }

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
                    //anledningen till detta är att pga av att när playerID 1 väljer kategori så hamnar det "skräp"
                    // i pipen hos playerID2 vilket gör att vi måste skicka dubbelt upp första turnen
                } else if (playerID == 2) {
                    if (turnsMade == 1) {
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
        Player p = new Player(400, 600);
        p.connectToServer();
        p.setUpStartGUI();
    }
}

package Testing;

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
    private int values[];
    private int maxTurns;
    private int turnsMade;
    private int myPoints;
    private int enemyPoints;
    private boolean buttonsEnable;

    ///FRÅGOR+SVAR HAR LAGTS I VARSIN ARRAY ISTÄLLET FÖR 6 STK
    //String arrays med frågor och svar, layout: fråga, svar1, svar2, svar3, svar4, rättSvar;
    private String[] question1 = new String[6];
    private String[] question2 = new String[6];
    private String[] question3 = new String[6];
    private String[] question4 = new String[6];

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

    public void setUpGUI() {
        frame.setSize(width, height);
        frame.setTitle("Player # " + playerID);
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

        if (playerID == 1) {
            message.setText("You are player #1. You go first!");
            labelQuestion.setText(question1[0]);
            b1.setText(question1[1]);
            b2.setText(question1[2]);
            b3.setText(question1[3]);
            b4.setText(question1[4]);
            otherPlayer = 2;
            buttonsEnable = true;
        } else {
            message.setText("You are player #2. Wait for your turn.");
            labelQuestion.setText(question2[0]);
            b1.setText(question2[1]);
            b2.setText(question2[2]);
            b3.setText(question2[3]);
            b4.setText(question2[4]);
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
                    labelQuestion.setText(question3[0]);
                    b1.setText(question3[1]);
                    b2.setText(question3[2]);
                    b3.setText(question3[3]);
                    b4.setText(question3[4]);
                } else {
                    labelQuestion.setText(question4[0]);
                    b1.setText(question4[1]);
                    b2.setText(question4[2]);
                    b3.setText(question4[3]);
                    b4.setText(question4[4]);
                }

                if (bNum.equalsIgnoreCase(question1[5])
                        || bNum.equalsIgnoreCase(question2[5])
                        || bNum.equalsIgnoreCase(question3[5])
                        || bNum.equalsIgnoreCase(question4[5])) {
                    myPoints++;
                }


                System.out.println("My points: " + myPoints);
                csc.sendButtonsNum(myPoints);

                if (playerID == 2 && turnsMade == maxTurns) {
                    checkWinner();
                } else {
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
        enemyPoints = csc.receiveButtonNum();
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
        csc.closeConnection();
    }

    //Client connection
    public class ClientSideConnection {

        private Socket socket;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;
        private int port = 51735;


        public ClientSideConnection() {
            System.out.println("---CLIENT CONNECTING---");
            try {
                socket = new Socket("localhost", port);
                //if(socket.isConnected() == true)
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                playerID = dataInputStream.readInt();
                System.out.println("Connected to server as player #" + playerID + ".");
                maxTurns = dataInputStream.readInt() / 2;

                question1[0] = dataInputStream.readUTF();
                question2[0] = dataInputStream.readUTF();
                question3[0] = dataInputStream.readUTF();
                question4[0] = dataInputStream.readUTF();
                for (int i = 1; i <= 4; i++) {
                    question1[i] = dataInputStream.readUTF();
                }
                for (int i = 1; i <= 4; i++) {
                    question2[i] = dataInputStream.readUTF();
                }
                for (int i = 1; i <= 4; i++) {
                    question3[i] = dataInputStream.readUTF();
                }
                for (int i = 1; i <= 4; i++) {
                    question4[i] = dataInputStream.readUTF();
                }
                question1[5] = dataInputStream.readUTF();
                question2[5] = dataInputStream.readUTF();
                question3[5] = dataInputStream.readUTF();
                question4[5] = dataInputStream.readUTF();

                System.out.println("MaxTurns:" + maxTurns);
                System.out.println("Right answer #1 is : " + question1[5]);
                System.out.println("Right answer #2 is : " + question2[5]);
                System.out.println("Right answer #3 is : " + question3[5]);
                System.out.println("Right answer #4 is : " + question4[5]);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendButtonsNum(int n) {
            try {
                dataOutputStream.writeInt(n);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public int receiveButtonNum() {
            int n = -1;
            try {
                n = dataInputStream.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return n;
        }

        public void closeConnection() {
            try {
                socket.close();
                System.out.println("---Connection Closed---");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Player p = new Player(500, 100);
        p.connectToServer();
        p.setUpGUI();
        p.setUpButtons();
    }
}

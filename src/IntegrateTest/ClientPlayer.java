package IntegrateTest;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientPlayer extends Application implements Runnable {

    private TextArea quizArea;
    private Label message;
    GridPane buttonLayout;
    private Button button;
    private Button button2;
    private Button button3;
    private Button button4;
    private BorderPane gameView;
    private int playerID;
    private int otherPlayer;
    private int values[];
    private int maxTurns;
    private int turnsMade;
    private int myPoints;
    private int enemyPoints;
    private boolean buttonsEnable;

    private Thread thread;

    ///FRÅGOR+SVAR HAR LAGTS I VARSIN ARRAY ISTÄLLET FÖR 6 STK
    //String arrays med frågor och svar, layout: fråga, svar1, svar2, svar3, svar4, rättSvar;
    private String[] question1 = new String[6];
    private String[] question2 = new String[6];
    private String[] question3 = new String[6];
    private String[] question4 = new String[6];

    private ClientSideConnection csc;
    public ClientPlayer(){}
    public ClientPlayer(int w, int h) throws IOException {

        message = new Label();
        quizArea = createTextArea("Quiz: ", "questionArea");
        button = new Button("1");
        button2 = new Button("2");
        button3 = new Button("3");
        button4 = new Button("4");
        int[] values = new int[4];
        myPoints = 0;
        enemyPoints = 0;
    }


    @Override
    public void start(Stage stage) throws Exception {
        VBox groundBox = new VBox();
        groundBox.setMinSize(400, 600);
        GridPane bottomPane = new GridPane();
        bottomPane.add(groundBox, 0, 0);
        TextArea quizArea = new TextArea();
        Label message = new Label();
        message.setWrapText(true);

        button = createButton();
        button2 = createButton();
        button3 = createButton();
        button4 = createButton();
        buttonLayout = createButtonLayout();

        gameView = createGameviewPane(quizArea, message, buttonLayout);
        groundBox.getChildren().add(gameView);

        Scene scene = new Scene(bottomPane);
        scene.getStylesheets().add("GameviewStyle.css");

        if (playerID == 1) {
            message.setText("You are player #1.\nYou go first!");
            quizArea.setText(question1[0]);
            button.setText(question1[1]);
            button2.setText(question1[2]);
            button3.setText(question1[3]);
            button4.setText(question1[4]);
            otherPlayer = 2;
            buttonsEnable = true;
        } else {
            message.setText("You are player #2.\nWait for your turn.");
            quizArea.setText(question2[0]);
            button.setText(question2[1]);
            button2.setText(question2[2]);
            button3.setText(question2[3]);
            button4.setText(question2[4]);
            otherPlayer = 1;
            buttonsEnable = false;
        }
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("ClientPlayer #" + playerID);
        stage.show();
    }
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    //Needed for gameView!
    private BorderPane createGameviewPane(TextArea quizArea, Label playerStatus, GridPane buttonLayout) {
        BorderPane Gameview = new BorderPane();
        Gameview.setPrefSize(400, 600);
        Gameview.setPadding(new Insets(10, 10, 10, 10));
        Gameview.setTop(playerStatus);
        Gameview.setBottom(buttonLayout);
        Gameview.setCenter(quizArea);

        Insets insets = new Insets(10);
        BorderPane.setMargin(playerStatus, insets);
        BorderPane.setMargin(quizArea, insets);
        BorderPane.setMargin(buttonLayout, insets);
        return Gameview;
    }

    //Needed for gameView!
    private HBox displayNames(String player1, String player2) {
        Label first = new Label(player1);
        Label second = new Label(player2);
        HBox playerStatus = new HBox(10, first, second);
        playerStatus.setSpacing(50);
        playerStatus.setAlignment(Pos.CENTER);
        playerStatus.setPrefHeight(50);
        return playerStatus;
    }

    //Needed for gameView!
    private Button createButton(String answer, TextArea quizArea) {
        Button button = new Button(answer);
        button.setDisable(buttonsEnable);
        button.setOnAction(getActionEventEventHandler());
        return button;
    }
    private Button createButton() {
        Button button = new Button();
        button.setDisable(buttonsEnable);
        button.setOnAction(getActionEventEventHandler());
        return button;
    }

    //Needed for gameView!
    private TextArea createTextArea(String label, String cssStyle) throws IOException {
        TextArea textArea = new TextArea();
        textArea.setText("");
        textArea.setId(cssStyle);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setFocusTraversable(false);
        return textArea;
    }

    //Needed for gameView!
    private GridPane createButtonLayout() {
        GridPane buttonLayout = new GridPane();
        buttonLayout.setHgap(10.0);
        buttonLayout.setVgap(10.0);
        buttonLayout.add(button, 0, 0);
        buttonLayout.add(button2, 1, 0);
        buttonLayout.add(button3, 0, 1);
        buttonLayout.add(button4, 1, 1);
        buttonLayout.setId("buttonLayout");
        return buttonLayout;
    }

    //Needed for gameView!
    private EventHandler<ActionEvent> getActionEventEventHandler() {
        return actionEvent -> {

            buttonsEnable = true;
            this.button.setDisable(buttonsEnable);
            this.button2.setDisable(buttonsEnable);
            this.button3.setDisable(buttonsEnable);
            this.button4.setDisable(buttonsEnable);

            Button[] buttonlist = {button, button2, button3, button4};
            for (Button button : buttonlist) {
                if (button.getText().equals("getAnswerText()"))
                    button.setId("correctAnswer");
                else
                    button.setId("wrongAnswer");
            }

        };
    }

    private void connectToServer() throws IOException {
        csc = new ClientPlayer.ClientSideConnection();
    }

    private void setUpButtons() {
        EventHandler<ActionEvent> al = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Button b = (Button) actionEvent.getSource();
                String bNum = b.getText();

                message.setText("You clicked button. now wait for player #" + otherPlayer);
                turnsMade++;
                System.out.println("Turns made: " + turnsMade);

                buttonsEnable = false;
                toggleButtons();

                if (playerID == 1) {
                    quizArea.setText(question3[0]);
                    button.setText(question3[1]);
                    button2.setText(question3[2]);
                    button3.setText(question3[3]);
                    button4.setText(question3[4]);
                } else {
                    quizArea.setText(question4[0]);
                    button.setText(question4[1]);
                    button2.setText(question4[2]);
                    button3.setText(question4[3]);
                    button4.setText(question4[4]);
                }

                if (bNum.equalsIgnoreCase(question1[5])
                        || bNum.equalsIgnoreCase(question2[5])
                        || bNum.equalsIgnoreCase(question3[5])
                        || bNum.equalsIgnoreCase(question4[5])) {
                    myPoints++;
                }


                System.out.println("My points: " + myPoints);
                csc.sendButtonsNum(myPoints);
            }
        };

        button.setOnAction(al);
        button2.setOnAction(al);
        button3.setOnAction(al);
        button4.setOnAction(al);
    }

    private void toggleButtons() {
        button.setDisable(buttonsEnable);
        button2.setDisable(buttonsEnable);
        button3.setDisable(buttonsEnable);
        button4.setDisable(buttonsEnable);
    }

    private void updateTurn() {
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

    @Override
    public void run() {
        try {
            connectToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setUpButtons();
        updateTurn();
    }

    //Client connection
    class ClientSideConnection {
        private int port = 51735;
        private String localHost = "127.0.0.1";
        private Socket socket = new Socket(localHost, port);
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;


        private ClientSideConnection() throws IOException {
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

        private void sendButtonsNum(int n) {
            try {
                dataOutputStream.writeInt(n);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private int receiveButtonNum() {
            int n = -1;
            try {
                n = dataInputStream.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return n;
        }

        private void closeConnection() {
            try {
                socket.close();
                System.out.println("---Connection Closed---");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
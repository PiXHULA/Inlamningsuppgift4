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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientPlayer extends Application {

    int width;
    int height;
    Stage window;
    GridPane bottomPane;
    Scene gameScene;
    TextArea quizArea;
    TextArea messageArea;
    Button button;
    Button button2;
    Button button3;
    Button button4;
    GridPane buttonLayout;
    BorderPane gameView;
    HBox displayPlayers;

    private int playerID;
    private int otherPlayer;

    private int values[];
    private int maxTurns;
    private int turnsMade;
    private int myPoints;
    private int enemyPoints;
    private boolean buttonsEnable;

    private int port = 51735;
    private String localHost = "127.0.0.1";
    private Socket socket = new Socket(InetAddress.getLocalHost(),port);

    ///FRÅGOR+SVAR HAR LAGTS I VARSIN ARRAY ISTÄLLET FÖR 6 STK
    //String arrays med frågor och svar, layout: fråga, svar1, svar2, svar3, svar4, rättSvar;
    private String[] question1 = new String[6];
    private String[] question2 = new String[6];
    private String[] question3 = new String[6];
    private String[] question4 = new String[6];

    private ClientSideConnection csc;

    public ClientPlayer(int w, int h) throws IOException {
        width = w;
        height = h;
        window = new Stage();
        messageArea = createTextArea("Message: ", "questionArea");
        quizArea = createTextArea("Quiz: ", "questionArea");
        button = new Button("1");
        button2 = new Button("2");
        button3 = new Button("3");
        button4 = new Button("4");
        values = new int[4];
        myPoints = 0;
        enemyPoints = 0;
    }


    @Override
    public void start(Stage stage) throws Exception {
        VBox groundBox = new VBox();
        groundBox.setMinSize(400,600);
        GridPane bottomPane = new GridPane();
        bottomPane.add(groundBox,0,0);
        quizArea = createTextArea(null, "gameviewPane");

        button = createButton("Fire", quizArea);
        button2 = createButton("Counter Strike", quizArea);
        button3 = createButton("Cerberus Starfish", quizArea);
        button4 = createButton("Computer Science", quizArea);

        displayPlayers = displayNames("Player #" + playerID, "Player #" + otherPlayer);
        buttonLayout = createButtonLayout();
        gameView = createGameviewPane(quizArea, displayPlayers, buttonLayout);
        groundBox.getChildren().add(gameView);
        Scene scene = new Scene(bottomPane);
        scene.getStylesheets().add("GameviewStyle.css");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("ClientPlayer #" +playerID);
        this.thread.start();
    }

    //Needed for loginView!
    public String getPlayerName() {
        return playerName;
    }

    //Needed for loginView!
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    //Needed for gameView!
    private BorderPane createGameviewPane(TextArea quizArea, HBox playerStatus, GridPane buttonLayout) {
        BorderPane Gameview = new BorderPane();
        Gameview.setPrefSize(400,600);
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
        HBox playerStatus = new HBox(10,first,second);
        playerStatus.setSpacing(50);
        playerStatus.setAlignment(Pos.CENTER);
        playerStatus.setPrefHeight(50);
        return playerStatus;
    }

    //Needed for gameView!
    private Button createButton(String answer, TextArea quizArea) {
        Button button = new Button (answer);
        button.setDisable(buttonsEnable);
        button.setOnAction(getActionEventEventHandler(quizArea, button.getText()));
        return button;
    }

    //Needed for gameView!
    private TextArea createTextArea(String label, String cssStyle) throws IOException {
        TextArea textArea = new TextArea();
        textArea.setText(getQuestionText());
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
        buttonLayout.add(button,0,0);
        buttonLayout.add(button2,1,0);
        buttonLayout.add(button3,0,1);
        buttonLayout.add(button4,1,1);
        buttonLayout.setId("buttonLayout");
        return buttonLayout;
    }

    //Needed for gameView!
    private EventHandler<ActionEvent> getActionEventEventHandler(TextArea quizArea, String s) {
        return actionEvent -> {

            int i = 0;
            buttonsEnable = true;
            this.button.setDisable(buttonsEnable);
            this.button2.setDisable(buttonsEnable);
            this.button3.setDisable(buttonsEnable);
            this.button4.setDisable(buttonsEnable);

            Button[] buttonlist = {button, button2, button3, button4};
            for (Button button : buttonlist){
                if(button.getText().equals(getAnswerText()))
                    button.setId("correctAnswer");
                else
                    button.setId("wrongAnswer");
            }

        };
    }

    //Client connection
    private class ClientSideConnection {

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
    public static void main(String[] args) throws IOException {
        ClientPlayer p = new ClientPlayer(400,600);
        //p.connectToServer();
        //p.setUpGUI();
        //p.setUpButtons();
        launch(args);
    }
}
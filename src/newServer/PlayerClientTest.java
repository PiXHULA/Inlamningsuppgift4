package newServer;

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

import java.io.*;
import java.net.Socket;



public class PlayerClientTest extends Application implements Runnable {
    Button button;
    Button button2;
    Button button3;
    Button button4;
    GridPane bottomPane;
    GridPane buttonLayout;
    BorderPane gameView;
    TextArea quizArea;
    TextArea onlineStatus;
    TextArea highscoreArea;
    HBox displayPlayers;
    Stage stage;

    Thread thread = new Thread(this);
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private PlayerClientTest.ClientSideConnection csc;

    String hostName = "127.0.0.1";
    private int port = 51734;
    private Socket socket = new Socket(hostName, port);
    private int playerID;
    private Scene gameviewScene;

    private String questionText;
    private String altText1_1;
    private String altText1_2;
    private String altText1_3;
    private String altText1_4;
    private String answerText;

    String playerName = "";
    Stage playerNameWindow;


    private int otherPlayer; //Control int so u can set "rules" later
    private int myPoints = 0; // so you can store turn points for yourself
    private int myTotalPoints; // so you can store your totalpoints
    private int enemyPoints; // so you can store points for enenmy
    private int enemyTotalPoints; // so you can store your enemy totalpoints
    private int turn; //so you can see / store points at diffrent "turns" of the game
    private boolean buttonsEnable = false; //so you can disable the buttons if its not your turn (if we want)

    public PlayerClientTest(Socket socketToClient) throws IOException {
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAltText1_1() {
        return altText1_1;
    }

    public void setAltText1_1(String altText1_1) {
        this.altText1_1 = altText1_1;
    }

    public String getAltText1_2() {
        return altText1_2;
    }

    public void setAltText1_2(String altText1_2) {
        this.altText1_2 = altText1_2;
    }

    public String getAltText1_3() {
        return altText1_3;
    }

    public void setAltText1_3(String altText1_3) { this.altText1_3 = altText1_3; }

    public String getAltText1_4() {
        return altText1_4;
    }

    public void setAltText1_4(String altText1_4) {
        this.altText1_4 = altText1_4;
    }

    public String getAnswerText() { return answerText; }

    public void setAnswerText(String answerText) { this.answerText = answerText; }

    @Override
    public void start(Stage stage) throws IOException {
        /*
        GridPane loginView = new GridPane();
        loginView.setMinSize(400,600);
        TextField loginName = new TextField("Enter name");
        Button submit = new Button ("Submit");
        loginName.setAlignment(Pos.CENTER);
        loginView.add(loginName, 0,0);
        loginView.add(submit, 0,1);
        Scene scene = new Scene(loginView);
        scene.getStylesheets().add("GameviewStyle.css");
        submit.setOnAction(actionEvent -> {
            //PlayerClientTest.setPlayerName(loginName.getText());
            stage.setScene(gameviewScene);
            System.out.println("Clicked Submit");
            //stage.setTitle("Player #1 - " + player.getPlayerName());
        });

         */

        VBox groundVBox = new VBox();
        groundVBox.setMinSize(400,600);
        GridPane bottomPane = new GridPane();
        bottomPane.add(groundVBox,0,0);
        quizArea = createTextArea(null, "gameviewPane");

        button = createButton(getAltText1_2(), quizArea);
        button2 = createButton(getAltText1_1(), quizArea);
        button3 = createButton(getAltText1_3(), quizArea);
        button4 = createButton(getAltText1_4(), quizArea);

        displayPlayers = displayNames("Player #" + playerID, "Player #" + otherPlayer);
        buttonLayout = createButtonLayout();
        gameView = createGameviewPane(quizArea, displayPlayers, buttonLayout);
        groundVBox.getChildren().add(gameView);
        Scene gameviewScene = new Scene(bottomPane);
        gameviewScene.getStylesheets().add("GameviewStyle.css");
//        stage.setScene(scene);
        stage.setScene(gameviewScene);
        stage.setTitle("QUIZKAMPEN 2.0");
        stage.show();
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
    private void loginView() throws IOException {
        //connectToServer();
        Stage stage = new Stage();
        //PlayerClientTest player = new PlayerClientTest();
        VBox groundVBox = new VBox();
        groundVBox.setMinSize(400,600);
        GridPane bottomPane = new GridPane();
        bottomPane.add(groundVBox,0,0);
        quizArea = createTextArea(null, "gameviewPane");

        button = createButton(getAltText1_2(), quizArea);
        button2 = createButton(getAltText1_1(), quizArea);
        button3 = createButton(getAltText1_3(), quizArea);
        button4 = createButton(getAltText1_4(), quizArea);

        //displayPlayers = displayNames(playerName, "Player #" + otherPlayer);
        displayPlayers = displayNames("Player #" + playerID, "Player #" + otherPlayer);
        buttonLayout = createButtonLayout();
        gameView = createGameviewPane(quizArea, displayPlayers, buttonLayout);
        groundVBox.getChildren().add(gameView);
        GridPane loginView = new GridPane();
        loginView.setMinSize(400,600);
        TextField loginName = new TextField("Enter name");
        Button submit = new Button ("Submit");
        submit.setOnAction(actionEvent -> {
            //player.setPlayerName(loginName.getText());
            System.out.println("Clicked submit");
            stage.setScene(gameviewScene);
        });
        loginName.setAlignment(Pos.CENTER);
        loginView.add(loginName, 0,0);
        loginView.add(submit, 0,1);
        Scene scene = new Scene(loginView);
        scene.getStylesheets().add("GameviewStyle.css");
        stage.setScene(scene);
        stage.setTitle("QUIZKAMPEN 2.0");
        stage.show();
        Scene gameviewScene = new Scene(bottomPane);
        gameviewScene.getStylesheets().add("GameviewStyle.css");
        this.thread.start();
    }

    private void gameView() throws IOException {
        VBox groundVBox = new VBox();
        groundVBox.setMinSize(400,600);
        GridPane bottomPane = new GridPane();
        bottomPane.add(groundVBox,0,0);
        quizArea = createTextArea(null, "gameviewPane");

        button = createButton(getAltText1_2(), quizArea);
        button2 = createButton(getAltText1_1(), quizArea);
        button3 = createButton(getAltText1_3(), quizArea);
        button4 = createButton(getAltText1_4(), quizArea);

        //displayPlayers = displayNames(playerName, "Player #" + otherPlayer);
        displayPlayers = displayNames("Player #" + playerID, "Player #" + otherPlayer);
        buttonLayout = createButtonLayout();
        gameView = createGameviewPane(quizArea, displayPlayers, buttonLayout);
        groundVBox.getChildren().add(gameView);
        Scene gameviewScene = new Scene(bottomPane);
        gameviewScene.getStylesheets().add("GameviewStyle.css");

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(gameviewScene);
        stage.show();
        stage.setTitle("Player #" +playerID);

    }

    //Needed for gameView?
    private GridPane createBottomPane(VBox vert1, VBox vert2, VBox vert3) {
        GridPane collectedGui = new GridPane();
        collectedGui.setMinSize(800,600);
        collectedGui.add(vert1,0,0);
        collectedGui.add(vert2,1,0);
        collectedGui.add(vert3,2,0);
        return collectedGui;
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
        button.setOnAction(getActionEventEventHandler(button, quizArea, button.getText()));
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
    private EventHandler<ActionEvent> getActionEventEventHandler(Button button, TextArea quizArea, String s) {
        return actionEvent -> {
            int i = 0;
            buttonsEnable = true;
            this.button.setDisable(buttonsEnable);
            this.button2.setDisable(buttonsEnable);
            this.button3.setDisable(buttonsEnable);
            this.button4.setDisable(buttonsEnable);
            Button[] buttonlist = {button, button2, button3, button4};
            if (getAnswerText().equals(button.getText())) {
                button.setId("correctAnswer");
                quizArea.appendText("Pushed " + s + "\n");
            } else
                button.setId("wrongAnswer");

            if (button.getId().equalsIgnoreCase("correctAnswer")){
                myPoints++;
             //   csc.sendMyPoints(myPoints);
            }
            if(!button.getId().equalsIgnoreCase("correctAnswer")){
                for (int j = 1; j <4 ; j++) {
                    buttonlist[j].setId("wrongAnswer");
                }
            //    csc.sendMyPoints(myPoints);
            }
        };
    }


    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));) {
            String message;
            while ((message = in.readLine()) != null) {
                quizArea.appendText(message + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //the "logic" for client connection to server
    private class ClientSideConnection {

        public void sendMyPoints(int myPoints){
            try{
                dataOutputStream.writeInt(myPoints);
                dataOutputStream.flush();
            }catch (IOException ex){
                System.out.println("IOException from sendMyPoints CSC");
            }
        }

        public ClientSideConnection (){
            System.out.println("--CLIENT CONNECTING--");
            try{
                socket = new Socket("localhost", port);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                playerID = dataInputStream.readInt();

                String questionText = dataInputStream.readUTF();
                setQuestionText(questionText);

                String altText1_1 = dataInputStream.readUTF();
                setAltText1_1(altText1_1);

                String altText1_2 = dataInputStream.readUTF();
                setAltText1_2(altText1_2);

                String altText1_3 = dataInputStream.readUTF();
                setAltText1_3(altText1_3);

                String altText1_4 = dataInputStream.readUTF();
                setAltText1_4(altText1_4);

                String answerText = dataInputStream.readUTF();
                setAnswerText(answerText);

                System.out.println("Connected to server as player #" + playerID + ".");
                otherPlayer();
            }catch (IOException ex){
                System.out.println("IOException from CSC constructor");
                ex.printStackTrace();
            }
        }
        //Needed for gameView!
        private void otherPlayer(){
            if (playerID == 1){
                otherPlayer = 2;
            } else{
                otherPlayer = 1;
            }
        }

        //method to call and run CSC
        public void connectToServer(){
            csc = new PlayerClientTest.ClientSideConnection();
        }
    }


    public static void main(String[] args) throws IOException {
        //PlayerClientTest p = new PlayerClientTest();
        launch(args);
    }
}




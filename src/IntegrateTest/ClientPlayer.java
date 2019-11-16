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

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientPlayer extends Application implements Runnable{

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

    private int port = 49494;
    private String localHost = "127.0.0.1";
    private int playerID;
    private Socket socket = new Socket(InetAddress.getLocalHost(),port);

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

    public ClientPlayer() throws IOException {
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
    public void start(Stage stage) throws Exception {
        VBox groundBox = new VBox();
        groundBox.setMinSize(400,600);
        GridPane bottomPane = new GridPane();
        bottomPane.add(groundBox,0,0);
        quizArea = createTextArea(null, "gameviewPane");
        onlineStatus = createTextArea("Online: ","onlineStatus");
        highscoreArea = createTextArea("Highscore: ", "highscoreArea");

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
        stage.setTitle("Player #" +playerID);
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
            /*
            int i = 0;
            buttonsEnable = true;
            this.button.setDisable(buttonsEnable);
            this.button2.setDisable(buttonsEnable);
            this.button3.setDisable(buttonsEnable);
            this.button4.setDisable(buttonsEnable);
             */
            String inputFromUser = this.button.getText();
            PrintWriter out = null;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            out.println(inputFromUser);

            Button[] buttonlist = {button, button2, button3, button4};
            for (Button button : buttonlist){
                if(button.getText().equals("Counter Strike"))
                    button.setId("correctAnswer");
                else
                    button.setId("wrongAnswer");
            }

        };
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(this.socket.getInputStream()));
        ) {
            String message;
            int count = 0;
            while ((message = in.readLine()) != null) {
               // if (count == 0)
                    quizArea.appendText(message + "\n");
                //else
                    //quizArea.setText(message + "\n");
                //count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
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
        csc = new ClientSideConnection();
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
    }


     */
    public static void main(String[] args) throws IOException {
        ClientPlayer p = new ClientPlayer();
        launch(args);
    }
}
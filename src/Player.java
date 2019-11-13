import java.net.*;
import java.io.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Player extends Application{

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
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ClientSideConnection csc;
    private int port = 51734;
    private int playerID;
    private int otherPlayer; //Control int so u can set "rules" later
    private int myPoints; // so you can store turn points for yourself
    private int myTotalPoints; // so you can store your totalpoints
    private int enemyPoints; // so you can store points for enenmy
    private int enemyTotalPoints; // so you can store your enemy totalpoints
    private int turn; //so you can see / store points at diffrent "turns" of the game
    private boolean buttonsEnable = false; //so you can disable the buttons if its not your turn (if we want)



    /**
     * somthing like this plus you set "buttonesEnable to true or false depending if its your turn or not
     * public void togglebuttons(){
     *     button1.setEnable(buttonsEnable);
     *     button2.setEnable(buttonsEnable);
     *     button3.setEnable(buttonsEnable);
     *     button4.setEnable(buttonsEnable);
     * }
     */


    @Override
    public void start(Stage stage) throws Exception {
        connectToServer();
        VBox vert2 = new VBox();
        vert2.setMinSize(400,600);
        GridPane bottomPane = new GridPane();
        bottomPane.add(vert2,0,0);
        quizArea = createTextArea(null, "gameviewPane");
        onlineStatus = createTextArea("Online: ","onlineStatus");
        highscoreArea = createTextArea("Highscore: ", "highscoreArea");

        button = createButton("Answer1", quizArea);
        button2 = createButton("Answer2", quizArea);
        button3 = createButton("Answer3", quizArea);
        button4 = createButton("Answer4", quizArea);

        displayPlayers = displayNames("Player #" + playerID, "Player #" + otherPlayer);
        buttonLayout = createButtonLayout();
        gameView = createGameviewPane(quizArea, displayPlayers, buttonLayout);
        vert2.getChildren().add(gameView);
        Scene scene = new Scene(bottomPane);
        scene.getStylesheets().add("GameviewStyle.css");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Player #" +playerID);
    }
    private GridPane createBottomPane(VBox vert1, VBox vert2, VBox vert3) {
        GridPane collectedGui = new GridPane();
        collectedGui.setMinSize(800,600);
        collectedGui.add(vert1,0,0);
        collectedGui.add(vert2,1,0);
        collectedGui.add(vert3,2,0);
        return collectedGui;
    }

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

    private HBox displayNames(String player1, String player2) {
        Label first = new Label(player1);
        Label second = new Label(player2);
        HBox playerStatus = new HBox(10,first,second);
        playerStatus.setSpacing(50);
        playerStatus.setAlignment(Pos.CENTER);
        playerStatus.setPrefHeight(50);
        return playerStatus;
    }

    private Button createButton(String answer, TextArea quizArea) {
        Button button = new Button (answer);
        button.setDisable(buttonsEnable);
        button.setOnAction(getActionEventEventHandler(button, quizArea, button.getText()));
        return button;
    }

    private TextArea createTextArea(String label, String cssStyle) {
        TextArea textArea = new TextArea();
        textArea.setText(label);
        textArea.setId(cssStyle);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setFocusTraversable(false);
        return textArea;
    }

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

    private EventHandler<ActionEvent> getActionEventEventHandler(Button button, TextArea quizArea, String s) {
        return actionEvent -> {
            buttonsEnable = true;
            this.button.setDisable(buttonsEnable);
            this.button2.setDisable(buttonsEnable);
            this.button3.setDisable(buttonsEnable);
            this.button4.setDisable(buttonsEnable);
            if("Answer1".equals(this.button.getText())){
                this.button.setId("correctAnswer");
                quizArea.appendText("Pushed " + s + "\n");
            }else
                this.button.setId("wrongAnswer");
            if("Answer1".equals(this.button2.getText())){
                this.button2.setId("correctAnswer");
                quizArea.appendText("Pushed " + s + "\n");
            }else
                this.button2.setId("wrongAnswer");
            if("Answer1".equals(this.button3.getText())){
                this.button3.setId("correctAnswer");
                quizArea.appendText("Pushed " + s + "\n");
            }else
                this.button3.setId("wrongAnswer");
            if("Answer1".equals(this.button4.getText())){
                this.button4.setId("correctAnswer");
                quizArea.appendText("Pushed " + s + "\n");
            }else
                this.button4.setId("wrongAnswer");


        };
    }

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
        private Socket socket;

        public ClientSideConnection (){
            System.out.println("--CLIENT CONNECTING--");
            try{
                socket = new Socket("localhost", port);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                playerID = dataInputStream.readInt();
                System.out.println("Connected to server as player #" + playerID + ".");
                otherPlayer();
            }catch (IOException ex){
                System.out.println("IOException from CSC constructor");
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Player p = new Player();
        launch(args);
    }
}
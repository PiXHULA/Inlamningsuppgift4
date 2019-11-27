package JavaFXVersion;

import java.awt.*;
import java.net.*;
import java.io.*;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;

public class Player extends Application {

    int width;
    int height;
    Stage window;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    TextArea scoreboard;
    Label message = new Label();
    Label question = new Label();
    GridPane bottomPane;
    GridPane buttonLayout;
    BorderPane gameView;
    HBox quizArea = new HBox();
    TextArea onlineStatus;
    TextArea highscoreArea;
    HBox displayPlayers;
    Stage stage;

    private int playerID = 1;
    private int otherPlayer;
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
    private int playerNumber;
    private Button historyButton = new Button("Historia");
    private Button sportButton = new Button("Sport");
    private Button filmButton = new Button("Film");
    private Button gamingButton = new Button("Gaming");
    private int category;
    //Måste skapa en kategoriScene som behöver disable när spelaren har valt kategori
    private JFrame startFrame;
    private JPanel panel;
    private JButton tempButton;

    private ClientSideConnection csc;

    public Player() {
        connectToServer();
        csc.sendCategori(1, playerID);
        csc.getQuestion();
    }
    public void setUpGameView(Stage stage){
        stage.setTitle("Player #" + playerID);
        stage.setResizable(false);

        VBox groundBox = new VBox();
        groundBox.setMinSize(width, height);
        GridPane bottomPane = new GridPane();
        bottomPane.add(groundBox, 0, 0);

        message.setText("You are player #1. You go first!");
        question.setText(questions[counter]);


        button1 = new Button();
        button1.setText(alt1[altcounter]);
        altcounter++;
        button2 = new Button();
        button2.setText(alt1[altcounter]);
        altcounter++;
        button3 = new Button();
        button3.setText(alt1[altcounter]);
        altcounter++;
        button4 = new Button();
        button4.setText(alt1[altcounter]);
        altcounter = 0;

        counter++;
        counter++;
        otherPlayer = 2;
        buttonsEnable = false;
        displayPlayers = displayNames("Player #" + playerID + "\nScore: " + myPoints,
                "Player #" + otherPlayer + "\nScore: " + enemyPoints);
        buttonLayout = createButtonLayout();
        quizArea.setBackground(new Background(new BackgroundFill(Color.rgb(240, 240, 240), CornerRadii.EMPTY, Insets.EMPTY)));
        quizArea.setPrefSize(300, 200);
        quizArea.getChildren().add(question);
        gameView = createGameviewPane(quizArea, displayPlayers, buttonLayout);
        groundBox.getChildren().add(gameView);
        Scene scene = new Scene(bottomPane);
        scene.getStylesheets().add("JavaFXVersion/GameviewStyle.css");
        stage.setScene(scene);
        stage.show();

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
                checkCorrectButton(b, bNum);
                changeButtonColor();

                System.out.println("My points: " + myPoints);
                csc.sendPoints(myPoints, playerNumber);
                if (playerID == 2) {
                    scoreboard.appendText("\n\nRound: " + turnsMade +
                            "\nPlayer #1: " + myPoints + "\nPlayer #2: " + enemyPoints);
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
        button1.setOnAction(al);
        button2.setOnAction(al);
        button3.setOnAction(al);
        button4.setOnAction(al);
    }
    @Override
    public void start(Stage stage) throws Exception {
        //Player player = new Player();
        setUpGameView(stage);

    }
    private BorderPane createGameviewPane(HBox quizArea, HBox displayPlayers, GridPane buttonLayout) {
        BorderPane Gameview = new BorderPane();

        Gameview.setPrefSize(400, 600);
        Gameview.setPadding(new Insets(10, 10, 10, 10));
        Gameview.setTop(displayPlayers);
        Gameview.setBottom(buttonLayout);
        Gameview.setCenter(quizArea);

        Insets insets = new Insets(10);
        Gameview.setMargin(displayPlayers, insets);
        Gameview.setMargin(quizArea, insets);
        Gameview.setMargin(buttonLayout, insets);
        return Gameview;
    }
    private HBox displayNames(String player1, String player2) {
        Label first = new Label(player1);
        Label second = new Label(player2);
        HBox playerStatus = new HBox(10, first, second);
        playerStatus.setSpacing(50);
        playerStatus.setAlignment(Pos.CENTER);
        playerStatus.setPrefHeight(50);
        return playerStatus;
    }
    private TextArea createTextArea(String label, String cssStyle) throws IOException {
        TextArea textArea = new TextArea();
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
        buttonLayout.add(button1, 0, 0);
        buttonLayout.add(button2, 1, 0);
        buttonLayout.add(button3, 0, 1);
        buttonLayout.add(button4, 1, 1);
        buttonLayout.setId("buttonLayout");
        return buttonLayout;
    }

    public void connectToServer() {
        csc = new ClientSideConnection();
    }

    public void setUpChooseCategoryButtons() {
        EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
            Button button;

            @Override
            public void handle(ActionEvent actionEvent) {

                Button button = (Button) actionEvent.getSource();
                String bNum = button.getText();

                if (bNum.equals(historyButton)) {
                    System.out.println("Categori selected: History");
                    category = 1;
                } else if (bNum.equals(sportButton)) {
                    System.out.println("Categori selected: Sport");
                    category = 2;
                } else if (bNum.equals(filmButton)) {
                    System.out.println("Categori selected: Film");
                    category = 3;
                } else if (bNum.equals(gamingButton)) {
                    System.out.println("Categori selected: Gaming");
                    category = 4;
                }
                csc.sendCategori(category, playerNumber);
                csc.getQuestion();
            }
        };
    }
    public void setUpGameViewButtons() {
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
                checkCorrectButton(b, bNum);
                changeButtonColor();

                System.out.println("My points: " + myPoints);
                csc.sendPoints(myPoints, playerNumber);
                if (playerID == 2) {
                    scoreboard.appendText("\n\nRound: " + turnsMade +
                            "\nPlayer #1: " + myPoints + "\nPlayer #2: " + enemyPoints);
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
        button1.setOnAction(al);
        button2.setOnAction(al);
        button3.setOnAction(al);
        button4.setOnAction(al);
    }

    public void checkCorrectButton(Button button, String answer) {
        int temp = 0;
        for (int i = 0; i <= 3; i++) {
            if (answer.equalsIgnoreCase(rightAnswer[i]))
                temp++;
        }
        if (temp > 0) {
            button.setId("correctAnswer");
            myPoints++;
        } else {
            button.setId("wrongAnswer");
        }
    }
    public void changeButtonColor() {
        Button[] buttons = new Button[]{button1, button2, button3, button4};
        for (Button button : buttons)
            if (button.getText().equalsIgnoreCase(rightAnswer[0])
                    || button.getText().equalsIgnoreCase(rightAnswer[1])
                    || button.getText().equalsIgnoreCase(rightAnswer[2])
                    || button.getText().equalsIgnoreCase(rightAnswer[3]))
                button.setId("correctAnswer");
    }
    public void toggleButtons() {
        button1.setDisable(buttonsEnable);
        button2.setDisable(buttonsEnable);
        button3.setDisable(buttonsEnable);
        button4.setDisable(buttonsEnable);
    }

    public void setRoundTwo() {
        resetButtonColors();
        if (playerID == 1) {
            question.setText(questions[counter]);
            button1.setText(alt3[altcounter]);
            altcounter++;
            button2.setText(alt3[altcounter]);
            altcounter++;
            button3.setText(alt3[altcounter]);
            altcounter++;
            button4.setText(alt3[altcounter]);
            altcounter = 0;
        } else if (playerID == 2 && turnsMade == 1) {
            question.setText(questions[counter]);
            button1.setText(alt4[altcounter]);
            altcounter++;
            button2.setText(alt4[altcounter]);
            altcounter++;
            button3.setText(alt4[altcounter]);
            altcounter++;
            button4.setText(alt4[altcounter]);
            altcounter = 0;
        }
    }
    public void resetButtonColors() {
        if (!(playerID == 1 && turnsMade == 2)) {
            button1.setBackground(null);
            button2.setBackground(null);
            button3.setBackground(null);
            button4.setBackground(null);
        }
    }

    public void updateTurn() {
        enemyPoints = csc.receiveEnemyPoints();
        if (playerID == 1) {
            scoreboard.appendText("\n\nRound: " + turnsMade +
                    "\nPlayer #1: " + myPoints + "\nPlayer #2: " + enemyPoints);
        }
        System.out.println("Your Enemy has " + enemyPoints + " points.");

        setRoundTwo();
        buttonsEnable = true;
        message.setText("It is your turn now");
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
            message.setText("Game finished");
            scoreboard.appendText("\n\nYOU WON!");
        } else if (myPoints < enemyPoints) {
            message.setText("Game finished");
            scoreboard.appendText("\n\nYOU LOST!");
        } else {
            message.setText("Game finished");
            scoreboard.appendText("\n\nYOU TIED!");
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
            System.out.println("in get Question");
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
                    System.out.println("Right answer #" + (j + 1) + " is : " + rightAnswer[j]);
                }
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

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}

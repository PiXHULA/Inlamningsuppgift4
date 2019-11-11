import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Gameview extends Application implements Runnable{
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox vert1 = new VBox();
        VBox vert2 = new VBox();
        VBox vert3 = new VBox();
        vert1.setMinSize(200,600);
        vert2.setMinSize(400,600);
        vert3.setMinSize(200,600);
        bottomPane = createBottomPane(vert1, vert2, vert3);

        quizArea = createTextArea();
        onlineStatus = createTextArea("Online: ","onlineStatus");
        highscoreArea = createTextArea("Highscore: ", "highscoreArea");

        button = createButton("Answer1", quizArea);
        button2 = createButton("Answer2", quizArea);
        button3 = createButton("Answer3", quizArea);
        button4 = createButton("Answer4", quizArea);

        displayPlayers = displayNames("Player1", "Player2");
        buttonLayout = createButtonLayout();
        gameView = gameviewPane(quizArea, displayPlayers, buttonLayout);
        vert1.getChildren().add(onlineStatus);
        vert2.getChildren().add(gameView);
        vert3.getChildren().add(highscoreArea);
        Scene scene = new Scene(bottomPane);
        scene.getStylesheets().add("GameviewStyle.css");
        stage.setScene(scene);
        stage.show();
    }

    private GridPane createBottomPane(VBox vert1, VBox vert2, VBox vert3) {
        GridPane collectedGui = new GridPane();
        collectedGui.setMinSize(800,600);
        collectedGui.add(vert1,0,0);
        collectedGui.add(vert2,1,0);
        collectedGui.add(vert3,2,0);
        return collectedGui;
    }

    private BorderPane gameviewPane(TextArea quizArea, HBox playerStatus, GridPane buttonLayout) {
        BorderPane Gameview = new BorderPane();
        Gameview.setPrefSize(400,600);
        Gameview.setTop(playerStatus);
        Gameview.setBottom(buttonLayout);
        Gameview.setCenter(quizArea);
        return Gameview;
    }

    private HBox displayNames(String player1, String player2) {
        Label first = new Label(player1);
        Label second = new Label(player2);
        HBox playerStatus = new HBox(10,first,second);
        playerStatus.setSpacing(200);
        playerStatus.setAlignment(Pos.CENTER);
        playerStatus.setPrefHeight(50);
        return playerStatus;
    }

    private Button createButton(String answer, TextArea quizArea) {
        Button button = new Button (answer);
        button.setOnAction(getActionEventEventHandler(button, quizArea, button.getText()));
        return button;
    }

    private TextArea createTextArea() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setFocusTraversable(false);
        return textArea;
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
        buttonLayout.add(button,0,0);
        buttonLayout.add(button2,1,0);
        buttonLayout.add(button3,0,1);
        buttonLayout.add(button4,1,1);
        return buttonLayout;
    }

    private EventHandler<ActionEvent> getActionEventEventHandler(Button button, TextArea quizArea, String s) {
        return actionEvent -> {
            if("Answer1".equals(s)){
                quizArea.appendText("Pushed " + s + "\n");
                button.setId("correctAnswer");
            } else
                button.setId("wrongAnswer");
        };
    }

    @Override
    public void run() {

    }
}

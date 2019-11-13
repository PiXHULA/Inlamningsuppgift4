import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.*;

public class LoadingView extends Application {
    double ii = 0;
    ProgressBar pb;
    public void start(Stage stage) throws Exception {
        EventHandler<ActionEvent> event = e -> {
            // set progress to different level of progressbar
            ii += 0.1;
            pb.setProgress(ii);
        };
        BorderPane loadingView = new BorderPane();
        HBox progressBar = new HBox();
        progressBar.setAlignment(Pos.CENTER);
        progressBar.setMinSize(400,100);
        loadingView.setMinSize(400, 600);
        Label loadingText = new Label("Please wait for a player to join your game!");

        ProgressBar pb = new ProgressBar();

        pb.setPrefSize(250,75);
        progressBar.getChildren().add(pb);
        loadingView.setCenter(loadingText);
        loadingView.setBottom(progressBar);

        Scene scene = new Scene(loadingView);
        scene.getStylesheets().add("GameviewStyle.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        LoadingView loadScreen = new LoadingView();
        launch(args);
    }
}

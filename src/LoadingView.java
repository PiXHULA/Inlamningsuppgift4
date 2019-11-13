import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoadingView extends Application {

    public void start(Stage stage) throws Exception {
        BorderPane loadingView = new BorderPane();
        loadingView.setMinSize(400, 600);
        Label loadingText = new Label("Please wait for a player to join your game!");
        loadingView.setCenter(loadingText);

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

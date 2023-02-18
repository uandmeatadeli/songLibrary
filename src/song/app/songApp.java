package song.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import song.view.songController;
public class songApp extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {

        // set up FXML loader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(
                getClass().getResource  ("/song/view/song.fxml"));

        // load the fxml
        AnchorPane root = (AnchorPane)loader.load();

        // get the controller (Do NOT create a new Controller()!!)
        // instead, get it through the loader
        songController SongController = loader.getController();

        //ListController listController = new ListController();

        SongController.start(primaryStage);

        Scene scene = new Scene(root, 600, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
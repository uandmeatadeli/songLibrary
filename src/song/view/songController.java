package song.view;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
public class songController {
    @FXML
    ListView<String> titleListView;
    @FXML
    ListView<String> artistListView;
    @FXML
    ListView<String> albumListView;
    @FXML
    ListView<String> yearListView;
    @FXML
    HBox HBox;

    String line;
    String[] songs;
    String[] elements;
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> artist = new ArrayList<String>();
    ArrayList<String> album = new ArrayList<String>();
    ArrayList<String> year = new ArrayList<String>();
    private ObservableList<String> obsTitleList;
    private ObservableList<String> obsArtistList;
    private ObservableList<String> obsAlbumList;
    private ObservableList<String> obsYearList;

    public void start(Stage mainStage) {
        // create an ObservableList
        // from an ArrayList

        // create read from file to pull songs
        try {
            File songFile = new File("songFile.txt");
            //System.out.println(songFile.exists());
            Scanner scanner = new Scanner(songFile);
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                songs = line.split(";");
                //System.out.println(songs);
                //songs.add(scanner.next());
            }
            for(int i = 0; i < songs.length; i++){
                elements = songs[i].split(",");
                title.add(elements[0]);
                artist.add(elements[1]);
                album.add(elements[2]);
                year.add(elements[3]);
            }
            //ArrayList<String> songsList = new ArrayList<String>(Arrays.asList(songs));
            obsTitleList = FXCollections.observableArrayList(title);
            obsArtistList = FXCollections.observableArrayList(artist);
            obsAlbumList = FXCollections.observableArrayList(album);
            obsYearList = FXCollections.observableArrayList(year);
        } catch(FileNotFoundException e){
            System.out.println("ERROR");
        }

        setListener(mainStage,titleListView,obsTitleList);
        setListener(mainStage,artistListView,obsArtistList);
        setListener(mainStage,albumListView,obsAlbumList);
        setListener(mainStage,yearListView,obsYearList);

//        titleListView.setItems(obsTitleList);
//
//        // select the first item
//        titleListView.getSelectionModel().select(0);
//
//        // set listener for the items
//        titleListView
//                .getSelectionModel()
//                .selectedIndexProperty()
//                .addListener(
//                        (obs, oldVal, newVal) ->
//                                //showItem(mainStage)
//                                showItemInputDialog(mainStage, titleListView, obsTitleList)
//                );
    }

    private void setListener(Stage mainStage,ListView<String> listView,ObservableList<String> obsList){
        listView.setItems(obsList);

        // select the first item
        listView.getSelectionModel().select(0);

        // set listener for the items
        listView
                .getSelectionModel()
                .selectedIndexProperty()
                .addListener(
                        (obs, oldVal, newVal) ->
                                //showItem(mainStage)
                                showItemInputDialog(mainStage, listView, obsList)
                );
    }

    private void showItem(Stage mainStage, ListView<String> listView) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(mainStage);
        alert.setTitle("List Item");
        alert.setHeaderText(
                "Selected list item properties");

        String content = "Index: " +
                listView.getSelectionModel()
                        .getSelectedIndex() +
                "\nValue: " +
                listView.getSelectionModel()
                        .getSelectedItem();

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showItemInputDialog(Stage mainStage, ListView<String> listView, ObservableList<String> obsList) {
        String item = listView.getSelectionModel().getSelectedItem();
        int index = listView.getSelectionModel().getSelectedIndex();

        TextInputDialog dialog = new TextInputDialog(item);
        dialog.initOwner(mainStage); dialog.setTitle("List Item");
        dialog.setHeaderText("Selected Item (Index: " + index + ")");
        dialog.setContentText("Enter name: ");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) { obsList.set(index, result.get()); }
    }

}

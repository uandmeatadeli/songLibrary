//Yifu Ge
//NetId: yg458
package song.view;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class songController {
    @FXML
    private ListView<Song> listView;
    @FXML
    ListView<String> titleListView;
    @FXML
    ListView<String> artistListView;
    @FXML
    ListView<String> albumListView;
    @FXML
    ListView<String> yearListView;
    @FXML
    TextField titleDisplay;
    @FXML
    TextField artistDisplay;
    @FXML
    TextField albumDisplay;
    @FXML
    TextField yearDisplay;
    @FXML
    Button Add;
    @FXML
    Button Edit;
    @FXML
    Button Delete;
    @FXML
    Button Save;
    @FXML
    Button Cancel;



    String line;
    String[] songs;
    static String[] elements;
    String sName;
    String sArtist;
    String sAlbum;
    String sYear;
    ScrollBar titleScroll;
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> artist = new ArrayList<String>();
    ArrayList<String> album = new ArrayList<String>();
    ArrayList<String> year = new ArrayList<String>();
    private static ObservableList<Song> obsList;
    private ObservableList<String> obsTitleList;
    private ObservableList<String> obsArtistList;
    private ObservableList<String> obsAlbumList;
    private ObservableList<String> obsYearList;
    static final String textFile = "songFile";
    public void start(Stage mainStage) {

        try {
            File songFile = new File("songFile.txt");
            //System.out.println(songFile.exists());
            Scanner scanner = new Scanner(songFile);
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                songs = line.split(";");
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
                                //showItem(mainStage, listView)
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
        if (result.isPresent()) {
            obsList.set(index, result.get()); }
    }

    @FXML
    public void addSong(ActionEvent actionEvent) throws IOException {

        Song item = listView.getSelectionModel().getSelectedItem();

        if(obsList.isEmpty()) {
            System.out.println("List Is Empty");
            Delete.setDisable(true);
            Edit.setDisable(true);
        }
        if(titleDisplay.getText().trim().isEmpty() || artistDisplay.getText().trim().isEmpty()){
            alertDialogue("Invalid Input", "Sorry, Name And Artist Are Required");
            titleDisplay.setText("");
            if(item == null) {
                titleDisplay.setPromptText("");
                artistDisplay.setText("");
                artistDisplay.setPromptText("");
                albumDisplay.setText("");
                albumDisplay.setPromptText("");
                yearDisplay.setText("");
                yearDisplay.setPromptText("");
            }else {
                setTextName(item);
            }
        }else if(isNameArtistInList(titleDisplay.getText().trim(),artistDisplay.getText().trim()  )){
            System.out.println("Duplicate Entry!");

            alertDialogue("Duplicate Input Error!", "Sorry, Duplicate Name and Artist Are Not Allowed");

            titleDisplay.setText("");
            setTextName(item);

        }else if(!(yearDisplay.getText().trim().isEmpty() ) && !(isValid(yearDisplay.getText().trim()))){

            alertDialogue("Invalid Input", "Please Enter A Valid Number In Year Column");

            yearDisplay.setText("");

            System.out.println("The number is not valid");
        }else {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Add Song");
            alert.setHeaderText("Do you want to add song?");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(cancelButton, okButton);
            alert.setContentText("Song Name:\t" + titleDisplay.getText() + "\n" +
                    "Artist Name:\t" + artistDisplay.getText() + "\n" +
                    "Album Name:\t" + albumDisplay.getText() + "\n" +
                    "Release Year:\t" + yearDisplay.getText());

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == okButton) {
                createSong();
                Delete.setDisable(false);
                Edit.setDisable(false);
                saveFile();
            }
        }
    }

    public void createSong() {
        Song newSong = new Song(titleDisplay.getText().trim(), artistDisplay.getText().trim(),albumDisplay.getText().trim(), yearDisplay.getText().trim());
        obsList.add(newSong);
        obsList.sort(Comparator.comparing(Song::getName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER));
        listView.setItems(obsList);
        listView.getSelectionModel().select(newSong);
        titleDisplay.setText("");
        setTextName(newSong);
    }

    public void setTextName(Song item) {
        titleDisplay.setPromptText(item.name);
        artistDisplay.setText("");
        artistDisplay.setPromptText(item.artist);
        albumDisplay.setText("");
        albumDisplay.setPromptText(item.album);
        yearDisplay.setText("");
        yearDisplay.setPromptText(item.year);
    }

    @FXML
    void deleteSong(ActionEvent event) throws IOException {
        if(obsList.isEmpty()) {
            System.out.println("List Is Empty");
            Delete.setDisable(true);
        }else{
            Song item = listView.getSelectionModel().getSelectedItem();
            int index = listView.getSelectionModel().getSelectedIndex();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Delete Song");
            alert.setHeaderText("Warning: Do you want to delete?");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(cancelButton, okButton);
            alert.setContentText("Song Name:\t" + item.name + "\n" +
                    "Artist Name:\t" + item.artist + "\n" +
                    "Album Name:\t" + item.album + "\n" +
                    "Release Year:\t" + item.year);

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == okButton)
            {
                obsList.remove(index);
                System.out.println("Deleting Item: " + item);
                obsList.sort(Comparator.comparing(Song::getName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER));
                if(obsList.isEmpty()) {
                    System.out.println("List Is Empty");
                    Delete.setDisable(true);
                    Edit.setDisable(true);
                    titleDisplay.setText("");
                    titleDisplay.setPromptText("");
                    artistDisplay.setText("");
                    artistDisplay.setPromptText("");
                    albumDisplay.setText("");
                    albumDisplay.setPromptText("");
                    yearDisplay.setText("");
                    yearDisplay.setPromptText("");
                }else {
                    listView.getSelectionModel().select(index);
                    item = listView.getSelectionModel().getSelectedItem();
                    titleDisplay.setText("");
                    setTextName(item);
                    obsList.sort(Comparator.comparing(Song::getName, String.CASE_INSENSITIVE_ORDER)
                            .thenComparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER));

                    saveFile();
                }
            }
        }
    }
    @FXML
    void editSong(ActionEvent event) {
        Edit.setVisible(false);
        Save.setVisible(true);
        Cancel.setVisible(true);
        Song item = listView.getSelectionModel().getSelectedItem();
        int index = listView.getSelectionModel().getSelectedIndex();
        titleDisplay.setText(item.name);
        artistDisplay.setText(item.artist);
        albumDisplay.setText(item.album);
        if(!(yearDisplay.getText().trim().isEmpty() ) && !(isValid(yearDisplay.getText().trim()))){
            alertDialogue("Invalid Input", "Please Enter A Valid Number In Year Column");
            yearDisplay.setText("");
            System.out.println("The number is not valid");
        }
        else{
            yearDisplay.setText(item.year);
        }
        listView.setMouseTransparent( true );
        listView.setFocusTraversable( false );
        Delete.setDisable(true);
        Add.setDisable(true);
    }
    @FXML
    void cancelEntry(ActionEvent event) {
        Edit.setVisible(true);
        Save.setVisible(false);
        Cancel.setVisible(false);
        Song item = listView.getSelectionModel().getSelectedItem();
        int index = listView.getSelectionModel().getSelectedIndex();
        titleDisplay.setText("");
        setTextName(item);
        listView.setMouseTransparent( false );
        listView.setFocusTraversable( true );
        Delete.setDisable(false);
        Add.setDisable(false);
    }

    @FXML
    void mouseClick(MouseEvent event) {
        if(obsList.isEmpty()) {
        }else {
            Song item = listView.getSelectionModel().getSelectedItem();
            int index = listView.getSelectionModel().getSelectedIndex();
            titleDisplay.setPromptText(item.name);
            artistDisplay.setPromptText(item.artist);
            albumDisplay.setPromptText(item.album);
            yearDisplay.setPromptText(item.year);
            listView.getSelectionModel().select(index);
        }
    }

    @FXML
    void saveEditSong(ActionEvent event) throws IOException {
        Song item = listView.getSelectionModel().getSelectedItem();
        int index = listView.getSelectionModel().getSelectedIndex();
        if(titleDisplay.getText().trim().isEmpty() || artistDisplay.getText().trim().isEmpty()){
            System.out.println("Sorry, Name And Artist Are Required");
            alertDialogue("Invalid Input", "Sorry, Name And Artist Are Required");
        }else if(isNameArtistInList(titleDisplay.getText().trim(),artistDisplay.getText().trim()  )){

            if(index == returnIndex(titleDisplay.getText().trim(), artistDisplay.getText().trim()) ) {
                System.out.println("Can be edited");
                System.out.println("Index: "+ index );
                System.out.println("Text1: "+returnIndex(titleDisplay.getText().trim(), artistDisplay.getText().trim()) );
                obRemove(index);
            }else {
                alertDialogue("Duplicate Input Error!", "Sorry, Duplicate Name and Artist Are Not Allowed");
                System.out.println("Duplicate Entry!");
                System.out.println("Unable to Save!");
            }

        }else {
            obRemove(index);

        }
    }
    private void obRemove(int index) throws IOException {
        obsList.remove(index);
        createSong();

        Edit.setVisible(true);
        Save.setVisible(false);
        Cancel.setVisible(false);

        Delete.setDisable(false);
        Add.setDisable(false);

        listView.setMouseTransparent( false );
        listView.setFocusTraversable( true );
        saveFile();
    }

    @FXML
    public static void exit() throws IOException {
        saveFile();
    }
    public static void saveFile() throws IOException {
        System.out.println("Saving...");

        File file = new File(textFile);

        file.createNewFile();

        FileWriter writer = new FileWriter(file);

        for (Song song : obsList) {
            writer.write(song.ToString());
        }

        writer.flush();
        writer.close();

        System.out.println("Saving Completed!");
    }
    public static boolean isNumeric(String string) {
        int num = 0;

        System.out.println(String.format("Parsing string: \"%s\"", string));

        if(string == null || string.equals("")) {
            System.out.println("String cannot be parsed, it is null or empty.");
            return false;
        }

        try {
            num = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }
    public static boolean isValid(String input) {

        if((isNumeric(String.valueOf(input.charAt(0)))) && !(input.charAt(0) == ('-'))){
            return true;
        }
        else {
            return false;
        }

    }
    public static boolean isNameArtistInList(String name, String artist) {

        for (Song song : obsList) {
            if(song.name.equalsIgnoreCase(name) && song.artist.equalsIgnoreCase(artist) ){
                return true;
            }

        }
        return false;
    }
    public static int returnIndex(String name, String artist) {
        int index = 0;
        for (Song song : obsList) {
            if(song.name.equalsIgnoreCase(name) && song.artist.equalsIgnoreCase(artist) ){

                return index;
            }
            index++;

        }
        return index;
    }
    @FXML
    public void alertDialogue(String title, String headerText) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    public void initialize() throws FileNotFoundException {
        obsList = FXCollections.observableArrayList();
        File fil = new File(textFile);
        if(fil.exists()) {
            System.out.println("FILE EXISTS!!");
            System.out.println("Loading Data ...");
            Scanner scanner = new Scanner(fil);
            while(scanner.hasNext()) {
                String string = scanner.nextLine();
                if(string.contains("name:")) {
                    sName = string.substring(6);
                }else if(string.contains("artist:") ){
                    sArtist = string.substring(8);
                }else if(string.contains("album:") ){
                    sAlbum = string.substring(7);
                }else if(string.contains("year:") ){
                    sYear= string.substring(6);
                    Song newSong = new Song(sName.trim(), sArtist.trim(), sAlbum.trim(), sYear.trim());
                    obsList.add(newSong);
                }
            }
            scanner.close();
            obsList.sort(Comparator.comparing(Song::getName, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER));
            listView.setItems(obsList);
            listView.getSelectionModel().select(0);
            Song item = listView.getSelectionModel().getSelectedItem();
            int index = listView.getSelectionModel().getSelectedIndex();
            if(item == null) {
                titleDisplay.setPromptText("");
                albumDisplay.setPromptText("");
                albumDisplay.setPromptText("");
                yearDisplay.setPromptText("");
                Edit.setDisable(true);
                Delete.setDisable(true);
            }else {
                titleDisplay.setPromptText(item.name);
                artistDisplay.setPromptText(item.artist);
                albumDisplay.setPromptText(item.album);
                yearDisplay.setPromptText(item.year);
            }
            Cancel.setVisible(false);
            Save.setVisible(false);
            System.out.println("Loading Data Completed!");
        }else {
            System.out.println("FILE DOESN'T EXISTS!!");
            obsList = FXCollections.observableArrayList();
            Cancel.setVisible(false);
            Save.setVisible(false);
            Delete.setDisable(true);
            Edit.setDisable(true);
        }
    }
}

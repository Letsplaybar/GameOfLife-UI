package de.letsplaybar.gameoflife.aplication;

import de.letsplaybar.gameoflife.utils.CellValueFactory;
import de.letsplaybar.gameoflife.utils.Game;
import de.letsplaybar.gameoflife.utils.ValueCallback;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class GameOfLife {

    @FXML // fx:id="gen"
    private Text gen; // Value injected by FXMLLoader

    @FXML // fx:id="game"
    private Pane game; // Value injected by FXMLLoader

    @FXML // fx:id="table1"
    private TableView<List<String>> table1; // Value injected by FXMLLoader

    @FXML // fx:id="length"
    private TextField length; // Value injected by FXMLLoader

    @FXML // fx:id="width"
    private TextField width; // Value injected by FXMLLoader

    @FXML // fx:id="table"
    private TableView<List<String>> table; // Value injected by FXMLLoader

    @FXML // fx:id="savepb"
    private ImageView savepb; // Value injected by FXMLLoader

    @FXML // fx:id="newGame"
    private Pane newGame; // Value injected by FXMLLoader

    @FXML // fx:id="newGame"
    private Pane newGame1; // Value injected by FXMLLoader

    @FXML // fx:id="ladepb"
    private ImageView ladepb; // Value injected by FXMLLoader

    @FXML // fx:id="closepb"
    private ImageView closepb; // Value injected by FXMLLoader

    @FXML // fx:id="newpb"
    private ImageView newpb; // Value injected by FXMLLoader

    @FXML // fx:id="play"
    private ToggleButton play; // Value injected by FXMLLoader

    private boolean saved;

    private Game currentGame;

    private int currentGen;

    private Timer timer;


    @FXML
    void onMouseDrag(MouseEvent event) throws IOException {
        if(event.getTarget() == newpb){
            newGame.setVisible(true);
            if(newGame1.isVisible())
                newGame1.setVisible(false);
            if (game.isVisible())
                game.setVisible(false);
            table.getItems().clear();
            table.getColumns().clear();
            length.setText("");
            width.setText("");
            currentGen = 0;
            if(timer != null)
                timer.cancel();
        }else if(event.getTarget() == ladepb){
            FileChooser chooser = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("GameofLife","*.gol");
            chooser.getExtensionFilters().add(filter);
            chooser.setSelectedExtensionFilter(filter);
            File file = chooser.showOpenDialog(newGame.getScene().getWindow());
            if(timer != null)
                timer.cancel();
            if(file!= null){
                currentGame = Game.load(file);
                gen.setText("Generation 0");
                load();
                game.setVisible(true);
                if(newGame.isVisible())
                    newGame.setVisible(false);
                if(newGame1.isVisible()){
                    newGame1.setVisible(false);
                }
            }
        }else if (event.getTarget() == savepb){
            if(currentGame != null){
                if(timer != null)
                    timer.cancel();
                FileChooser chooser = new FileChooser();FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("GameofLife","*.gol");
                chooser.getExtensionFilters().add(filter);
                chooser.setSelectedExtensionFilter(filter);
                File file = chooser.showSaveDialog(newGame.getScene().getWindow());
                if (file != null)
                    currentGame.save(file);
            }
        }else if(event.getTarget() == closepb){
            if(saved){
                if(timer != null)
                    timer.cancel();
                ((Stage)newGame.getScene().getWindow()).close();
                System.exit(0);
            }
        }
    }

    @FXML
    void next(ActionEvent event) {
        if(width.getText() == "" || length.getText() == "")
            return;
        int length = Integer.parseInt(this.length.getText());
        int width = Integer.parseInt(this.width.getText());
        currentGame = new Game(length,width);
        for(int i =0; i<width; i++){
            List<String> list = new ArrayList<>();
            for(int z =0; z<length; z++)
                list.add("0");
            table.getItems().add(list);
        }
        for(int i =0; i<length; i++){
            TableColumn<List<String>,String> col = new TableColumn<>(String.valueOf(i+1));
            col.setPrefWidth(20);
            col.setCellValueFactory(new CellValueFactory(i));
            col.setCellFactory(column -> new TableCell<List<String>, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    if (item != null) {
                        if (item.equals("0")) {
                            setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000");
                        } else if (item.equals("1")) {
                            setStyle("-fx-background-color: #000000; -fx-border-color: #FFFFFF");
                        } else {
                            setStyle("-fx-background-color: red; -fx-border-color: #FFFFFF");
                            setText(item);
                        }
                    }
                }
            });
            table.getColumns().add(col);
        }
        table.refresh();
        newGame.setVisible(false);
        newGame1.setVisible(true);
    }

    @FXML
    void startGame(ActionEvent event)  {
        currentGame.getRounds().get(currentGen).closeGen();
        load();
        gen.setText("Generation 0");
        newGame1.setVisible(false);
        game.setVisible(true);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(currentGame.getRounds().get(currentGen).isDead())
                    timer.cancel();
                if(!play.isSelected())
                    return;
                if(!currentGame.getRounds().containsKey(currentGen+1)) {
                    try {
                        currentGame.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                currentGen++;
                gen.setText("Generation " + currentGen);
                Platform.runLater(() -> load());
            }
        },0, 250);

    }

    private void load(){
        table1.getItems().clear();
        table1.getColumns().clear();
        int length = currentGame.getLength();
        int width = currentGame.getWidth();
        for (int x = 0; x< width; x++){
            List<String> list = new ArrayList<>();
            for(int y =0; y<length;y++){
                list.add(String.valueOf(currentGame.getRounds().get(currentGen).getGame()[y+1][x+1]));
            }
            table1.getItems().add(list);
        }
        for(int i =0; i<length; i++){
            TableColumn<List<String>,String> col = new TableColumn<>(String.valueOf(i+1));
            col.setPrefWidth(20);
            col.setCellValueFactory(new CellValueFactory(i));
            col.setCellFactory(column -> new TableCell<List<String>, String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    if(item != null){
                        if(item.equals("0")){
                            setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000");
                        }else if(item.equals("1")){
                            setStyle("-fx-background-color: #000000; -fx-border-color: #FFFFFF");
                        }else {
                            setStyle("-fx-background-color: red; -fx-border-color: #FFFFFF");
                            setText(item);
                        }
                    }
                }
            });
            table1.getColumns().add(col);
        }
        table1.refresh();
    }

    @FXML
    void back(ActionEvent event) {
        newGame1.setVisible(false);
        newGame.setVisible(true);
        table.getItems().clear();
        table.getColumns().clear();
    }

    @FXML
    void onInit(MouseEvent event) {
        if(table.getSelectionModel() ==null)
            return;
        TablePosition cell = table.getSelectionModel().getSelectedCells().get(0);
        int x = cell.getColumn();
        int y = cell.getRow();
        List<String> list = table.getItems().get(y);
        currentGame.getRounds().get(currentGen).changeFeld(x+1,y+1);
        list.set(x, String.valueOf(currentGame.getRounds().get(currentGen).getGame()[x+1][y+1]));
        table.getItems().set(y,list);
        table.refresh();
    }

    @FXML
    void randomize(ActionEvent event){
        int length = currentGame.getLength();
        int width = currentGame.getWidth();

        try {
            currentGame.init(length, width, new ValueCallback() {
                @Override
                public void handle(int x, int y) {
                    List<String> list = table.getItems().get(y);
                    list.set(x, String.valueOf(currentGame.getRounds().get(currentGen).getGame()[x+1][y+1]));
                    table.getItems().set(y,list);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        table.refresh();
    }



    @FXML
    void addGen(ActionEvent event) throws InterruptedException, CloneNotSupportedException {
        if(!currentGame.getRounds().get(currentGen).isDead()) {
            if(!currentGame.getRounds().containsKey(currentGen+1))
                currentGame.run();
            currentGen++;
            gen.setText("Generation " + currentGen);
            load();
        }
    }

    @FXML
    void backGen(ActionEvent event) {
        if(currentGen == 0){
            return;
        }
        currentGen--;
        gen.setText("Generation "+currentGen);
        load();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert play != null : "fx:id=\"play\" was not injected: check your FXML file 'GameOfLife.fxml'.";
        assert gen != null : "fx:id=\"gen\" was not injected: check your FXML file 'GameOfLife.fxml'.";
        assert newGame1 != null : "fx:id=\"newGame1\" was not injected: check your FXML file 'GameOfLife.fxml'.";
        assert game != null : "fx:id=\"game\" was not injected: check your FXML file 'GameOfLife.fxml'.";
        assert savepb != null : "fx:id=\"savepb\" was not injected: check your FXML file 'GameOfLife.fxml'.";
        assert table1 != null : "fx:id=\"table1\" was not injected: check your FXML file 'GameOfLife.fxml'.";
        assert newGame != null : "fx:id=\"newGame\" was not injected: check your FXML file 'GameOfLife.fxml'.";
        assert length != null : "fx:id=\"length\" was not injected: check your FXML file 'GameOfLife.fxml'.";
        assert width != null : "fx:id=\"width\" was not injected: check your FXML file 'GameOfLife.fxml'.";
        assert ladepb != null : "fx:id=\"ladepb\" was not injected: check your FXML file 'GameOfLife.fxml'.";
        assert closepb != null : "fx:id=\"closepb\" was not injected: check your FXML file 'GameOfLife.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'GameOfLife.fxml'.";
        assert newpb != null : "fx:id=\"newpb\" was not injected: check your FXML file 'GameOfLife.fxml'.";

        newGame.setVisible(false);
        newGame1.setVisible(false);
        game.setVisible(false);
        saved = true;
        currentGen = 0;

    }
}

package project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class GamesList extends Application {
    static final String DB_NAME = "Games";
    static final String TABLE_NAME = "games";
    static final String URL = "jdbc:postgresql://localhost/" + DB_NAME;
    static final String USERNAME = "postgres";
    static final String PASSWORD = "postgre";
    static Connection connection;
    static ResultSet result;
    static Statement statement;
    static ObservableList<GameRow> rows = FXCollections.observableArrayList();
    static TableView<GameRow> tableView;
    static ArrayList<Integer> ids = new ArrayList<>();
    static TableColumn<GameRow, Integer> idColumn = new TableColumn<>("id");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        connectToDB();
        loadTable();
        setUpTableView();

        Button addBtn = new Button("Add Game");
        Button removeBtn = new Button("Remove Game");
        Button changeBtn = new Button("Change");
        Button refreshBtn = new Button("Refresh");

        VBox root = new VBox();
        HBox controls = new HBox();
        controls.getChildren().addAll(addBtn, changeBtn, removeBtn, refreshBtn);
        controls.setSpacing(30);
        controls.setPadding(new Insets(20,0,20,0));
        controls.setAlignment(Pos.CENTER);

        root.getChildren().addAll(controls,tableView);

        addBtn.setOnMouseClicked(e -> handleAddData(statement));
        refreshBtn.setOnMouseClicked(e -> loadTable());
        removeBtn.setOnMouseClicked(e -> handleRemoveData());

        Scene scene = new Scene(root, 400,300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Games List");
        primaryStage.show();
    }

    public static void connectToDB(){
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Successfully connected to database!");
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public static void loadTable(){
        String query = "select * from " + TABLE_NAME;

        // Clear rows and ids before refresh
        rows.clear();
        ids.clear();

        try{
            statement = connection.createStatement();
            result = statement.executeQuery(query);
            while(result.next()){
                int id = result.getInt(1);
                ids.add(id);
                String name = result.getString(2);
                double rating = result.getDouble(3);
                String comment = result.getString(4);
                boolean finished = result.getBoolean(5);
                rows.add(new GameRow(id,name,rating,comment,finished));
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }

        try {
            sortTableView();
        }
        catch (NullPointerException ignored){

        }
    }

    public static void setUpTableView(){
        tableView = new TableView<>(rows);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        TableColumn<GameRow, String> nameColumn = new TableColumn<>("Name");
        TableColumn<GameRow, Double> ratingColumn = new TableColumn<>("Rating");
        TableColumn<GameRow, String> commentColumn = new TableColumn<>("Comment");
        TableColumn<GameRow, Boolean> finishedColumn = new TableColumn<>("Finished");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        finishedColumn.setCellValueFactory(new PropertyValueFactory<>("finished"));

        tableView.getColumns().addAll(idColumn,nameColumn,ratingColumn,commentColumn,finishedColumn);
        sortTableView();
    }

    public static boolean editData(GameRow row){
        DataEditController controller = new DataEditController(row);
        FXMLLoader loader = new FXMLLoader();
        loader.setController(controller);
        loader.setLocation(GamesList.class.getResource("DataEdit.fxml"));
        try {
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            controller.setStage(stage);
            stage.setTitle("Edit Row");
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return controller.isOkClicked();
    }

    @FXML
    public static void handleAddData(Statement statement) {
        GameRow temp = new GameRow();
        boolean okClicked = editData(temp);
        if(okClicked){
            String toExecute = String.format("INSERT INTO %s (id,name,rating,comment,finished) VALUES (%d,'%s', %s, '%s', %b)"
                    ,TABLE_NAME,getNextID(),temp.getName(),temp.getRating(),temp.getComment(),temp.isFinished());
            System.out.println(toExecute);
            try {
                statement.executeQuery(toExecute);
            } catch (SQLException ignored) {

            }

        }
        loadTable();
    }

    @FXML
    public static void handleRemoveData() {
        try {
            int id = tableView.getSelectionModel().getSelectedItem().getId();
            String toExecute = String.format("DELETE FROM %s WHERE ID = %d", TABLE_NAME, id);
            System.out.println(toExecute);
            statement.executeQuery(toExecute);
        } catch (SQLException ignored) {

        }
        catch (NullPointerException e){
            new Alert(Alert.AlertType.WARNING,"Row not selected").show();
        }

        loadTable();
    }

    public static int getNextID(){
        for(int i = 1; i <= ids.size(); i++){
            if(!ids.contains(i)) return i;
        }
        return ids.size()+1;
    }

    public static void sortTableView(){
        tableView.getSortOrder().add(idColumn);
        tableView.sort();
    }
}

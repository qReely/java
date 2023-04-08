package project;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DataEditController {
    @FXML
    public TextField nameField = new TextField("");
    @FXML
    public TextField ratingField = new TextField("");
    @FXML
    public TextField commentField = new TextField("");
    @FXML
    public CheckBox finishedCheckBox = new CheckBox();

    Stage stage;
    GameRow row;
    boolean okClicked = false;
    String error = "";

    DataEditController(GameRow row){
        if(row.getName() == null){
            nameField.setText(row.getName());
            ratingField.setText(row.getRating() + "");
            commentField.setText(row.getComment());
            finishedCheckBox.setSelected(row.isFinished());
        }
        else{
            this.row = row;
        }
    }

    public boolean isOkClicked(){
        return okClicked;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private void handleCancel() {

        stage.close();
    }

    @FXML
    private void handleOk() {
        //TODO HANDLE OK
        if(!isValid()){
            Alert alert  = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Bad Input");
            alert.setContentText(error);
            alert.show();
            return;
        }

        setRow();
        okClicked = true;
        stage.close();
    }

    private boolean isValidRating(String text){
        try{
            double d = Double.parseDouble(text);
            if(d >= 0 && d <= 10) return true;
        }
        catch (Exception ignored){

        }
        return false;
    }

    private boolean isValid(){
        error = "";
        if(nameField.getText().length() == 0){
            error += "Name field should not be empty\n";
        }
        if(nameField.getText().length() > 30){
            error += "Name field should be less than 30 characters\n";
        }
        if(!isValidRating(ratingField.getText())){
            error += "Rating Field should be a value in range of 0 and 10\n";
        }
        if(commentField.getText().length() > 100){
            error += "Comment Field should be less than 100 characters\n";
        }
        return error.length() == 0;
    }

    private void setRow(){
        row.setName(nameField.getText());
        row.setRating(Double.parseDouble(ratingField.getText()));
        row.setComment(commentField.getText());
        row.setFinished(finishedCheckBox.isSelected() ? "Yes" : "No");
    }
}

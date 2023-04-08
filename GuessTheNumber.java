package com.project.myprojects.profile;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GuessTheNumber extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    public final int DEFAULT_TRIES_NUMBER = 5;
    public static int numOfTries, numFrom, numTo, numGoal;
    public static Label triesLabel, titleLabel, rangeLabel, outputLabel = new Label();
    public static Alert wonAlert, lostAlert;
    public static TextField answerField = new TextField("");
    public static ButtonType tryAgainButton;
    public static CheckBox tryModeCheck;
    public static Button checkButton;
    public static VBox root = new VBox();

    @Override
    public void start(Stage primaryStage){
        // Creating a range of numbers and a random number in that range to player to find
        createGoal();

        // Creating a GUI for an application
        createGUIPane();

        // Creating alerts for a winning/losing state
        createAlerts();

        // Check button listener
        checkButton.setOnMouseClicked(e -> checkGuess());

        // TryMode toggle button listener
        tryModeCheck.setOnMouseClicked(e -> toggleTryMode());

        // Alerts listeners
        wonAlert.setOnCloseRequest(e -> restart(wonAlert.getResult()));
        lostAlert.setOnCloseRequest(e -> restart(lostAlert.getResult()));

        Scene scene = new Scene(root,275,300);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Guess The Number");
        primaryStage.show();
    }

    public void createGoal(){
        // Setting the number of tries to default
        numOfTries = DEFAULT_TRIES_NUMBER;

        // Creating the two ("From" and "To") random numbers
        numFrom = (int)(Math.random()*100);
        numTo = (int)(Math.random()*100);

        // Swap values if higherEnd is smaller than lowerEnd
        if(numFrom > numTo){
            int temp = numFrom;
            numFrom = numTo;
            numTo = temp;
        }

        // If numbers are to close to each other, separate them by a random offset
        int offset = (int)(Math.random()*10);
        if(numTo - numFrom < 5){
            numTo += offset;
            numFrom -= offset;
            if(numFrom < 0) numFrom = 0;
        }

        // Creating random number that player need to guess
        numGoal = (int)(Math.random()*(numTo - numFrom) + numFrom);
    }

    public void createGUIPane(){
        // Panes
        VBox inputs = new VBox(), outputs = new VBox(), labels = new VBox();

        // Labels
        titleLabel = new Label("Guess The Number In Range Between");
        rangeLabel = new Label(numFrom + " and " + numTo);
        triesLabel = new Label(numOfTries + "");
        triesLabel.setVisible(false);

        // Inputs
        answerField.setMaxHeight(15);
        answerField.setMaxWidth(120);
        answerField.setPromptText("answer");

        checkButton = new Button("Check");
        tryAgainButton = new ButtonType("Try again!");
        tryModeCheck = new CheckBox("Enable try mode?");

        // adding children to Panes
        labels.getChildren().addAll(titleLabel, rangeLabel);
        inputs.getChildren().addAll(answerField, checkButton);
        outputs.getChildren().addAll(outputLabel, tryModeCheck, triesLabel);

        root.getChildren().addAll(labels, inputs, outputs);

        // Alignment, Margin, Spacing
        labels.setAlignment(Pos.CENTER);
        inputs.setAlignment(Pos.CENTER);
        outputs.setAlignment(Pos.CENTER);

        VBox.setMargin(labels, new Insets(15,0,0,0));
        VBox.setMargin(inputs, new Insets(50,0,0,0));
        VBox.setMargin(outputs, new Insets(30,0,0,0));

        inputs.setSpacing(30);
        outputs.setSpacing(10);
    }

    public void createAlerts(){
        wonAlert = new Alert(Alert.AlertType.CONFIRMATION);
        wonAlert.setTitle("You won!");
        wonAlert.setHeaderText("Congratulations!");
        wonAlert.setContentText("Try Again?");
        wonAlert.getButtonTypes().setAll(tryAgainButton);

        lostAlert = new Alert(Alert.AlertType.CONFIRMATION);
        lostAlert.setTitle("You lost!");
        lostAlert.setHeaderText("Better luck next time!");
        lostAlert.setContentText("Try Again?");
        lostAlert.getButtonTypes().setAll(tryAgainButton);
    }

    public void checkGuess(){
        try{
            int guess = Integer.parseInt(answerField.getText().trim());
            outputLabel.setVisible(true);

            if(tryModeCheck.isSelected()){
                numOfTries--;
                triesLabel.setText(numOfTries + "");
            }

            if(guess == numGoal){
                outputLabel.setText("You are correct! Congratulations!");
                wonAlert.show();
            }
            else if(guess < numGoal){
                outputLabel.setText("No, the number is bigger!");
            }
            else{
                outputLabel.setText("No, the number is smaller!");
                if(numOfTries < 1) lostAlert.show();
            }
        }
        catch (NumberFormatException ex){
            outputLabel.setText("You should write a number!");
        }
        outputLabel.setVisible(true);
    }

    public void toggleTryMode(){
        triesLabel.setVisible(tryModeCheck.isSelected());

        if(tryModeCheck.isSelected()){
            tryModeCheck.setText("Disable try mode?");
        }
        else{
            tryModeCheck.setText("Enable try mode?");
        }
        createGoal();
        updateLabels();
    }

    public void updateLabels(){
        rangeLabel.setText(numFrom + " and " + numTo);
        triesLabel.setText(numOfTries + "");
        outputLabel.setVisible(false);
    }

    public void restart(ButtonType result){
        if (result != null && result == tryAgainButton) {
            createGoal();
            updateLabels();
        } else {
            Platform.exit();
        }
    }
}

package generator;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.prefs.Preferences;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PasswordGenerator extends Application {
    //TODO Comment, Advanced

    final int DEFAULT_PASSWORD_LENGTH = 16;
    String path = "password.txt";
    Label[] settingLabels = new Label[8];
    Label passwordLabel;
    CheckBox[] settingCheckBoxes = new CheckBox[7];
    Button generatePasswordButton, textEditorButton, copyButton;
    ObservableList<Integer> passwordLengthList = FXCollections.observableArrayList(8,16,24,32,40);
    ComboBox<Integer> passwordLengthBox = new ComboBox<>(passwordLengthList);
    TextField passwordField = new TextField();
    Character[] lowercase = ("abcdefghijklmnopqrstuvwxyz").chars().mapToObj(c -> (char)c).toArray(Character[]::new);
    Character[] uppercase = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ").chars().mapToObj(c -> (char)c).toArray(Character[]::new);
    Character[] numbers = ("0123456789").chars().mapToObj(c -> (char)c).toArray(Character[]::new);
    Character[] symbols = ("!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~").chars().mapToObj(c -> (char)c).toArray(Character[]::new);
    VBox root = new VBox();
    HBox buttons = new HBox(), generatedPassword = new HBox();
    GridPane settings = new GridPane();
    Preferences prefs;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        createGUIPane();
        prefs = Preferences.userRoot().node(this.getClass().getName());
        loadPreference();

        for(CheckBox box : settingCheckBoxes){
            box.setOnMouseClicked(e ->{
                if(settingCheckBoxes[6].isSelected()){
                    addPreference();
                }
                else if(settingCheckBoxes[6].equals(box)){
                    removePreference();
                }
            });
        }

        passwordLengthBox.setOnAction(e ->{
            if(settingCheckBoxes[6].isSelected()) addPreference();
        });

        generatePasswordButton.setOnMouseClicked(e -> generatePassword());
        copyButton.setOnMouseClicked(e -> copyPassword());
        textEditorButton.setOnMouseClicked(e -> openPasswordFile());
        Scene scene = new Scene(root,635,335);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Generate Password");
        primaryStage.show();
    }

    public void createGUIPane(){
        long start = System.currentTimeMillis();
        // Initialize Labels
        settingLabels[0] = new Label("Password Length:");
        settingLabels[1] = new Label("Include Symbols:");
        settingLabels[2] = new Label("Include Numbers:");
        settingLabels[3] = new Label("Include Lowercase Characters:");
        settingLabels[4] = new Label("Include Uppercase Characters:");
        settingLabels[5] = new Label("Generate On Your Device:");
        settingLabels[6] = new Label("Auto-Select:");
        settingLabels[7] = new Label("Save My Preference:");
        passwordLabel = new Label("Your New Password:");

        // Initialize CheckBoxes
        settingCheckBoxes[0] = new CheckBox("(e.g. @#$%)");
        settingCheckBoxes[1] = new CheckBox("(e.g. 123456)");
        settingCheckBoxes[2] = new CheckBox("(e.g. abcdefg)");
        settingCheckBoxes[3] = new CheckBox("(e.g. ABCDEFG)");
        settingCheckBoxes[4] = new CheckBox("(do NOT send across the Internet)");
        settingCheckBoxes[5] = new CheckBox("(select the password automatically)");
        settingCheckBoxes[6] = new CheckBox("(save all the settings above for later use)");

        // Initialize Buttons
        generatePasswordButton = new Button("Generate Password");
        textEditorButton = new Button("Text Editor");
        copyButton = new Button("Copy");

        // Adding children to Panes
        settings.getColumnConstraints().add(new ColumnConstraints(180));
        settings.getColumnConstraints().add(new ColumnConstraints(250));

        for(int i = 0; i < settingLabels.length; i++){
            settings.getRowConstraints().add(new RowConstraints(25));
            settings.add(settingLabels[i],0,i);
        }

        for(int i = 0; i < settingCheckBoxes.length; i++){
            settings.add(settingCheckBoxes[i],1,i+1);
        }

        passwordLengthBox.setValue(DEFAULT_PASSWORD_LENGTH);
        passwordLengthBox.setMinWidth(150);
        settings.add(passwordLengthBox, 1, 0);

        buttons.getChildren().addAll(generatePasswordButton, textEditorButton);
        buttons.setSpacing(10);
        buttons.setPadding(new Insets(20,0,20,180));

        generatedPassword.getChildren().addAll(passwordLabel, passwordField, copyButton);
        generatedPassword.setSpacing(10);
        passwordLabel.setMinWidth(170);
        passwordField.setMinWidth(300);
        passwordField.setPromptText("Your new password will appear here.");
        passwordField.setEditable(false);

        root.getChildren().addAll(settings, buttons,generatedPassword);
        root.setPadding(new Insets(20,0,0,50));
        long end = System.currentTimeMillis();
        System.out.println("GUI created in " + (end - start) + " ms");
    }

    public void generatePassword(){
        long start = System.currentTimeMillis();
        StringBuilder password = new StringBuilder("");
        ArrayList<Character> usableCharacters = new ArrayList<>();
        if(settingCheckBoxes[0].isSelected()) usableCharacters.addAll(List.of(symbols));
        if(settingCheckBoxes[1].isSelected()) usableCharacters.addAll(List.of(numbers));
        if(settingCheckBoxes[2].isSelected()) usableCharacters.addAll(List.of(lowercase));
        if(settingCheckBoxes[3].isSelected()) usableCharacters.addAll(List.of(uppercase));
        Collections.shuffle(usableCharacters);
        if(usableCharacters.size() == 0) return;

        Random r = new Random();
        for(int i = 0; i < passwordLengthBox.getValue(); i++){
            password.append(usableCharacters.get(r.nextInt(0,usableCharacters.size())));
        }
        passwordField.setText(password.toString());

        if(settingCheckBoxes[4].isSelected()) savePasswordFile();
        if(settingCheckBoxes[5].isSelected()) {
            passwordField.requestFocus();
            passwordField.selectAll();
        }
        long end = System.currentTimeMillis();
        System.out.println("Password Generated in " + (end - start) + " ms");
    }

    public void copyPassword(){
        if(passwordField.getText().length() == 0) return;
        ClipboardContent content = new ClipboardContent();
        passwordField.requestFocus();
        passwordField.selectAll();
        content.putString(passwordField.getText());
        Clipboard.getSystemClipboard().setContent(content);
    }

    public void savePasswordFile(){
        long start = System.currentTimeMillis();
        if(passwordField.getText().length() == 0) return;
        try(FileOutputStream file = new FileOutputStream(path)){
            file.write(passwordField.getText().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        System.out.println("Password saved in " + (end - start) + " ms");
    }

    public void openPasswordFile(){
        try {
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe", path);
            pb.start();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPreference(){
        prefs.putInt("Password_Length", passwordLengthBox.getValue());
        prefs.putBoolean("Include_Symbols",settingCheckBoxes[0].isSelected());
        prefs.putBoolean("Include_Numbers",settingCheckBoxes[1].isSelected());
        prefs.putBoolean("Include_Lowercase",settingCheckBoxes[2].isSelected());
        prefs.putBoolean("Include_Uppercase",settingCheckBoxes[3].isSelected());
        prefs.putBoolean("Generate_On_Device",settingCheckBoxes[4].isSelected());
        prefs.putBoolean("Auto_Select",settingCheckBoxes[5].isSelected());
        prefs.putBoolean("Save_Preferences",settingCheckBoxes[6].isSelected());
    }

    public void removePreference(){
        prefs.putInt("Password_Length", DEFAULT_PASSWORD_LENGTH);
        prefs.putBoolean("Include_Symbols",false);
        prefs.putBoolean("Include_Numbers",false);
        prefs.putBoolean("Include_Lowercase",false);
        prefs.putBoolean("Include_Uppercase",false);
        prefs.putBoolean("Generate_On_Device",false);
        prefs.putBoolean("Auto_Select",false);
        prefs.putBoolean("Save_Preferences",false);
    }

    public void loadPreference(){
        passwordLengthBox.setValue(prefs.getInt("Password_Length", DEFAULT_PASSWORD_LENGTH));
        settingCheckBoxes[0].setSelected(prefs.getBoolean("Include_Symbols", false));
        settingCheckBoxes[1].setSelected(prefs.getBoolean("Include_Numbers", false));
        settingCheckBoxes[2].setSelected(prefs.getBoolean("Include_Lowercase", false));
        settingCheckBoxes[3].setSelected(prefs.getBoolean("Include_Uppercase", false));
        settingCheckBoxes[4].setSelected(prefs.getBoolean("Generate_On_Device", false));
        settingCheckBoxes[5].setSelected(prefs.getBoolean("Auto_Select", false));
        settingCheckBoxes[6].setSelected(prefs.getBoolean("Save_Preferences", false));

    }
}

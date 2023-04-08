package com.project.myprojects.profile;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Weather extends Application {
    final String url = "https://yandex.kz/pogoda/";
    final String divNameOfTemperature = "temp__value temp__value_with-unit";
    final String divNameOfCity = "title title_level_1 header-title__title";
    final String divNameOfCondition = "link__feelings fact__feelings";
    final String divNameOfWind = "term term_orient_v fact__wind-speed";
    final String divNameOfHumidity = "term term_orient_v fact__humidity";
    final String divNameOfPressure = "term term_orient_v fact__pressure";

    Label city = new Label();
    Label temperature = new Label();
    Label condition = new Label();
    Label wind = new Label();
    Label humidity = new Label();
    Label pressure = new Label();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        connect();
        VBox root = new VBox();
        HBox top = new HBox();
        top.setMinHeight(24);
        temperature.setFont(new Font("Roboto", 24));
        top.setAlignment(Pos.BOTTOM_LEFT);

        top.getChildren().addAll(temperature, city);
        top.setSpacing(3);


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        connect();
                    }
                }
        ), new KeyFrame(Duration.minutes(30)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


        root.getChildren().addAll(top, condition, pressure, humidity, wind);
        root.setPadding(new Insets(0, 0, 0, 10));
        Scene scene = new Scene(root, 450, 150);
        primaryStage.setTitle("Weather app");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void connect() {
        try {
            final Document document = Jsoup.connect(url).get();
            String temp = document.getElementsByClass(divNameOfTemperature).toArray()[1].toString();
            temp = temp.substring(48, temp.length() - 7);
            temp = temp.replace("?", "-");
            temperature.setText(temp);
            city.setText(", " + document.getElementsByClass(divNameOfCity).text());
            condition.setText(document.getElementsByClass(divNameOfCondition).text());
            temp = document.getElementsByClass(divNameOfWind).text().split(":")[0];
            temp = "Ветер: " + temp.substring(0, temp.length() - 5);
            wind.setText(temp);
            temp = document.getElementsByClass(divNameOfHumidity).text().split(":")[0];
            temp = "Влажность: " + temp.substring(0, temp.length() - 9);
            humidity.setText(temp);
            temp = document.getElementsByClass(divNameOfPressure).text().split(":")[0];
            temp = "Давление: " + temp.substring(0, temp.length() - 8);
            pressure.setText(temp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

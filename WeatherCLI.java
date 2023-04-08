package com.project.myprojects.profile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WeatherCLI {
    public static final String url = "https://yandex.kz/pogoda/";
    public static final String divNameOfTemperature = "temp__value temp__value_with-unit";
    public static final String divNameOfCity = "title title_level_1 header-title__title";
    public static final String divNameOfCondition = "link__feelings fact__feelings";
    public static final String divNameOfWind = "term term_orient_v fact__wind-speed";
    public static final String divNameOfHumidity = "term term_orient_v fact__humidity";
    public static final String divNameOfPressure = "term term_orient_v fact__pressure";

    public static String city;
    public static String temperature;
    public static String condition;
    public static String wind;
    public static String humidity;
    public static String pressure;

    public static void main(String[] args) {
        connect();
        currentWeather();


    }
    public static void connect(){
        try{
            final Document document = Jsoup.connect(url).get();

            temperature = document.getElementsByClass(divNameOfTemperature).toArray()[1].toString();
            temperature = temperature.substring(48, temperature.length()-7);

            city = document.getElementsByClass(divNameOfCity).text();

            condition = document.getElementsByClass(divNameOfCondition).text();

            wind = document.getElementsByClass(divNameOfWind).text().split(":")[0];
            wind = "Ветер: " + wind.substring(0,wind.length()-5);

            humidity = document.getElementsByClass(divNameOfHumidity).text().split(":")[0];
            humidity = "Влажность: " + humidity.substring(0,humidity.length()-9);

            pressure = document.getElementsByClass(divNameOfPressure).text().split(":")[0];
            pressure = "Давление: " + pressure.substring(0,pressure.length()-8);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public static void currentWeather(){
        System.out.println("Текущая температура в городе " + city.replace(", ", "") + ": " + temperature);
        System.out.println(condition.replace(", ", ""));
        System.out.println(pressure);
        System.out.println(humidity);
        System.out.println(wind);
    }

}

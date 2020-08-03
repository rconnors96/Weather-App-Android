package com.ryanconnors.weatherapp;

public class WeatherData {
    private long[] coords;
    private Object[] weather;
    private String base;
    private Main main;
    private double visibility;
    private Wind wind;
    private Clouds clouds;
    private int dt;

    public long[] getCoords() {
        return coords;
    }

    public Object[] getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public double getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public int getDt() {
        return dt;
    }

    public System getSys() {
        return sys;
    }

    public int getTimezone() {
        return timezone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }

    private System sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;
}

class Main {
    double temp;
    double feels_like;
    double temp_min;
    double temp_max;
    double pressure;
    double humidity;
}

class Wind {
    double speed;
    double deg;
}

class Clouds {
    double all;
}

class System {
    double type;
    int id;
    String country;
    int sunrise;
    int sunset;
}
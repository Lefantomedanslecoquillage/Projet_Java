package fr.school.smartenergy.model;

public class WeatherData {
    private double temperature;
    private double humidity;
    private double windSpeed;
    private double precipitation;
    private int weatherCode;
    private String time;

    //Constructeur à écrire

    //Getter à écrire

    public WeatherData(double temperature, double humidity, double windSpeed, double precipitation, int weatherCode, String time) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.precipitation = precipitation;
        this.weatherCode = weatherCode;
        this.time = time;
    }

    public String getTime() {return time;}
    public double getTemperature() {return temperature;}
    public double getHumidity() {return humidity;}
    public double getWindSpeed() {return windSpeed;}
    public double getPrecipitation() {return precipitation;}
    public int getWeatherCode() {return weatherCode;}

    public String getWeatherDescription(){
        return switch(weatherCode){
            case 0 -> "Ciel dégagé";
            case 1 , 2 , 3 -> "Partiellement nuageux";
            case 45 , 48 -> "Brouillard";
            case 51 , 53 , 55 -> "Bruine";
            case 61, 63, 65 -> "Pluie";
            case 71, 73, 75 -> "Neige";
            case 95 -> "Orage";
            default -> "Météo inconnue";
        };
    }

    public String getEnergyAdvice(){
        if (temperature < 10){
            return "Température basse : surveiller la consommation de chauffage.";
        }

        if (temperature > 28){
            return "Température élevée : surveiller la consommation de climatisation.";
        }

        if (precipitation > 5){
            return "Précipitations importantes : possible hausse des besoins de chauffage.";
        }

        return "Conditions météorologiques normales.";
    }
}

package fr.school.smartenergy.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherDataTest {
    @Test
    void shouldReturnRainDescriptionWhenWeatherCodeIs61(){
        WeatherData weather = new WeatherData(15.0, 80.0, 10.0, 2.0, 61, "2026-05-28T14:00");
        assertEquals("Pluie", weather.getWeatherDescription());
    }

    @Test
    void shouldReturnHeatingAdviceWhenTemperatureIsLow(){
        WeatherData weather = new WeatherData(7.0, 70.0, 8.0, 0.0, 2, "2026-05-28T14:00");
        assertEquals("Température basse : surveiller la consommation de chauffage.", weather.getEnergyAdvice());
    }

    @Test
    void shouldReturnCoolingAdviceWhenTemperatureIsHigh(){
        WeatherData weather = new WeatherData(31.0, 45.0, 5.0, 0.0, 0, "2026-05-28T14:00");
        assertEquals("Température élevée : surveiller la consommation de climatisation.", weather.getEnergyAdvice());
    }
}
//
//String time,
//double temperature,
//double humidity,
//double windSpeed,
//double precipitation,
//int weatherCode

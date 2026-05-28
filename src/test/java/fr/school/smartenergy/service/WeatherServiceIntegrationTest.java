package fr.school.smartenergy.service;

import fr.school.smartenergy.exception.WeatherException;
import fr.school.smartenergy.model.WeatherData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceIntegrationTest {

    @Test
    void shouldFetchCurrentWeatherFromApi() throws WeatherException {
        WeatherService service = new WeatherService();

        WeatherData weather = service.getCurrentWeather(48.8245, 2.2743);

        assertNotNull(weather);
        assertNotNull(weather.getTime());
        assertTrue(weather.getTemperature() > -30 && weather.getTemperature() < 60);
        assertTrue(weather.getHumidity() >= 0 && weather.getHumidity() <= 100);
        assertTrue(weather.getWindSpeed() >= 0);
    }
}
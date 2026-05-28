package fr.school.smartenergy.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.school.smartenergy.exception.WeatherException;
import fr.school.smartenergy.model.WeatherData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class WeatherService {
    private static final String API_URL = "https://api.open-meteo.com/v1/forecast";
    private final HttpClient httpClient;

    public WeatherService() {
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }

    public WeatherData getCurrentWeather(double latitude, double longitude) throws WeatherException {
        try{
            URI uri = buildWeatherUri(latitude, longitude);

            HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofSeconds(15)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200){
                throw new WeatherException("Erreur de l'API météo. Code HTTP : " + response.statusCode());
            }
            return parseWeatherResponse(response.body());
        } catch (IOException e) {
            throw new WeatherException("Erreur réseau lors de la récupération de la météo.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WeatherException("La récupération de la météo a été interrompue.", e);
        } catch(RuntimeException e){
            throw new WeatherException("Réponse météo invalide ou impossible à analyser.", e);
        }
    }

    private URI buildWeatherUri(double latitude, double longitude){
        String url = API_URL + "?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m,precipitation,weather_code" + "&timezone=auto";
        return URI.create(url);
    }

    private WeatherData parseWeatherResponse(String jsonResponse){
        JsonObject root = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonObject current = root.getAsJsonObject("current");

        String time = current.get("time").getAsString();
        double temperature = current.get("temperature_2m").getAsDouble();
        double humidity = current.get("relative_humidity_2m").getAsDouble();
        double windSpeed = current.get("wind_speed_10m").getAsDouble();
        double precipitation = current.get("precipitation").getAsDouble();
        int weatherCode = current.get("weather_code").getAsInt();
        return new WeatherData(temperature, humidity, windSpeed, precipitation, weatherCode, time);
    }
}

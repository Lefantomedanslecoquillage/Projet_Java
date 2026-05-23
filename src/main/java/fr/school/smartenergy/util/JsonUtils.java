package fr.school.smartenergy.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class JsonUtils {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private JsonUtils() {}

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return GSON.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("JSON invalide", e);
        }
    }

    public static <T> T fromJson(String json, Type type) {
        try {
            return GSON.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("JSON invalide", e);
        }
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    /** Loads a JSON resource from the classpath and deserializes it. */
    public static <T> T loadResource(String resourcePath, Class<T> type) {
        try (InputStream is = JsonUtils.class.getResourceAsStream(resourcePath)) {
            if (is == null) return null;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                return fromJson(sb.toString(), type);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T loadResource(String resourcePath, Type type) {
        try (InputStream is = JsonUtils.class.getResourceAsStream(resourcePath)) {
            if (is == null) return null;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                return fromJson(sb.toString(), type);
            }
        } catch (Exception e) {
            return null;
        }
    }
}

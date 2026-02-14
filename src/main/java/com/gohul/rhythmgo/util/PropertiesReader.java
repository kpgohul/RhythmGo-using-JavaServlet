package com.gohul.rhythmgo.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input =
                     PropertiesReader.class.getClassLoader()
                             .getResourceAsStream("app.properties")) {

            if (input == null) {
                throw new RuntimeException("application.properties not found");
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load app.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}

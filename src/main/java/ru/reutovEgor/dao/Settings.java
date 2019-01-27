package ru.reutovEgor.dao;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    private static final Settings INSTANCE = new Settings();
    private static final Logger LOGGER = Logger.getLogger(Settings.class);
    private final Properties properties = new Properties();

    private Settings() {
        try {
            properties.load(new FileInputStream(this.getClass().getClassLoader().getResource("postgresql.properties").getFile()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    static Settings getInstance() {
        return INSTANCE;
    }

    String value(String key) {
        return this.properties.getProperty(key);
    }
}

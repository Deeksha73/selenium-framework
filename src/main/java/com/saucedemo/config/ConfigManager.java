package com.saucedemo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = LoadProperties();

    private ConfigManager(){

    }

    private static Properties LoadProperties(){
        Properties settings = new Properties();
//        Imp syntax
        try(InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config/config.properties")){
            if(input==null){
                throw new IllegalStateException(
                        "Configuration file not found: config/config.properties"
                );
            }
            settings.load(input);
            return settings;
        }
        catch (IOException exception){
            throw new IllegalStateException("Could not load configuration",exception);
        }
    }

    public static String BaseUrl(){
        return properties.getProperty(
                "base.url"
        );
    }

    public static int getTimeoutSeconds(){
        return Integer.parseInt(
                properties.getProperty("timeout.seconds")
        );
    }

    public static String getBrowser(){
        return System.getProperty("browser",properties.getProperty(
                "browser","chrome"
        ));
    }

    public static boolean isHeadless(){
        String value = System.getProperty(("headless"),
                properties.getProperty("headless","false")
        );
        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            throw new IllegalArgumentException("Headless should be either true or false");
        }
        return Boolean.parseBoolean(value);
    }

}

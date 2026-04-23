package com.miniradar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("miniradar.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Config config;

    public ConfigManager() {
        loadConfig();
    }

    private void loadConfig() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                config = GSON.fromJson(json, Config.class);
                if (config == null) {
                    config = new Config();
                }
            } else {
                config = new Config();
            }
        } catch (IOException e) {
            config = new Config();
        }
        validateConfig();
    }

    private void saveConfig() {
        try {
            Path parent = CONFIG_PATH.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            String json = GSON.toJson(config);
            Files.writeString(CONFIG_PATH, json);
        } catch (IOException e) {
        }
    }

    private void validateConfig() {
        if (config == null) {
            config = new Config();
        }
        if (config.detectionRadius < 16) {
            config.detectionRadius = 16;
            saveConfig();
        } else if (config.detectionRadius > 128) {
            config.detectionRadius = 128;
            saveConfig();
        }
    }

    public int getDetectionRadius() {
        return config.detectionRadius;
    }

    public void setDetectionRadius(int radius) {
        config.detectionRadius = radius;
        validateConfig();
        saveConfig();
    }

    public Config getConfig() {
        return config;
    }

    public static class Config {
        public int detectionRadius = 64;
    }
}

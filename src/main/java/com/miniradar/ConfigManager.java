package com.miniradar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigManager {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("miniradar.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    private Config config;
    private final ExecutorService executorService;
    
    public ConfigManager() {
        try {
            loadConfig();
            executorService = Executors.newSingleThreadExecutor();
            startFileWatcher();
        } catch (Exception e) {
            e.printStackTrace();
            config = new Config();
            executorService = Executors.newSingleThreadExecutor();
        }
    }
    
    private void startFileWatcher() {
        executorService.submit(() -> {
            try {
                if (CONFIG_PATH == null || CONFIG_PATH.getParent() == null) {
                    return;
                }
                
                WatchService watchService = FileSystems.getDefault().newWatchService();
                CONFIG_PATH.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
                
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        WatchKey key = watchService.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            try {
                                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                                    Path modifiedPath = (Path) event.context();
                                    if (modifiedPath != null && CONFIG_PATH.getFileName() != null && modifiedPath.equals(CONFIG_PATH.getFileName())) {
                                        // 配置文件被修改，重新加载
                                        loadConfig();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        key.reset();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    public void shutdown() {
        try {
            if (executorService != null) {
                executorService.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadConfig() {
        try {
            if (CONFIG_PATH == null) {
                config = new Config();
                return;
            }
            
            if (Files.exists(CONFIG_PATH)) {
                Reader reader = null;
                try {
                    reader = Files.newBufferedReader(CONFIG_PATH);
                    config = GSON.fromJson(reader, Config.class);
                    
                    // 验证配置值
                    validateConfig();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                // 创建默认配置
                config = new Config();
                saveConfig();
            }
        } catch (Exception e) {
            // 如果加载失败，使用默认配置
            e.printStackTrace();
            config = new Config();
            saveConfig();
        }
    }
    
    public void saveConfig() {
        try {
            if (CONFIG_PATH == null || config == null) {
                return;
            }
            
            // 确保配置目录存在
            try {
                Files.createDirectories(CONFIG_PATH.getParent());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            
            Writer writer = null;
            try {
                writer = Files.newBufferedWriter(CONFIG_PATH);
                GSON.toJson(config, writer);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void validateConfig() {
        try {
            if (config == null) {
                config = new Config();
                return;
            }
            
            // 确保检测半径在有效范围内
            if (config.detectionRadius < 16) {
                config.detectionRadius = 16;
            } else if (config.detectionRadius > 128) {
                config.detectionRadius = 128;
            }
        } catch (Exception e) {
            e.printStackTrace();
            config = new Config();
        }
    }
    
    public int getDetectionRadius() {
        try {
            if (config == null) {
                config = new Config();
            }
            return config.detectionRadius;
        } catch (Exception e) {
            e.printStackTrace();
            return 64; // 默认值
        }
    }
    
    public void setDetectionRadius(int radius) {
        try {
            if (config == null) {
                config = new Config();
            }
            config.detectionRadius = radius;
            validateConfig();
            saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }
    
    public static class Config {
        public int detectionRadius = 64; // 默认64格
    }
}

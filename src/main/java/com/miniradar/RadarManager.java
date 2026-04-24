package com.miniradar;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import java.util.List;

public class RadarManager
{
    private final MinecraftClient client;
    private List<Entity> detectedEntities;
    private final ConfigManager configManager;
    private long lastUpdateTime;
    private static final long UPDATE_INTERVAL_MS = 50;

    public RadarManager()
    {
        this.client = MinecraftClient.getInstance();
        this.detectedEntities = new ArrayList<>();
        this.configManager = new ConfigManager();
        this.lastUpdateTime = 0;
    }

    public void update()
    {
        try {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime < UPDATE_INTERVAL_MS) {
                return;
            }

            if (client == null || client.level == null || client.player == null) {
                detectedEntities.clear();
                return;
            }

            var level = client.level;
            var player = client.player;

            double maxDistance = configManager.getDetectionRadius();
            double maxDistanceSqr = maxDistance * maxDistance;
            Vec3d playerPos = player.getPos();

            detectedEntities = level.getEntities().getAll().stream()
                .filter(entity -> entity != null && entity != player)
                .filter(entity -> {
                    try {
                        double distSqr = entity.distanceToSqr(playerPos);
                        return distSqr <= maxDistanceSqr;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

            lastUpdateTime = currentTime;
        } catch (Exception e) {
            e.printStackTrace();
            detectedEntities.clear();
        }
    }

    public List<Entity> getEntitiesInRadarRange()
    {
        if (client == null || client.player == null) {
            return new ArrayList<>();
        }

        try {
            var player = client.player;
            Vec3d playerPos = player.getPos();
            float playerYaw = player.getYaw();
            double maxDistance = configManager.getDetectionRadius();

            return detectedEntities.stream()
                .filter(entity -> {
                    try {
                        Vec3d rotatedPos = rotateCoordinates(entity.getPos(), playerPos, playerYaw);
                        return Math.abs(rotatedPos.x) <= maxDistance && Math.abs(rotatedPos.z) <= maxDistance;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Vec3d rotateCoordinates(Vec3d entityPos, Vec3d playerPos, float playerYaw)
    {
        try {
            if (entityPos == null || playerPos == null) {
                return new Vec3d(0, 0, 0);
            }

            double dx = entityPos.x - playerPos.x;
            double dz = entityPos.z - playerPos.z;

            double angle = -playerYaw * Math.PI / 180;

            double rotatedX = dx * Math.cos(angle) - dz * Math.sin(angle);
            double rotatedZ = dx * Math.sin(angle) + dz * Math.cos(angle);

            return new Vec3d(rotatedX, entityPos.y - playerPos.y, rotatedZ);
        } catch (Exception e) {
            e.printStackTrace();
            return new Vec3d(0, 0, 0);
        }
    }

    public List<Entity> getDetectedEntities()
    {
        return detectedEntities != null ? detectedEntities : new ArrayList<>();
    }

    public void reloadConfig()
    {
        try {
            if (configManager != null) {
                configManager.loadConfig();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConfigManager getConfigManager()
    {
        return configManager;
    }
}
package com.miniradar;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;

public class RadarManager
{
    private final Minecraft client;
    private List<Entity> detectedEntities;
    private final ConfigManager configManager;
    private long lastUpdateTime;
    private static final long UPDATE_INTERVAL_MS = 50;

    public RadarManager()
    {
        this.client = Minecraft.getInstance();
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

            Player player = client.player;
            double maxDistance = configManager.getDetectionRadius();
            
            detectedEntities = new ArrayList<>(client.level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(maxDistance)));
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
            Player player = client.player;
            Vec3 playerPos = player.position();
            float playerYaw = player.getYRot();
            double maxDistance = configManager.getDetectionRadius();

            return detectedEntities.stream()
                .filter(entity -> {
                    try {
                        Vec3 rotatedPos = rotateCoordinates(entity.position(), playerPos, playerYaw);
                        return Math.abs(rotatedPos.x) <= maxDistance && Math.abs(rotatedPos.z) <= maxDistance;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Vec3 rotateCoordinates(Vec3 entityPos, Vec3 playerPos, float playerYaw)
    {
        try {
            if (entityPos == null || playerPos == null) {
                return new Vec3(0, 0, 0);
            }

            double dx = entityPos.x - playerPos.x;
            double dz = entityPos.z - playerPos.z;
            double angle = -playerYaw * Math.PI / 180;
            double rotatedX = dx * Math.cos(angle) - dz * Math.sin(angle);
            double rotatedZ = dx * Math.sin(angle) + dz * Math.cos(angle);

            return new Vec3(rotatedX, entityPos.y - playerPos.y, rotatedZ);
        } catch (Exception e) {
            e.printStackTrace();
            return new Vec3(0, 0, 0);
        }
    }

    public List<Entity> getDetectedEntities()
    {
        return detectedEntities != null ? detectedEntities : new ArrayList<>();
    }

    public ConfigManager getConfigManager()
    {
        return configManager;
    }
}
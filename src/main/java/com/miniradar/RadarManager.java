package com.miniradar;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RadarManager {
    private final MinecraftClient client;
    private List<Entity> detectedEntities;
    private final ConfigManager configManager;
    private long lastUpdateTime;
    private static final long UPDATE_INTERVAL_MS = 50; // 20次/秒
    
    public RadarManager(MinecraftClient client) {
        this.client = client;
        this.detectedEntities = new ArrayList<>();
        this.configManager = new ConfigManager();
        this.lastUpdateTime = 0;
    }
    
    public void update() {
        try {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime < UPDATE_INTERVAL_MS) {
                return; // 控制更新频率不超过20次/秒
            }
            
            if (client == null || client.world == null || client.player == null) {
                detectedEntities.clear();
                return;
            }
            
            ClientWorld world = client.world;
            PlayerEntity player = client.player;
            
            // 获取玩家周围已加载区块中的实体
            BlockPos playerPos = player.getBlockPos();
            int maxDistance = configManager.getDetectionRadius();
            
            // 只检测已加载区块内的实体，不主动加载未加载区域
            detectedEntities = world.getEntitiesByClass(Entity.class, player.getBoundingBox().expand(maxDistance), entity -> {
                try {
                    // 过滤掉玩家自己
                    if (entity == player) return false;
                    
                    // 过滤掉太远的实体
                    if (entity.squaredDistanceTo(player) > maxDistance * maxDistance) return false;
                    
                    // 过滤掉不在已加载区块中的实体
                    BlockPos entityPos = entity.getBlockPos();
                    if (!world.isChunkLoaded(entityPos.getX() >> 4, entityPos.getZ() >> 4)) return false;
                    
                    return true;
                } catch (Exception e) {
                    // 捕获单个实体处理的异常，避免影响整个更新过程
                    return false;
                }
            });
            
            lastUpdateTime = currentTime;
        } catch (Exception e) {
            // 捕获并记录任何更新过程中的异常
            e.printStackTrace();
            detectedEntities.clear();
        }
    }
    
    // 获取在雷达范围内的实体
    public List<Entity> getEntitiesInRadarRange() {
        if (client == null || client.player == null) {
            return new ArrayList<>();
        }
        
        try {
            PlayerEntity player = client.player;
            Vec3d playerPos = player.getPos();
            float playerYaw = player.getYaw();
            
            return detectedEntities.stream()
                .filter(entity -> {
                    try {
                        Vec3d rotatedPos = rotateCoordinates(entity.getPos(), playerPos, playerYaw);
                        // 检查是否在雷达范围内
                        double maxDistance = configManager.getDetectionRadius();
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
    
    public List<Entity> getDetectedEntities() {
        return detectedEntities != null ? detectedEntities : new ArrayList<>();
    }
    
    // 坐标旋转算法，确保视角同步
    public Vec3d rotateCoordinates(Vec3d entityPos, Vec3d playerPos, float playerYaw) {
        try {
            if (entityPos == null || playerPos == null) {
                return new Vec3d(0, 0, 0);
            }
            
            // 计算实体相对于玩家的位置
            double dx = entityPos.x - playerPos.x;
            double dz = entityPos.z - playerPos.z;
            
            // 计算旋转角度（弧度）
            double angle = Math.toRadians(-playerYaw);
            
            // 应用旋转
            double rotatedX = dx * Math.cos(angle) - dz * Math.sin(angle);
            double rotatedZ = dx * Math.sin(angle) + dz * Math.cos(angle);
            
            return new Vec3d(rotatedX, entityPos.y - playerPos.y, rotatedZ);
        } catch (Exception e) {
            e.printStackTrace();
            return new Vec3d(0, 0, 0);
        }
    }
    
    // 获取旋转后的实体坐标，用于雷达显示
    public List<Vec3d> getRotatedEntityCoordinates() {
        if (client == null || client.player == null) {
            return new ArrayList<>();
        }
        
        try {
            PlayerEntity player = client.player;
            Vec3d playerPos = player.getPos();
            float playerYaw = player.getYaw();
            
            return detectedEntities.stream()
                .map(entity -> {
                    try {
                        return rotateCoordinates(entity.getPos(), playerPos, playerYaw);
                    } catch (Exception e) {
                        return new Vec3d(0, 0, 0);
                    }
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // 实体过滤方法，可以根据需要添加更多过滤条件
    public List<Entity> filterEntitiesByType(Class<? extends Entity> entityClass) {
        try {
            if (entityClass == null || detectedEntities == null) {
                return new ArrayList<>();
            }
            
            return detectedEntities.stream()
                .filter(entityClass::isInstance)
                .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // 过滤出敌对生物
    public List<LivingEntity> getHostileEntities() {
        try {
            if (detectedEntities == null) {
                return new ArrayList<>();
            }
            
            return detectedEntities.stream()
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .filter(entity -> {
                    try {
                        return entity.isHostile();
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
    
    // 过滤出友好生物
    public List<LivingEntity> getFriendlyEntities() {
        try {
            if (detectedEntities == null) {
                return new ArrayList<>();
            }
            
            return detectedEntities.stream()
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .filter(entity -> {
                    try {
                        return !entity.isHostile();
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
    
    // 重新加载配置文件
    public void reloadConfig() {
        try {
            if (configManager != null) {
                configManager.loadConfig();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 获取配置管理器
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
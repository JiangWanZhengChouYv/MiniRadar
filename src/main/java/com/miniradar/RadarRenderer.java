package com.miniradar;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemEntity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class RadarRenderer implements HudRenderCallback {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final RadarManager radarManager;
    
    private static final int RADAR_SIZE = 100;
    private static final int RADAR_CENTER = RADAR_SIZE / 2;
    private static final int MAX_DISTANCE = 100;
    
    public RadarRenderer(RadarManager radarManager) {
        this.radarManager = radarManager;
    }
    
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        try {
            if (client == null || client.player == null || client.world == null || radarManager == null) return;
            
            // 每帧更新雷达数据
            radarManager.update();
            
            // 绘制雷达背景
            drawRadarBackground(drawContext);
            
            // 绘制玩家位置
            drawPlayerMarker(drawContext);
            
            // 绘制实体
            drawEntities(drawContext);
        } catch (Exception e) {
            // 捕获并记录任何渲染过程中的异常
            e.printStackTrace();
        }
    }
    
    private void drawRadarBackground(DrawContext drawContext) {
        try {
            if (client == null) return;
            
            // 绘制半透明黑色背景
            drawContext.fill(10, 10, 10 + RADAR_SIZE, 10 + RADAR_SIZE, 0x99000000);
            
            // 绘制白色边框
            drawContext.drawBorder(10, 10, RADAR_SIZE, RADAR_SIZE, 0xFFFFFFFF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void drawPlayerMarker(DrawContext drawContext) {
        try {
            if (client == null) return;
            
            // 绘制玩家位置（白色大点，直径8像素）
            int centerX = 10 + RADAR_CENTER;
            int centerY = 10 + RADAR_CENTER;
            
            // 黑色描边
            drawContext.fill(centerX - 4 - 1, centerY - 4 - 1, centerX + 4 + 1, centerY + 4 + 1, 0xFF000000);
            // 白色填充
            drawContext.fill(centerX - 4, centerY - 4, centerX + 4, centerY + 4, 0xFFFFFFFF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void drawEntities(DrawContext drawContext) {
        try {
            if (client == null || radarManager == null) return;
            
            // 使用优化后的方法获取在雷达范围内的实体
            List<Entity> entities = radarManager.getEntitiesInRadarRange();
            PlayerEntity player = client.player;
            
            if (player == null) return;
            
            Vec3d playerPos = player.getPos();
            float playerYaw = player.getYaw();
            int maxDistance = radarManager.getConfigManager().getDetectionRadius();
            
            // 预计算缩放因子，避免在循环中重复计算
            double scaleFactor = RADAR_CENTER / (double) maxDistance;
            
            for (Entity entity : entities) {
                try {
                    if (entity == null) continue;
                    
                    Vec3d rotatedPos = radarManager.rotateCoordinates(entity.getPos(), playerPos, playerYaw);
                    
                    // 计算雷达上的坐标
                    double radarX = (rotatedPos.x * scaleFactor) + RADAR_CENTER;
                    double radarZ = (rotatedPos.z * scaleFactor) + RADAR_CENTER;
                    
                    // 确保点在雷达范围内（双重检查，确保万无一失）
                    if (radarX >= 0 && radarX < RADAR_SIZE && radarZ >= 0 && radarZ < RADAR_SIZE) {
                        drawEntityMarker(drawContext, (int) radarX, (int) radarZ, entity);
                    }
                } catch (Exception e) {
                    // 捕获单个实体渲染的异常，避免影响其他实体的渲染
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void drawEntityMarker(DrawContext drawContext, int x, int y, Entity entity) {
        try {
            if (client == null || entity == null) return;
            
            int screenX = 10 + x;
            int screenY = 10 + y;
            
            int size = 0;
            int color = 0;
            
            if (entity instanceof PlayerEntity) {
                // 其他玩家：青色点，直径6像素
                size = 3;
                color = 0xFF00FFFF;
            } else if (entity instanceof LivingEntity livingEntity) {
                try {
                    if (livingEntity.isHostile()) {
                        // 敌对生物：红色点，直径5像素
                        size = 2;
                        color = 0xFFFF0000;
                    } else {
                        // 友好生物：绿色点，直径5像素
                        size = 2;
                        color = 0xFF00FF00;
                    }
                } catch (Exception e) {
                    // 捕获isHostile()调用的异常
                    size = 2;
                    color = 0xFF808080; // 灰色点表示未知类型
                }
            } else if (entity instanceof ItemEntity) {
                // 掉落物：黄色点，直径4像素
                size = 2;
                color = 0xFFFFFF00;
            }
            
            if (size > 0) {
                // 黑色描边
                drawContext.fill(screenX - size - 1, screenY - size - 1, screenX + size + 1, screenY + size + 1, 0xFF000000);
                // 颜色填充
                drawContext.fill(screenX - size, screenY - size, screenX + size, screenY + size, color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.miniradar;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemEntity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class RadarRenderer {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final RadarManager radarManager;
    
    private static final int RADAR_SIZE = 100;
    private static final int RADAR_CENTER = RADAR_SIZE / 2;
    
    public RadarRenderer(RadarManager radarManager) {
        this.radarManager = radarManager;
    }
    
    public void render(Object gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
        try {
            if (client == null || client.player == null || client.level == null || radarManager == null) return;
            
            radarManager.update();
            
            drawRadarBackground(guiGraphics);
            
            drawPlayerMarker(guiGraphics);
            
            drawEntities(guiGraphics);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void drawRadarBackground(GuiGraphics guiGraphics) {
        try {
            if (client == null) return;
            
            guiGraphics.fill(10, 10, 10 + RADAR_SIZE, 10 + RADAR_SIZE, 0x99000000);
            
            guiGraphics.drawOutline(10, 10, 10 + RADAR_SIZE, 10 + RADAR_SIZE, 0xFFFFFFFF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void drawPlayerMarker(GuiGraphics guiGraphics) {
        try {
            if (client == null) return;
            
            int centerX = 10 + RADAR_CENTER;
            int centerY = 10 + RADAR_CENTER;
            
            guiGraphics.fill(centerX - 4 - 1, centerY - 4 - 1, centerX + 4 + 1, centerY + 4 + 1, 0xFF000000);
            
            guiGraphics.fill(centerX - 4, centerY - 4, centerX + 4, centerY + 4, 0xFFFFFFFF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void drawEntities(GuiGraphics guiGraphics) {
        try {
            if (client == null || radarManager == null) return;
            
            List<Entity> entities = radarManager.getEntitiesInRadarRange();
            PlayerEntity player = client.player;
            
            if (player == null) return;
            
            Vec3d playerPos = player.getPosition(0);
            float playerYaw = player.getYRot();
            int maxDistance = radarManager.getConfigManager().getDetectionRadius();
            
            double scaleFactor = RADAR_CENTER / (double) maxDistance;
            
            for (Entity entity : entities) {
                try {
                    if (entity == null) continue;
                    
                    Vec3d rotatedPos = radarManager.rotateCoordinates(entity.getPosition(0), playerPos, playerYaw);
                    
                    double radarX = (rotatedPos.x * scaleFactor) + RADAR_CENTER;
                    double radarZ = (rotatedPos.z * scaleFactor) + RADAR_CENTER;
                    
                    if (radarX >= 0 && radarX < RADAR_SIZE && radarZ >= 0 && radarZ < RADAR_SIZE) {
                        drawEntityMarker(guiGraphics, (int) radarX, (int) radarZ, entity);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void drawEntityMarker(GuiGraphics guiGraphics, int x, int y, Entity entity) {
        try {
            if (client == null || entity == null) return;
            
            int screenX = 10 + x;
            int screenY = 10 + y;
            
            int size = 0;
            int color = 0;
            
            if (entity instanceof PlayerEntity) {
                size = 3;
                color = 0xFF00FFFF;
            } else if (entity instanceof LivingEntity livingEntity) {
                try {
                    if (livingEntity.isHostile()) {
                        size = 2;
                        color = 0xFFFF0000;
                    } else {
                        size = 2;
                        color = 0xFF00FF00;
                    }
                } catch (Exception e) {
                    size = 2;
                    color = 0xFF808080;
                }
            } else if (entity instanceof ItemEntity) {
                size = 2;
                color = 0xFFFFFF00;
            }
            
            if (size > 0) {
                guiGraphics.fill(screenX - size - 1, screenY - size - 1, screenX + size + 1, screenY + size + 1, 0xFF000000);
                
                guiGraphics.fill(screenX - size, screenY - size, screenX + size, screenY + size, color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
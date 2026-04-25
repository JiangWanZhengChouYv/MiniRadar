package com.miniradar;

import net.minecraft.client.gui.GuiGraphics;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.phys.Vec3;


import java.util.List;

public class RadarRenderer
{
    private final Minecraft client = Minecraft.getInstance();
    private final RadarManager radarManager;

    private static final int RADAR_SIZE = 100;
    private static final int RADAR_CENTER = 50;
    private static final int RADAR_OFFSET = 10;

    public RadarRenderer(RadarManager radarManager)
    {
        this.radarManager = radarManager;
    }

    public void render(GuiGraphics guiGraphics, float partialTick) {
    try {
        if (client == null || client.player == null || client.level == null || radarManager == null) {
            return;
        }

        radarManager.update();

        // 只改这里：poseStack → guiGraphics
        drawRadarBackground(guiGraphics);
        drawPlayerMarker(guiGraphics);
        drawEntities(guiGraphics, partialTick);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void drawRadarBackground(GuiGraphics guiGraphics) {
        guiGraphics.fill(RADAR_OFFSET, RADAR_OFFSET, RADAR_OFFSET + RADAR_SIZE, RADAR_OFFSET + RADAR_SIZE, 0x99000000);
        guiGraphics.fill(RADAR_OFFSET, RADAR_OFFSET, RADAR_OFFSET + RADAR_SIZE, RADAR_OFFSET+1, 0xFFFFFFFF);
        guiGraphics.fill(RADAR_OFFSET, RADAR_OFFSET, RADAR_OFFSET+1, RADAR_OFFSET + RADAR_SIZE, 0xFFFFFFFF);
        guiGraphics.fill(RADAR_OFFSET + RADAR_SIZE-1, RADAR_OFFSET, RADAR_OFFSET + RADAR_SIZE, RADAR_OFFSET + RADAR_SIZE, 0xFFFFFFFF);
        guiGraphics.fill(RADAR_OFFSET, RADAR_OFFSET + RADAR_SIZE-1, RADAR_OFFSET + RADAR_SIZE, RADAR_OFFSET + RADAR_SIZE, 0xFFFFFFFF);
    }

    private void drawPlayerMarker(GuiGraphics guiGraphics) {
        int centerX = RADAR_OFFSET + RADAR_CENTER;
        int centerY = RADAR_OFFSET + RADAR_CENTER;
        int size = 4;

        guiGraphics.fill(centerX - size - 1, centerY - size - 1, centerX + size + 1, centerY + size + 1, 0xFF000000);
        guiGraphics.fill(centerX - size, centerY - size, centerX + size, centerY + size, 0xFFFFFFFF);
    }

    private void drawEntities(GuiGraphics guiGraphics, float partialTick) {
        if (client == null || radarManager == null) {
            return;
        }

        List<Entity> entities = radarManager.getEntitiesInRadarRange();
        Player player = client.player;

        if (player == null) {
            return;
        }

        Vec3 playerPos = player.getPosition(partialTick);
        float playerYaw = player.getYRot();
        int maxDistance = radarManager.getConfigManager().getDetectionRadius();

        double scaleFactor = RADAR_CENTER / (double) maxDistance;

        for (Entity entity : entities) {
            if (entity == null) {
                continue;
            }

            Vec3 rotatedPos = radarManager.rotateCoordinates(entity.getPosition(partialTick), playerPos, playerYaw);

            double radarX = (rotatedPos.x * scaleFactor) + RADAR_CENTER;
            double radarZ = (rotatedPos.z * scaleFactor) + RADAR_CENTER;

            if (radarX >= 0 && radarX < RADAR_SIZE && radarZ >= 0 && radarZ < RADAR_SIZE) {
                drawEntityMarker(guiGraphics, (int) radarX, (int) radarZ, entity);
            }
        }
    }

    private void drawEntityMarker(GuiGraphics guiGraphics, int x, int y, Entity entity) {
        int screenX = RADAR_OFFSET + x;
        int screenY = RADAR_OFFSET + y;
        int size = 2;

        int color = getEntityColor(entity);

        guiGraphics.fill(screenX - size - 1, screenY - size - 1, screenX + size + 1, screenY + size + 1, 0xFF000000);  
        guiGraphics.fill(screenX - size, screenY - size, screenX + size, screenY + size, color);
    }

    private int getEntityColor(Entity entity) {
        if (entity instanceof Player) {
            return 0xFF00FFFF;
        }

        if (entity instanceof ItemEntity) {
            return 0xFFFFFF00;
        }

        if (entity instanceof LivingEntity livingEntity) {
            if (entity instanceof Creeper || entity instanceof Zombie || entity instanceof Skeleton || entity instanceof Spider || entity instanceof Enderman) {
                return 0xFFFF0000;
            }

            return 0xFF00FF00;
        }

        return 0xFF808080;
    }
}
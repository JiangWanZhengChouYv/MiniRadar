package com.miniradar;

import net.minecraft.client.gui.GuiGraphics;        // 26.1 新渲染上下文
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;                    // 替代具体怪物类
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.DeltaTracker;              // 新增，替代浮点 partialTick

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

    // 方法签名改为 GuiGraphicsExtractor + DeltaTracker
    public void render(GuiGraphics graphics, DeltaTracker delta) {
        try {
            if (client == null || client.player == null || client.level == null || radarManager == null) {
                return;
            }

            radarManager.update();

            // 从 DeltaTracker 中提取部分刻时间
            float partialTick = delta.getGameTimeDeltaPartialTick(false);

            drawRadarBackground(graphics);
            drawPlayerMarker(graphics);
            drawEntities(graphics, partialTick);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 以下所有方法参数由 GuiGraphics 改为 GuiGraphicsExtractor
    private void drawRadarBackground(GuiGraphicsExtractor graphics) {
        graphics.fill(RADAR_OFFSET, RADAR_OFFSET, RADAR_OFFSET + RADAR_SIZE, RADAR_OFFSET + RADAR_SIZE, 0x99000000);
        graphics.fill(RADAR_OFFSET, RADAR_OFFSET, RADAR_OFFSET + RADAR_SIZE, RADAR_OFFSET+1, 0xFFFFFFFF);
        graphics.fill(RADAR_OFFSET, RADAR_OFFSET, RADAR_OFFSET+1, RADAR_OFFSET + RADAR_SIZE, 0xFFFFFFFF);
        graphics.fill(RADAR_OFFSET + RADAR_SIZE-1, RADAR_OFFSET, RADAR_OFFSET + RADAR_SIZE, RADAR_OFFSET + RADAR_SIZE, 0xFFFFFFFF);
        graphics.fill(RADAR_OFFSET, RADAR_OFFSET + RADAR_SIZE-1, RADAR_OFFSET + RADAR_SIZE, RADAR_OFFSET + RADAR_SIZE, 0xFFFFFFFF);
    }

    private void drawPlayerMarker(GuiGraphicsExtractor graphics) {
        int centerX = RADAR_OFFSET + RADAR_CENTER;
        int centerY = RADAR_OFFSET + RADAR_CENTER;
        int size = 4;

        graphics.fill(centerX - size - 1, centerY - size - 1, centerX + size + 1, centerY + size + 1, 0xFF000000);
        graphics.fill(centerX - size, centerY - size, centerX + size, centerY + size, 0xFFFFFFFF);
    }

    private void drawEntities(GuiGraphicsExtractor graphics, float partialTick) {
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
                drawEntityMarker(graphics, (int) radarX, (int) radarZ, entity);
            }
        }
    }

    private void drawEntityMarker(GuiGraphicsExtractor graphics, int x, int y, Entity entity) {
        int screenX = RADAR_OFFSET + x;
        int screenY = RADAR_OFFSET + y;
        int size = 2;

        int color = getEntityColor(entity);

        graphics.fill(screenX - size - 1, screenY - size - 1, screenX + size + 1, screenY + size + 1, 0xFF000000);  
        graphics.fill(screenX - size, screenY - size, screenX + size, screenY + size, color);
    }

    private int getEntityColor(Entity entity) {
        if (entity instanceof Player) {
            return 0xFF00FFFF;
        }

        if (entity instanceof ItemEntity) {
            return 0xFFFFFF00;
        }

        if (entity instanceof LivingEntity) {
            // 26.1 中不再有具体怪物类，改用 EntityType 判断
            EntityType<?> type = entity.getType();
            if (type == EntityType.CREEPER
                || type == EntityType.ZOMBIE
                || type == EntityType.SKELETON
                || type == EntityType.SPIDER
                || type == EntityType.ENDERMAN) {
                return 0xFFFF0000;
            }

            return 0xFF00FF00;
        }

        return 0xFF808080;
    }
}
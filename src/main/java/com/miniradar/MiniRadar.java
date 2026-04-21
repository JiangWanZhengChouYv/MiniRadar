package com.miniradar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class MiniRadar implements ClientModInitializer {
    public static RadarManager radarManager;
    
    @Override
    public void onInitializeClient() {
        try {
            // 初始化RadarManager，确保MinecraftClient实例不为空
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null) {
                radarManager = new RadarManager(client);
                
                // 注册HudRenderCallback，确保radarManager不为空
                if (radarManager != null) {
                    HudRenderCallback.EVENT.register(new RadarRenderer(radarManager));
                }
            }
        } catch (Exception e) {
            // 捕获并记录任何初始化过程中的异常
            e.printStackTrace();
        }
    }
}
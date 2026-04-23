package com.miniradar;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.MinecraftClient;

@Mod("miniradar")
public class MiniRadar {
    public static RadarManager radarManager;
    
    public MiniRadar(IEventBus modEventBus) {
        try {
            modEventBus.addListener(this::onClientSetup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void onClientSetup(FMLClientSetupEvent event) {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null) {
                radarManager = new RadarManager(client);
                if (radarManager != null) {
                    net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent.register(this::registerOverlays);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void registerOverlays(net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent event) {
        try {
            event.registerAboveAll("miniradar", (gui, poseStack, partialTick, width, height) -> {
                if (radarManager != null) {
                    new RadarRenderer(radarManager).render(gui, poseStack, partialTick, width, height);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
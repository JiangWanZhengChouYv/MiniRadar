package com.miniradar;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;
import net.neoforged.neoforge.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = MiniRadar.MOD_ID, dist = Dist.CLIENT)
public class MiniRadar
{
    public static final String MOD_ID = "miniradar";

    public static ConfigManager configManager;
    public static RadarManager radarManager;
    public static RadarRenderer radarRenderer;

    @SubscribeEvent
    public static void onClientSetup(ClientSetupEvent event)
    {
        configManager = new ConfigManager();
        radarManager = new RadarManager();
        radarRenderer = new RadarRenderer();
        NeoForge.EVENT_BUS.register(MiniRadar.class);
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event)
    {
        event.registerAboveAll(MiniRadar.MOD_ID, (gui, poseStack, partialTick, width, height) -> {
            if (radarManager != null && radarRenderer != null) {
                radarManager.update();
                radarRenderer.render(gui, poseStack, partialTick, width, height);
            }
        });
    }
}

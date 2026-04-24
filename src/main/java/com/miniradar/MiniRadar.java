package com.miniradar;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = MiniRadar.MOD_ID, dist = Dist.CLIENT)
public class MiniRadar
{
    public static final String MOD_ID = "miniradar";

    public static ConfigManager configManager;
    public static RadarManager radarManager;
    public static RadarRenderer radarRenderer;

    public MiniRadar() {
        configManager = new ConfigManager();
        radarManager = new RadarManager();
        radarRenderer = new RadarRenderer(radarManager);

        NeoForge.EVENT_BUS.addListener(this::onClientSetup);
        NeoForge.EVENT_BUS.addListener(this::onRegisterGuiLayers);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {});
    }

    public void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(MOD_ID, radarRenderer::render);
    }
}
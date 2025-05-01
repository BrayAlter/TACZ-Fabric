package com.tacz.guns.compat.zoomify;

import dev.isxander.zoomify.Zoomify;
import net.fabricmc.loader.api.FabricLoader;

public class ZoomifyCompat {

    private static final String MOD_ID = "zoomify";
    private static boolean INSTALLED = false;

    public static void init() {
        INSTALLED = FabricLoader.getInstance().isModLoaded(MOD_ID);
    }

    public static double getFov(double fov, float tickDelta) {
        if (INSTALLED) {
            try {
                return fov / Zoomify.getZoomDivisor(tickDelta);
            } catch (Exception e) {
                System.err.println("Error while getting Zoomify zoom divisor: " + e.getMessage());
                return fov;
            }
        }
        return fov;
    }
}
package com.tacz.guns.compat.mkb;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;

/**
 * Compatibility layer for Modern Keyboard Binding (MKB) mod
 * Handles optional integration when MKB is available
 */
public class MkbCompat {
    private static final boolean MKB_LOADED = FabricLoader.getInstance().isModLoaded("modernkeybinding");
    
    /**
     * Check if MKB mod is loaded
     */
    public static boolean isMkbLoaded() {
        return MKB_LOADED;
    }
    
    /**
     * Set key conflict context for a keybinding if MKB is available
     */
    public static void setKeyConflictContext(KeyBinding keyBinding) {
        if (!MKB_LOADED) {
            return;
        }
        
        try {
            // Use reflection to avoid compile-time dependency
            Class<?> iKeyBindingClass = Class.forName("committee.nova.mkb.api.IKeyBinding");
            Class<?> keyConflictContextClass = Class.forName("committee.nova.mkb.keybinding.KeyConflictContext");
            
            Object keyConflictContext = keyConflictContextClass.getField("IN_GAME").get(null);
            
            // Cast keyBinding to IKeyBinding and set context
            Object iKeyBinding = iKeyBindingClass.cast(keyBinding);
            iKeyBindingClass.getMethod("setKeyConflictContext", keyConflictContextClass)
                    .invoke(iKeyBinding, keyConflictContext);
                    
        } catch (Exception e) {
            // Silently ignore if MKB classes are not available
        }
    }
    
    /**
     * Set key modifier for a keybinding if MKB is available
     */
    public static void setKeyModifier(KeyBinding keyBinding, String modifierName) {
        if (!MKB_LOADED) {
            return;
        }
        
        try {
            // Use reflection to avoid compile-time dependency
            Class<?> iKeyBindingClass = Class.forName("committee.nova.mkb.api.IKeyBinding");
            Class<?> keyModifierClass = Class.forName("committee.nova.mkb.keybinding.KeyModifier");
            
            Object keyModifier = keyModifierClass.getField(modifierName).get(null);
            
            // Cast keyBinding to IKeyBinding and set modifier
            Object iKeyBinding = iKeyBindingClass.cast(keyBinding);
            
            // Set the keyModifierDefault field
            java.lang.reflect.Field field = iKeyBinding.getClass().getDeclaredField("keyModifierDefault");
            field.setAccessible(true);
            field.set(iKeyBinding, keyModifier);
            field.setAccessible(false);
            
        } catch (Exception e) {
            // Silently ignore if MKB classes are not available
        }
    }
    
    /**
     * Check if the active modifier matches the keybinding's modifier
     */
    public static boolean checkKeyModifier(KeyBinding keyBinding) {
        if (!MKB_LOADED) {
            return true; // If MKB is not loaded, always allow the key
        }
        
        try {
            // Use reflection to avoid compile-time dependency
            Class<?> iKeyBindingClass = Class.forName("committee.nova.mkb.api.IKeyBinding");
            Class<?> keyModifierClass = Class.forName("committee.nova.mkb.keybinding.KeyModifier");
            
            Object iKeyBinding = iKeyBindingClass.cast(keyBinding);
            Object keyModifier = iKeyBindingClass.getMethod("getKeyModifier").invoke(iKeyBinding);
            Object activeModifier = keyModifierClass.getMethod("getActiveModifier").invoke(null);
            
            return keyModifier.equals(activeModifier);
            
        } catch (Exception e) {
            // If MKB classes are not available, always allow the key
            return true;
        }
    }
}
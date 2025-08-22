package com.tacz.guns.api.mixin;

import net.minecraft.client.sound.SoundEngine;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;

// From Kilt
public interface ChannelAccessHandleInjection {
    void tacz$setPool(SoundEngine.RunMode pool);

    void tacz$setSoundInstance(SoundInstance instance);

    void tacz$setSoundEngine(SoundSystem engine);
}
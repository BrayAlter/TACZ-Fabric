package com.tacz.guns.mixin.client;

import com.tacz.guns.api.mixin.ChannelAccessHandleInjection;
import com.tacz.guns.client.event.PlayGunSoundEvent;
import com.tacz.guns.util.kilt.SoundConsumerStorage;
import net.minecraft.client.sound.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

// From kilt
@Mixin(Channel.SourceManager.class)
public abstract class ChannelAccessHandleMixin implements ChannelAccessHandleInjection {
    @Shadow
    @Nullable Source source;
    @Unique
    private SoundEngine.RunMode tacz$pool;
    @Unique
    private SoundSystem tacz$soundEngine;
    @Unique
    private SoundInstance tacz$soundInstance;

    @Override
    public void tacz$setPool(SoundEngine.RunMode pool) {
        this.tacz$pool = pool;
    }

    @Override
    public void tacz$setSoundEngine(SoundSystem engine) {
        this.tacz$soundEngine = engine;
    }

    @Override
    public void tacz$setSoundInstance(SoundInstance instance) {
        this.tacz$soundInstance = instance;
    }

    @Inject(method = "method_19737", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", shift = At.Shift.AFTER))
    private void tacz$callPlaySoundEvents(Consumer<Channel> consumer, CallbackInfo ci) {
        if (this.source != null && tacz$soundEngine != null && tacz$soundInstance != null && SoundConsumerStorage.soundConsumerChannels.remove(consumer)) {
            if (tacz$pool == SoundEngine.RunMode.STATIC) {
                PlayGunSoundEvent.onPlaySoundSource(tacz$soundInstance, this.source);
            }
        }
    }
}
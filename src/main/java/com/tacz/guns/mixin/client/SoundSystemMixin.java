package com.tacz.guns.mixin.client;

import com.tacz.guns.api.mixin.ChannelAccessHandleInjection;
import com.llamalad7.mixinextras.sugar.Local;
import com.tacz.guns.util.kilt.SoundConsumerStorage;
import net.minecraft.client.sound.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {
    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/Channel$SourceManager;run(Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER))
    private void tacz$prepareChannelInfo(SoundInstance soundInstance, CallbackInfo ci, @Local Channel.SourceManager channelHandle, @Local Sound sound) {
        var injection = ((ChannelAccessHandleInjection) channelHandle);

        if (sound.isStreamed())
            injection.tacz$setPool(SoundEngine.RunMode.STREAMING);
        else
            injection.tacz$setPool(SoundEngine.RunMode.STATIC);

        injection.tacz$setSoundInstance(soundInstance);
        injection.tacz$setSoundEngine((SoundSystem) (Object) this);
    }

    @ModifyArg(method = "method_19757", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/Channel$SourceManager;run(Ljava/util/function/Consumer;)V"))
    private static Consumer<Source> tacz$storeSourceConsumer(Consumer<Source> consumer) {
        SoundConsumerStorage.soundConsumerChannels.add(consumer);
        return consumer;
    }
}

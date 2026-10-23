package com.example.addon.modules;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;

public class AutoSpawn extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> distance = sgGeneral.add(new DoubleSetting.Builder()
        .name("distance")
        .description("Mesafe sınırı (X, Y, Z her yöne)")
        .defaultValue(10.0)
        .min(1.0)
        .sliderMax(30.0)
        .build()
    );

    private final Setting<String> command = sgGeneral.add(new StringSetting.Builder()
        .name("command")
        .description("Gönderilecek komut")
        .defaultValue("/spawn")
        .build()
    );

    private final Setting<Boolean> toggleOff = sgGeneral.add(new BoolSetting.Builder()
        .name("toggle-off")
        .description("Komut atılınca modül kapansın mı?")
        .defaultValue(true)
        .build()
    );

    public AutoSpawn() {
        super(Addon.CATEGORY, "auto-spawn", "10 blok yakınına biri gelirse /spawn atar.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.world == null) return;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || player.isSpectator()) continue;

            if (mc.player.distanceTo(player) <= distance.get()) {
                mc.player.networkHandler.sendChatMessage(command.get());
                info("Oyuncu algılandı! Komut atıldı: " + command.get());
                if (toggleOff.get()) toggle();
                break;
            }
        }
    }
}

package com.gabsavard.spokhud.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;

public final class HudUtil {
    private HudUtil() {}

    public static ArrayList<MobEffectCategory> getActiveEffectCategories() {
        ArrayList<MobEffectCategory> effectList = new ArrayList<>();

        var player = Minecraft.getInstance().player;
        if (player == null) return effectList;

        for (MobEffectInstance instance : player.getActiveEffects()) {
            if (!instance.showIcon()) continue;

            MobEffectCategory category = instance.getEffect().value().getCategory();

            if (!effectList.contains(category))
                effectList.add(category);
        }

        return effectList;
    }

    public static int calculateYPos(int y, ArrayList<MobEffectCategory> activeEffectCategories) {
        if (activeEffectCategories.isEmpty()) return y;

        if (activeEffectCategories.size() == 1) {
            if (activeEffectCategories.getFirst() == MobEffectCategory.BENEFICIAL) return y + 20;
            else return y;
        }

        if (activeEffectCategories.size() == 2 &&
                !activeEffectCategories.contains(MobEffectCategory.BENEFICIAL)) return y;

        return y + 45;
    }
}

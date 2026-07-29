package com.gabsavard.spokhud.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public final class BiomeHelper {
    public static String getPlayerBiomeId() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null || minecraft.level == null) {
            return "unknown";
        }

        BlockPos pos = minecraft.player.blockPosition();
        Holder<Biome> biome = minecraft.level.getBiome(pos);

        // Convert the biome registry key into a string such as "minecraft:old_growth_birch_forest".
        String biomeStr = biome.unwrapKey()
                .map(key -> key.identifier().toString())
                .orElse("unknown");

        return formatBiomeName(biomeStr);
    }

    private static String formatBiomeName(String biomeId) {
        if (biomeId == null || biomeId.equals("unknown")) {
            return "Unknown";
        }

        // Remove the namespace so "minecraft:deep_dark" becomes "deep_dark".
        String biomeName = biomeId.contains(":")
                ? biomeId.substring(biomeId.indexOf(":") + 1)
                : biomeId;

        String[] words = biomeName.split("_");
        StringBuilder formatted = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }

            formatted.append(Character.toUpperCase(word.charAt(0)));

            if (word.length() > 1) {
                formatted.append(word.substring(1));
            }

            formatted.append(" ");
        }

        return formatted.toString().trim();
    }
}
package com.gabsavard.spokhud.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ArmorHelper {
    public static ItemStack getHelmet() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return ItemStack.EMPTY;
        }

        return minecraft.player.getItemBySlot(EquipmentSlot.HEAD);
    }

    public static ItemStack getChest() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return ItemStack.EMPTY;
        }

        return minecraft.player.getItemBySlot(EquipmentSlot.CHEST);
    }

    public static ItemStack getLegs() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return ItemStack.EMPTY;
        }

        return minecraft.player.getItemBySlot(EquipmentSlot.LEGS);
    }

    public static ItemStack getFeet() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return ItemStack.EMPTY;
        }

        return minecraft.player.getItemBySlot(EquipmentSlot.FEET);
    }

    public static ItemStack getPrimaryHand() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return ItemStack.EMPTY;
        }

        return minecraft.player.getMainHandItem();
    }

    public static String getDurabilityPercent(ItemStack stack) {
        // Non-damageable items do not have meaningful durability to display.
        if (stack.isEmpty() || !stack.isDamageableItem()) {
            return "";
        }

        int maxDamage = stack.getMaxDamage();
        int currDamage = stack.getDamageValue();

        // Minecraft stores damage taken, so durability is max damage minus current damage.
        int durability = maxDamage - currDamage;
        int percent = (int) Math.floor(((double) durability / (double) maxDamage) * 100);

        return String.format("%d%%", percent);
    }

    public static int getDurabilityColor(ItemStack stack) {
        if (stack.isEmpty() || !stack.isDamageableItem()) {
            return 0xFFFFFFFF;
        }

        int maxDamage = stack.getMaxDamage();
        int damage = stack.getDamageValue();

        if (maxDamage <= 0) {
            return 0xFFFFFFFF;
        }

        double ratio = (double) (maxDamage - damage) / (double) maxDamage;

        // Higher durability is green, medium durability is yellow, and low durability is red.
        if (ratio >= 0.75) return 0xFF57FF6B;
        if (ratio >= 0.50) return 0xFFB6FF5C;
        if (ratio >= 0.25) return 0xFFFFE45C;
        if (ratio >= 0.10) return 0xFFFF9F3F;
        return 0xFFFF4D4D;
    }
}
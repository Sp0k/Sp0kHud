package com.gabsavard.spokhud.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.gui.Font;

public class Sp0kHUDClient implements ClientModInitializer {
	public static final String MOD_ID = "spokhud";

	@Override
	public void onInitializeClient() {
		HudElementRegistry.attachElementBefore(
				VanillaHudElements.CHAT,
				Identifier.fromNamespaceAndPath(MOD_ID, "my_hud"),
				Sp0kHUDClient::renderHud
		);
	}

	private static void renderLeftSide(Minecraft minecraft, GuiGraphicsExtractor graphics, int x, int y,
	                                   Sp0kHUDConfig config) {
		// Position
		if (config.showPosition) {
			int posX = minecraft.player.getBlockX();
			int posY = minecraft.player.getBlockY();
			int posZ = minecraft.player.getBlockZ();
			drawScaledText(
					graphics,
					minecraft.font,
					String.format("Position: %d, %d, %d", posX, posY, posZ),
					x,
					y,
					config.uiSize.scale,
					0xFFFFFFFF,
					true
			);
		}

		// Biome
		if (config.showBiome) {
			String biome = BiomeHelper.getPlayerBiomeId();
			int offset = config.showPosition ? (int)(12 * config.uiSize.scale) : 0;
			drawScaledText(
					graphics,
					minecraft.font,
					String.format("Biome: %s", biome),
					x,
					y + offset,
					config.uiSize.scale,
					0xFFFFFFFF,
					true
			);
		}
	}

	private static void renderCenter(Minecraft minecraft, GuiGraphicsExtractor graphics, int screenWidth, int y,
									 float scale) {
		// Cardinal Direction
		String cardinal = minecraft.player.getDirection().toString();
		String direction = switch (cardinal) {
			case "north" -> "N";
			case "south" -> "S";
			case "east" -> "E";
			case "west" -> "W";
			default -> "Null";
		};

		int textWidth = minecraft.font.width(direction);
		int scaledTextWidth = Math.round(textWidth * scale);

		int centerX = (screenWidth / 2) + scaledTextWidth;

		drawScaledText(
				graphics,
				minecraft.font,
				direction,
				centerX,
				y,
				scale,
				0xFFFFFFFF,
				true
		);

		// Cardinal Direction Lines
		int scaledLineLength = Math.round(24 * scale);
		int scaledLineGap = Math.round(6 * scale);
		int lineY = y + Math.round((minecraft.font.lineHeight * scale) / 2.0f);

		graphics.horizontalLine(
				centerX - scaledLineGap - scaledLineLength,
				centerX - scaledLineGap,
				lineY,
				0xFFFFFFFF
		);

		graphics.horizontalLine(
				centerX + scaledTextWidth + scaledLineGap,
				centerX + scaledTextWidth + scaledLineGap + scaledLineLength,
				lineY,
				0xFFFFFFFF
		);
	}

	private static void renderRightSide(Minecraft minecraft, GuiGraphicsExtractor graphics, int x,
	                                    int screenWidth, int y, float scale) {
		int iconSize = 16;
		int gap = 10;

		int scaledIconSize = Math.round(iconSize * scale);
		int scaledGap = Math.round(gap * scale);
		int slotSpacing = scaledIconSize + scaledGap;

		// Feet
		ItemStack feet = ArmorHelper.getFeet();
		int feetX = screenWidth - scaledIconSize - x;
		drawArmorSlot(minecraft, graphics, feet, feetX, y, scale);

		// Legs
		ItemStack legs = ArmorHelper.getLegs();
		int legsX = screenWidth - scaledIconSize - x - slotSpacing;
		drawArmorSlot(minecraft, graphics, legs, legsX, y, scale);

		// Chest
		ItemStack chest = ArmorHelper.getChest();
		int chestX = screenWidth - scaledIconSize - x - (slotSpacing * 2);
		drawArmorSlot(minecraft, graphics, chest, chestX, y, scale);

		// Helmet
		ItemStack helmet = ArmorHelper.getHelmet();
		int helmetX = screenWidth - scaledIconSize - x - (slotSpacing * 3);
		drawArmorSlot(minecraft, graphics, helmet, helmetX, y, scale);

		// Primary Hand
		ItemStack primaryHand = ArmorHelper.getPrimaryHand();
		if (primaryHand.isDamageableItem()) {
			int primaryX = screenWidth - scaledIconSize - x - (slotSpacing * 4);
			drawArmorSlot(minecraft, graphics, primaryHand, primaryX, y, scale);
		}

		// Offhand
		ItemStack offHand = ArmorHelper.getSecondaryHand();
		if (offHand.isDamageableItem()) {
			int offHandX = screenWidth - scaledIconSize - x - (slotSpacing * 5);
			drawArmorSlot(minecraft, graphics, offHand, offHandX, y, scale);
		}
	}

	private static boolean isF3Open(Minecraft minecraft) {
		return minecraft.getDebugOverlay().showDebugScreen();
	}

	private static void drawScaledText(
			GuiGraphicsExtractor graphics,
			Font font,
			String text,
			int x,
			int y,
			float scale,
			int color,
			boolean shadow
	) {
		graphics.pose().pushMatrix();

		try {
			graphics.pose().scale(scale, scale);

			graphics.text(
					font,
					text,
					Math.round(x / scale),
					Math.round(y / scale),
					color,
					shadow
			);
		} finally {
			graphics.pose().popMatrix();
		}
	}

	private static void drawScaledItem(
			GuiGraphicsExtractor graphics,
			ItemStack stack,
			int x,
			int y,
			float scale
	) {
		graphics.pose().pushMatrix();

		try {
			graphics.pose().scale(scale, scale);

			graphics.item(
					stack,
					Math.round(x / scale),
					Math.round(y / scale)
			);
		} finally {
			graphics.pose().popMatrix();
		}
	}

	private static void drawArmorSlot(
			Minecraft minecraft,
			GuiGraphicsExtractor graphics,
			ItemStack stack,
			int x,
			int y,
			float scale
	) {
		int iconSize = 16;
		int scaledIconSize = Math.round(iconSize * scale);

		drawScaledItem(graphics, stack, x, y, scale);

		if (stack.isDamaged()) {
			String durability = ArmorHelper.getDurabilityPercent(stack);

			int durabilityWidth = Math.round(minecraft.font.width(durability) * scale);
			int durabilityX = x + (scaledIconSize - durabilityWidth) / 2;
			int durabilityY = y + scaledIconSize;

			drawScaledText(
					graphics,
					minecraft.font,
					durability,
					durabilityX,
					durabilityY,
					scale,
					ArmorHelper.getDurabilityColor(stack),
					true
			);
		}
	}

	private static void renderHud(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
		Minecraft minecraft = Minecraft.getInstance();

		if (minecraft.player == null || minecraft.level == null) return;

		Sp0kHUDConfig config = Sp0kHUDConfig.get();

		// Hide if F3 is open
		if (isF3Open(minecraft)) return;

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int x = config.hudX;
		int y = config.hudY;

		renderLeftSide(minecraft, graphics, x, y, config);

		if (config.showDirection) {
			renderCenter(minecraft, graphics, screenWidth, y, config.uiSize.scale);
		}

		if (config.showArmor) {
			// Shift armor icons down when potion effects are displayed.
			int rightY = !minecraft.player.getActiveEffects().isEmpty() ? y + 20 : y;
			renderRightSide(minecraft, graphics, x, screenWidth, rightY, config.uiSize.scale);
		}
	}
}
package com.gabsavard.spokhud.client;

import com.gabsavard.spokhud.Sp0kHUD;
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
	public static final String MOD_ID = Sp0kHUD.MOD_ID;

	private static final Identifier SLIME_CHUNK_ICON =
			Identifier.fromNamespaceAndPath(MOD_ID, "textures/gui/slime_icon.png");

	@Override
	public void onInitializeClient() {
		HudElementRegistry.attachElementBefore(
				VanillaHudElements.CHAT,
				Identifier.fromNamespaceAndPath(MOD_ID, "my_hud"),
				Sp0kHUDClient::renderHud
		);
	}

	private static void renderLeftSide(
			Minecraft minecraft,
			GuiGraphicsExtractor graphics,
			int x,
			int y,
			Sp0kHUDConfig config
	) {
		int currentY = y;
		int lineSpacing = Math.round(12 * config.uiSize.scale);

		// Position
		if (config.showPosition) {
			int posX = minecraft.player.getBlockX();
			int posY = minecraft.player.getBlockY();
			int posZ = minecraft.player.getBlockZ();

			drawScaledText(
					graphics,
					minecraft.font,
					String.format(
							"Position: %d, %d, %d",
							posX,
							posY,
							posZ
					),
					x,
					currentY,
					config.uiSize.scale,
					0xFFFFFFFF,
					true
			);

			currentY += lineSpacing;
		}

		// Biome
		if (config.showBiome) {
			String biomeText = String.format(
					"Biome: %s",
					BiomeHelper.getPlayerBiomeId()
			);

			drawScaledText(
					graphics,
					minecraft.font,
					biomeText,
					x,
					currentY,
					config.uiSize.scale,
					0xFFFFFFFF,
					true
			);

			currentY += lineSpacing;
		}

		// Dedicated icon row
		renderLocationIcons(
				minecraft,
				graphics,
				x,
				currentY,
				config
		);
	}

	private static void renderCenter(Minecraft minecraft, GuiGraphicsExtractor graphics, int screenWidth, int y,
									 Sp0kHUDConfig config) {
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
		int scaledTextWidth = Math.round(textWidth * config.uiSize.scale);

		int centerX = (screenWidth / 2) + scaledTextWidth;

		drawScaledText(
				graphics,
				minecraft.font,
				direction,
				centerX,
				y,
				config.uiSize.scale,
				0xFFFFFFFF,
				true
		);

		// Cardinal Direction Lines
		if (config.showDirectionLines) {
			int scaledLineLength = Math.round(24 * config.uiSize.scale);
			int scaledLineGap = Math.round(6 * config.uiSize.scale);
			int lineY = y + Math.round((minecraft.font.lineHeight * config.uiSize.scale) / 2.0f);

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
	}

	private static void renderRightSide(Minecraft minecraft, GuiGraphicsExtractor graphics, int x,
	                                    int screenWidth, int y, Sp0kHUDConfig config) {
		int iconSize = 16;
		int gap = 10;

		int scaledIconSize = Math.round(iconSize * config.uiSize.scale);
		int scaledGap = Math.round(gap * config.uiSize.scale);
		int slotSpacing = scaledIconSize + scaledGap;

		if (config.showArmor) {
			// Feet
			ItemStack feet = ArmorHelper.getFeet();
			int feetX = screenWidth - scaledIconSize - x;
			drawArmorSlot(minecraft, graphics, feet, feetX, y, config);

			// Legs
			ItemStack legs = ArmorHelper.getLegs();
			int legsX = screenWidth - scaledIconSize - x - slotSpacing;
			drawArmorSlot(minecraft, graphics, legs, legsX, y, config);

			// Chest
			ItemStack chest = ArmorHelper.getChest();
			int chestX = screenWidth - scaledIconSize - x - (slotSpacing * 2);
			drawArmorSlot(minecraft, graphics, chest, chestX, y, config);

			// Helmet
			ItemStack helmet = ArmorHelper.getHelmet();
			int helmetX = screenWidth - scaledIconSize - x - (slotSpacing * 3);
			drawArmorSlot(minecraft, graphics, helmet, helmetX, y, config);
		}

		if (config.showTools) {
			int spacingAmount = config.showArmor ? 4 : 0;

			// Primary Hand
			ItemStack primaryHand = ArmorHelper.getPrimaryHand();
			if (primaryHand.isDamageableItem()) {
				int primaryX = screenWidth - scaledIconSize - x - (slotSpacing * spacingAmount);
				drawArmorSlot(minecraft, graphics, primaryHand, primaryX, y, config);
			}

			spacingAmount++;

			// Offhand
			ItemStack offHand = ArmorHelper.getSecondaryHand();
			if (offHand.isDamageableItem()) {
				int offHandX = screenWidth - scaledIconSize - x - (slotSpacing * spacingAmount);
				drawArmorSlot(minecraft, graphics, offHand, offHandX, y, config);
			}
		}
	}

	private static boolean isF3Open(Minecraft minecraft) {
		return minecraft.gui.hud.getDebugOverlay().showDebugScreen();
	}

	private static String getDurabilityText(ItemStack stack, Sp0kHUDConfig config) {
		return switch (config.durationType) {
			case PERCENTAGE -> ArmorHelper.getDurabilityPercent(stack);
			case HITS -> ArmorHelper.getDurabilityHits(stack);
		};
	}

	private static void drawScaledText(GuiGraphicsExtractor graphics, Font font, String text, int x, int y, float scale,
			int color, boolean shadow) {
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

	private static void drawScaledItem(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y, float scale) {
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

	private static void drawTexture(
			GuiGraphicsExtractor graphics,
			Identifier texture,
			int x,
			int y,
			int size
	) {
		graphics.blit(
				texture,
				x,
				y,
				x + size,
				y + size,
				0.0F,
				1.0F,
				0.0F,
				1.0F
		);
	}

	private static void renderLocationIcons(
			Minecraft minecraft,
			GuiGraphicsExtractor graphics,
			int x,
			int y,
			Sp0kHUDConfig config
	) {
		int iconSize = Math.round(9 * config.uiSize.scale);
		int iconGap = Math.round(3 * config.uiSize.scale);

		int currentIconX = x;

		// Slime chunk icon
		if (config.showSlimeChunkIndicator
				&& SlimeChunkHelper.isPlayerInSlimeChunk(minecraft)) {

			drawTexture(
					graphics,
					SLIME_CHUNK_ICON,
					currentIconX,
					y,
					iconSize
			);

			currentIconX += iconSize + iconGap;
		}
	}

	private static void drawArmorSlot(Minecraft minecraft, GuiGraphicsExtractor graphics, ItemStack stack, int x, int y,
			Sp0kHUDConfig config) {
		int iconSize = 16;
		int scaledIconSize = Math.round(iconSize * config.uiSize.scale);

		drawScaledItem(graphics, stack, x, y, config.uiSize.scale);

		if (stack.isDamaged()) {
			String durability = getDurabilityText(stack, config);

			int durabilityWidth = Math.round(minecraft.font.width(durability) * config.uiSize.scale);
			int durabilityX = x + (scaledIconSize - durabilityWidth) / 2;
			int durabilityY = y + scaledIconSize;

			drawScaledText(
					graphics,
					minecraft.font,
					durability,
					durabilityX,
					durabilityY,
					config.uiSize.scale,
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
		if (!config.showHudInF3 && isF3Open(minecraft)) return;

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int x = config.hudX;
		int y = config.hudY;

		if (config.showLocationInfo) {
			renderLeftSide(minecraft, graphics, x, y, config);
		}

		if (config.showDirection) {
			renderCenter(minecraft, graphics, screenWidth, y, config);
		}

		if (config.showEquipmentDisplay) {
			// Shift armor icons down when potion effects are displayed.
			int rightY = HudUtil.calculateYPos(y, HudUtil.getActiveEffectCategories());
			renderRightSide(minecraft, graphics, x, screenWidth, rightY, config);
		}
	}
}
package com.gabsavard.spokhud.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

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

	private static void renderLeftSide(Minecraft minecraft, GuiGraphicsExtractor graphics, int x, int y) {
		// Position
		int posX = minecraft.player.getBlockX();
		int posY = minecraft.player.getBlockY();
		int posZ = minecraft.player.getBlockZ();
		graphics.text(
				minecraft.font,
				String.format("Position: %d, %d, %d", posX, posY, posZ),
				x,
				y,
				0xFFFFFFFF,
				true
		);

		// Biome
		String biome = BiomeHelper.getPlayerBiomeId();
		graphics.text(
				minecraft.font,
				String.format("Biome: %s", biome),
				x,
				y + 12,
				0xFFFFFFFF,
				true
		);
	}

	private static void renderCenter(Minecraft minecraft, GuiGraphicsExtractor graphics, int screenWidth, int y) {
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
		int centerX = (screenWidth - textWidth) / 2;

		graphics.text(
				minecraft.font,
				direction,
				centerX,
				y,
				0xFFFFFFFF,
				true
		);

		// Cardinal Direction Lines
		graphics.horizontalLine(
				centerX - 30,
				centerX - 6,
				y + (minecraft.font.lineHeight / 2),
				0xFFFFFFFF
		);

		graphics.horizontalLine(
				centerX + textWidth + 6,
				centerX + textWidth + 30,
				y + (minecraft.font.lineHeight / 2),
				0xFFFFFFFF
		);
	}

	private static void renderRightSide(Minecraft minecraft, GuiGraphicsExtractor graphics, int x,
										int screenWidth, int y) {
		int iconWidth = 15;
		int iconMargin = iconWidth + 10;

		// Feet
		ItemStack boots = ArmorHelper.getFeet();
		int bootX = screenWidth - iconWidth - x;
		graphics.item(boots, bootX, y);

		if (boots.isDamaged()) {
			String bootsDurability = ArmorHelper.getDurabilityPercent(boots);
			int bootsDurWidth = minecraft.font.width(bootsDurability);
			int bootsDurX = bootX + (iconWidth - bootsDurWidth) / 2;
			graphics.text(
					minecraft.font,
					bootsDurability,
					bootsDurX,
					y + 16,
					ArmorHelper.getDurabilityColor(boots),
					true
			);
		}

		// Legs
		ItemStack legs = ArmorHelper.getLegs();
		int legsX = screenWidth - iconWidth - iconWidth - x;
		graphics.item(legs, legsX, y);

		if (legs.isDamaged()) {
			String legsDurability = ArmorHelper.getDurabilityPercent(legs);
			int legsDurWidth = minecraft.font.width(legsDurability);
			int legsDurX = legsX + (iconWidth - legsDurWidth) / 2;
			graphics.text(
					minecraft.font,
					legsDurability,
					legsDurX,
					y + 16,
					ArmorHelper.getDurabilityColor(legs),
					true
			);
		}

		// Chest
		ItemStack chest = ArmorHelper.getChest();
		int chestX = screenWidth - (iconMargin * 2) - iconWidth - x;
		graphics.item(chest, chestX, y);

		if (chest.isDamaged()) {
			String chestDurability = ArmorHelper.getDurabilityPercent(chest);
			int chestDurWidth = minecraft.font.width(chestDurability);
			int chestDurX = chestX + (iconWidth - chestDurWidth) / 2;
			graphics.text(
					minecraft.font,
					chestDurability,
					chestDurX,
					y + 16,
					ArmorHelper.getDurabilityColor(chest),
					true
			);
		}

		// Helmet
		ItemStack helmet = ArmorHelper.getHelmet();
		int helmetX = screenWidth - (iconMargin * 3) - iconWidth - x;
		graphics.item(helmet, helmetX, y);

		if (helmet.isDamaged()) {
			String helmetDurability = ArmorHelper.getDurabilityPercent(helmet);
			int helmetDurWidth = minecraft.font.width(helmetDurability);
			int helmetDurX = helmetX + (iconWidth - helmetDurWidth) / 2;
			graphics.text(
					minecraft.font,
					helmetDurability,
					helmetDurX,
					y + 16,
					ArmorHelper.getDurabilityColor(helmet),
					true
			);
		}
	}

	private static boolean isF3Open(Minecraft minecraft) {
		return minecraft.getDebugOverlay().showDebugScreen();
	}

	private static void renderHud(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
		Minecraft minecraft = Minecraft.getInstance();

		if (minecraft.player == null || minecraft.level == null) return;

		// Hide if F3 is open
		if (isF3Open(minecraft)) return;

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int x = 6;
		int y = 6;

		renderLeftSide(minecraft, graphics, x, y);
		renderCenter(minecraft, graphics, screenWidth, y);

		// Shift armor icons down when potion effects are displayed.
        int rightY = !minecraft.player.getActiveEffects().isEmpty() ? y + 20 : y;
		renderRightSide(minecraft, graphics, x, screenWidth, rightY);
	}
}
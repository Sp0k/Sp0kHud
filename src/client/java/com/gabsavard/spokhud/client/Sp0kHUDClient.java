package com.gabsavard.spokhud.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

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

	private static void renderHud(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
		Minecraft minecraft = Minecraft.getInstance();

		int x = 10;
		int y = 10;

		graphics.fill(x, y, x + 120, y + 24, 0xAA000000);

		graphics.outline(x, y, 120, 24, 0xFFFFFFFF);

		graphics.text(
				minecraft.font,
				"Hello, HUD!",
				x + 6,
				y + 8,
				0xFFFFFFFF,
				true
		);
	}
}
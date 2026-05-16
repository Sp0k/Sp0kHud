package com.gabsavard.spokhud.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class Sp0kHUDConfigScreen extends Screen {
    private final Screen parent;
    private final Sp0kHUDConfig config;

    public Sp0kHUDConfigScreen(Screen parent) {
        super(Component.literal("Sp0k's HUD+ Options"));
        this.parent = parent;
        this.config = Sp0kHUDConfig.get();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int buttonWidth = 180;
        int buttonHeight = 20;
        int x = centerX - buttonWidth / 2;
        int y = this.height / 4;

        this.addRenderableWidget(Button.builder(
                Component.literal("UI Size: " + config.uiSize.label),
                button -> {
                    config.uiSize = config.uiSize.next();
                    Sp0kHUDConfig.save();
                    button.setMessage(Component.literal("UI Size: " + config.uiSize.label));
                }
        ).bounds(x, y, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Position: " + onOff(config.showPosition)),
                button -> {
                    config.showPosition = !config.showPosition;
                    Sp0kHUDConfig.save();
                    button.setMessage(Component.literal("Position: " + onOff(config.showPosition)));
                }
        ).bounds(x, y + 24, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Biome: " + onOff(config.showBiome)),
                button -> {
                    config.showBiome = !config.showBiome;
                    Sp0kHUDConfig.save();
                    button.setMessage(Component.literal("Biome: " + onOff(config.showBiome)));
                }
        ).bounds(x, y + 48, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Direction: " + onOff(config.showDirection)),
                button -> {
                    config.showDirection = !config.showDirection;
                    Sp0kHUDConfig.save();
                    button.setMessage(Component.literal("Direction: " + onOff(config.showDirection)));
                }
        ).bounds(x, y + 72, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Armor HUD: " + onOff(config.showArmor)),
                button -> {
                    config.showArmor = !config.showArmor;
                    Sp0kHUDConfig.save();
                    button.setMessage(Component.literal("Armor HUD: " + onOff(config.showArmor)));
                }
        ).bounds(x, y + 96, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> this.minecraft.setScreen(parent)
        ).bounds(x, y + 136, buttonWidth, buttonHeight).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        graphics.text(
                this.font,
                this.title.getString(),
                (this.width - this.font.width(this.title)) / 2,
                20,
                0xFFFFFFFF,
                true
        );
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    private static String onOff(boolean value) {
        return value ? "ON" : "OFF";
    }
}
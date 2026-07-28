package com.gabsavard.spokhud.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class Sp0kHUDConfigScreen extends Screen {
    private static final int BUTTON_WIDTH = 180;
    private static final int BUTTON_HEIGHT = 20;
    private static final int ROW_SPACING = 24;

    private static final int HUD_MIN = 0;
    private static final int HUD_MAX = 100;

    private final Screen parent;
    private final Sp0kHUDConfig config;

    private MenuPage page = MenuPage.MAIN;

    private enum MenuPage {
        MAIN,
        LOCATION,
        DIRECTION,
        EQUIPMENT
    }

    public Sp0kHUDConfigScreen(Screen parent) {
        super(Component.literal("Sp0k's HUD+ Settings"));
        this.parent = parent;
        this.config = Sp0kHUDConfig.get();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int x = centerX - BUTTON_WIDTH / 2;
        int y = this.height / 4;

        switch (page) {
            case MAIN -> initMainMenu(x, y);
            case LOCATION -> initLocationMenu(x, y);
            case DIRECTION -> initDirectionMenu(x, y);
            case EQUIPMENT -> initEquipmentMenu(x, y);
        }
    }

    private void initMainMenu(int x, int y) {
        addButton(x, y,
                "Location Information...",
                button -> openPage(MenuPage.LOCATION)
        );

        addSmallButton(x, y + ROW_SPACING,
                "Direction...",
                button -> openPage(MenuPage.DIRECTION)
        );

        addSmallButton(x + (BUTTON_WIDTH / 2) + 3, y + ROW_SPACING,
                "Equipment HUD...",
                button -> openPage(MenuPage.EQUIPMENT)
        );

        addSmallToggle(
                x,
                y + ROW_SPACING * 2,
                "Show in F3",
                () -> config.showHudInF3,
                value -> config.showHudInF3 = value
        );

        addSmallButton(
                x + (BUTTON_WIDTH / 2) + 3,
                y + ROW_SPACING * 2,
                "UI Size: " + config.uiSize.label,
                button -> {
                    config.uiSize = config.uiSize.next();
                    Sp0kHUDConfig.save();
                    button.setMessage(Component.literal("UI Size: " + config.uiSize.label));
                }
        );

        this.addRenderableWidget(new IntSlider(
                x,
                y + ROW_SPACING * 3,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "HUD X",
                HUD_MIN,
                HUD_MAX,
                config.hudX,
                value -> {
                    config.hudX = value;
                    Sp0kHUDConfig.save();
                }
        ));

        this.addRenderableWidget(new IntSlider(
                x,
                y + ROW_SPACING * 4,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "HUD Y",
                HUD_MIN,
                HUD_MAX,
                config.hudY,
                value -> {
                    config.hudY = value;
                    Sp0kHUDConfig.save();
                }
        ));

        addButton(x, y + ROW_SPACING * 6,
                "Done",
                button -> this.minecraft.setScreen(parent)
        );
    }

    private void initLocationMenu(int x, int y) {
        addToggle(
                x,
                y,
                "Location Info",
                () -> config.showLocationInfo,
                value -> config.showLocationInfo = value
        );

        addToggle(
                x,
                y + ROW_SPACING,
                "Position",
                () -> config.showPosition,
                value -> config.showPosition = value
        );

        addToggle(
                x,
                y + ROW_SPACING * 2,
                "Biome",
                () -> config.showBiome,
                value -> config.showBiome = value
        );

        addButton(x, y + ROW_SPACING * 4,
                "Back",
                button -> openPage(MenuPage.MAIN)
        );
    }

    private void initDirectionMenu(int x, int y) {
        addToggle(
                x,
                y,
                "Direction Display",
                () -> config.showDirection,
                value -> config.showDirection = value
        );

        addToggle(
                x,
                y + ROW_SPACING * 2,
                "Direction Lines",
                () -> config.showDirectionLines,
                value -> config.showDirectionLines = value
        );

        addButton(x, y + ROW_SPACING * 4,
                "Back",
                button -> openPage(MenuPage.MAIN)
        );
    }

    private void initEquipmentMenu(int x, int y) {
        addToggle(
                x,
                y,
                "Equipment Display",
                () -> config.showEquipmentDisplay,
                value -> config.showEquipmentDisplay = value
        );

        addToggle(
                x,
                y + ROW_SPACING,
                "Armor",
                () -> config.showArmor,
                value -> config.showArmor = value
        );

        addToggle(
                x,
                y + ROW_SPACING * 2,
                "Tools",
                () -> config.showTools,
                value -> config.showTools = value
        );

        addButton(
                x,
                y + ROW_SPACING * 3,
                "Durability: " + config.durationType.label,
                button -> {
                    config.durationType = config.durationType.next();
                    Sp0kHUDConfig.save();

                    button.setMessage(Component.literal(
                            "Durability: " + config.durationType.label
                    ));
                }
        );

        addButton(x, y + ROW_SPACING * 5,
                "Back",
                button -> openPage(MenuPage.MAIN)
        );
    }
    private void addButton(int x, int y, String text, Button.OnPress onPress) {
        this.addRenderableWidget(Button.builder(
                Component.literal(text),
                onPress
        ).bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    private void addSmallButton(int x, int y, String text, Button.OnPress onPress) {
        this.addRenderableWidget(Button.builder(
                Component.literal(text),
                onPress
        ).bounds(x, y, (BUTTON_WIDTH / 2) - 3, BUTTON_HEIGHT).build());
    }

    private void addToggle(
            int x,
            int y,
            String label,
            BooleanSupplier getter,
            Consumer<Boolean> setter
    ) {
        this.addRenderableWidget(Button.builder(
                Component.literal(label + ": " + onOff(getter.getAsBoolean())),
                button -> {
                    boolean newValue = !getter.getAsBoolean();
                    setter.accept(newValue);
                    Sp0kHUDConfig.save();

                    button.setMessage(Component.literal(label + ": " + onOff(newValue)));
                }
        ).bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    private void addSmallToggle(
            int x,
            int y,
            String label,
            BooleanSupplier getter,
            Consumer<Boolean> setter
    ) {
        this.addRenderableWidget(Button.builder(
                Component.literal(label + ": " + onOff(getter.getAsBoolean())),
                button -> {
                    boolean newValue = !getter.getAsBoolean();
                    setter.accept(newValue);
                    Sp0kHUDConfig.save();

                    button.setMessage(Component.literal(label + ": " + onOff(newValue)));
                }
        ).bounds(x, y, (BUTTON_WIDTH / 2) - 3, BUTTON_HEIGHT).build());
    }

    private void openPage(MenuPage page) {
        this.page = page;
        this.clearWidgets();
        this.init();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        String title = getPageTitle();

        graphics.text(
                this.font,
                title,
                (this.width - this.font.width(title)) / 2,
                20,
                0xFFFFFFFF,
                true
        );
    }

    private String getPageTitle() {
        return switch (page) {
            case MAIN -> "Sp0k's HUD+ Options";
            case LOCATION -> "Location Information";
            case DIRECTION -> "Direction";
            case EQUIPMENT -> "Equipment HUD";
        };
    }

    @Override
    public void onClose() {
        if (page != MenuPage.MAIN) {
            openPage(MenuPage.MAIN);
        } else {
            this.minecraft.setScreen(parent);
        }
    }

    private static String onOff(boolean value) {
        return value ? "ON" : "OFF";
    }

    private static class IntSlider extends AbstractSliderButton {
        private final String label;
        private final int min;
        private final int max;
        private final IntConsumer setter;

        public IntSlider(
                int x,
                int y,
                int width,
                int height,
                String label,
                int min,
                int max,
                int currentValue,
                IntConsumer setter
        ) {
            super(
                    x,
                    y,
                    width,
                    height,
                    Component.literal(label + ": " + currentValue),
                    valueToSliderValue(min, max, currentValue)
            );

            this.label = label;
            this.min = min;
            this.max = max;
            this.setter = setter;

            updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Component.literal(label + ": " + getIntValue()));
        }

        @Override
        protected void applyValue() {
            setter.accept(getIntValue());
        }

        private int getIntValue() {
            return min + (int) Math.round(this.value * (max - min));
        }

        private static double valueToSliderValue(int min, int max, int value) {
            if (max <= min) {
                return 0.0D;
            }

            int clamped = Math.max(min, Math.min(max, value));
            return (double) (clamped - min) / (double) (max - min);
        }
    }
}
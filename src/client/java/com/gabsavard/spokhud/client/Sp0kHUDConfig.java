package com.gabsavard.spokhud.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Sp0kHUDConfig {
    public enum UiSize {
        SMALL(0.75f, "Small"),
        MEDIUM(1.0f, "Medium"),
        LARGE(1.25f, "Large");

        public final float scale;
        public final String label;

        UiSize(float scale, String label) {
            this.scale = scale;
            this.label = label;
        }

        public UiSize next() {
            UiSize[] values = values();
            return values[(this.ordinal() + 1) % values.length];
        }
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("spokhud.json");

    public boolean showLocationInfo = true;
    public boolean showPosition = true;
    public boolean showBiome = true;

    public boolean showDirection = true;
    public boolean showDirectionLines = true;

    public boolean showEquipmentDisplay = true;
    public boolean showArmor = true;
    public boolean showTools = true;

    public UiSize uiSize = UiSize.MEDIUM;

    public int hudX = 6;
    public int hudY = 6;

    private static Sp0kHUDConfig instance;

    public static Sp0kHUDConfig get() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            instance = new Sp0kHUDConfig();
            save();
            return;
        }

        try {
            String json = Files.readString(CONFIG_PATH);
            instance = GSON.fromJson(json, Sp0kHUDConfig.class);

            if (instance == null) {
                instance = new Sp0kHUDConfig();
            }
        } catch (IOException error) {
            instance = new Sp0kHUDConfig();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(get()));
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}

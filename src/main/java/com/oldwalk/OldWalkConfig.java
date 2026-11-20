package com.oldwalk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public class OldWalkConfig {
    public static boolean rotateBackwardsWalking = true;
    public static boolean oldSneakAnimation = true;
    public static boolean uncapBlockingHeadRotation = true;

    private static class Data {
        boolean rotateBackwardsWalking = true;
        boolean oldSneakAnimation = true;
        boolean uncapBlockingHeadRotation = true;
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("oldwalk.json");
    }

    public static void load() {
        try {
            Data d = new Data();
            if (Files.exists(file())) {
                Data read = GSON.fromJson(Files.readString(file()), Data.class);
                if (read != null) d = read;
            }
            rotateBackwardsWalking = d.rotateBackwardsWalking;
            oldSneakAnimation = d.oldSneakAnimation;
            uncapBlockingHeadRotation = d.uncapBlockingHeadRotation;
            save();
        } catch (Exception e) {
            System.err.println("[oldwalk] could not read config, using defaults: " + e);
        }
    }

    public static void save() {
        try {
            Data d = new Data();
            d.rotateBackwardsWalking = rotateBackwardsWalking;
            d.oldSneakAnimation = oldSneakAnimation;
            d.uncapBlockingHeadRotation = uncapBlockingHeadRotation;
            Files.writeString(file(), GSON.toJson(d));
        } catch (Exception e) {
            System.err.println("[oldwalk] could not save config: " + e);
        }
    }
}

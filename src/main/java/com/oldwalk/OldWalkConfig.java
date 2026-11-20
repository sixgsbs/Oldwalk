package com.oldwalk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class OldWalkConfig {
    public static boolean rotateBackwardsWalking = true;
    public static boolean oldSneakAnimation = true;
    public static boolean uncapBlockingHeadRotation = true;
    public static float headTurnLimit = 75.0F;
    public static final Map<String, Integer> friends = new LinkedHashMap<>();

    private static class Data {
        boolean rotateBackwardsWalking = true;
        boolean oldSneakAnimation = true;
        boolean uncapBlockingHeadRotation = true;
        float headTurnLimit = 75.0F;
        Map<String, Integer> friends = new LinkedHashMap<>();
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("oldwalk.json");
    }

    public static void reset() {
        rotateBackwardsWalking = true;
        oldSneakAnimation = true;
        uncapBlockingHeadRotation = true;
        headTurnLimit = 75.0F;
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
            headTurnLimit = Math.max(30.0F, Math.min(90.0F, d.headTurnLimit));
            friends.clear();
            if (d.friends != null) friends.putAll(d.friends);
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
            d.headTurnLimit = headTurnLimit;
            d.friends = new LinkedHashMap<>(friends);
            Files.writeString(file(), GSON.toJson(d));
        } catch (Exception e) {
            System.err.println("[oldwalk] could not save config: " + e);
        }
    }
}

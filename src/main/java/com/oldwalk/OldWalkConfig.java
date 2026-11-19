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

    public static void load() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path file = FabricLoader.getInstance().getConfigDir().resolve("oldwalk.json");
        try {
            Data d = new Data();
            if (Files.exists(file)) {
                Data read = gson.fromJson(Files.readString(file), Data.class);
                if (read != null) d = read;
            }
            rotateBackwardsWalking = d.rotateBackwardsWalking;
            oldSneakAnimation = d.oldSneakAnimation;
            uncapBlockingHeadRotation = d.uncapBlockingHeadRotation;
            Files.writeString(file, gson.toJson(d));
        } catch (Exception e) {
            System.err.println("[oldwalk] could not read config, using defaults: " + e);
        }
    }
}

package com.oldwalk;

import net.fabricmc.api.ClientModInitializer;

public class OldWalkMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        OldWalkConfig.load();
    }
}

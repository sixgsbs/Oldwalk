package com.oldwalk;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class OldWalkScreen extends Screen {
    private final Screen parent;

    public OldWalkScreen(Screen parent) {
        super(Component.literal("Old Walk"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int w = 240;
        int x = this.width / 2 - w / 2;
        int y = this.height / 4;

        addToggle(x, y, w, "Backwards walking",
                () -> OldWalkConfig.rotateBackwardsWalking,
                v -> OldWalkConfig.rotateBackwardsWalking = v);
        addToggle(x, y + 26, w, "1.8 sneak animation",
                () -> OldWalkConfig.oldSneakAnimation,
                v -> OldWalkConfig.oldSneakAnimation = v);
        addToggle(x, y + 52, w, "Shield head rotation",
                () -> OldWalkConfig.uncapBlockingHeadRotation,
                v -> OldWalkConfig.uncapBlockingHeadRotation = v);

        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> onClose())
                .bounds(x, y + 90, w, 20).build());
    }

    private void addToggle(int x, int y, int w, String name, BooleanSupplier get, Consumer<Boolean> set) {
        addRenderableWidget(Button.builder(label(name, get.getAsBoolean()), b -> {
            boolean v = !get.getAsBoolean();
            set.accept(v);
            b.setMessage(label(name, v));
        }).bounds(x, y, w, 20).build());
    }

    private static Component label(String name, boolean on) {
        return Component.literal(name + ": " + (on ? "ON" : "OFF"));
    }

    @Override
    public void onClose() {
        OldWalkConfig.save();
        this.minecraft.setScreen(parent);
    }
}

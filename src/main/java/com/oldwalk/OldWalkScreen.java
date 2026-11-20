package com.oldwalk;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class OldWalkScreen extends Screen {
    private static final int W = 250;
    private final Screen parent;
    private int panelTop;
    private int panelBottom;

    public OldWalkScreen(Screen parent) {
        super(Component.literal("Old Walk"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - W / 2 + 10;
        int bw = W - 20;
        panelTop = Math.max(4, this.height / 2 - 105);
        int y = panelTop + 40;

        addToggle(x, y, bw, "Backwards walking",
                "Your body twists toward where you move, like 1.8.",
                () -> OldWalkConfig.rotateBackwardsWalking,
                v -> OldWalkConfig.rotateBackwardsWalking = v);
        addToggle(x, y + 24, bw, "1.8 sneak animation",
                "The old crouch pose with the lower head and legs.",
                () -> OldWalkConfig.oldSneakAnimation,
                v -> OldWalkConfig.oldSneakAnimation = v);
        addToggle(x, y + 48, bw, "Shield head rotation",
                "Your head can turn freely while blocking with a shield.",
                () -> OldWalkConfig.uncapBlockingHeadRotation,
                v -> OldWalkConfig.uncapBlockingHeadRotation = v);

        addRenderableWidget(new LimitSlider(x, y + 76, bw, 20));

        addRenderableWidget(Button.builder(Component.literal("Friend name colors..."),
                        b -> this.minecraft.setScreen(new OldWalkFriendsScreen(this)))
                .bounds(x, y + 104, bw, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Reset to defaults"), b -> {
            OldWalkConfig.reset();
            OldWalkConfig.save();
            rebuildWidgets();
        }).bounds(x, y + 130, bw / 2 - 2, 20).build());

        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> onClose())
                .bounds(x + bw / 2 + 2, y + 130, bw / 2 - 2, 20).build());

        panelBottom = y + 176;
    }

    private void addToggle(int x, int y, int w, String name, String tip,
                           BooleanSupplier get, Consumer<Boolean> set) {
        addRenderableWidget(Button.builder(label(name, get.getAsBoolean()), b -> {
            boolean v = !get.getAsBoolean();
            set.accept(v);
            b.setMessage(label(name, v));
        }).bounds(x, y, w, 20).tooltip(Tooltip.create(Component.literal(tip))).build());
    }

    private static Component label(String name, boolean on) {
        return Component.literal(name + ": ").append(
                Component.literal(on ? "ON" : "OFF")
                        .withStyle(on ? ChatFormatting.GREEN : ChatFormatting.RED));
    }

    @Override
    public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float delta) {
        super.renderBackground(g, mouseX, mouseY, delta);
        int left = this.width / 2 - W / 2;
        int right = left + W;
        g.fill(left, panelTop, right, panelBottom, 0xD0101018);
        g.fill(left, panelTop, right, panelTop + 2, 0xFF55AAFF);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        super.render(g, mouseX, mouseY, delta);
        g.drawCenteredString(this.font, this.title, this.width / 2, panelTop + 10, 0xFFFFFFFF);
        g.drawCenteredString(this.font, Component.literal("1.8 style movement"),
                this.width / 2, panelTop + 22, 0xFFAAAAAA);
        g.drawCenteredString(this.font,
                Component.literal("Change the open key: Options > Controls > Miscellaneous"),
                this.width / 2, panelBottom - 14, 0xFF888888);
    }

    @Override
    public void onClose() {
        OldWalkConfig.save();
        this.minecraft.setScreen(parent);
    }

    private static class LimitSlider extends AbstractSliderButton {
        private static final float MIN = 30.0F;
        private static final float MAX = 90.0F;

        LimitSlider(int x, int y, int w, int h) {
            super(x, y, w, h, Component.empty(), (OldWalkConfig.headTurnLimit - MIN) / (MAX - MIN));
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            long deg = Math.round(MIN + this.value * (MAX - MIN));
            setMessage(Component.literal("Head turn limit: " + deg + " degrees"));
        }

        @Override
        protected void applyValue() {
            OldWalkConfig.headTurnLimit = MIN + (float) this.value * (MAX - MIN);
        }
    }
}

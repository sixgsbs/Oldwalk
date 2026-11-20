package com.oldwalk;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class OldWalkFriendsScreen extends Screen {
    private static final int W = 250;
    private static final int MAX_FRIENDS = 6;
    private static final String[] COLOR_NAMES =
            {"Red", "Orange", "Yellow", "Green", "Aqua", "Blue", "Purple", "Pink", "White", "Gray"};
    private static final int[] COLOR_VALUES =
            {0xFF5555, 0xFFAA00, 0xFFFF55, 0x55FF55, 0x55FFFF, 0x5555FF, 0xAA00AA, 0xFF55FF, 0xFFFFFF, 0xAAAAAA};

    private final Screen parent;
    private EditBox nameBox;
    private String pending = "";
    private int newColor = 0;
    private int panelTop;
    private int panelBottom;

    public OldWalkFriendsScreen(Screen parent) {
        super(Component.literal("Friend name colors"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - W / 2 + 10;
        int bw = W - 20;
        int count = OldWalkConfig.friends.size();
        int total = 36 + 52 + count * 22 + 34;
        panelTop = Math.max(4, (this.height - total) / 2);
        int y = panelTop + 36;

        nameBox = new EditBox(this.font, x, y, bw - 84, 20, Component.literal("Player name"));
        nameBox.setMaxLength(16);
        nameBox.setHint(Component.literal("Player name"));
        nameBox.setValue(pending);
        addRenderableWidget(nameBox);

        addRenderableWidget(Button.builder(colorLabel(newColor), b -> {
            newColor = (newColor + 1) % COLOR_VALUES.length;
            b.setMessage(colorLabel(newColor));
        }).bounds(x + bw - 80, y, 80, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Add friend"), b -> {
            String n = nameBox.getValue().trim().toLowerCase(Locale.ROOT);
            if (n.isEmpty()) return;
            if (OldWalkConfig.friends.size() >= MAX_FRIENDS && !OldWalkConfig.friends.containsKey(n)) return;
            OldWalkConfig.friends.put(n, COLOR_VALUES[newColor]);
            OldWalkConfig.save();
            pending = "";
            rebuildWidgets();
        }).bounds(x, y + 24, bw, 20).build());

        int row = y + 52;
        for (Map.Entry<String, Integer> e : new ArrayList<>(OldWalkConfig.friends.entrySet())) {
            String name = e.getKey();
            addRenderableWidget(Button.builder(friendLabel(name, e.getValue()), b -> {
                Integer cur = OldWalkConfig.friends.get(name);
                int next = (indexOf(cur == null ? COLOR_VALUES[0] : cur) + 1) % COLOR_VALUES.length;
                OldWalkConfig.friends.put(name, COLOR_VALUES[next]);
                b.setMessage(friendLabel(name, COLOR_VALUES[next]));
            }).bounds(x, row, bw - 24, 20).build());

            addRenderableWidget(Button.builder(Component.literal("X"), b -> {
                pending = nameBox.getValue();
                OldWalkConfig.friends.remove(name);
                OldWalkConfig.save();
                rebuildWidgets();
            }).bounds(x + bw - 20, row, 20, 20).build());
            row += 22;
        }

        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> onClose())
                .bounds(x, row + 4, bw, 20).build());
        panelBottom = row + 34;
    }

    private static int indexOf(int rgb) {
        for (int i = 0; i < COLOR_VALUES.length; i++) {
            if (COLOR_VALUES[i] == rgb) return i;
        }
        return 0;
    }

    private static Component colorLabel(int i) {
        int c = COLOR_VALUES[i];
        return Component.literal("Color: " + COLOR_NAMES[i]).withStyle(s -> s.withColor(c));
    }

    private static Component friendLabel(String name, int rgb) {
        return Component.literal(name + "  (" + COLOR_NAMES[indexOf(rgb)] + ")")
                .withStyle(s -> s.withColor(rgb));
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
        g.drawCenteredString(this.font, Component.literal("Click a name to change its color"),
                this.width / 2, panelTop + 22, 0xFFAAAAAA);
    }

    @Override
    public void onClose() {
        OldWalkConfig.save();
        this.minecraft.setScreen(parent);
    }
}

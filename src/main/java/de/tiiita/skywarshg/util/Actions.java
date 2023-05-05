package de.tiiita.skywarshg.util;

import org.bukkit.event.block.Action;

public class Actions {
    public static boolean isRightClick(Action action) {
        return action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
    }

    public static boolean isLeftClick(Action action) {
        return action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
    }

    public static boolean isBlockClick(Action action) {
        return action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK;
    }

    public static boolean isAirClick(Action action) {
        return action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_AIR;
    }
}
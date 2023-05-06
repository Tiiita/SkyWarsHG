package de.tiiita.skywarshg.game.phase.event;

import de.tiiita.skywarshg.game.phase.GamePhase;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

/**
 * Created on Mai 06, 2023 | 12:48:03
 * (●'◡'●)
 */
public class PlayerRemoveEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Player removed;

    public PlayerRemoveEvent(Player removed) {
        this.removed = removed;
    }

    public Player getRemoved() {
        return removed;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}

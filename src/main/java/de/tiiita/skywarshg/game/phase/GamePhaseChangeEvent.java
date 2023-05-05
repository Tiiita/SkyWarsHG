package de.tiiita.skywarshg.game.phase;

import de.tiiita.skywarshg.game.phase.GamePhase;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.awt.*;

/**
 * Created on Mai 05, 2023 | 15:01:01
 * (●'◡'●)
 */
public class GamePhaseChangeEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final GamePhase gamePhase;
    private final GamePhase beforeGamePhase;

    public GamePhaseChangeEvent(GamePhase gamePhase, GamePhase beforeGamePhase) {
        this.gamePhase = gamePhase;
        this.beforeGamePhase = beforeGamePhase;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public GamePhase getBeforeGamePhase() {
        return beforeGamePhase;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}

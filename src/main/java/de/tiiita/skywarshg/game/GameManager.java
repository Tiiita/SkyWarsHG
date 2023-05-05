package de.tiiita.skywarshg.game;

import de.tiiita.skywarshg.game.phase.GamePhase;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;

import javax.print.attribute.standard.PrinterLocation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on Mai 05, 2023 | 15:03:55
 * (●'◡'●)
 */
public class GameManager {
    private final List<Player> players = new ArrayList<>();
    private GamePhase currentGamePhase = GamePhase.PLAYER_WAITING;

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.add(player);
    }

    public void setCurrentGamePhase(GamePhase gamePhase) {
        this.currentGamePhase = gamePhase;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }
}

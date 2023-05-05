package de.tiiita.skywarshg.game;

import de.tiiita.skywarshg.game.phase.GamePhase;
import de.tiiita.skywarshg.game.phase.GamePhaseChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import sun.util.resources.cldr.zh.CalendarData_zh_Hans_HK;

import javax.print.attribute.standard.PrinterLocation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on Mai 05, 2023 | 15:03:55
 * (●'◡'●)
 */
public class GameManager {
    private final Set<Player> players = new HashSet<>();
    private GamePhase currentGamePhase = GamePhase.LOBBY_PHASE;
    private final FileConfiguration config;

    public GameManager(FileConfiguration config) {
        this.config = config;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void setCurrentGamePhase(GamePhase gamePhase) {
        //Current "currentGamePhase" has not changed yet. The "gamePhase" is the new one.
        Bukkit.getPluginManager().callEvent(new GamePhaseChangeEvent(currentGamePhase, gamePhase));
        this.currentGamePhase = gamePhase;
    }

    public int getMaxPlayers() {
        return config.getInt("player-settings.max");
    }

    public Set<Player> getPlayers() {
        return new HashSet<>(players);
    }

    public int getMinPlayers() {
        return config.getInt("player-settings.min");
    }
    public int getPlayerCount() {
        return players.size();
    }

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }
}

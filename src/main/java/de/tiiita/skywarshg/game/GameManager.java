package de.tiiita.skywarshg.game;

import de.tiiita.skywarshg.game.phase.GamePhase;
import de.tiiita.skywarshg.game.phase.event.GamePhaseChangeEvent;
import de.tiiita.skywarshg.game.phase.event.PlayerRemoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on Mai 05, 2023 | 15:03:55
 * (●'◡'●)
 */
public class GameManager implements Listener {
    private final Set<Player> players = new HashSet<>();
    private GamePhase currentGamePhase = GamePhase.LOBBY_PHASE;
    private final FileConfiguration config;
    private boolean currentlyCounting;

    public GameManager(FileConfiguration config) {
        this.config = config;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
        Bukkit.getPluginManager().callEvent(new PlayerRemoveEvent(player));
    }

    public void setCurrentGamePhase(GamePhase gamePhase) {
        //Current "currentGamePhase" has not changed yet. The "gamePhase" is the new one.
        Bukkit.getPluginManager().callEvent(new GamePhaseChangeEvent(gamePhase, currentGamePhase));

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

    public boolean isCurrentlyCounting() {
        return currentlyCounting;
    }

    public void setCurrentlyCounting(boolean currentlyCounting) {
        this.currentlyCounting = currentlyCounting;
    }

    @EventHandler
    public void onPlayerRemove(PlayerRemoveEvent event) {
        if (getCurrentGamePhase() != GamePhase.STARTED) return;
        if (getPlayerCount() < 2) {
            setCurrentGamePhase(GamePhase.WINNING);
        }
    }

    @EventHandler
    public void onAchievementAward(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }
}

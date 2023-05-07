package de.tiiita.skywarshg.mode.spectator;

import de.tiiita.skywarshg.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on Mai 05, 2023 | 21:18:27
 * (●'◡'●)
 */
public class SpectatorHandler {
    private final Config messagesConfig;
    private final Set<Player> playersInSpectator = new HashSet<>();
    private final SpectatorItems spectatorItems;

    public SpectatorHandler(Config messagesConfig) {
        this.messagesConfig = messagesConfig;
        this.spectatorItems = new SpectatorItems(messagesConfig);
    }

    public void setSpectator(Player player) {
        playersInSpectator.add(player);
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(true);
        player.setFlying(true);
        spectatorItems.apply(player);
        player.updateInventory();
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.hidePlayer(player));
    }

    public boolean isSpectator(Player player) {
        return playersInSpectator.contains(player);
    }

    public SpectatorItems getSpectatorItems() {
        return spectatorItems;
    }
}

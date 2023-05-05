package de.tiiita.skywarshg.spectator;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.game.phase.GamePhase;
import de.tiiita.skywarshg.util.Config;
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
       player.setGameMode(GameMode.CREATIVE);
       spectatorItems.apply(player);
    }

    public boolean isSpectator(Player player) {
        return playersInSpectator.contains(player);
    }

    public SpectatorItems getSpectatorItems() {
        return spectatorItems;
    }
}

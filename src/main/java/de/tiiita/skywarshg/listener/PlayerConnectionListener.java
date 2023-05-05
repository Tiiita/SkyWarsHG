package de.tiiita.skywarshg.listener;

import de.tiiita.skywarshg.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created on Mai 05, 2023 | 14:44:37
 * (●'◡'●)
 */
public class PlayerConnectionListener implements Listener {

    private final GameManager gameManager;

    public PlayerConnectionListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        gameManager.addPlayer(player);
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        gameManager.removePlayer(player);
    }
}

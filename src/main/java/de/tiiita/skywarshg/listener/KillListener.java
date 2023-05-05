package de.tiiita.skywarshg.listener;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.game.StatsHandler;
import de.tiiita.skywarshg.scoreboard.GameBoard;
import de.tiiita.skywarshg.spectator.SpectatorHandler;
import de.tiiita.skywarshg.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created on Mai 05, 2023 | 20:09:44
 * (●'◡'●)
 */
public class KillListener implements Listener {

    private final StatsHandler statsHandler;
    private final GameBoard gameBoard;
    private final GameManager gameManager;
    private final Config messageConfig;
    private final SpectatorHandler spectatorHandler;

    public KillListener(StatsHandler statsHandler, GameBoard gameBoard, GameManager gameManager, Config messageConfig, SpectatorHandler spectatorHandler) {
        this.statsHandler = statsHandler;
        this.gameBoard = gameBoard;
        this.gameManager = gameManager;
        this.messageConfig = messageConfig;
        this.spectatorHandler = spectatorHandler;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        String killBroadcast;
        if (killer != null) {
            statsHandler.addKill(killer);
            gameBoard.updateScoreboard(killer);
            killBroadcast = messageConfig.getString("kill-message")
                    .replaceAll("%player%", player.getName())
                    .replaceAll("%killer%", killer.getName());
            Bukkit.broadcastMessage(killBroadcast);
            spectatorHandler.setSpectator(player);
            return;
        }

        killBroadcast = messageConfig.getString("die-message")
                .replaceAll("%player%", player.getName());

        spectatorHandler.setSpectator(player);
        gameManager.removePlayer(player);
        Bukkit.broadcastMessage(killBroadcast);
    }
}
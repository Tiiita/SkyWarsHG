package de.tiiita.skywarshg.listener;

import de.tiiita.skywarshg.game.StatsHandler;
import de.tiiita.skywarshg.scoreboard.GameBoard;
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

    public KillListener(StatsHandler statsHandler, GameBoard gameBoard) {
        this.statsHandler = statsHandler;
        this.gameBoard = gameBoard;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer != null) {
            statsHandler.addKill(killer);
            gameBoard.updateScoreboard(killer);
        }
    }
}

package de.tiiita.skywarshg.game.phase.impl;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.scoreboard.GameBoard;
import de.tiiita.skywarshg.util.PositiveTimer;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * Created on Mai 05, 2023 | 19:17:29
 * (●'◡'●)
 */
public class StartedPhase implements Listener {

    private final GameBoard gameBoard;
    private final GameManager gameManager;
    private final Plugin plugin;
    private PositiveTimer timer;
    private boolean phaseActivated;

    public StartedPhase(GameBoard gameBoard, GameManager gameManager, Plugin plugin) {
        this.gameBoard = gameBoard;
        this.gameManager = gameManager;
        this.plugin = plugin;
    }

    public void start() {
        this.phaseActivated = true;
        startScoreboardTimer();
    }

    public void stop() {
        this.phaseActivated = false;
        timer.stop();
        Bukkit.getOnlinePlayers().forEach(player -> {
            gameBoard.updateTimer(player, "Game Stopped");
        });
    }

    private void startScoreboardTimer() {
        this.timer = new PositiveTimer(plugin);
        timer.start();
        timer.eachSecond(() -> {
            String format = secondsToString(timer.getCounter());
            Bukkit.getOnlinePlayers().forEach(player -> {
                Bukkit.getScheduler().runTask(plugin, () -> gameBoard.updateTimer(player, format));
            });
        });

    }


    public String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }
}

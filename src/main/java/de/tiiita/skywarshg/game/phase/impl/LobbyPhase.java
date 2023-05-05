package de.tiiita.skywarshg.game.phase.impl;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.game.phase.GamePhase;
import de.tiiita.skywarshg.util.Config;
import de.tiiita.skywarshg.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

/**
 * Created on Mai 05, 2023 | 18:47:25
 * (●'◡'●)
 */
public class LobbyPhase implements Listener {
    private final GameManager gameManager;
    private final Plugin plugin;
    private final Config messagesConfig;
    private final FileConfiguration config;
    private boolean phaseActivated;
    private Timer startTimer;

    public LobbyPhase(GameManager gameManager, Plugin plugin, Config messagesConfig, FileConfiguration config) {
        this.gameManager = gameManager;
        this.plugin = plugin;
        this.messagesConfig = messagesConfig;
        this.config = config;
    }


    public void start() {
        this.phaseActivated = true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!phaseActivated) return;
        int currentPlayerCount = gameManager.getPlayerCount();
        int playerCountToStart = gameManager.getMinPlayers();

        if (currentPlayerCount >= playerCountToStart) {
            startCounting();
        } else {
            if (currentPlayerCount > 1) {
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_BASS, 10, 1));
                int neededPlayers = playerCountToStart - currentPlayerCount;

                String morePlayersNeeded = messagesConfig.getString("need-more-players")
                        .replaceAll("%needed%", "" + neededPlayers);
                Bukkit.broadcastMessage(morePlayersNeeded);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        int currentPlayerCount = gameManager.getPlayerCount();
        int playerCountToStart = gameManager.getMinPlayers();


    }


    private void startCounting() {
        final int seconds = config.getInt("start-counter");
        final String countingMessage = messagesConfig.getString("start-counting");
        final String startedMessage = messagesConfig.getString("start-complete");

        this.startTimer = new Timer(seconds, plugin);
        startTimer.start();
        startTimer.eachSecond(() -> {
            Bukkit.broadcastMessage(countingMessage.replaceAll("%seconds%", "" + startTimer.getCounter()));
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_STICKS, 10, 1));
        });

        startTimer.whenComplete(() -> {
            Bukkit.broadcastMessage(startedMessage);
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 1));
            gameManager.setCurrentGamePhase(GamePhase.STARTED);
        });

    }
    private void stopCounting() {
        if (this.startTimer != null) {
            this.startTimer.stop();

            String needMorePlayers = messagesConfig.getString("need-more-players");
            Bukkit.broadcastMessage(needMorePlayers);
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_BASS, 10, 1));
        }
    }
}


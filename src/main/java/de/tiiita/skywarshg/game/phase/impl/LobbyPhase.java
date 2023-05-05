package de.tiiita.skywarshg.game.phase.impl;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.game.phase.GamePhase;
import de.tiiita.skywarshg.scoreboard.GameBoard;
import de.tiiita.skywarshg.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created on Mai 05, 2023 | 18:47:25
 * (●'◡'●)
 */
public class LobbyPhase implements Listener {
    private final GameManager gameManager;
    private final GameBoard gameBoard;
    private final Config messagesConfig;
    private boolean phaseStarted;

    public LobbyPhase(GameManager gameManager, GameBoard gameBoard, Config messagesConfig) {
        this.gameManager = gameManager;
        this.gameBoard = gameBoard;
        this.messagesConfig = messagesConfig;
    }

    public void start() {
        this.phaseStarted = true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!phaseStarted) return;
        int currentPlayerCount = gameManager.getPlayerCount();
        int playerCountToStart = gameManager.getMinPlayers();

        if (currentPlayerCount >= playerCountToStart) {
            startCounting();

        } else {
            if (currentPlayerCount > 1) {
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_STICKS, 10, 1));
                int neededPlayers = playerCountToStart - currentPlayerCount;

                String morePlayersNeeded = messagesConfig.getString("need-more-players")
                        .replaceAll("%needed%", "" + neededPlayers);
                Bukkit.broadcastMessage(morePlayersNeeded);
            }
        }

    }


    private void startCounting() {



    }

    private void stopCounting() {


    }
}


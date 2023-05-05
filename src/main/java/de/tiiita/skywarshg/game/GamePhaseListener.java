package de.tiiita.skywarshg.game;

import de.tiiita.skywarshg.game.phase.GamePhase;
import de.tiiita.skywarshg.game.phase.GamePhaseChangeEvent;
import de.tiiita.skywarshg.game.phase.impl.LobbyPhase;
import de.tiiita.skywarshg.game.phase.impl.StartedPhase;
import de.tiiita.skywarshg.game.phase.impl.WinningPhase;
import de.tiiita.skywarshg.scoreboard.GameBoard;
import de.tiiita.skywarshg.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

/**
 * Created on Mai 05, 2023 | 15:38:24
 * (●'◡'●)
 */
public class GamePhaseListener implements Listener {
    private final GameManager gameManager;
    private final GameBoard gameBoard;
    private final Plugin plugin;
    private final Config messagesConfig;
    private final FileConfiguration config;

    private LobbyPhase lobbyPhase;
    private StartedPhase startedPhase;

    public GamePhaseListener(GameManager gameManager, GameBoard gameBoard, Plugin plugin, Config messagesConfig, FileConfiguration config) {
        this.gameManager = gameManager;
        this.gameBoard = gameBoard;
        this.plugin = plugin;
        this.messagesConfig = messagesConfig;
        this.config = config;
    }

    @EventHandler
    public void onGamePhaseChange(GamePhaseChangeEvent event) {
        GamePhase gamePhase = event.getGamePhase();
        switch (gamePhase) {
            case LOBBY_PHASE: {
                this.lobbyPhase=  new LobbyPhase(gameManager, plugin, messagesConfig, config);
                Bukkit.getPluginManager().registerEvents(lobbyPhase, plugin);
                lobbyPhase.start();
                break;
            }

            case STARTED: {
                this.lobbyPhase.stop();
                this.startedPhase = new StartedPhase(gameBoard, gameManager, plugin);
                Bukkit.getPluginManager().registerEvents(startedPhase, plugin);
                startedPhase.start();
                break;
            }

            case WINNING: {
                startedPhase.stop();
                WinningPhase phase = new WinningPhase();
                Bukkit.getPluginManager().registerEvents(phase, plugin);
                phase.start(); //Stops with shutting down the server after a few seconds
                break;
            }
        }

    }

}

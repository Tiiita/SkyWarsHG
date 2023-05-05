package de.tiiita.skywarshg;

import de.tiiita.skywarshg.command.StartCommand;
import de.tiiita.skywarshg.game.GamePhaseListener;
import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.game.StatsHandler;
import de.tiiita.skywarshg.game.phase.GamePhase;
import de.tiiita.skywarshg.listener.KillListener;
import de.tiiita.skywarshg.listener.PlayerConnectionListener;
import de.tiiita.skywarshg.scoreboard.GameBoard;
import de.tiiita.skywarshg.spectator.SpectatorHandler;
import de.tiiita.skywarshg.spectator.SpectatorItemListener;
import de.tiiita.skywarshg.spectator.SpectatorListener;
import de.tiiita.skywarshg.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyWarsHG extends JavaPlugin {
    private Config messagesConfig;
    private Config mapSavesConfig;
    private StatsHandler statsHandler;
    private SpectatorHandler spectatorHandler;
    private GameBoard gameBoard;
    private GameManager gameManager;
    @Override
    public void onEnable() {
        // Plugin startup logic

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        //This is for the config.yml
        saveDefaultConfig();

        //This is for the custom configs
        this.messagesConfig = new Config("messages.yml", this);
        this.mapSavesConfig = new Config("mapsaves.yml", this);

        this.statsHandler = new StatsHandler();
        this.gameManager = new GameManager(getConfig());
        this.gameBoard = new GameBoard(gameManager, messagesConfig, statsHandler);
        this.spectatorHandler = new SpectatorHandler(messagesConfig);
        registerListeners();
        registerCommands();


        gameManager.setCurrentGamePhase(GamePhase.LOBBY_PHASE);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void registerCommands() {
        getCommand("start").setExecutor(new StartCommand(gameManager, messagesConfig, getConfig()));
    }
    private void registerListeners() {
        registerListener(statsHandler);
        registerListener(new SpectatorListener(spectatorHandler, messagesConfig));
        registerListener(new SpectatorItemListener(spectatorHandler, gameManager, messagesConfig, this, getConfig()));
        registerListener(new KillListener(statsHandler, gameBoard, gameManager, messagesConfig, spectatorHandler));
        registerListener(new GamePhaseListener(gameManager, gameBoard, this, messagesConfig, getConfig()));
        registerListener(new PlayerConnectionListener(gameManager, messagesConfig, gameBoard));
    }

    //Just for the clean code...
    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

}

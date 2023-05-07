package de.tiiita.skywarshg;

import de.tiiita.skywarshg.mode.configure.ConfigureCommand;
import de.tiiita.skywarshg.command.StartCommand;
import de.tiiita.skywarshg.game.GamePhaseListener;
import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.game.StatsHandler;
import de.tiiita.skywarshg.game.phase.GamePhase;
import de.tiiita.skywarshg.listener.KillListener;
import de.tiiita.skywarshg.listener.PlayerConnectionListener;
import de.tiiita.skywarshg.scoreboard.GameBoard;
import de.tiiita.skywarshg.mode.spectator.SpectatorHandler;
import de.tiiita.skywarshg.mode.spectator.SpectatorItemListener;
import de.tiiita.skywarshg.mode.spectator.SpectatorListener;
import de.tiiita.skywarshg.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.defaults.GameRuleCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.List;

public final class SkyWarsHG extends JavaPlugin {
    private Config messagesConfig;
    private Config mapSavesConfig;
    private Config config;
    private Config chestsConfig;

    private StatsHandler statsHandler;
    private SpectatorHandler spectatorHandler;
    private GameBoard gameBoard;
    private GameManager gameManager;
    @Override
    public void onEnable() {
        // Plugin startup logic

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        //This is for the custom configs
        this.messagesConfig = new Config("messages.yml", this, true);
        this.mapSavesConfig = new Config("mapsaves.yml", this, false);
        this.chestsConfig = new Config("chests.yml", this, false);
        this.config = new Config("config.yml", this, false);

        this.statsHandler = new StatsHandler();
        this.gameManager = new GameManager(getConfig());
        this.gameBoard = new GameBoard(gameManager, messagesConfig, statsHandler);
        this.spectatorHandler = new SpectatorHandler(messagesConfig);
        registerListeners();
        registerCommands();
        configureWorlds();

        gameManager.setCurrentGamePhase(GamePhase.LOBBY_PHASE);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void configureWorlds() {
        Collection<World> worlds = Bukkit.getWorlds();
        worlds.forEach(world -> {
            world.setAnimalSpawnLimit(0);
            world.setAmbientSpawnLimit(0);
            world.getEntities().clear();
            world.setPVP(true);
            world.setTime(1000);
            world.setThundering(false);
            world.setStorm(false);
            world.setGameRuleValue("doMobSpawning", "false");
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("keepInventory", "false");
            world.save();
        });

    }
    private void registerCommands() {
        getCommand("start").setExecutor(new StartCommand(gameManager, messagesConfig, config));
        getCommand("configure").setExecutor(new ConfigureCommand());
    }
    private void registerListeners() {
        registerListener(statsHandler);
        registerListener(new SpectatorListener(spectatorHandler, messagesConfig));
        registerListener(new SpectatorItemListener(spectatorHandler, gameManager, messagesConfig, this, config));
        registerListener(new KillListener(statsHandler, gameBoard, gameManager, messagesConfig, spectatorHandler));
        registerListener(new GamePhaseListener(gameManager, gameBoard, this, messagesConfig, config));
        registerListener(new PlayerConnectionListener(gameManager, messagesConfig, gameBoard, this));
        registerListener(gameManager);
    }

    //Just for the clean code...
    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

}

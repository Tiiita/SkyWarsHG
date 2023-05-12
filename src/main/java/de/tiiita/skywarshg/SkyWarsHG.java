package de.tiiita.skywarshg;

import de.tiiita.skywarshg.chest.ChestManager;
import de.tiiita.skywarshg.command.ChestCommand;
import de.tiiita.skywarshg.map.WorldManager;
import de.tiiita.skywarshg.mode.configure.ConfigureCommand;
import de.tiiita.skywarshg.command.StartCommand;
import de.tiiita.skywarshg.game.phase.GamePhaseListener;
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
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.logging.Level;

public final class SkyWarsHG extends JavaPlugin {
    private Config messagesConfig;
    private Config mapSavesConfig;
    private Config config;
    private Config chestsConfig;

    private StatsHandler statsHandler;
    private SpectatorHandler spectatorHandler;
    private GameBoard gameBoard;
    private GameManager gameManager;
    private ChestManager chestManager;
    private WorldManager worldManager;
    @Override
    public void onEnable() {
        // Plugin startup logic

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        //This is for the custom configs
        this.messagesConfig = new Config("messages.yml", this, true);
        this.mapSavesConfig = new Config("mapsaves.yml", this, false);
        this.chestsConfig = new Config("chests.yml", this, false);
        this.config = new Config("config.yml", this, false);


        configureWorlds(); //Configure presets copy the configures later.
        this.worldManager = new WorldManager(this, config);


        this.chestManager = new ChestManager(chestsConfig);
        this.statsHandler = new StatsHandler();
        this.gameManager = new GameManager(getConfig());
        this.gameBoard = new GameBoard(gameManager, messagesConfig, statsHandler);
        this.spectatorHandler = new SpectatorHandler(messagesConfig);
        registerListeners();
        registerCommands();


        gameManager.setCurrentGamePhase(GamePhase.LOBBY_PHASE);
        worldManager.copyRandomWorld().whenComplete((world, throwable) -> {
         getLogger().log(Level.INFO, "*** DONE! Server is ready ***");
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        worldManager.deleteCopy();
    }


    private void configureWorlds() {
        Collection<World> worlds = Bukkit.getWorlds();
        worlds.forEach(world -> {
            world.setAnimalSpawnLimit(0);
            world.setMonsterSpawnLimit(0);
            world.getEntities().clear();
            world.setPVP(true);
            world.setTime(1000);
            world.setThundering(false);
            world.setStorm(false);
            String version = Bukkit.getVersion(); // Get the version string
            String[] parts = version.split("\\."); // Split the version string into parts
            if (Integer.parseInt(parts[1]) > 13) { // Check if the minor version is greater than 13
                world.setGameRuleValue("announceAdvancements", "false");
            }

            world.setGameRuleValue("doMobSpawning", "false");
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("keepInventory", "false");
            world.save();
        });

    }
    private void registerCommands() {
        getCommand("start").setExecutor(new StartCommand(gameManager, messagesConfig, config));
        getCommand("configure").setExecutor(new ConfigureCommand());
        getCommand("chest").setExecutor(new ChestCommand(chestManager));
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

package de.tiiita.skywarshg;

import de.tiiita.skywarshg.command.StartCommand;
import de.tiiita.skywarshg.game.GameListener;
import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.listener.PlayerConnectionListener;
import de.tiiita.skywarshg.scoreboard.GameBoard;
import de.tiiita.skywarshg.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyWarsHG extends JavaPlugin {
    private Config messagesConfig;
    private Config mapSavesConfig;
    private GameBoard gameBoard;
    private GameManager gameManager;
    @Override
    public void onEnable() {
        // Plugin startup logic

        //This is for the config.yml
        saveDefaultConfig();

        //This is for the custom configs
        this.messagesConfig = new Config("messages.yml", this);
        this.mapSavesConfig = new Config("mapsaves.yml", this);

        this.gameManager = new GameManager(getConfig());
        this.gameBoard = new GameBoard(gameManager, messagesConfig);

        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void registerCommands() {
        getCommand("start").setExecutor(new StartCommand());
    }
    private void registerListeners() {
        registerListener(new GameListener());
        registerListener(new PlayerConnectionListener(gameManager, messagesConfig, gameBoard));
    }

    //Just for the clean code...
    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

}

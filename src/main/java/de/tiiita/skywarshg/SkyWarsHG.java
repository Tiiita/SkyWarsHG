package de.tiiita.skywarshg;

import de.tiiita.skywarshg.util.Config;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyWarsHG extends JavaPlugin {
    private Config messagesConfig;
    private Config mapSavesConfig;
    @Override
    public void onEnable() {
        // Plugin startup logic

        //This is for the config.yml
        saveDefaultConfig();

        //This is for the custom configs
        this.messagesConfig = new Config("messages.yml", this);
        this.mapSavesConfig = new Config("mapsaves.yml", this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

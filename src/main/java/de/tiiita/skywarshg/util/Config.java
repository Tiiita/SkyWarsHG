package de.tiiita.skywarshg.util;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * @author NieGestorben, tiiita_
 * Created on Juli 29, 2022 | 18:31:59
 * (●'◡'●)
 */

public class Config {
    private final Plugin plugin;

    private final FileConfiguration fileConfiguration;
    private final File file;
    private final boolean isMessagesConfig;
    public Config(String name, Plugin plugin, boolean isMessagesConfig) {
        this.plugin = plugin;
        this.isMessagesConfig = isMessagesConfig;
        plugin.saveResource(name, false);
        File path = plugin.getDataFolder();

        file = new File(path, name);
        if (!file.exists()) {
            path.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().log(Level.SEVERE, "There was an error creating the config!");
            }
        }
        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "There was an error loading the config!");
        }
    }

    public File getFile() {
        return file;
    }

    public void save() {
        try {
            fileConfiguration.save(file);
            plugin.saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.SEVERE, "There was an error saving the config!");
        }
    }

    public void reload() {
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "There was an error reloading the config!");
        }
    }


    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String getString(String path) {
        String string = color(getRawString(path));
        if (isMessagesConfig) {
            String prefix = color(getRawString("prefix"));
            return string.replaceAll("%prefix%", prefix);
        } else return string;
    }


    public List<String> getStringList(String path) {
        return fileConfiguration.getStringList(path);
    }
    public String getRawString(String path) {
        return fileConfiguration.getString(path);
    }

    public int getInt(String path) {
        return fileConfiguration.getInt(path);
    }

    public double getDouble(String path) {
        return fileConfiguration.getDouble(path);
    }

    public boolean getBoolean(String path) {
        return fileConfiguration.getBoolean(path);
    }
}
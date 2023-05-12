package de.tiiita.skywarshg.map;

import de.tiiita.skywarshg.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * Created on Mai 12, 2023 | 20:55:33
 * (●'◡'●)
 */
public class WorldManager {

    private World copiedWorld;
    private final Plugin plugin;
    private final Config config;

    public WorldManager(Plugin plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

    public CompletableFuture<Void> copyRandomWorld() {
        List<String> worldNames = config.getStringList("arenas");
        int randomIndex = new Random().nextInt(worldNames.size());
        String worldName = worldNames.get(randomIndex);
        return copyWorld(worldName);
    }

    public CompletableFuture<Void> copyWorld(String originalWorldName) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Server server = Bukkit.getServer();
            World originalWorld = server.getWorld(originalWorldName);

            if (originalWorld != null) {
                String newWorldName = "gameWorld";
                String originalWorldPath = originalWorld.getWorldFolder().getPath();
                String newWorldPath = server.getWorldContainer().getPath() + File.separator + newWorldName;

                File originalWorldFolder = new File(originalWorldPath);
                File newWorldFolder = new File(newWorldPath);
                copyFolder(originalWorldFolder, newWorldFolder);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    WorldCreator worldCreator = new WorldCreator(newWorldName);
                    copiedWorld = server.createWorld(worldCreator);
                    future.complete(null);
                });
            } else {
                future.completeExceptionally(new IllegalArgumentException("The given world does not exist!"));
            }
        });

        return future;
    }

    public void deleteCopy() {
        if (copiedWorld != null) {
            Bukkit.getServer().unloadWorld(copiedWorld, false);
            File copiedWorldFolder = copiedWorld.getWorldFolder();
            deleteFolder(copiedWorldFolder);
            copiedWorld = null;
        }
    }

    private void copyFolder(File source, File destination) {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }

            File[] files = source.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.getName().equals("uid.dat")) {
                        File newFile = new File(destination, file.getName());
                        copyFolder(file, newFile);
                    }
                }
            }
        } else {
            if (!source.getName().equals("uid.dat")) {
                try {
                    FileInputStream fis = new FileInputStream(source);
                    FileOutputStream fos = new FileOutputStream(destination);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                    fis.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }
        folder.delete();
    }

    public World getCopiedWorld() {
        return copiedWorld;
    }
}
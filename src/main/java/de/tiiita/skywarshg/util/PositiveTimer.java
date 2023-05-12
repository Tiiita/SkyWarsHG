package de.tiiita.skywarshg.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author tiiita_
 * Created on Dezember 17, 2022 | 18:21:40
 * (●'◡'●)
 */
public class PositiveTimer {
    private final Plugin plugin;

    public PositiveTimer(Plugin plugin) {
        this.plugin = plugin;
    }

    private boolean running = false;
    private Runnable eachSecond;
    int counter;
    private BukkitTask bukkitTask;

    public void start() {
        running = true;
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (eachSecond != null) {
                eachSecond.run();
            }
            counter++;

        }, 0, 20);
    }

    public void stop() {
        bukkitTask.cancel();
        bukkitTask = null;
        counter = 0;
        running = false;
    }

    public void eachSecond(Runnable runnable) {
        this.eachSecond = runnable;
    }

    public boolean isRunning() {
        return running;
    }

    public int getCounter() {
        return counter;
    }
}

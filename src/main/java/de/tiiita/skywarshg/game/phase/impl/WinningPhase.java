package de.tiiita.skywarshg.game.phase.impl;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.util.Config;
import de.tiiita.skywarshg.util.PlayerUtil;
import de.tiiita.skywarshg.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

/**
 * Created on Mai 05, 2023 | 19:17:52
 * (●'◡'●)
 */
public class WinningPhase implements Listener {

    private final Config messagesConfig;
    private final GameManager gameManager;
    private final Plugin plugin;
    private final Config config;
    private boolean phaseActivated;

    public WinningPhase(Config messagesConfig, GameManager gameManager, Plugin plugin, Config config) {
        this.messagesConfig = messagesConfig;
        this.gameManager = gameManager;
        this.plugin = plugin;
        this.config = config;
    }

    public void start() {
        this.phaseActivated = true;

        managePlayers();
        broadcastWin();
        Timer shutdownTimer = new Timer(5, plugin);
        shutdownTimer.start();
        String shutdownMessage = messagesConfig.getString("restart-announce");
        shutdownTimer.eachSecond(() -> {
            Bukkit.broadcastMessage(shutdownMessage);
        });
        String fallbackServer = config.getString("move-back-lobby");
        shutdownTimer.whenComplete(() -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                PlayerUtil.movePlayerToServer(player, plugin, fallbackServer);
            });
            Bukkit.getScheduler().runTaskLater(plugin, Bukkit::shutdown, 20);
        });
    }


    private void managePlayers() {
        gameManager.getPlayers().forEach(player -> {
            player.setGameMode(GameMode.CREATIVE);
        });
    }
    private void broadcastWin() {

        int currentPlayerCount = gameManager.getPlayerCount();
        if (currentPlayerCount >= 2) {
            Bukkit.getLogger().log(Level.SEVERE, "*** Winning Phase has been activated but there are more than 1 player in the game. ***");
            Bukkit.getLogger().log(Level.SEVERE, "*** This is a bug! Please report it imminently! ***");
            return;
        }

        Player winner = gameManager.getPlayers().iterator().next();
        String winMessage = messagesConfig.getString("player-won")
                .replaceAll("%player%", winner.getName());

        Timer timer = new Timer(3, plugin);
        timer.start();
        timer.eachSecond(() -> {
            Bukkit.broadcastMessage(winMessage);
        });
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!phaseActivated) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!phaseActivated) return;
        event.setCancelled(true);
    }
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!phaseActivated) return;
        event.setCancelled(true);
    }
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (!phaseActivated) return;
        event.setCancelled(true);
    }


    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!phaseActivated) return;
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }
}

package de.tiiita.skywarshg.game.phase.impl;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.game.phase.GamePhase;
import de.tiiita.skywarshg.util.Config;
import de.tiiita.skywarshg.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

/**
 * Created on Mai 05, 2023 | 18:47:25
 * (●'◡'●)
 */
public class LobbyPhase implements Listener {
    private final GameManager gameManager;
    private final Plugin plugin;
    private final Config messagesConfig;
    private final FileConfiguration config;
    private boolean phaseActivated;
    private Timer startTimer;

    public LobbyPhase(GameManager gameManager, Plugin plugin, Config messagesConfig, FileConfiguration config) {
        this.gameManager = gameManager;
        this.plugin = plugin;
        this.messagesConfig = messagesConfig;
        this.config = config;
    }


    public void start() {
        this.phaseActivated = true;
    }

    public void stop() {
        this.phaseActivated = false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        if (!phaseActivated) return;
        Player player = event.getPlayer();
        if (!gameManager.getCurrentGamePhase().equals(GamePhase.LOBBY_PHASE)) {
            String kickMessage = messagesConfig.getString("kick-message");
            player.kickPlayer(kickMessage);
            return;
        }


        int currentPlayerCount = gameManager.getPlayerCount();
        int playerCountToStart = gameManager.getMinPlayers();

        if (currentPlayerCount >= playerCountToStart) {
            startCounting();
        } else {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_BASS, 10, 1));
            int neededPlayers = playerCountToStart - currentPlayerCount;

            String morePlayersNeeded = messagesConfig.getString("need-more-players").replaceAll("%needed%", "" + neededPlayers);
            Bukkit.broadcastMessage(morePlayersNeeded);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        int currentPlayerCount = gameManager.getPlayerCount();
        int playerCountToStart = gameManager.getMinPlayers();

        if (currentPlayerCount < playerCountToStart) {
            stopCounting();
        }
    }


    private void startCounting() {
        final int seconds = config.getInt("start-counter");
        final String countingMessage = messagesConfig.getString("start-counting");
        final String startedMessage = messagesConfig.getString("start-complete");

        this.startTimer = new Timer(seconds, plugin);
        startTimer.start();
        startTimer.eachSecond(() -> {
            Bukkit.broadcastMessage(countingMessage.replaceAll("%seconds%", "" + startTimer.getCounter()));
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_STICKS, 10, 1));
        });

        startTimer.whenComplete(() -> {
            Bukkit.broadcastMessage(startedMessage);
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 1));
            gameManager.setCurrentGamePhase(GamePhase.STARTED);

            //Shut lobby phase down because another phase began
            stop();
        });

    }

    private void stopCounting() {
        if (!phaseActivated) return;
        if (this.startTimer != null) {
            this.startTimer.stop();
            int currentPlayerCount = gameManager.getPlayerCount();
            int playerCountToStart = gameManager.getMinPlayers();
            int neededPlayers = playerCountToStart - currentPlayerCount;

            String needMorePlayers = messagesConfig.getString("need-more-players").replaceAll("%needed%", "" + neededPlayers);
            Bukkit.broadcastMessage(needMorePlayers);
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_BASS, 10, 1));
        }
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


package de.tiiita.skywarshg.mode.spectator;

import de.tiiita.skywarshg.util.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Created on Mai 05, 2023 | 21:20:53
 * (●'◡'●)
 */
public class SpectatorListener implements Listener {

    private final SpectatorHandler spectatorHandler;
    private final Config messagesConfig;

    public SpectatorListener(SpectatorHandler spectatorHandler, Config messagesConfig) {
        this.spectatorHandler = spectatorHandler;
        this.messagesConfig = messagesConfig;
    }

    //Build
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (spectatorHandler.isSpectator(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (spectatorHandler.isSpectator(event.getPlayer())) event.setCancelled(true);
    }
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (spectatorHandler.isSpectator(event.getPlayer())) event.setCancelled(true);
    }
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (spectatorHandler.isSpectator(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (spectatorHandler.isSpectator((Player) event.getWhoClicked())) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (spectatorHandler.isSpectator((Player) event.getDamager())) event.setCancelled(true);
        }
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (spectatorHandler.isSpectator(event.getPlayer())) {
            event.getPlayer().sendMessage(messagesConfig.getString("cannot-chat"));
            event.setCancelled(true);
        }
    }
}

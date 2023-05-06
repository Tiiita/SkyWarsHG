package de.tiiita.skywarshg.mode.spectator;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.util.Actions;
import de.tiiita.skywarshg.util.Config;
import de.tiiita.skywarshg.util.ItemBuilder;
import de.tiiita.skywarshg.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

/**
 * Created on Mai 05, 2023 | 21:33:39
 * (●'◡'●)
 */
public class SpectatorItemListener implements Listener {
    private final SpectatorHandler spectatorHandler;
    private final GameManager gameManager;
    private final Config messagesConfig;
    private final Plugin plugin;
    private final FileConfiguration config;

    public SpectatorItemListener(SpectatorHandler spectatorHandler, GameManager gameManager, Config messagesConfig, Plugin plugin, FileConfiguration config) {
        this.spectatorHandler = spectatorHandler;
        this.gameManager = gameManager;
        this.messagesConfig = messagesConfig;
        this.plugin = plugin;
        this.config = config;
    }



    @EventHandler
    public void onTeleporterItemClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null) return;
        if (!spectatorHandler.isSpectator(player)) return;
        if (!event.getItem().equals(spectatorHandler.getSpectatorItems().teleporter())) return;
        if (!Actions.isRightClick(event.getAction())) return;

        Inventory inventory = Bukkit.createInventory(null, 54);

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, backgroundItem());
        }

        int counter = 0;
        for (Player current : gameManager.getPlayers()) {
            if (counter == inventory.getSize() - 1) {
                return;
            }

            if (!current.equals(player)) {
                inventory.setItem(counter, getHeadItem(current));
            }
            counter++;
        }

        player.openInventory(inventory);

    }
    @EventHandler
    public void onBackgroundClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!spectatorHandler.isSpectator(player)) return;
        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getCurrentItem().equals(backgroundItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSkullClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }


        Player player = (Player) event.getWhoClicked();

        if (!spectatorHandler.isSpectator(player)) return;
        if (!(event.getCurrentItem().getType() == Material.SKULL_ITEM)) {
            return;
        }

        Player target = Bukkit.getPlayer(((SkullMeta) event.getCurrentItem().getItemMeta()).getOwner());

        if (target == null) {
            return;
        }

        event.setCancelled(true);

        player.teleport(target.getLocation());
        String teleportMessage = messagesConfig.getString("teleported-to")
                        .replaceAll("%player%", target.getName());
        player.sendMessage(teleportMessage);
    }

    @EventHandler
    public void onLeaveClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null) return;
        if (!spectatorHandler.isSpectator(player)) return;
        if (!event.getItem().equals(spectatorHandler.getSpectatorItems().leave())) return;
        if (!Actions.isRightClick(event.getAction())) return;


        String server = config.getString("move-back-lobby");
        PlayerUtil.movePlayerToServer(player, plugin, server);
    }

    private ItemStack getHeadItem(Player player) {
        return new ItemBuilder(Material.SKULL_ITEM)
                .setDurability((short) 3)
                .setSkullOwner(player.getName())
                .setName("§7" + player.getName())
                .toItemStack();
    }

    private ItemStack backgroundItem() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDyeColor(DyeColor.BLACK)
                .toItemStack();
    }
}

package de.tiiita.skywarshg.spectator;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.util.Actions;
import de.tiiita.skywarshg.util.ItemBuilder;
import de.tiiita.skywarshg.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.util.stream.Collectors;

/**
 * Created on Mai 05, 2023 | 21:33:39
 * (●'◡'●)
 */
public class SpectatorItemListener implements Listener {
    private final SpectatorHandler spectatorHandler;
    private final GameManager gameManager;
    private final Plugin plugin;
    private final FileConfiguration config;

    public SpectatorItemListener(SpectatorHandler spectatorHandler, GameManager gameManager, Plugin plugin, FileConfiguration config) {
        this.spectatorHandler = spectatorHandler;
        this.gameManager = gameManager;
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

        Inventory inventory = Bukkit.createInventory(null, 54, "§8Online: " + Bukkit.getOnlinePlayers().size());

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
            player.sendMessage("§cPlayer not found!");
            return;
        }

        event.setCancelled(true);

        if (player.equals(target)) {
            player.sendMessage("§7» You cannot teleport to yourself.");
            return;
        }

        player.teleport(target.getLocation());
        player.sendMessage("§7» Teleported to §c" + target.getName() + "§7.");
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

package de.tiiita.skywarshg.mode.spectator;

import de.tiiita.skywarshg.util.Config;
import de.tiiita.skywarshg.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created on Mai 05, 2023 | 21:22:28
 * (●'◡'●)
 */
public class SpectatorItems {

    private final Config messageConfig;

    public SpectatorItems(Config messageConfig) {
        this.messageConfig = messageConfig;
    }

    public ItemStack teleporter() {
        return new ItemBuilder(Material.COMPASS)
                .setName(messageConfig.getString("teleporter"))
                .toItemStack();

    }

    public ItemStack leave() {
        return new ItemBuilder(Material.REDSTONE)
                .setName(messageConfig.getString("leave"))
                .toItemStack();

    }
    public void apply(Player player) {
        player.getInventory().setItem(0, teleporter());
        player.getInventory().setItem(8, leave());
    }
}

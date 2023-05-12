package de.tiiita.skywarshg.chest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Created on Mai 07, 2023 | 21:16:26
 * (●'◡'●)
 */
public class SWChest {

    private final ChestType chestType;
    private final Location location;
    private final Chest chest;

    public SWChest(ChestType chestType, Location location) {
        this.chestType = chestType;
        this.location = location;

        location.getBlock().setType(Material.CHEST);
        Block chestBlock = location.getBlock();
        this.chest = (Chest) chestBlock.getState();
    }


    public ChestType getChestType() {
        return chestType;
    }

    public Chest getChest() {
        return chest;
    }

    public Location getLocation() {
        return location;
    }
}

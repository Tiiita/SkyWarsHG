package de.tiiita.skywarshg.chest;


import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on Mai 07, 2023 | 21:16:35
 * (●'◡'●)
 */
public class ChestManager {

    private final Collection<SWChest> currentChests = new HashSet<>();


    /**
     *
     * @param chestType the type of the chest.
     * @param location the location where the chest should spawn.
     * @return the spawned chest.
     */
    public SWChest createChest(ChestType chestType, Location location) {
        SWChest chest = new SWChest(chestType, location);
        currentChests.add(chest);

        return chest;
    }

    /**
     *
     * @param chest the chest where you want to put items in.
     * @param items an array of items you want to put into the chest.
     */
    public void applyItems(SWChest chest, ItemStack[] items) {
        chest.getChest().getInventory().setContents(items);
    }


    /**
     * Get all chests that are registered currently!
     * @return a copy of the chest collection.
     */
    public Collection<SWChest> getCurrentChests() {
        return new HashSet<>(currentChests);
    }
}

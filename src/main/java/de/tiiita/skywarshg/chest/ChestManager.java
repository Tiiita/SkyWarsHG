package de.tiiita.skywarshg.chest;


import de.tiiita.skywarshg.util.Config;
import de.tiiita.skywarshg.util.ItemBuilder;
import de.tiiita.skywarshg.util.UniqueRandomNumberGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created on Mai 07, 2023 | 21:16:35
 * (●'◡'●)
 */
public class ChestManager {

    private final Config chestsConfig;
    private final Collection<SWChest> currentChests = new HashSet<>();


    public ChestManager(Config chestsConfig) {
        this.chestsConfig = chestsConfig;
    }


    /**
     *
     * @param chestType the type of the chest.
     * @param location the location where the chest should spawn.
     * @return the spawned chest.
     */
    public SWChest createChest(ChestType chestType, Location location) {
        SWChest chest = new SWChest(chestType, location);
        currentChests.add(chest);
        applyItems(chest, chestType);
        return chest;
    }

    public void applyItems(SWChest chest, ChestType chestType) {
        int slots = new UniqueRandomNumberGenerator(getMinSlots(), getMaxSlots()).getRandomNumber();
        UniqueRandomNumberGenerator uniqueRandomNumberGenerator = new UniqueRandomNumberGenerator(0, 27);
        for (int i = 0; i < slots; i++) {
            int randomSlot = uniqueRandomNumberGenerator.getRandomNumber();

            Set<Material> possibleMaterials = getPossibleMaterials(chestType).keySet();
            Material[] materialArray = possibleMaterials.toArray(new Material[0]);
            int randomMaterialIndex = new Random().nextInt(possibleMaterials.size());
            Material randomMaterialKey = materialArray[randomMaterialIndex];


            ItemStack randomItem = new ItemBuilder(randomMaterialKey, getPossibleMaterials(chestType).get(randomMaterialKey)).toItemStack();
            chest.getChest().getInventory().setItem(randomSlot, randomItem);
        }
    }


    /**
     * Get all chests that are registered currently!
     * @return a copy of the chest collection.
     */
    public Collection<SWChest> getCurrentChests() {
        return new HashSet<>(currentChests);
    }
    /**
     *
     * @param chestType the chest type you want to get the possible items from.
     * @return a map with the possible item and the max stack size. It returns
     * null if the path was not found in the chest config or the material was not found.
     */
    public Map<Material, Integer> getPossibleMaterials(ChestType chestType) {
        switch (chestType) {
            case NORMAL_CHEST: return getPossibleMaterialsFromConfig("normal-chest");
            case MIDDLE_CHEST: return getPossibleMaterialsFromConfig("middle-chest");
        }
        return null;
    }


    private Map<Material, Integer> getPossibleMaterialsFromConfig(String path) {
        Collection<String> entries = chestsConfig.getStringList(path);
        if (entries == null) return null;
        Map<Material, Integer> materialToMaxStackSize = new HashMap<>();

        for (String entry : entries) {
            String[] parts = entry.split("#", 2);
            String materialName = parts[0];
            materialName = materialName.replaceAll("\\d+$", "");
            int maxStackSize = Integer.parseInt(parts[1]);

            Material material = Material.getMaterial(materialName);
            if (material == null) return null;

            materialToMaxStackSize.put(material, maxStackSize);
        }

        return materialToMaxStackSize;
    }

    public int getMaxSlots() {
        return chestsConfig.getInt("max-slots-set");
    }

    public int getMinSlots() {
        return chestsConfig.getInt("min-slots-set");
    }
}

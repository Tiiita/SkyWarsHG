package de.tiiita.skywarshg.command;

import de.tiiita.skywarshg.chest.ChestManager;
import de.tiiita.skywarshg.chest.ChestType;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created on Mai 12, 2023 | 18:47:26
 * (●'◡'●)
 */
public class ChestCommand implements CommandExecutor {

    private final ChestManager chestManager;

    public ChestCommand(ChestManager chestManager) {
        this.chestManager = chestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            final Location location = ((Player) sender).getLocation();
            if (args.length != 1) {

                sender.sendMessage("§7Create a chest with /chest 1 or /chest 2");
                return true;
            }
            switch (args[0]) {

                case "1": {
                    chestManager.createChest(ChestType.NORMAL_CHEST, location);
                    sender.sendMessage("§7You have created a §anormal §7chest!");
                    break;
                }
                case "2": {
                    chestManager.createChest(ChestType.MIDDLE_CHEST, location);
                    sender.sendMessage("§7You have created a §amiddle §7chest!");
                    break;
                }

                case "remove": {
                    sender.sendMessage("§7You have removed §a" + chestManager.removeChests() + " §7chests!");
                    break;
                }

                default: sender.sendMessage("§7Create a chest with /chest 1 or /chest 2");
            }
        }
        return true;
    }
}

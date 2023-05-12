package de.tiiita.skywarshg.command;

import de.tiiita.skywarshg.chest.ChestManager;
import de.tiiita.skywarshg.chest.ChestType;
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
            chestManager.createChest(ChestType.valueOf(args[0]), ((Player) sender).getLocation());
            sender.sendMessage("§7You have created a test chest!");
        }
        return true;
    }
}

package de.tiiita.skywarshg.command;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.game.phase.GamePhase;
import de.tiiita.skywarshg.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * Created on Mai 05, 2023 | 14:45:15
 * (●'◡'●)
 */
public class StartCommand implements CommandExecutor {
    private final GameManager gameManager;
    private final Config messagesConfig;
    private final Config config;

    public StartCommand(GameManager gameManager, Config messagesConfig, Config config) {
        this.gameManager = gameManager;
        this.messagesConfig = messagesConfig;
        this.config = config;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        String noPermissions = messagesConfig.getString("no-permissions");
        String permission = config.getString("command-permission.start");
        if (!player.hasPermission(permission)) {
            player.sendMessage(noPermissions);
            return true;
        }

        if (args.length == 0) {
            if (gameManager.getCurrentGamePhase() != GamePhase.LOBBY_PHASE || gameManager.isCurrentlyCounting()) {
                String cannotDoThatMessage = messagesConfig.getString("command.start.cannot-start");
                player.sendMessage(cannotDoThatMessage);
                return true;
            }

            if (gameManager.getPlayerCount() < 2) {
                String notEnoughPlayers = messagesConfig.getString("command.start.not-enough");
                player.sendMessage(notEnoughPlayers);
                return true;
            }
            gameManager.setCurrentGamePhase(GamePhase.STARTED);
            Bukkit.broadcastMessage(messagesConfig.getString("command.start.broadcast"));
        } else {
            String usage = messagesConfig.getString("command.start.usage");
            player.sendMessage(usage);
        }
        return true;
    }
}

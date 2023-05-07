package de.tiiita.skywarshg.listener;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.game.phase.GamePhase;
import de.tiiita.skywarshg.scoreboard.GameBoard;
import de.tiiita.skywarshg.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

/**
 * Created on Mai 05, 2023 | 14:44:37
 * (●'◡'●)
 */
public class PlayerConnectionListener implements Listener {

    private final GameManager gameManager;
    private final Config messagesConfig;
    private final GameBoard gameBoard;
    private final Plugin plugin;

    public PlayerConnectionListener(GameManager gameManager, Config messagesConfig, GameBoard gameBoard, Plugin plugin) {
        this.gameManager = gameManager;
        this.messagesConfig = messagesConfig;
        this.gameBoard = gameBoard;
        this.plugin = plugin;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!gameManager.getCurrentGamePhase().equals(GamePhase.LOBBY_PHASE)) {
            event.setJoinMessage(null);
            String kickMessage = messagesConfig.getString("kick-message");
            player.kickPlayer(kickMessage);
            return;
        }
        gameManager.addPlayer(player);
        event.setJoinMessage(getFinalJoinMessage(player));

        //Player Management:
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getActivePotionEffects().clear();
        player.setLevel(0);
        player.setExp(0);
        gameBoard.set(player);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach(gameBoard::update);
        }, 15);
        player.getInventory().clear();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (gameManager.getCurrentGamePhase().equals(GamePhase.LOBBY_PHASE)) {
            event.setQuitMessage(getFinalQuitMessage(player));
        } else event.setQuitMessage(null);

        gameManager.removePlayer(player);
        event.setQuitMessage(getFinalQuitMessage(player));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach(gameBoard::update);
        }, 15);
    }


    private String getFinalJoinMessage(Player joinPlayer) {
        String joinMessage = messagesConfig.getString("join-message");
        if (joinMessage.length() == 0) return null;

        return joinMessage
                .replaceAll("%player%", joinPlayer.getName())
                .replaceAll("%players%", "" + gameManager.getPlayerCount())
                .replaceAll("%maxPlayers%", "" + gameManager.getMaxPlayers());

    }


    private String getFinalQuitMessage(Player quitPlayer) {
        String quitMessage = messagesConfig.getString("quit-message");
        if (quitMessage.length() == 0) return null;

        return quitMessage
                .replaceAll("%player%", quitPlayer.getName())
                .replaceAll("%players%", "" + gameManager.getPlayerCount())
                .replaceAll("%maxPlayers%", "" + gameManager.getMaxPlayers());

    }
}

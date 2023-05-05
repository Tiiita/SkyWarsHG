package de.tiiita.skywarshg.listener;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.util.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created on Mai 05, 2023 | 14:44:37
 * (●'◡'●)
 */
public class PlayerConnectionListener implements Listener {

    private final GameManager gameManager;
    private final Config messagesConfig;

    public PlayerConnectionListener(GameManager gameManager, Config messagesConfig) {
        this.gameManager = gameManager;
        this.messagesConfig = messagesConfig;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();


        gameManager.addPlayer(player);


        event.setJoinMessage(getFinalJoinMessage(player));
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        gameManager.removePlayer(player);
        event.setQuitMessage(getFinalQuitMessage(player));
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

package de.tiiita.skywarshg.listener;

import com.sun.istack.internal.Nullable;
import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.util.Config;
import org.bukkit.configuration.file.FileConfiguration;
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
    private final FileConfiguration config;

    public PlayerConnectionListener(GameManager gameManager, Config messagesConfig, FileConfiguration config) {
        this.gameManager = gameManager;
        this.messagesConfig = messagesConfig;
        this.config = config;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();


        gameManager.addPlayer(player);


        event.setJoinMessage(getFinalJoinMessage());
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        gameManager.removePlayer(player);
        event.setQuitMessage(getFinalQuitMessage());
    }


    @Nullable
    private String getFinalJoinMessage() {
        String joinMessage = messagesConfig.getString("join-message");
        if (joinMessage.length() == 0) return null;

        return joinMessage
                .replaceAll("%players%", "" + gameManager.getPlayerCount())
                .replaceAll("%maxPlayers%", "" + gameManager.getMaxPlayers()));

    }


    @Nullable
    private String getFinalQuitMessage() {
        String quitMessage = messagesConfig.getString("quit-message");
        if (quitMessage.length() == 0) return null;

        return quitMessage
                .replaceAll("%players%", "" + gameManager.getPlayerCount())
                .replaceAll("%maxPlayers%", "" + gameManager.getMaxPlayers());

    }
}

package de.tiiita.skywarshg.game;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

/**
 * Created on Mai 05, 2023 | 17:48:04
 * (●'◡'●)
 */
public class StatsHandler implements Listener {
    private final HashMap<Player, Integer> playerKillsMap = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        playerKillsMap.put(event.getPlayer(), 0);
    }

    public void addKill(Player player) {
        if (playerKillsMap.get(player) == null) {
            throw new NullArgumentException("Could not add kill to player. Player is null");
        }

        int currentKills = playerKillsMap.get(player);
        playerKillsMap.put(player, currentKills + 1);

    }

    public int getKills(Player player) {
        return playerKillsMap.get(player);
    }
}
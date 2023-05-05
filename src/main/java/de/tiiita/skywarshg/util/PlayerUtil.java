package de.tiiita.skywarshg.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

/**
 * @author tiiita_
 * Created on März 01, 2022 | 00:23:35
 * (●'◡'●)
 */
public class PlayerUtil {


    public static void movePlayerToServer(Player player, Plugin plugin, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

}

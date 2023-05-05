package de.tiiita.skywarshg.scoreboard;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.util.Config;
import de.tiiita.skywarshg.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Created on Mai 05, 2023 | 16:17:51
 * (●'◡'●)
 */
public class GameBoard {

    private final GameManager gameManager;
    private final Config messagesConfig;

    public GameBoard(GameManager gameManager, Config messagesConfig) {
        this.gameManager = gameManager;
        this.messagesConfig = messagesConfig;
    }


    public void setScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective("startBoard", "dummy");

        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("§f§lSkyWarsHG");

        Team playersTeam = scoreboard.registerNewTeam("players");
        Team mapTeam = scoreboard.registerNewTeam("map");
        Team killsTeam = scoreboard.registerNewTeam("kills");
        Team gameTime = scoreboard.registerNewTeam("gameTime");
        Team dateTeam = scoreboard.registerNewTeam("date");

        obj.getScore("§7§m--------------------§f").setScore(0);
        obj.getScore("§7§o ").setScore(0);
        obj.getScore("§a").setScore(0);
        obj.getScore("§fGame Time: ").setScore(0);
        obj.getScore("§fPlayers: ").setScore(0);
        obj.getScore("§f").setScore(0);
        obj.getScore("§fKills: ").setScore(0);
        obj.getScore("§fMap: ").setScore(0);
        obj.getScore("§e").setScore(0);
        obj.getScore(messagesConfig.getString("scoreboard-ip")).setScore(0);
        obj.getScore("§7§m--------------------§7").setScore(0);

        playersTeam.addEntry("§fPlayers: ");
        playersTeam.setSuffix("§a" + gameManager.getMinPlayers() + "§7/§a" + gameManager.getMaxPlayers());

        killsTeam.addEntry("§fKills: ");
        killsTeam.setSuffix("§cSoon");

        gameTime.addEntry("§fGame Time: ");
        gameTime.setSuffix("§c" + "Soon");

        mapTeam.addEntry("§fMap: ");
        mapTeam.setSuffix("§c" + "Soon");

        dateTeam.addEntry("§7§o ");
        String date = TimeUtil.getTimeInPattern("MM/dd/yyyy");
        dateTeam.setSuffix("§7§o " + date);
        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard == null) {
            setScoreboard(player);
            return;
        }
        Team playersTeam = scoreboard.getTeam("players");
        Team mapTeam = scoreboard.getTeam("map");
        Team killsTeam = scoreboard.getTeam("kills");
        Team gameTime = scoreboard.getTeam("gameTime");
        Team dateTeam = scoreboard.getTeam("date");


        String date = TimeUtil.getTimeInPattern("MM/dd/yyyy");
        dateTeam.setSuffix("§7§o " + date);

        playersTeam.setSuffix("§a" + gameManager.getMinPlayers() + "§7/§a" + gameManager.getMaxPlayers());
        killsTeam.setSuffix("§cSoon");
        gameTime.setSuffix("§c" + "Soon");
        mapTeam.setSuffix("§c" + "Soon");
    }
}

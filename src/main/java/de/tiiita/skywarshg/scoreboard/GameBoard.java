package de.tiiita.skywarshg.scoreboard;

import de.tiiita.skywarshg.game.GameManager;
import de.tiiita.skywarshg.game.StatsHandler;
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
    private final StatsHandler statsHandler;

    public GameBoard(GameManager gameManager, Config messagesConfig, StatsHandler statsHandler) {
        this.gameManager = gameManager;
        this.messagesConfig = messagesConfig;
        this.statsHandler = statsHandler;
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

        obj.getScore("§7§m-----------------§f").setScore(10);
        obj.getScore("§8§o").setScore(9);
        obj.getScore("§a").setScore(8);
        obj.getScore("§fGame Time: ").setScore(7);
        obj.getScore("§fPlayers: ").setScore(6);
        obj.getScore("§f").setScore(5);
        obj.getScore("§fKills: ").setScore(4);
        obj.getScore("§fMap: ").setScore(3);
        obj.getScore("§e").setScore(2);
        obj.getScore(messagesConfig.getString("scoreboard-ip")).setScore(1);
        obj.getScore("§7§m-----------------§7").setScore(0);

        playersTeam.addEntry("§fPlayers: ");
        playersTeam.setSuffix("§a" + gameManager.getPlayerCount() + "§7/§a" + gameManager.getMaxPlayers());

        killsTeam.addEntry("§fKills: ");
        killsTeam.setSuffix("§c" + statsHandler.getKills(player));

        gameTime.addEntry("§fGame Time: ");
        gameTime.setSuffix("§7"+ "00:00");

        mapTeam.addEntry("§fMap: ");
        mapTeam.setSuffix("§b" + "Soon");

        dateTeam.addEntry("§8§o");
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
        dateTeam.setSuffix("§8§o" + date);

        playersTeam.setSuffix("§a" + gameManager.getPlayerCount() + "§7/§a" + gameManager.getMaxPlayers());
        killsTeam.setSuffix("§c" + statsHandler.getKills(player));
        gameTime.setSuffix("§7" + "00:00");
        mapTeam.setSuffix("§b" + "Soon");
    }
}

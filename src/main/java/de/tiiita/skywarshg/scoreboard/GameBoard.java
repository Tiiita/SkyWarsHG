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

import java.util.logging.Level;

/**
 * Created on Mai 05, 2023 | 16:17:51
 * (●'◡'●)
 */
public class GameBoard {

    private final GameManager gameManager;
    private final Config messagesConfig;
    private final StatsHandler statsHandler;

    private final String title;
    private final String header;
    private final String footer;
    private final String gameText;
    private final String gameValue;
    private final String playersText;
    private final String playersValue;
    private final String killsText;
    private final String killsValue;
    private final String mapText;
    private final String mapValue;
    public GameBoard(GameManager gameManager, Config messagesConfig, StatsHandler statsHandler) {
        this.gameManager = gameManager;
        this.messagesConfig = messagesConfig;
        this.statsHandler = statsHandler;


        this.title = messagesConfig.getString("scoreboard.title");
        this.header = messagesConfig.getString("scoreboard.header")
                .replaceAll("%date%", TimeUtil.getTimeInPattern("MM/dd/yyyy"));
        this.footer = messagesConfig.getString("scoreboard.footer");

        //Game
        this.gameText = messagesConfig.getString("scoreboard.game.text");
        this.gameValue = messagesConfig.getString("scoreboard.game.value"); //Placeholder replacement later

        //Players
        this.playersText = messagesConfig.getString("scoreboard.players.text");
        this.playersValue = messagesConfig.getString("scoreboard.players.value"); //Placeholder replacement later

        //Kills
        this.killsText = messagesConfig.getString("scoreboard.kills.text");
        this.killsValue = messagesConfig.getString("scoreboard.kills.value"); //Placeholder replacement later

        //Map
        this.mapText = messagesConfig.getString("scoreboard.map.text");
        this.mapValue = messagesConfig.getString("scoreboard.map.value"); //Placeholder replacement later
    }


    public void set(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective("GameBoard", "dummy");

        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(title);

        Team playersTeam = scoreboard.registerNewTeam("players");
        Team mapTeam = scoreboard.registerNewTeam("map");
        Team killsTeam = scoreboard.registerNewTeam("kills");
        Team gameTime = scoreboard.registerNewTeam("gameTime");

        obj.getScore("§7§m-----------------§f").setScore(10);
        obj.getScore(header).setScore(9);
        obj.getScore("§a").setScore(8);
        obj.getScore(gameText).setScore(7);
        obj.getScore(playersText).setScore(6);
        obj.getScore("§f").setScore(5);
        obj.getScore(killsText).setScore(4);
        obj.getScore(mapText).setScore(3);
        obj.getScore("§e").setScore(2);
        obj.getScore(footer).setScore(1);
        obj.getScore("§7§m-----------------§7").setScore(0);

        playersTeam.addEntry(playersText);
        mapTeam.addEntry(mapText);
        killsTeam.addEntry(killsText);
        gameTime.addEntry(gameText);


        playersTeam.setSuffix(playersValue.replaceAll("%p%", "" + gameManager.getPlayerCount())
                .replaceAll("%max%", "" + gameManager.getMaxPlayers()));
        killsTeam.setSuffix(killsValue.replaceAll("%kills%", "" + statsHandler.getKills(player)));
        mapTeam.setSuffix(mapValue.replaceAll("%map%", "Soon"));
        gameTime.setSuffix(gameValue.replaceAll("%info%", "Not Started..."));
        player.setScoreboard(scoreboard);
        update(player);
    }

    public void updateTimer(Player player, String gameTimeValue) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard == null) {
            set(player);
            return;
        }
        Team gameTime = scoreboard.getTeam("gameTime");
        gameTime.setSuffix(gameValue.replaceAll("%info%", gameTimeValue));
    }
    public void update(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard == null) {
            set(player);
            return;
        }
        Team playersTeam = scoreboard.getTeam("players");
        Team mapTeam = scoreboard.getTeam("map");
        Team killsTeam = scoreboard.getTeam("kills");


        playersTeam.setSuffix(playersValue.replaceAll("%p%", "" + gameManager.getPlayerCount())
                .replaceAll("%max%", "" + gameManager.getMaxPlayers()));
        killsTeam.setSuffix(killsValue.replaceAll("%kills%", "" + statsHandler.getKills(player)));
        mapTeam.setSuffix(mapValue.replaceAll("%map%", "Soon"));
    }
}

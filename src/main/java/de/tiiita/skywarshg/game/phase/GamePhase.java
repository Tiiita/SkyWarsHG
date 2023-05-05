package de.tiiita.skywarshg.game.phase;

import de.tiiita.skywarshg.game.phase.impl.LastTwoPlayersPhase;
import de.tiiita.skywarshg.game.phase.impl.LobbyPhase;
import de.tiiita.skywarshg.game.phase.impl.StartedPhase;
import de.tiiita.skywarshg.game.phase.impl.WinningPhase;

/**
 * Created on Mai 05, 2023 | 14:59:33
 * (●'◡'●)
 */
public enum GamePhase {

    LOBBY_PHASE(),
    STARTED(),
    LAST_TWO_PLAYERS(),
    WINNING();

}

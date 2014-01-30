package se.xjobb.scardfeud.JsonGetClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Svempa on 2014-01-30.
 */
public class GameListResult {

    // All GameList json response objects will be stored here
    // And can be easily accessed all over the app

    private static List<Response> invitations = new ArrayList<Response>();
    private static List<Response> myTurns = new ArrayList<Response>();
    private static List<Response> opponentsTurns = new ArrayList<Response>();
    private static List<Response> finishedGames = new ArrayList<Response>();

    public static List<Response> getInvitations() {
        return invitations;
    }

    public static void setInvitations(List<Response> invitations) {
        GameListResult.invitations = invitations;
    }

    public static List<Response> getMyTurns() {
        return myTurns;
    }

    public static void setMyTurns(List<Response> myTurns) {
        GameListResult.myTurns = myTurns;
    }

    public static List<Response> getOpponentsTurns() {
        return opponentsTurns;
    }

    public static void setOpponentsTurns(List<Response> opponentsTurns) {
        GameListResult.opponentsTurns = opponentsTurns;
    }

    public static List<Response> getFinishedGames() {
        return finishedGames;
    }

    public static void setFinishedGames(List<Response> finishedGames) {
        GameListResult.finishedGames = finishedGames;
    }
}

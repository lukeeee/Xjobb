package se.xjobb.scardfeud;

/**
 * Created by Svempa on 2014-01-30.
 *
 * Used to save responses to invitations (Yes or No)
 * and the userId for the opponent.
 * Before sending to server
 */
public class InvitationResponse {
    private int opponentId;
    private int answer;  // 1 = Yes, 2 = No

    public InvitationResponse(int opponentId, int answer){
        this.opponentId = opponentId;
        this.answer = answer;
    }

    public int getOpponentId() {
        return opponentId;
    }

    public int getAnswer() {
        return answer;
    }
}

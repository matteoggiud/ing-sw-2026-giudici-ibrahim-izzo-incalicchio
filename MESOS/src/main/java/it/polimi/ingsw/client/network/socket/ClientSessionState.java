package it.polimi.ingsw.client.network.socket;

public class ClientSessionState {
    private String nickname;
    private Integer activeGameId;

    public ClientSessionState(String nickname, Integer activeGameId) {
        this.nickname = nickname;
        this.activeGameId = activeGameId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getActiveGameId() {
        return activeGameId;
    }

    public void setActiveGameId(Integer activeGameId) {
        this.activeGameId = activeGameId;
    }
}

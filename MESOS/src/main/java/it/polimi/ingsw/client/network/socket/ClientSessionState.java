package it.polimi.ingsw.client.network.socket;

public class ClientSessionState {
    private boolean connected;
    private String nickname;
    private Integer activeGameId;

    public ClientSessionState(String nickname, Integer activeGameId) {
        this.nickname = nickname;
        this.activeGameId = activeGameId;
        connected = true;
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

    public boolean isConnected() {
        return connected;
    }

    public void disconnect() {
        this.nickname = null;
        this.activeGameId = null;
        this.connected = false;
    }
}

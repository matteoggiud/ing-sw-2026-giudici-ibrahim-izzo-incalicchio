package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.server.model.observer.dto.PlayerInfoDTO;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClientSessionState {
    private boolean connected;
    private String nickname;
    private Integer activeGameId;
    private final Map<String, PlayerInfoDTO> playersInfo;

    public ClientSessionState(String nickname, Integer activeGameId) {
        this.nickname = nickname;
        this.activeGameId = activeGameId;
        this.connected = true;
        this.playersInfo = new LinkedHashMap<>();
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

    /**
     * Adds or updates the PlayerInfoDTO for the given player. The key is the player's nickname.
     * @param playerInfos the List of PlayerInfoDTO to add or update. If null or if the nickname is null, it will be ignored.
     */
    public void updatePlayersInfo(List<PlayerInfoDTO> playerInfos) {
        for (PlayerInfoDTO playerInfo : playerInfos) {
            if (playerInfo != null && playerInfo.nickname() != null) {
                playersInfo.put(playerInfo.nickname(), playerInfo);
            }
        }
    }

    public void updatePlayerResources(String nickname, int food, int pp) {
        PlayerInfoDTO oldInfo = playersInfo.get(nickname);

        PlayerInfoDTO updatedInfo = new PlayerInfoDTO(
                nickname,
                food,
                pp,
                oldInfo == null ? null : oldInfo.totem(),
                oldInfo == null ? null : oldInfo.deck()
        );

        playersInfo.put(nickname, updatedInfo);
    }

    public Collection<PlayerInfoDTO> getPlayersInfo() {
        return playersInfo.values();
    }

    public void clearPlayersInfo() {
        playersInfo.clear();
    }

    public void disconnect() {
        this.nickname = null;
        this.activeGameId = null;
        this.playersInfo.clear();
        this.connected = false;
    }
}

package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.observer.dto.*;


import java.util.*;

public class ClientState {
    private boolean connected;
    private String nickname;
    private Integer activeGameId;

    private List<GameInfoDTO> availableGames;
    private GameInfoDTO currentGameInfo;
    private BoardDTO board;

    private String currentPhase;
    private String currentPlayerNickname;
    private String statusMessage;
    private String errorMessage;
    private String resolvingEventName;
    private Map<String, Integer> finalRanking;
    private List<LeadBoardDTO> historicalRankings;

    private final Map<String, PlayerInfoDTO> playersInfo;

    public ClientState(String nickname, Integer activeGameId) {
        this.nickname = nickname;
        this.activeGameId = activeGameId;
        this.connected = true;
        this.availableGames = List.of();
        this.currentPhase = "MENU";
        this.playersInfo = new LinkedHashMap<>();
        this.finalRanking = Map.of();
        this.historicalRankings = List.of();
    }

    public ClientState() {
        this(null, null);
        this.connected = false;
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

    public List<GameInfoDTO> getAvailableGames() {
        return availableGames;
    }

    public void setAvailableGames(List<GameInfoDTO> availableGames) {
        this.availableGames = availableGames == null ? List.of() : List.copyOf(availableGames);
    }

    public GameInfoDTO getCurrentGameInfo() {
        return currentGameInfo;
    }

    public void setCurrentGameInfo(GameInfoDTO currentGameInfo) {
        this.currentGameInfo = currentGameInfo;
    }

    public BoardDTO getBoard() {
        return board;
    }

    public void setBoard(BoardDTO board) {
        this.board = board;
    }

    public String getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(String currentPhase) {
        this.currentPhase = currentPhase;
    }

    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }

    private final Deque<EventCardDTO> resolvingQueue = new ArrayDeque<>();

    public void setCurrentPlayerNickname(String currentPlayerNickname) {
        this.currentPlayerNickname = currentPlayerNickname;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        this.errorMessage = null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResolvingEventName() {
        return resolvingEventName;
    }

    public void setResolvingEventName(String resolvingEventName) {
        this.resolvingEventName = resolvingEventName;
    }

    public Map<String, Integer> getFinalRanking() {
        return finalRanking;
    }

    public void setFinalRanking(Map<String, Integer> finalRanking) {
        this.finalRanking = finalRanking == null ? Map.of() : new LinkedHashMap<>(finalRanking);
    }

    public void updatePlayersInfo(List<PlayerInfoDTO> playerInfos) {
        if (playerInfos == null) {
            return;
        }

        playersInfo.clear();

        for (PlayerInfoDTO playerInfo : playerInfos) {
            if (playerInfo != null && playerInfo.nickname() != null) {
                playersInfo.put(playerInfo.nickname(), playerInfo);
            }
        }
    }

    public Collection<PlayerInfoDTO> getPlayersInfo() {
        return playersInfo.values();
    }

    public void clearPlayersInfo() {
        playersInfo.clear();
    }

    public void clearGameState() {
        activeGameId = null;
        currentGameInfo = null;
        board = null;
        currentPhase = "MENU";
        currentPlayerNickname = null;
        resolvingEventName = null;
        finalRanking = Map.of();
        playersInfo.clear();
        historicalRankings = List.of();
    }

    public void disconnect() {
        nickname = null;
        clearGameState();
        connected = false;
    }

    public void connect() {
        clearGameState();
        this.nickname = null;
        this.connected = true;
    }

    public List<LeadBoardDTO> getHistoricalRankings() {
        return historicalRankings;
    }

    public void setHistoricalRankings(List<LeadBoardDTO> historicalRankings) {
        this.historicalRankings = historicalRankings == null ? List.of() : List.copyOf(historicalRankings);
    }


    public void pushEventCard(EventCardDTO eventCard) {
        synchronized (resolvingQueue) {
            resolvingQueue.addLast(eventCard);
        }
    }

    public EventCardDTO popEventCard() {
        synchronized (resolvingQueue) {
            return resolvingQueue.removeFirst();
        }
    }
}

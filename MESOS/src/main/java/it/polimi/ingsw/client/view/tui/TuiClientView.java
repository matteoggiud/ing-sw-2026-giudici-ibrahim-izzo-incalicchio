package it.polimi.ingsw.client.view.tui;

import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.common.network.dto.*;
import it.polimi.ingsw.common.network.dto.*;

import java.util.Collection;
import java.util.List;

public class TuiClientView implements ClientView {
    private final TerminalRenderer renderer;

    public TuiClientView(ClientState sessionState) {
        this.renderer = new TerminalRenderer(sessionState);
    }

    @Override
    public void showMessage(String message) {
        renderer.render();
    }

    @Override
    public void showError(String message) {
        renderer.renderError(message);
    }

    @Override
    public void showDisconnected(String message) {
        renderer.render();
    }


    @Override
    public void showAvailableGames(List<GameInfoDTO> games) {
        renderer.render();
    }


    @Override
    public void showGameInfo(GameInfoDTO gameInfo) {
        renderer.render();
    }


    @Override
    public void showGameStarted(int gameId, List<String> playerNicknames) {
        renderer.render();
    }


    @Override
    public void showBoardSummary(BoardDTO board) {
        renderer.render();
    }


    @Override
    public void showPlaceTotemTurn(String currentPlayerNickname, boolean isMyTurn) {
        renderer.render();
    }


    @Override
    public void showResolveActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        renderer.render();
    }


    @Override
    public void showExtraActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        renderer.render();
    }


    @Override
    public void showPlayerResources(String playerNickname, int food, int prestigePoints, boolean isMe) {
        renderer.render();
    }


    @Override
    public void showPlayersInfo(Collection<PlayerInfoDTO> playersInfo, String myNickname) {
        renderer.render();
    }

    public void showMyDeck() {
        renderer.renderMyDeck();
    }

    public void showDecks() {
        renderer.renderDecks();
    }

    public void showDeck(String nickname) {
        renderer.renderDeck(nickname);
    }



    @Override
    public void showPlayerDeck(String playerNickname, PlayerDeckDTO playerDeck, boolean isMe) {
        renderer.render();
    }


    @Override
    public void showGameEnded() {
        renderer.render();
    }

    @Override
    public void showAuthenticator(String message) {
        renderer.render();
    }

    @Override
    public void showResolvingEvent(int gameId, EventCardDTO eventCardDTO) {
        renderer.render();
    }

    @Override
    public void shutdown(String reason) {
        renderer.render();
    }


    @Override
    public void showRankings(List<LeadBoardDTO> rankings) {
        renderer.render();
    }


}

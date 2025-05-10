package org.example.cartgame.repository;

import org.example.cartgame.model.Card;
import org.example.cartgame.model.Suits;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GameRepository {

    Optional<Card> getCardFromDeck();

    List<Card> getCards();

    void setCards(List<Card> cards);

    Optional<Card> getCardFromPlayer(String playerId, Card card);

    List<Card> getPlayerCards(String playerId);

    void setPlayerCards(String playerId, List<Card> playedOneCards);

    Map<String, List<Card>> getPlayerCardsMap();

    List<String> getPlayersId();

    void clearPlayerCardsMap();

    Suits getTrump();

    void setTrump(Suits trump);

    String getAttacker();

    void setAttacker(String attacker);

}

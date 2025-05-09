package org.example.cartgame.repository;

import org.example.cartgame.model.Card;
import org.example.cartgame.model.Players;
import org.example.cartgame.model.Suits;

import java.util.List;
import java.util.Optional;

public interface GameRepository {

    Optional<Card> getCardFromDeck();

    List<Card> getCards();

    void setCards(List<Card> cards);

    Optional<Card> getCardFromPlayer(Players player, Card card);

    void setPlayedOneCards(List<Card> playedOneCards);

    List<Card> getPlayedOneCards();

    void setPlayedTwoCards(List<Card> playedTwoCards);

    List<Card> getPlayedTwoCards();

    Suits getTrump();

    void setTrump(Suits trump);

    Players getAttacker();

    void setAttacker(Players attacker);

}

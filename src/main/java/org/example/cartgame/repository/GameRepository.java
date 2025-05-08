package org.example.cartgame.repository;

import org.example.cartgame.model.Card;
import org.example.cartgame.model.Players;
import org.example.cartgame.model.Suits;

import java.util.List;
import java.util.Optional;

public interface GameRepository {

    Optional<Card> getCardFromDeck();

    List<Card> getCards();

    Optional<Card> getCardFromPlayer(Players player, Card card);

    List<Card> getPlayedOneCards();

    void setPlayedOneCards(List<Card> playedOneCards);

    void setPlayedTwoCards(List<Card> playedTwoCards);

    List<Card> getPlayedTwoCards();

    void shuffleCards();

    Suits getTrump();

    Players getAttacker();

    void setAttacker(Players attacker);

}

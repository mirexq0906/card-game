package org.example.cartgame.repository;

import org.example.cartgame.model.Card;
import org.example.cartgame.model.Suits;

import java.util.List;
import java.util.Optional;

public interface GameRepository {

    Optional<Card> getCardFromDeck();

    List<Card> getCards();

    List<Card> getPlayedOneCards();

    void setPlayedOneCards(List<Card> playedOneCards);

    void setPlayedTwoCards(List<Card> playedTwoCards);

    List<Card> getPlayedTwoCards();

    void shuffleCards();

    Suits getTrump();

}

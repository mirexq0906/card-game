package org.example.cartgame.repository;

import org.example.cartgame.model.Card;
import org.example.cartgame.model.CardPair;

import java.util.List;
import java.util.Optional;

public interface CardPairRepository {

    List<CardPair> getAllCardPairs();

    Optional<CardPair> getCardPairByAttackCard(Card attackCard);

    void setCardPair(CardPair pair);

    void clearCardPairs();

}

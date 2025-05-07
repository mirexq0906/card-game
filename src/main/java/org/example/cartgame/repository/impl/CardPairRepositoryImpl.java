package org.example.cartgame.repository.impl;

import org.example.cartgame.model.Card;
import org.example.cartgame.model.CardPair;
import org.example.cartgame.repository.CardPairRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CardPairRepositoryImpl implements CardPairRepository {

    private final Set<CardPair> cardPairs = new HashSet<>();

    @Override
    public List<CardPair> getAllCardPairs() {
        return this.cardPairs.stream().toList();
    }

    @Override
    public Optional<CardPair> getCardPairByAttackCard(Card attackCard) {
        for (CardPair cardPair : cardPairs) {
            if (cardPair.getAttackCard().equals(attackCard)) {
                CardPair copy = CardPair.builder()
                        .attackCard(cardPair.getAttackCard())
                        .defenseCard(cardPair.getDefenseCard())
                        .build();
                return Optional.of(copy);
            }
        }
        return Optional.empty();
    }

    @Override
    public void setCardPair(CardPair pair) {
        this.cardPairs.remove(pair);
        this.cardPairs.add(pair);
    }

    @Override
    public void clearCardPairs() {
        this.cardPairs.clear();
    }

}

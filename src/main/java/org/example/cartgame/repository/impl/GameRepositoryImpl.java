package org.example.cartgame.repository.impl;

import org.example.cartgame.model.Card;
import org.example.cartgame.model.Player;
import org.example.cartgame.model.Suits;
import org.example.cartgame.repository.GameRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private List<Card> cards = new ArrayList<>();
    private final Map<String, List<Card>> playerCardsMap = new HashMap<>();
    private String attacker;
    private Suits trump;

    @Override
    public Optional<Card> getCardFromDeck() {
        if (this.cards.isEmpty()) {
            return Optional.empty();
        }
        Card card = this.cards.get(0);
        this.cards.remove(card);
        return Optional.of(card);
    }

    @Override
    public List<Card> getCards() {
        return new ArrayList<>(this.cards);
    }

    @Override
    public void setCards(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    @Override
    public Optional<Card> getCardFromPlayer(String playerId, Card card) {
        if (this.playerCardsMap.containsKey(playerId) && this.playerCardsMap.get(playerId).remove(card)) {
            return Optional.of(card);
        }

        return Optional.empty();
    }

    @Override
    public List<Card> getPlayerCards(String playerId) {
        return new ArrayList<>(this.playerCardsMap.get(playerId));
    }

    @Override
    public void setPlayerCards(String playerId, List<Card> playedOneCards) {
        this.playerCardsMap.put(playerId, new ArrayList<>(playedOneCards));
    }

    @Override
    public Map<String, List<Card>> getPlayerCardsMap() {
        Map<String, List<Card>> copyMap = new HashMap<>();
        for (Map.Entry<String, List<Card>> entry : this.playerCardsMap.entrySet()) {
            copyMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copyMap;
    }

    @Override
    public List<String> getPlayersId() {
        return new ArrayList<>(this.playerCardsMap.keySet());
    }

    @Override
    public void clearPlayerCardsMap() {
        this.playerCardsMap.clear();
    }

    @Override
    public Suits getTrump() {
        return this.trump;
    }

    @Override
    public void setTrump(Suits trump) {
        this.trump = trump;
    }

    @Override
    public String getAttacker() {
        return this.attacker;
    }

    @Override
    public void setAttacker(String attacker) {
        this.attacker = attacker;
    }

}

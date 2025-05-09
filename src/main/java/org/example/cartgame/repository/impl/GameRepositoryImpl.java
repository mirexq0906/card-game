package org.example.cartgame.repository.impl;

import org.example.cartgame.model.Card;
import org.example.cartgame.model.Players;
import org.example.cartgame.model.Suits;
import org.example.cartgame.repository.GameRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private List<Card> cards = new ArrayList<>();
    private List<Card> playerOneCards = new ArrayList<>();
    private List<Card> playerTwoCards = new ArrayList<>();
    private Players attacker;
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
    public Optional<Card> getCardFromPlayer(Players player, Card card) {
        switch (player) {
            case PLAYER_ONE -> {
                if (this.playerOneCards.remove(card)) {
                    return Optional.of(card);
                }
            }
            case PLAYER_TWO -> {
                if (this.playerTwoCards.remove(card)) {
                    return Optional.of(card);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Card> getPlayedOneCards() {
        return new ArrayList<>(this.playerOneCards);
    }

    @Override
    public void setPlayedOneCards(List<Card> playedOneCards) {
        this.playerOneCards = new ArrayList<>(playedOneCards);
    }

    @Override
    public List<Card> getPlayedTwoCards() {
        return new ArrayList<>(this.playerTwoCards);
    }

    @Override
    public void setPlayedTwoCards(List<Card> playedTwoCards) {
        this.playerTwoCards = new ArrayList<>(playedTwoCards);
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
    public Players getAttacker() {
        return this.attacker;
    }

    @Override
    public void setAttacker(Players attacker) {
        this.attacker = attacker;
    }

}

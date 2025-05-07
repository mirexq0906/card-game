package org.example.cartgame.repository.impl;

import org.example.cartgame.model.Card;
import org.example.cartgame.model.Suits;
import org.example.cartgame.repository.GameRepository;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private final List<Card> cards = new ArrayList<>();
    private List<Card> playerOneCards = new ArrayList<>();
    private List<Card> playerTwoCards = new ArrayList<>();
    private final Suits trump;

    {
        try(
                InputStream stream = getClass().getResourceAsStream("/assets/Cards.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] params = line.split("; ");

                if (params.length != 2) {
                    throw new RuntimeException("Failed to parse");
                }

                this.cards.add(
                        Card.builder()
                                .name(params[0])
                                .suit(Suits.valueOf(params[1]))
                                .build()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.trump = Arrays.stream(Suits.values()).findAny().get();
    }

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
    public void shuffleCards() {
        Collections.shuffle(this.cards);
    }

    @Override
    public Suits getTrump() {
        return this.trump;
    }

}

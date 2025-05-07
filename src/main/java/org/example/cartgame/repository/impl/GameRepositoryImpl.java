package org.example.cartgame.repository.impl;

import org.example.cartgame.model.Card;
import org.example.cartgame.model.Suits;
import org.example.cartgame.repository.GameRepository;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private final List<Card> cards = new ArrayList<>();

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

    }

    @Override
    public List<Card> getCardsByCount(Integer count) {
        Collections.shuffle(this.cards);
        return this.cards.subList(0, count);
    }

}

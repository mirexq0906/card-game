package org.example.cartgame.parser;

import org.example.cartgame.model.Card;
import org.example.cartgame.model.Suits;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class TxtFileParser {

    public List<Card> parseCards(String filePath) {
        try(
                InputStream stream = getClass().getResourceAsStream(filePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            List<Card> cards = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] params = line.split("; ");

                if (params.length != 3) {
                    throw new RuntimeException("Failed to parse");
                }

                cards.add(
                        Card.builder()
                                .name(params[0])
                                .suit(Suits.valueOf(params[1]))
                                .rank(Integer.parseInt(params[2]))
                                .build()
                );
            }

            return cards;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

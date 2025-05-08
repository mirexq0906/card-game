package org.example.cartgame.model;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class Card {

    private String name;
    private Suits suit;
    private Integer rank;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(name, card.name) && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, suit);
    }

}

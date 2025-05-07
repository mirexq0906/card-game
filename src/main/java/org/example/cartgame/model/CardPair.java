package org.example.cartgame.model;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class CardPair {

    private Card attackCard;
    private Card defenseCard;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CardPair cardPair = (CardPair) o;
        return Objects.equals(attackCard, cardPair.attackCard);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(attackCard);
    }
}

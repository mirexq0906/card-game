package org.example.cartgame.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Card {

    private String name;

    private Suits suit;

}

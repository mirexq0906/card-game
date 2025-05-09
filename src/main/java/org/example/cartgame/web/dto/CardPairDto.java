package org.example.cartgame.web.dto;

import lombok.Builder;
import lombok.Data;
import org.example.cartgame.model.Card;

@Data
@Builder
public class CardPairDto {

    private String playerId;
    private Card attackCard;
    private Card defenseCard;

}

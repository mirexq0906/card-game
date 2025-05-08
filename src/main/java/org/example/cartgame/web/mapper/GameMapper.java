package org.example.cartgame.web.mapper;

import org.example.cartgame.model.CardPair;
import org.example.cartgame.web.dto.CardPairDto;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public CardPair dtoToCardPair(CardPairDto cardPairDto) {
        return CardPair.builder()
                .attackCard(cardPairDto.getAttackCard())
                .defenseCard(cardPairDto.getDefenseCard())
                .build();
    }

}

package org.example.cartgame.web.mapper;

import org.example.cartgame.model.CardPair;
import org.example.cartgame.web.dto.CardPairDto;
import org.example.cartgame.web.response.CardPairResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameMapper {

    public CardPair dtoToCardPair(CardPairDto cardPairDto) {
        return CardPair.builder()
                .attackCard(cardPairDto.getAttackCard())
                .defenseCard(cardPairDto.getDefenseCard())
                .build();
    }

    public CardPairResponse cardPairListToResponse(List<CardPair> cardPairList) {
        return CardPairResponse.builder()
                .cardPairs(cardPairList)
                .build();
    }

}

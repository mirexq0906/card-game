package org.example.cartgame.web.response;

import lombok.Builder;
import lombok.Data;
import org.example.cartgame.model.CardPair;

import java.util.List;

@Data
@Builder
public class CardPairResponse {

    private List<CardPair> cardPairs;

}

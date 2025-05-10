package org.example.cartgame.web.response;

import lombok.Builder;
import lombok.Data;
import org.example.cartgame.model.Card;
import org.example.cartgame.model.CardPair;
import org.example.cartgame.model.Player;
import org.example.cartgame.model.Suits;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class GameResponse {

    private Map<String, List<Card>> playerCardsMap;
    private List<CardPair> cardPairsOnTable;
    private List<Player> players;
    private Integer countCards;
    private String attacker;
    private Suits trump;
    private String message;

}

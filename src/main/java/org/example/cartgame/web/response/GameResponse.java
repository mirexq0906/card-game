package org.example.cartgame.web.response;

import lombok.Builder;
import lombok.Data;
import org.example.cartgame.model.Card;
import org.example.cartgame.model.CardPair;
import org.example.cartgame.model.Players;
import org.example.cartgame.model.Suits;

import java.util.List;

@Data
@Builder
public class GameResponse {

    private List<Card> playerOneCards;
    private List<Card> playerTwoCards;
    private List<CardPair> cardPairsOnTable;
    private Integer countCards;
    private Players attacker;
    private Suits trump;
    private String message;

}

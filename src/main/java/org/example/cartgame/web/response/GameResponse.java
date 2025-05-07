package org.example.cartgame.web.response;

import lombok.Builder;
import lombok.Data;
import org.example.cartgame.model.Card;
import org.example.cartgame.model.Suits;

import java.util.List;

@Data
@Builder
public class GameResponse {

    private List<Card> playerOneCards;
    private List<Card> playerTwoCards;
    private Integer countCards;
    private Suits trump;

}

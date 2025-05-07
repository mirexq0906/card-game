package org.example.cartgame.web.response;

import lombok.Builder;
import lombok.Data;
import org.example.cartgame.model.Card;

import java.util.List;

@Data
@Builder
public class GameResponse {

    private List<Card> cards;

}

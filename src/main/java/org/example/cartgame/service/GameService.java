package org.example.cartgame.service;

import org.example.cartgame.web.dto.CardPairDto;
import org.example.cartgame.web.response.GameResponse;

public interface GameService {

    GameResponse start();

    GameResponse attack(CardPairDto cardPairDto);

    GameResponse defend(CardPairDto cardPairDto);

    GameResponse endTurn();

}

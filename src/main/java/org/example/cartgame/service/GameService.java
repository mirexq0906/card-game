package org.example.cartgame.service;

import org.example.cartgame.model.Card;
import org.example.cartgame.web.dto.CardPairDto;
import org.example.cartgame.web.response.CardPairResponse;
import org.example.cartgame.web.response.GameResponse;

import java.util.List;

public interface GameService {

    GameResponse start();

    CardPairResponse attack(CardPairDto cardPairDto);

    CardPairResponse defend(CardPairDto cardPairDto);

}

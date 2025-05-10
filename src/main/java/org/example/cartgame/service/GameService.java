package org.example.cartgame.service;

import org.example.cartgame.web.dto.CardPairDto;
import org.example.cartgame.web.dto.PlayerDto;
import org.example.cartgame.web.response.GameResponse;

import java.util.List;

public interface GameService {

    void start(PlayerDto startGameDto);

    void attack(CardPairDto cardPairDto);

    void defend(CardPairDto cardPairDto);

    void endTurn(PlayerDto playerDto);

    void finish(PlayerDto playerDto);

}

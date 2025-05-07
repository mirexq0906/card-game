package org.example.cartgame.web.controller;


import lombok.RequiredArgsConstructor;
import org.example.cartgame.service.GameService;
import org.example.cartgame.web.dto.CardPairDto;
import org.example.cartgame.web.response.CardPairResponse;
import org.example.cartgame.web.response.GameResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @MessageMapping("/start")
    @SendTo("/topic/player")
    public GameResponse start() {
        return this.gameService.start();
    }

    @MessageMapping("/attack")
    @SendTo("/topic/table")
    public CardPairResponse attack(CardPairDto cardPairDto) {
        return this.gameService.attack(cardPairDto);
    }

    @MessageMapping("/defend")
    @SendTo("/topic/table")
    public CardPairResponse defend(CardPairDto cardPairDto) {
        return this.gameService.defend(cardPairDto);
    }

}

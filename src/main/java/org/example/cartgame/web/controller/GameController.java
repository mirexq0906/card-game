package org.example.cartgame.web.controller;


import lombok.RequiredArgsConstructor;
import org.example.cartgame.exception.GameException;
import org.example.cartgame.service.GameService;
import org.example.cartgame.web.dto.CardPairDto;
import org.example.cartgame.web.mapper.GameMapper;
import org.example.cartgame.web.response.GameResponse;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final GameMapper gameMapper;

    @MessageMapping("/start")
    @SendTo("/topic/game")
    public GameResponse start() {
        return  this.gameService.start();
    }

    @MessageMapping("/attack")
    @SendTo("/topic/game")
    public GameResponse attack(CardPairDto cardPairDto) {
        return this.gameService.attack(cardPairDto);
    }

    @MessageMapping("/defend")
    @SendTo("/topic/game")
    public GameResponse defend(CardPairDto cardPairDto) {
        return this.gameService.defend(cardPairDto);
    }

    @MessageMapping("/end-turn")
    @SendTo("/topic/game")
    public GameResponse endTurn() {
        return this.gameService.endTurn();
    }

    @MessageExceptionHandler
    @SendTo("/topic/game")
    public GameResponse handleException(GameException e) {
        return this.gameMapper.toGameResponseWithMessage(e.getMessage());
    }

}

package org.example.cartgame.web.controller;


import lombok.RequiredArgsConstructor;
import org.example.cartgame.exception.GameException;
import org.example.cartgame.model.Player;
import org.example.cartgame.repository.PlayerRepository;
import org.example.cartgame.service.GameService;
import org.example.cartgame.web.dto.CardPairDto;
import org.example.cartgame.web.dto.PlayerDto;
import org.example.cartgame.web.mapper.GameMapper;
import org.example.cartgame.web.response.GameResponse;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final PlayerRepository playerRepository;
    private final GameMapper gameMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/start")
    public void start(PlayerDto startGameDto) {
        this.gameService.start(startGameDto);
        this.sendMessage();
    }

    @MessageMapping("/attack")
    @SendTo("/topic/game")
    public void attack(CardPairDto cardPairDto) {
        this.gameService.attack(cardPairDto);
        this.sendMessage();
    }

    @MessageMapping("/defend")
    public void defend(CardPairDto cardPairDto) {
        this.gameService.defend(cardPairDto);
        this.sendMessage();
    }

    @MessageMapping("/end-turn")
    public void endTurn(PlayerDto playerDto) {
        this.gameService.endTurn(playerDto);
        this.sendMessage();
    }

    @MessageMapping("/finish")
    public void finish(PlayerDto playerDto) {
        this.gameService.finish(playerDto);
        this.sendMessage();
    }

    @MessageExceptionHandler
    public void handleException(GameException e) {
        GameResponse gameResponse = this.gameMapper.toGameResponseWithMessage(e.getPlayerId(), e.getMessage());
        this.simpMessagingTemplate.convertAndSend("/topic/game/" + e.getPlayerId(), gameResponse);
    }

    private void sendMessage() {
        for (Player player : this.playerRepository.getPlayers()) {
            this.simpMessagingTemplate.convertAndSend(
                    "/topic/game/" + player.getPlayerId(),
                    this.gameMapper.toGameResponse(player.getPlayerId())
            );
        }

    }

}

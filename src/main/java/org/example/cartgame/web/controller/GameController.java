package org.example.cartgame.web.controller;


import lombok.RequiredArgsConstructor;
import org.example.cartgame.service.GameService;
import org.example.cartgame.web.response.GameResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @MessageMapping("/start")
    @SendTo("/topic/start")
    public GameResponse start() {
        GameResponse response = GameResponse.builder()
                .cards(this.gameService.start())
                .build();

        System.out.println(response);
        return response;
    }

}

package org.example.cartgame.exception;

import lombok.Getter;

@Getter
public class GameException extends RuntimeException {

    private final String playerId;

    public GameException(String message, String playerId) {
        super(message);
        this.playerId = playerId;
    }
}

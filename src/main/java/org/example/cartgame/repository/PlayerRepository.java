package org.example.cartgame.repository;

import org.example.cartgame.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository {

    List<Player> getPlayers();

    Optional<Player> getPlayer(String playerId);

    void setPlayer(Player player);

    boolean isExistByPlayerId(String playerId);

    void clearPlayers();

}

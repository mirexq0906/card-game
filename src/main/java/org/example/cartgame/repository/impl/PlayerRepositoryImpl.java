package org.example.cartgame.repository.impl;

import org.example.cartgame.model.Player;
import org.example.cartgame.repository.PlayerRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    private final Set<Player> players = new HashSet<>();

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public Optional<Player> getPlayer(String playerId) {
        return this.players.stream().filter(player -> player.getPlayerId().equals(playerId)).findFirst();
    }

    @Override
    public void setPlayer(Player player) {
        this.players.remove(player);
        this.players.add(player);
    }

    @Override
    public boolean isExistByPlayerId(String playerId) {
        return this.players.stream().anyMatch(player -> player.getPlayerId().equals(playerId));
    }

    @Override
    public void clearPlayers() {
        this.players.clear();
    }


}

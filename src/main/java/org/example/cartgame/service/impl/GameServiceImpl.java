package org.example.cartgame.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.cartgame.model.Card;
import org.example.cartgame.repository.GameRepository;
import org.example.cartgame.service.GameService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public List<Card> start() {
        return this.gameRepository.getCardsByCount(12);
    }

}

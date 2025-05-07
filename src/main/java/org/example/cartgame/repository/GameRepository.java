package org.example.cartgame.repository;

import org.example.cartgame.model.Card;

import java.util.List;

public interface GameRepository {

    List<Card> getCardsByCount(Integer count);

}

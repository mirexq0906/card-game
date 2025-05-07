package org.example.cartgame.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.cartgame.model.Card;
import org.example.cartgame.model.CardPair;
import org.example.cartgame.repository.CardPairRepository;
import org.example.cartgame.repository.GameRepository;
import org.example.cartgame.service.GameService;
import org.example.cartgame.web.dto.CardPairDto;
import org.example.cartgame.web.mapper.GameMapper;
import org.example.cartgame.web.response.CardPairResponse;
import org.example.cartgame.web.response.GameResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final CardPairRepository cardPairRepository;
    private final GameMapper gameMapper;

    @Override
    public GameResponse start() {
        this.gameRepository.shuffleCards();
        this.takeCards();
        return GameResponse.builder()
                .playerOneCards(this.gameRepository.getPlayedOneCards())
                .playerTwoCards(this.gameRepository.getPlayedTwoCards())
                .countCards(this.gameRepository.getCards().size())
                .trump(this.gameRepository.getTrump())
                .build();
    }

    @Override
    public CardPairResponse attack(CardPairDto cardPairDto) {
        this.cardPairRepository.setCardPair(
                this.gameMapper.dtoToCardPair(cardPairDto)
        );

        return this.gameMapper.cardPairListToResponse(
                this.cardPairRepository.getAllCardPairs()
        );
    }

    @Override
    public CardPairResponse defend(CardPairDto cardPairDto) {
        this.cardPairRepository.setCardPair(
                this.gameMapper.dtoToCardPair(cardPairDto)
        );

        return this.gameMapper.cardPairListToResponse(
                this.cardPairRepository.getAllCardPairs()
        );
    }

    private void takeCards() {
        List<Card> playedOneCards = this.gameRepository.getPlayedOneCards();
        List<Card> playedTwoCards = this.gameRepository.getPlayedTwoCards();
        Optional<Card> card;
        while (playedOneCards.size() < 6) {
            card = this.gameRepository.getCardFromDeck();
            card.ifPresent(playedOneCards::add);
        }

        while (playedTwoCards.size() < 6) {
            card = this.gameRepository.getCardFromDeck();
            card.ifPresent(playedTwoCards::add);
        }

        this.gameRepository.setPlayedOneCards(playedOneCards);
        this.gameRepository.setPlayedTwoCards(playedTwoCards);
    }

}

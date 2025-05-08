package org.example.cartgame.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.cartgame.model.Card;
import org.example.cartgame.model.CardPair;
import org.example.cartgame.model.Players;
import org.example.cartgame.model.Suits;
import org.example.cartgame.repository.CardPairRepository;
import org.example.cartgame.repository.GameRepository;
import org.example.cartgame.service.GameService;
import org.example.cartgame.web.dto.CardPairDto;
import org.example.cartgame.web.mapper.GameMapper;
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
        return this.getGameResponse();
    }

    @Override
    public GameResponse attack(CardPairDto cardPairDto) {
        CardPair cardPair = this.gameMapper.dtoToCardPair(cardPairDto);
        this.gameRepository.getCardFromPlayer(
                this.gameRepository.getAttacker(),
                cardPair.getAttackCard()
        ).orElseThrow(() -> new RuntimeException("Card not found"));

        this.cardPairRepository.setCardPair(cardPair);

        return this.getGameResponse();
    }

    @Override
    public GameResponse defend(CardPairDto cardPairDto) {
        CardPair cardPair = this.gameMapper.dtoToCardPair(cardPairDto);

        if (!this.canBeat(cardPair)) {
            return this.getResponseWithMessage("Не удалось отбить этой картой");
        }

        this.gameRepository.getCardFromPlayer(
                this.gameRepository.getAttacker() == Players.PLAYER_ONE ? Players.PLAYER_TWO : Players.PLAYER_ONE,
                cardPair.getDefenseCard()
        ).orElseThrow(() -> new RuntimeException("Card not found"));

        this.cardPairRepository.setCardPair(cardPair);

        return this.getGameResponse();
    }

    @Override
    public GameResponse endTurn() {
        List<CardPair> cardPairList = this.cardPairRepository.getAllCardPairs();
        for (CardPair cardPair : cardPairList) {
            if (cardPair.getDefenseCard() == null) {
                switch (this.gameRepository.getAttacker()) {
                    case PLAYER_ONE -> {
                        this.takeCardFromTable(
                                this.gameRepository.getPlayedTwoCards(),
                                cardPairList,
                                Players.PLAYER_TWO
                        );
                    }
                    case PLAYER_TWO -> {
                        this.takeCardFromTable(
                                this.gameRepository.getPlayedOneCards(),
                                cardPairList,
                                Players.PLAYER_ONE
                        );
                    }
                }
            }
        }

        this.cardPairRepository.clearCardPairs();
        this.takeCards();
        return this.getGameResponse();
    }

    private boolean canBeat(CardPair cardPair) {
        Suits trump = this.gameRepository.getTrump();
        Card defenseCard = cardPair.getDefenseCard();
        Card attackCard = cardPair.getAttackCard();

        if (defenseCard.getSuit().equals(attackCard.getSuit()) && defenseCard.getRank() > attackCard.getRank()) {
            return true;
        }

        if (defenseCard.getSuit().equals(trump) && !attackCard.getSuit().equals(trump)) {
            return true;
        }

        return false;
    }

    private void takeCardFromTable(List<Card> playerCard, List<CardPair> cardPairList, Players player) {
        for (CardPair cardPair : cardPairList) {
            if (cardPair.getDefenseCard() != null) {
                playerCard.add(cardPair.getDefenseCard());
            }
            if (cardPair.getAttackCard() != null) {
                playerCard.add(cardPair.getAttackCard());
            }
        }

        switch (player) {
            case PLAYER_ONE -> this.gameRepository.setPlayedOneCards(playerCard);
            case PLAYER_TWO -> this.gameRepository.setPlayedTwoCards(playerCard);
        }
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

    private GameResponse getGameResponse() {
        return GameResponse.builder()
                .playerOneCards(this.gameRepository.getPlayedOneCards())
                .playerTwoCards(this.gameRepository.getPlayedTwoCards())
                .countCards(this.gameRepository.getCards().size())
                .trump(this.gameRepository.getTrump())
                .cardPairsOnTable(this.cardPairRepository.getAllCardPairs())
                .attacker(this.gameRepository.getAttacker())
                .build();
    }

    private GameResponse getResponseWithMessage(String message) {
        GameResponse gameResponse = this.getGameResponse();
        gameResponse.setMessage(message);
        return gameResponse;
    }

}

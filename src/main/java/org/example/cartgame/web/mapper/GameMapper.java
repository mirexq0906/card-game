package org.example.cartgame.web.mapper;

import lombok.RequiredArgsConstructor;
import org.example.cartgame.model.CardPair;
import org.example.cartgame.repository.CardPairRepository;
import org.example.cartgame.repository.GameRepository;
import org.example.cartgame.web.dto.CardPairDto;
import org.example.cartgame.web.response.GameResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameMapper {

    private final GameRepository gameRepository;
    private final CardPairRepository cardPairRepository;

    public CardPair dtoToCardPair(CardPairDto cardPairDto) {
        return CardPair.builder()
                .attackCard(cardPairDto.getAttackCard())
                .defenseCard(cardPairDto.getDefenseCard())
                .build();
    }

    public GameResponse toGameResponse() {
        return GameResponse.builder()
                .playerOneCards(gameRepository.getPlayedOneCards())
                .playerTwoCards(gameRepository.getPlayedTwoCards())
                .countCards(gameRepository.getCards().size())
                .trump(gameRepository.getTrump())
                .cardPairsOnTable(cardPairRepository.getAllCardPairs())
                .attacker(gameRepository.getAttacker())
                .build();
    }

    public GameResponse toGameResponseWithMessage(String message) {
        GameResponse gameResponse = this.toGameResponse();
        gameResponse.setMessage(message);
        return gameResponse;
    }

}

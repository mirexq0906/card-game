package org.example.cartgame.web.mapper;

import lombok.RequiredArgsConstructor;
import org.example.cartgame.model.Card;
import org.example.cartgame.model.CardPair;
import org.example.cartgame.repository.CardPairRepository;
import org.example.cartgame.repository.GameRepository;
import org.example.cartgame.web.dto.CardPairDto;
import org.example.cartgame.web.response.GameResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    public GameResponse toGameResponse(String playerId) {
        Map<String, List<Card>> playerCardsMap = this.gameRepository.getPlayerCardsMap();
        for (Map.Entry<String, List<Card>> entry : playerCardsMap.entrySet()) {
            List<Card> cards = entry.getValue();
            if (entry.getKey().equals(playerId)) {
                continue;
            }

            cards.replaceAll(ignored -> null);
        }
        return GameResponse.builder()
                .playerCardsMap(playerCardsMap)
                .countCards(gameRepository.getCards().size())
                .trump(gameRepository.getTrump())
                .cardPairsOnTable(cardPairRepository.getAllCardPairs())
                .attacker(gameRepository.getAttacker())
                .build();
    }

    public GameResponse toGameResponseWithMessage(String playerId, String message) {
        GameResponse gameResponse = this.toGameResponse(playerId);
        gameResponse.setMessage(message);
        return gameResponse;
    }

}

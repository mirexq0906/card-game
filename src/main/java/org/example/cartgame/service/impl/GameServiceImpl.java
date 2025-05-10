package org.example.cartgame.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.cartgame.exception.GameException;
import org.example.cartgame.model.Card;
import org.example.cartgame.model.CardPair;
import org.example.cartgame.model.Suits;
import org.example.cartgame.parser.TxtFileParser;
import org.example.cartgame.repository.CardPairRepository;
import org.example.cartgame.repository.GameRepository;
import org.example.cartgame.service.GameService;
import org.example.cartgame.web.dto.CardPairDto;
import org.example.cartgame.web.dto.PlayerDto;
import org.example.cartgame.web.mapper.GameMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final CardPairRepository cardPairRepository;
    private final GameMapper gameMapper;
    private final TxtFileParser txtFileParser;

    @Override
    public void start(PlayerDto startGameDto) {
        if (this.getPlayersId().contains(startGameDto.getPlayerId())) {
            throw new GameException("Этот игрок уже подписался", startGameDto.getPlayerId());
        }

        this.gameRepository.setPlayerCards(startGameDto.getPlayerId(), new ArrayList<>());

        if (this.getPlayersId().size() < 2) {
            throw new GameException("Недостаточно игроков, чтобы начать игру", startGameDto.getPlayerId());
        }

        List<Card> cards = this.txtFileParser.parseCards("/assets/Cards.txt");
        Collections.shuffle(cards);
        this.gameRepository.setCards(cards);
        this.gameRepository.setTrump(Arrays.stream(Suits.values()).findAny().get());
        this.gameRepository.setAttacker(this.gameRepository.getPlayerCardsMap().keySet().stream().findAny().get());
        this.cardPairRepository.clearCardPairs();
        this.takeCards();
    }

    @Override
    public void attack(CardPairDto cardPairDto) {
        CardPair cardPair = this.gameMapper.dtoToCardPair(cardPairDto);

        if (!this.gameRepository.getAttacker().equals(cardPairDto.getPlayerId())) {
            throw new GameException("Атакует другой игрок", cardPairDto.getPlayerId());
        }

        this.gameRepository.getCardFromPlayer(
                cardPairDto.getPlayerId(),
                cardPair.getAttackCard()
        ).orElseThrow(() -> new GameException("Карта не найдена", cardPairDto.getPlayerId()));

        this.cardPairRepository.setCardPair(cardPair);
    }

    @Override
    public void defend(CardPairDto cardPairDto) {
        CardPair cardPair = this.gameMapper.dtoToCardPair(cardPairDto);

        if (
                !this.gameRepository.getPlayerCardsMap().containsKey(cardPairDto.getPlayerId()) ||
                this.gameRepository.getAttacker().equals(cardPairDto.getPlayerId())
        ) {
            throw new GameException("Атакует другой игрок", cardPairDto.getPlayerId());
        }

        if (!this.canBeat(cardPair)) {
            throw new GameException("Не удалось отбить этой картой", cardPairDto.getPlayerId());
        }

        this.gameRepository.getCardFromPlayer(
                cardPairDto.getPlayerId(),
                cardPair.getDefenseCard()
        ).orElseThrow(() -> new GameException("Карта не найдена", cardPairDto.getPlayerId()));

        this.cardPairRepository.setCardPair(cardPair);
    }

    @Override
    public void endTurn(PlayerDto playerDto) {
        if (
                !this.gameRepository.getPlayerCardsMap().containsKey(playerDto.getPlayerId()) ||
                        this.gameRepository.getAttacker().equals(playerDto.getPlayerId())
        ) {
            throw new GameException("Закончить ход может только защитник", playerDto.getPlayerId());
        }

        List<CardPair> cardPairList = this.cardPairRepository.getAllCardPairs();
        for (CardPair cardPair : cardPairList) {
            if (cardPair.getDefenseCard() == null) {
                List<Card> playerCards = this.gameRepository.getPlayerCards(playerDto.getPlayerId());
                for (CardPair cardPairSub : cardPairList) {
                    if (cardPair.getDefenseCard() != null) {
                        playerCards.add(cardPairSub.getDefenseCard());
                    }
                    if (cardPair.getAttackCard() != null) {
                        playerCards.add(cardPairSub.getAttackCard());
                    }
                }
                this.gameRepository.setPlayerCards(playerDto.getPlayerId(), playerCards);
                break;
            }
        }

        this.cardPairRepository.clearCardPairs();
        this.gameRepository.setAttacker(playerDto.getPlayerId());
        this.takeCards();
    }

    @Override
    public List<String> getPlayersId() {
        return this.gameRepository.getPlayersId();
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

    private void takeCards() {
        Map<String, List<Card>> playerCardsMap = this.gameRepository.getPlayerCardsMap();

        for (Map.Entry<String, List<Card>> entry : playerCardsMap.entrySet()) {
            String playerId = entry.getKey();
            List<Card> playerCards = entry.getValue();
            Optional<Card> card;
            while (playerCards.size() < 6) {
                card = this.gameRepository.getCardFromDeck();
                card.ifPresent(playerCards::add);
            }

            this.gameRepository.setPlayerCards(playerId, playerCards);
        }
    }

}

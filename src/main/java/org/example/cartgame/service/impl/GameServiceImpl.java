package org.example.cartgame.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.cartgame.exception.GameException;
import org.example.cartgame.model.Card;
import org.example.cartgame.model.CardPair;
import org.example.cartgame.model.Player;
import org.example.cartgame.model.Suits;
import org.example.cartgame.parser.TxtFileParser;
import org.example.cartgame.repository.CardPairRepository;
import org.example.cartgame.repository.GameRepository;
import org.example.cartgame.repository.PlayerRepository;
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
    private final PlayerRepository playerRepository;
    private final GameMapper gameMapper;
    private final TxtFileParser txtFileParser;

    @Override
    public void start(PlayerDto startGameDto) {
        if (this.playerRepository.isExistByPlayerId(startGameDto.getPlayerId())) {
            throw new GameException("Этот игрок уже подписался", startGameDto.getPlayerId());
        }

        if (this.playerRepository.getPlayers().size() >= 2) {
            throw new GameException("Достигнут лимит игроков", startGameDto.getPlayerId());
        }

        Player player = Player.builder().playerId(startGameDto.getPlayerId()).build();
        this.playerRepository.setPlayer(player);
        this.gameRepository.setPlayerCards(player.getPlayerId(), new ArrayList<>());

        if (this.playerRepository.getPlayers().size() < 2) {
            throw new GameException("Недостаточно игроков, чтобы начать игру", startGameDto.getPlayerId());
        }

        List<Card> cards = this.txtFileParser.parseCards("/assets/Cards.txt");
        Collections.shuffle(cards);
        this.gameRepository.setCards(cards);
        Suits[] suits = Suits.values();
        Suits trump = suits[new Random().nextInt(suits.length)];
        this.gameRepository.setTrump(trump);
        this.gameRepository.setAttacker(this.playerRepository.getPlayers().stream().findAny().get().getPlayerId());
        this.takeCards();
    }

    @Override
    public void attack(CardPairDto cardPairDto) {
        List<CardPair> cardPairList = this.cardPairRepository.getAllCardPairs();

        if (cardPairList.size() >= 6) {
            throw new GameException("Нельзя ложить больше 6 карт", cardPairDto.getPlayerId());
        }

        CardPair cardPair = this.gameMapper.dtoToCardPair(cardPairDto);

        if (!this.gameRepository.getAttacker().equals(cardPairDto.getPlayerId())) {
            throw new GameException("Атакует другой игрок", cardPairDto.getPlayerId());
        }

        if (!canAttack(cardPair, cardPairList)) {
            throw new GameException("Нельзя атаковать этой картой", cardPairDto.getPlayerId());
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

        if (this.gameRepository.getAttacker().equals(cardPairDto.getPlayerId())) {
            throw new GameException("Этот игрок не может защищаться", cardPairDto.getPlayerId());
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
        Player currentPlayer = this.playerRepository.getPlayer(playerDto.getPlayerId())
                .orElseThrow(() -> new GameException("Игрок не найден", playerDto.getPlayerId()));
        currentPlayer.setEndTurn(true);
        this.playerRepository.setPlayer(currentPlayer);

        if (!this.canEndTurn()) {
            throw new GameException("Ход может быть завершён только после подтверждения всех игроков", playerDto.getPlayerId());
        }

        List<CardPair> cardPairList = this.cardPairRepository.getAllCardPairs();
        String playerId;
        String attacker = this.gameRepository.getAttacker();

        if (attacker.equals(currentPlayer.getPlayerId())) {
            playerId = this.playerRepository.getPlayers().stream()
                    .filter(item -> !item.getPlayerId().equals(currentPlayer.getPlayerId()))
                    .map(Player::getPlayerId)
                    .findAny()
                    .orElseThrow(() -> new GameException("Защитник не найден", currentPlayer.getPlayerId()));
        } else {
            playerId = currentPlayer.getPlayerId();
        }

        this.gameRepository.setAttacker(playerId);

        for (CardPair cardPair : cardPairList) {
            if (cardPair.getDefenseCard() == null) {
                List<Card> playerCards = this.gameRepository.getPlayerCards(playerId);
                for (CardPair cardPairSub : cardPairList) {
                    if (cardPair.getDefenseCard() != null) {
                        playerCards.add(cardPairSub.getDefenseCard());
                    }
                    if (cardPair.getAttackCard() != null) {
                        playerCards.add(cardPairSub.getAttackCard());
                    }
                }
                this.gameRepository.setPlayerCards(playerId, playerCards);
                this.gameRepository.setAttacker(attacker);
                break;
            }
        }

        for (Player player : this.playerRepository.getPlayers()) {
            player.setEndTurn(false);
            this.playerRepository.setPlayer(player);
        }

        this.cardPairRepository.clearCardPairs();
        this.takeCards();
    }

    @Override
    public void finish(PlayerDto playerDto) {
        Player currentPlayer = this.playerRepository.getPlayer(playerDto.getPlayerId())
                .orElseThrow(() -> new GameException("Игрок не найден", playerDto.getPlayerId()));
        currentPlayer.setFinish(true);
        this.playerRepository.setPlayer(currentPlayer);

        if (!this.canFinish()) {
            throw new GameException("Игра может быть завершена только после подтверждения всех игроков", playerDto.getPlayerId());
        }

        this.playerRepository.clearPlayers();
        this.cardPairRepository.clearCardPairs();
        this.gameRepository.clearPlayerCardsMap();
    }

    private boolean canFinish() {
        for (Player player : this.playerRepository.getPlayers()) {
            if (!player.isFinish()) {
                return false;
            }
        }

        return true;
    }

    private boolean canEndTurn() {
        for (Player player : this.playerRepository.getPlayers()) {
            if (!player.isEndTurn()) {
                return false;
            }
        }

        return true;
    }

    private boolean canAttack(CardPair cardPair, List<CardPair> cardPairList) {
        Card attackCard = cardPair.getAttackCard();

        if (cardPairList.isEmpty()) {
            return true;
        }

        for (CardPair cardPairSub : cardPairList) {
            if (
                    (cardPairSub.getAttackCard() != null && cardPairSub.getAttackCard().getName().equals(attackCard.getName())) ||
                            (cardPairSub.getDefenseCard() != null && cardPairSub.getDefenseCard().getName().equals(attackCard.getName()))

            ) {
                return true;
            }
        }

        return false;
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

package com.example.springboot;

import com.example.springboot.dao.PlayerDAOInter;
import com.example.springboot.model.Player;
import com.example.springboot.service.PlayerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class PlayerServiceTests {

    @Mock
    private PlayerDAOInter playerDAO;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void testListOfPlayers() {
        List<Player> players = Arrays.asList(new Player(), new Player());
        Mockito.when(playerDAO.findAll()).thenReturn(players);

        List<Player> result = playerService.playerList();

        Assertions.assertEquals(players, result);
    }

    @Test
    void testSavePlayer() {
        Player player = new Player();
        Mockito.when(playerDAO.save(player)).thenReturn(player);

        Player result = playerService.savePlayer(player);

        Assertions.assertEquals(player, result);
    }

    @Test
    void testGetPlayerById() {
        // Arrange
        UUID playerId = UUID.randomUUID();
        Player player = new Player();
        Mockito.when(playerDAO.findById(playerId)).thenReturn(Optional.of(player));

        Player result = playerService.getPlayerById(playerId);

        Assertions.assertEquals(player, result);
    }

    @Test
    void testDeletePlayerById() {
        UUID playerId = UUID.randomUUID();

        playerService.deletePlayerById(playerId);

        Mockito.verify(playerDAO, Mockito.times(1)).deleteById(playerId);
    }
}
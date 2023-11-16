package com.example.springboot;

import com.example.springboot.controller.PlayerController;
import com.example.springboot.model.Player;
import com.example.springboot.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Mock
    PlayerService playerService;

    @Test
    public void testGetListPlayers(){
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player();
        Player player2 = new Player();
        player1.setName("Viktor");
        player2.setName("Roman");
        players.add(player1);
        players.add(player2);
        when(playerService.get()).thenReturn(players);
        PlayerController playerController = new PlayerController(playerService);
        List<Player> list = playerController.get();
        assertEquals(players, list);

    }
}

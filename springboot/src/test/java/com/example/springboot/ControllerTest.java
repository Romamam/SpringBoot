package com.example.springboot;

import com.example.springboot.controller.PlayerController;
import com.example.springboot.model.Player;
import com.example.springboot.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Mock
    PlayerService playerService;

    @InjectMocks
    PlayerController playerController;

    @Test
    public void testGetListPlayers(){
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player();
        Player player2 = new Player();

        player1.setId(1);
        player2.setId(1);
        player1.setName("Roma");
        player2.setName("Roman");

        players.add(player1);
        players.add(player2);
        when(playerService.get()).thenReturn(players);
        List<Player> list = playerController.get();
        assertEquals(players, list);
    }

    @Test
    public void testCreatePlayer(){
        Player player = new Player();

        player.setId(1);
        player.setName("Roman");

        doNothing().when(playerService).save(player);
        Player savedPlayer = playerController.save(player);

        assertThat(savedPlayer).isNotNull();
        assertThat(savedPlayer.getName()).isEqualTo("Roman");

        verify(playerService).save(player);
    }

    @Test
    public void testGetById(){
        Player player = new Player();
        Player player2 = new Player();

        player.setName("Roman");
        player.setId(1);
        player2.setName("RR");
        player2.setId(2);

        when(playerService.get(1)).thenReturn(player);
        Player player1 = playerController.get(1);
        when(playerService.get(2)).thenReturn(player2);
        Player player3 = playerController.get(2);
        assertEquals(player, player1);
        assertEquals(player2, player3);
    }

    @Test
    public void testDeleteById(){
        List<Player> list1 = new ArrayList<>();
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();

        player1.setId(1);
        player2.setId(2);
        player3.setId(3);
        player1.setName("R");
        player2.setName("RR");
        player3.setName("RRR");

        list1.add(player1);
        list1.add(player2);
        list1.add(player3);

        playerController.delete(1);
        verify(playerService).delete(1);
        playerController.delete(3);
        verify(playerService).delete(3);
    }

    @Test
    public void testUpdate(){
        Player player = new Player();
        player.setId(1);
        player.setName("Roma");

        playerController.save(player);
        verify(playerService).save(player);
    }

}













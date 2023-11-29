package com.example.springboot;

import com.example.springboot.controller.PlayerController;
import com.example.springboot.model.Player;
import com.example.springboot.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PlayerControllerTests {

    private MockMvc mockMvc;
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        playerService = mock(PlayerService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new PlayerController(playerService)).build();
    }

    @Test
    void testGetPlayers() throws Exception {
        List<Player> players = Arrays.asList(
                new Player(UUID.randomUUID(), "John", "Doe", 80),
                new Player(UUID.randomUUID(), "Jane", "Doe", 85)
        );

        when(playerService.listOfPlayers()).thenReturn(players);

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(players.size()));

        verify(playerService, times(1)).listOfPlayers();
        verifyNoMoreInteractions(playerService);
    }

    @Test
    void testGetPlayerById() throws Exception {
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "John", "Doe", 80);

        when(playerService.getPlayerById(playerId)).thenReturn(player);

        mockMvc.perform(get("/api/player/{id}", playerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(playerId.toString()))
                .andExpect(jsonPath("$.name").value(player.getName()))
                .andExpect(jsonPath("$.secondName").value(player.getSecondName()))
                .andExpect(jsonPath("$.rating").value(player.getRating()));

        verify(playerService, times(1)).getPlayerById(playerId);
        verifyNoMoreInteractions(playerService);
    }

    @Test
    void testSavePlayer() throws Exception {
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "John", "Doe", 80);

        when(playerService.savePlayer(any(Player.class))).thenReturn(player);

        mockMvc.perform(post("/api/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John\", \"secondName\": \"Doe\", \"rating\": 80 }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(playerId.toString()))
                .andExpect(jsonPath("$.name").value(player.getName()))
                .andExpect(jsonPath("$.secondName").value(player.getSecondName()))
                .andExpect(jsonPath("$.rating").value(player.getRating()));

        verify(playerService, times(1)).savePlayer(any(Player.class));
        verifyNoMoreInteractions(playerService);
    }

    @Test
    void testDeletePlayer() throws Exception {
        UUID playerId = UUID.randomUUID();

        mockMvc.perform(delete("/api/player/{id}", playerId))
                .andExpect(status().isOk());

        verify(playerService, times(1)).deletePlayerById(playerId);
        verifyNoMoreInteractions(playerService);
    }
}
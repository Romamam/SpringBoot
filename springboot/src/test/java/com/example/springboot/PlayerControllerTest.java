package com.example.springboot;
import com.example.springboot.controller.PlayerController;
import com.example.springboot.dao.PlayerDAOInter;
import com.example.springboot.model.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerDAOInter playerDAO;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private PlayerController playerController;



    @Test
    void testGetAllPlayers() throws Exception {
        Player player = new Player();
        player.setId(1);
        player.setName("John Doe");
        player.setRating(80);

        List<Player> players = Collections.singletonList(player);

        when(playerDAO.findAll()).thenReturn(players);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/players"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("John Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rating", Matchers.is(80)));
    }

    @Test
    void testGetPlayer() throws Exception {
        Player player = new Player();
        player.setId(1);
        player.setName("John Doe");
        player.setRating(80);

        when(playerDAO.findById(1)).thenReturn(Optional.of(player));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/player/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(80));
    }

    @Test
    void testDeletePlayer() throws Exception {
        doNothing().when(playerDAO).deleteById(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/player/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdatePlayer() throws Exception {
        Player existingPlayer = new Player();
        existingPlayer.setId(1);
        existingPlayer.setName("John Doe");
        existingPlayer.setRating(80);

        Player updatedPlayer = new Player();
        updatedPlayer.setId(1);
        updatedPlayer.setName("Updated Name");
        updatedPlayer.setRating(90);

        when(playerDAO.findById(1)).thenReturn(Optional.of(existingPlayer));

        when(playerDAO.save(any(Player.class))).thenReturn(updatedPlayer);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPlayer)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(90));
    }

//    @Test
//    void testGetSavedTeams() throws Exception {
//        int teamId = 1;
//        List<Player> teamPlayers = createMockPlayers(11);
//
//        Mockito.when(playerDAO.findById(teamId)).thenReturn(Optional.ofNullable(teamPlayers));
//
//        generatedTeamsMap.put(teamId, teamPlayers);
//
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-saved-teams/{teamId}", teamId))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(11)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Player1")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rating", Matchers.is(71)));
//
//    }
//
//    private List<Player> createMockPlayers(int count) {
//        List<Player> players = new ArrayList<>();
//        for (int i = 1; i <= count; i++) {
//            Player player = new Player();
//            player.setId(i);
//            player.setName("Player" + i);
//            player.setRating(70 + i);
//            players.add(player);
//        }
//        return players;
//    }


}
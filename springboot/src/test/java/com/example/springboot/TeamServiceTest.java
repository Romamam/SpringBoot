package com.example.springboot;

import com.example.springboot.controller.TeamController;
import com.example.springboot.dao.PlayerRepository;
import com.example.springboot.dao.TeamRepository;
import com.example.springboot.model.Player;
import com.example.springboot.model.Team;
import com.example.springboot.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TeamServiceTest {
    @Mock
    private TeamService teamService;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private TeamController teamController;

    public List<Player> createPlayersList() {
        List<Player> players = new ArrayList<>();

        // Додавання гравців до списку
        players.add(new Player(UUID.randomUUID(), "Roman", "Rybachok", 99));
        players.add(new Player(UUID.randomUUID(), "Dmytro", "Rybachok", 87));
        players.add(new Player(UUID.randomUUID(), "Ruslan", "Rybachok", 86));
        players.add(new Player(UUID.randomUUID(), "Artem", "Pryzhkov", 98));
        players.add(new Player(UUID.randomUUID(), "Romaan", "Rybachok", 96));
        players.add(new Player(UUID.randomUUID(), "Romasn", "Rybachok", 65));
        players.add(new Player(UUID.randomUUID(), "Romadn", "Rybachok", 76));
        players.add(new Player(UUID.randomUUID(), "Romafn", "Rybachok", 81));
        players.add(new Player(UUID.randomUUID(), "Romacfn", "Rybachok", 57));
        players.add(new Player(UUID.randomUUID(), "Romxan", "Rybachok", 92));
        players.add(new Player(UUID.randomUUID(), "Romban", "Rybachok", 89));
        players.add(new Player(UUID.randomUUID(), "Roweman", "Rybachok", 79));
        players.add(new Player(UUID.randomUUID(), "Romyeryan", "Rybachok", 75));
        players.add(new Player(UUID.randomUUID(), "Roegeman", "Rybachok", 57));

        return players;
    }


    @Test
    public void testGenerateTeamsWithBalancedRating() {

        List<Player> mockPlayers = createPlayersList();
        when(playerRepository.findRandomPlayers(anyInt())).thenReturn(mockPlayers);

        System.out.println(mockPlayers.toString());

        // Act
        List<List<Player>> resultTeams = teamService.generateTeamsWithBalancedRating(6, "Team1", "Team2");

        // Assert
        assertEquals(2, resultTeams.size());
        assertEquals(3, resultTeams.get(0).size());
        assertEquals(3, resultTeams.get(1).size());

        // Verify the saveTeam method is called with the expected arguments
        verify(teamService, times(1)).saveTeam(any(), anyInt(), eq("Team1"));
        verify(teamService, times(1)).saveTeam(any(), anyInt(), eq("Team2"));
    }
}

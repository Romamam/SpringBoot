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


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TeamServiceTest {
    @Mock
    private TeamService teamService;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private TeamController teamController;


    @Test
    void generateTeamsWithBalancedRating_Success() {
        String[] teamNames = {"Team1", "Team2"};
        List<Player> listOfRandomPlayers = Arrays.asList(
                new Player(null, "Player1", "Last1", 90),
                new Player(null, "Player2", "Last2", 85),
                new Player(null, "Player3", "Last3", 80),
                new Player(null, "Player4", "Last4", 95)
        );
        when(playerRepository.findPlayers()).thenReturn(listOfRandomPlayers);

        List<List<Player>> result = teamService.generateTeamsWithBalancedRating(teamNames);

        assertEquals(2, result.size());
    }
}

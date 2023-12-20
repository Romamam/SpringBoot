package com.example.springboot;

import com.example.springboot.dao.PlayerRepository;
import com.example.springboot.dao.TeamRepository;
import com.example.springboot.model.Player;
import com.example.springboot.model.Team;
import com.example.springboot.service.TeamService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class TeamServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    void generateTeamsWithBalancedRating_ShouldGenerateTeams() {
        MockitoAnnotations.openMocks(this);

        when(playerRepository.findPlayers()).thenReturn(Arrays.asList(
                new Player(UUID.randomUUID(),"Player1", "Last1", 90),
                new Player(UUID.randomUUID(),"Player2", "Last2", 80),
                new Player(UUID.randomUUID(),"Player3", "Last3", 70),
                new Player(UUID.randomUUID(),"Player4", "Last4", 60)
        ));

        List<List<Player>> generatedTeams = teamService.generateTeamsWithBalancedRating(new String[]{"Team1", "Team2"});

        assertEquals(2, generatedTeams.size());

        assertEquals(2, generatedTeams.get(0).size());
        assertEquals(2, generatedTeams.get(1).size());

        verify(teamRepository, times(2)).save(any(Team.class));
    }

    @Test
    void generateTeamsWithBalancedRating_ShouldHandleEmptyPlayersList() {
        MockitoAnnotations.openMocks(this);


        when(playerRepository.findPlayers()).thenReturn(Arrays.asList());

        List<List<Player>> generatedTeams = teamService.generateTeamsWithBalancedRating(new String[]{"Team1", "Team2"});

        assertNull(generatedTeams);
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    void generateTeamsWithBalancedRating_ShouldNotGenerateTeamsWithDuplicatePlayers() {
        MockitoAnnotations.openMocks(this);

        when(playerRepository.findPlayers()).thenReturn(Arrays.asList(
                new Player(UUID.randomUUID(), "Player1", "Last1", 90),
                new Player(UUID.randomUUID(), "Player2", "Last2", 80),
                new Player(UUID.randomUUID(), "Player3", "Last3", 70),
                new Player(UUID.randomUUID(), "Player4", "Last4", 60)
        ));

        List<List<Player>> generatedTeams = teamService.generateTeamsWithBalancedRating(new String[]{"Team1", "Team2"});

        assertEquals(2, generatedTeams.size());

        for (int i = 0; i < generatedTeams.size(); i++) {
            List<Player> teamA = generatedTeams.get(i);

            for (int j = i + 1; j < generatedTeams.size(); j++) {
                List<Player> teamB = generatedTeams.get(j);

                for (Player playerA : teamA) {
                    for (Player playerB : teamB) {
                        assertNotEquals(playerA, playerB);
                    }
                }
            }
        }
    }

    @Test
    void generateTeamsWithBalancedRating_ShouldGenerateTeamsWithBalancedRatings() {
        MockitoAnnotations.openMocks(this);

        when(playerRepository.findPlayers()).thenReturn(Arrays.asList(
                new Player(UUID.randomUUID(),"Player1", "Last1", 90),
                new Player(UUID.randomUUID(),"Player2", "Last2", 80),
                new Player(UUID.randomUUID(),"Player3", "Last3", 70),
                new Player(UUID.randomUUID(),"Player4", "Last4", 60),
                new Player(UUID.randomUUID(),"Player5", "Last4", 50),
                new Player(UUID.randomUUID(),"Player6", "Last4", 40)
        ));

        List<List<Player>> generatedTeams = teamService.generateTeamsWithBalancedRating(new String[]{"Team1", "Team2", "Team3"});
        assertEquals(3, generatedTeams.size());

        for (int i = 0; i < generatedTeams.size(); i++) {
            for (int j = i + 1; j < generatedTeams.size(); j++) {
                List<Player> teamA = generatedTeams.get(i);
                List<Player> teamB = generatedTeams.get(j);

                int ratingDifference = Math.abs(teamService.calculateTeamRating(teamA) - teamService.calculateTeamRating(teamB));
                assertEquals(0,ratingDifference);
                assertEquals(65,teamService.calculateTeamRating(teamA));
                assertEquals(65,teamService.calculateTeamRating(teamB));
            }
        }
    }

    @Test
    void generateTeamsWithBalancedRating_ShouldReturnEmptyList_WhenNoTeams() {
        MockitoAnnotations.openMocks(this);

        when(playerRepository.findPlayers()).thenReturn(Arrays.asList(
                new Player(UUID.randomUUID(),"Player1", "Last1", 90),
                new Player(UUID.randomUUID(),"Player2", "Last2", 80),
                new Player(UUID.randomUUID(),"Player3", "Last3", 70),
                new Player(UUID.randomUUID(),"Player4", "Last4", 60)
        ));

        List<List<Player>> generatedTeams = teamService.generateTeamsWithBalancedRating(new String[]{});

        assertNull(generatedTeams);
    }

    @Test
    void generateTeamsWithBalancedRating_ShouldReturnTeamsWithBalancedRating_WhenPlayerCountNotMultipleOfTeamCount() {
        MockitoAnnotations.openMocks(this);

        when(playerRepository.findPlayers()).thenReturn(Arrays.asList(
                new Player(UUID.randomUUID(),"Player1", "Last1", 90),
                new Player(UUID.randomUUID(),"Player2", "Last2", 80),
                new Player(UUID.randomUUID(),"Player3", "Last3", 70),
                new Player(UUID.randomUUID(),"Player4", "Last4", 60),
                new Player(UUID.randomUUID(),"Player5", "Last4", 50)
        ));

        List<List<Player>> generatedTeams = teamService.generateTeamsWithBalancedRating(new String[]{"Team1", "Team2"});

        // Assert
        assertEquals(2, generatedTeams.size());
        for (List<Player> team : generatedTeams) {
            assertEquals(2, team.size());
        }
    }

}
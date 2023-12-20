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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamController teamController;

    @BeforeEach
    void setUp() {
        playerRepository = mock(PlayerRepository.class);
        teamRepository = mock(TeamRepository.class);
        teamService = mock(TeamService.class);
        teamController = new TeamController(teamService, playerRepository, teamRepository);
    }

    @Test
    void getTeamByName_ShouldReturnTeam() {
        MockitoAnnotations.openMocks(this);
        String teamName = "Team1";

        Team expectedTeam = new Team();
        when(teamService.getTeamByName(teamName)).thenReturn(Optional.of(expectedTeam));

        Optional<Team> result = teamController.getTeamByName(teamName);

        assertEquals(expectedTeam, result.orElse(null));
    }

    @Test
    void generateTeamsWithBalancedRating_ShouldReturnGeneratedTeams() {
        MockitoAnnotations.openMocks(this);
        String[] teamNames = {"Team1", "Team2"};
        List<List<Player>> expectedTeams = Arrays.asList(
                Arrays.asList(new Player(UUID.randomUUID(),"Player1", "Last1", 90), new Player(UUID.randomUUID(),"Player2", "Last2", 80)),
                Arrays.asList(new Player(UUID.randomUUID(),"Player3", "Last3", 97), new Player(UUID.randomUUID(),"Player4", "Last4", 90))
        );
        when(teamService.generateTeamsWithBalancedRating(teamNames)).thenReturn(expectedTeams);

        List<List<Player>> result = teamController.generateTeamsWithBalancedRating(teamNames);

        assertEquals(expectedTeams, result);
    }

    @Test
    void getTeamByName_ShouldReturnTeamOptional() {
        MockitoAnnotations.openMocks(this);
        String teamName = "Team1";
        Team expectedTeam = new Team();
        expectedTeam.setTeamName(teamName);
        when(teamService.getTeamByName(teamName)).thenReturn(Optional.of(expectedTeam));

        Optional<Team> result = teamController.getTeamByName(teamName);

        assertEquals(expectedTeam, result.orElse(null));
    }

    @Test
    void addPlayerToTeam_Success() {
        String teamName = "Team1";
        String playerName = "Player1";
        Player player = new Player(UUID.randomUUID(), playerName, "Last1", 90);
        Team team = new Team();
        when(playerRepository.findPlayerByName(playerName)).thenReturn(player);
        when(teamService.addPlayerToTeam(player, teamName)).thenReturn(Optional.of(team));

        Optional<Team> response = teamController.addPlayerToTeam(teamName, playerName);

        assertEquals(Optional.of(team), response);
        verify(playerRepository, times(1)).findPlayerByName(playerName);
        verify(teamService, times(1)).addPlayerToTeam(player, teamName);
    }


    @Test
    void deletePlayerFromTeam_Success() {
        String teamName = "Team1";
        String playerName = "Player1";

        teamController.deletePlayerFromTeam(teamName, playerName);

        verify(teamService, times(1)).deletePlayerFromTeam(teamName, playerName);
    }


}

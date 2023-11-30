package com.example.springboot;

import com.example.springboot.dao.TeamDAOInter;
import com.example.springboot.model.Player;
import com.example.springboot.model.Team;
import com.example.springboot.service.PlayerService;
import com.example.springboot.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
class TeamServiceTests {

    @Mock
    private TeamDAOInter teamDAO;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private TeamService teamService;

    @Test
    void testGetTeamWithPlayersById() {
        UUID teamId = UUID.randomUUID();
        Optional<Team> team = Optional.of(new Team());
        Mockito.when(teamDAO.findByIdWithPlayers(teamId)).thenReturn(team);

        Optional<Team> result = teamService.getTeamWithPlayersById(teamId);

        Assertions.assertEquals(team, result);
    }

    @Test
    void testGetTeamByName() {
        String teamName = "TestTeam";
        Optional<Team> team = Optional.of(new Team());
        Mockito.when(teamDAO.findByTeamName(teamName)).thenReturn(team);

        Optional<Team> result = teamService.getTeamByName(teamName);

        Assertions.assertEquals(team, result);
    }

    @Test
    void testDeleteTeamById() {
        UUID teamId = UUID.randomUUID();
        teamService.deleteTeamById(teamId);
        verify(teamDAO, times(1)).deleteById(teamId);
        verifyNoMoreInteractions(teamDAO);
    }



}

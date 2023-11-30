package com.example.springboot;

import com.example.springboot.controller.TeamController;
import com.example.springboot.model.Player;
import com.example.springboot.model.Team;
import com.example.springboot.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class TeamControllerTests {

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    @Test
    void testGetTeamByName() {
        String teamName = "TestTeam";
        Optional<Team> team = Optional.of(new Team());
        Mockito.when(teamService.getTeamByName(teamName)).thenReturn(team);

        Optional<Team> result = teamController.getTeamByName(teamName);

        Assertions.assertEquals(team, result);
    }

    @Test
    void testGenerateTeamsWithBalancedRating() {
        String teamName1 = "Team1";
        String teamName2 = "Team2";
        List<List<Player>> teams = Arrays.asList(Arrays.asList(new Player()), Arrays.asList(new Player()));
        Mockito.when(teamService.generateTeamsWithBalancedRating(teamName1, teamName2)).thenReturn(teams);

        List<List<Player>> result = teamController.generateTeamsWithBalancedRating(teamName1, teamName2);

        Assertions.assertEquals(teams, result);
    }
}

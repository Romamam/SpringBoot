package com.example.springboot.controller;

import com.example.springboot.model.Player;
import com.example.springboot.model.Team;
import com.example.springboot.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/team-by-name/{name}")
    public Optional<Team> getTeamByName(@PathVariable String name){
        return teamService.getTeamByName(name);
    }

    @DeleteMapping("/delete-team/{id}")
    public void deleteTeamById(@PathVariable UUID id){teamService.deleteTeamById(id);}

    @GetMapping("/generated-teams/{teamName1}-{teamName2}")
    public List<List<Player>> generateTeamsWithBalancedRating(@PathVariable String teamName1, @PathVariable String teamName2) {
        return teamService.generateTeamsWithBalancedRating(teamName1, teamName2);
    }

    @GetMapping("/get-team/{id}")
    public Optional<Team> getTeamById(@PathVariable UUID id){
        return teamService.getTeamWithPlayersById(id);
    }

}

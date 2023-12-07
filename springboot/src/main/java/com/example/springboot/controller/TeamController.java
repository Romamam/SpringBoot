package com.example.springboot.controller;

import com.example.springboot.dao.PlayerRepository;
import com.example.springboot.dao.TeamRepository;
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
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public TeamController(TeamService teamService, PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.teamService = teamService;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }


    @GetMapping("/find-by-name/{name}")
    public Optional<Team> getTeamByName(@PathVariable String name){
        return teamService.getTeamByName(name);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public void deleteTeamById(@PathVariable UUID id){teamService.deleteTeamById(id);}

    @GetMapping("/generated-teams/{count}-{teamName1}-{teamName2}")
    public List<List<Player>> generateTeamsWithBalancedRating(@PathVariable int count, @PathVariable String teamName1, @PathVariable String teamName2) {
        return teamService.generateTeamsWithBalancedRating(count, teamName1, teamName2);
}

    @GetMapping("/get-by-id/{id}")
    public Optional<Team> getTeamById(@PathVariable UUID id){
        return teamService.getTeamWithPlayersById(id);
    }

    @PostMapping("/add-player/{teamName}-{playerName}")
    public Optional<Team> addPlayerToTeam(@PathVariable String teamName, @PathVariable String playerName){
        return teamService.addPlayerToTeam(playerRepository.findPlayerByName(playerName), teamName);
    }

    @DeleteMapping("/delete-player/{teamName}-{playerName}")
    public void deletePlayerFromTeam(@PathVariable String teamName, @PathVariable String playerName){
        teamService.deletePlayerFromTeam(teamName, playerName);
    }

    @DeleteMapping("/delete")
    public void deleteAllTeams(){
        teamRepository.deleteAll();
    }
}

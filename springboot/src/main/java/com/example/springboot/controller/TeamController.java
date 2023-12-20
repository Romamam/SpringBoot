package com.example.springboot.controller;

import com.example.springboot.dao.PlayerRepository;
import com.example.springboot.dao.TeamRepository;
import com.example.springboot.model.Player;
import com.example.springboot.model.Team;
import com.example.springboot.service.TeamService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/generated-teams")
    public List<List<Player>> generateTeamsWithBalancedRating(@RequestParam String[] teamNames) {
        return teamService.generateTeamsWithBalancedRating(teamNames);
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

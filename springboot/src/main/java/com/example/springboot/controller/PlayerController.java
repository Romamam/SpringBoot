package com.example.springboot.controller;

import com.example.springboot.dao.PlayerDAOInter;
import com.example.springboot.model.Player;
import com.example.springboot.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PlayerController {

    private final Map<Integer, List<Player>> generatedTeamsMap = new HashMap<>();
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public List<Player> get(){
        return playerService.listOfPlayers();
    }

    @PostMapping("/player")
    public Player save(@RequestBody Player playerObj){
         return playerService.savePlayer(playerObj);
    }

    @GetMapping("/player/{id}")
    public Player get(@PathVariable int id){
        return playerService.getPlayerById(id);
    }

    @DeleteMapping("/player/{id}")
    public void delete(@PathVariable int id){
        playerService.deletePlayerById(id);
    }


    @PutMapping("/player")
    public Player update(@RequestBody Player playerObj){
        return playerService.updatePlayer(playerObj);
    }


    @GetMapping("/generated-teams")
    public List<List<Player>> generateTeamsWithBalancedRating() {
        return playerService.generateTeamsWithBalancedRating();
    }

//    @GetMapping("/get-saved-teams/{teamId}")
//    public ResponseEntity<List<Player>> getSavedTeams(@PathVariable int teamId) {
//        Optional<List<Player>> team = Optional.ofNullable(generatedTeamsMap.get(teamId));
//
//        return team.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
}

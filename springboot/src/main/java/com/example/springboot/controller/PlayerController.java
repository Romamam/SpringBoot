package com.example.springboot.controller;

import com.example.springboot.model.Player;
import com.example.springboot.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public List<Player> get(){
        return playerService.get();
    }

    @PostMapping("/player")
    public Player save(@RequestBody Player playerObj){
        playerService.save(playerObj);
        return playerObj;
    }

    @GetMapping("/player/{id}")
    public Player get(@PathVariable int id){
        Player playerObj = playerService.get(id);
        if(playerObj == null){
            throw new RuntimeException("Player with id: " + id + " not found");
        }
        return playerObj;
    }

    @DeleteMapping("/player/{id}")
    public String delete(@PathVariable int id){
        playerService.delete(id);
        return "Player has benn deleted with id: " + id;
    }


    @PutMapping("/player")
    public Player update(@RequestBody Player playerObj){
        playerService.save(playerObj);
        return playerObj;
    }

    @GetMapping("/generated-teams")
    public List<List<Player>> generatedTeams(){
        return playerService.generateTeamsWithBalancedRating();
    }
}

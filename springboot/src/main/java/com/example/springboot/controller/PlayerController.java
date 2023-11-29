package com.example.springboot.controller;

import com.example.springboot.model.Player;
import com.example.springboot.service.PlayerService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class PlayerController {

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
    public Player get(@PathVariable UUID id){
        return playerService.getPlayerById(id);
    }

    @DeleteMapping("/player/{id}")
    public void delete(@PathVariable UUID id){
        playerService.deletePlayerById(id);
    }


    @PutMapping("/player")
    public Player update(@RequestBody Player playerObj){
        return playerService.updatePlayer(playerObj);
    }

}

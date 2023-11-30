package com.example.springboot.controller;

import com.example.springboot.model.Player;
import com.example.springboot.service.PlayerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<Player> get(Pageable pageable){
        Sort sortByRatingDescending = Sort.by("rating").descending();
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortByRatingDescending);
        return playerService.listOfPlayers(pageableWithSort);
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

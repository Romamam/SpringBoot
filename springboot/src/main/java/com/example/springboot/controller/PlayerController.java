package com.example.springboot.controller;

import com.example.springboot.dao.PlayerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> makePlayerNotActive(@PathVariable UUID id){
        playerRepository.makeNotActive(id);
        return ResponseEntity.status(204).build();
    }
}

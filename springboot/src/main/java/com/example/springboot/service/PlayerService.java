package com.example.springboot.service;

import com.example.springboot.model.Player;

import java.util.List;

public interface PlayerService {

    List<Player> get();

    Player get(int id);

    void save(Player player);

    void delete(int id);
}

package com.example.springboot.dao;

import com.example.springboot.model.Player;

import java.util.List;

public interface PlayerDAO {

    List<Player> get();

    Player get(int id);

    void save(Player player);

    void delete(int id);
}

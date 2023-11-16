package com.example.springboot.dao;

import com.example.springboot.model.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerDAOInter extends CrudRepository<Player, Integer> {
}

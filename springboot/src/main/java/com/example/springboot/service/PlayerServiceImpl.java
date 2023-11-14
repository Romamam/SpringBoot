package com.example.springboot.service;

import com.example.springboot.dao.PlayerDAO;
import com.example.springboot.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerDAO playerDAO;

    @Transactional
    @Override
    public List<Player> get() {
        return playerDAO.get();
    }

    @Transactional
    @Override
    public Player get(int id) {
        return null;
    }

    @Transactional
    @Override
    public void save(Player player) {

    }

    @Transactional
    @Override
    public void delete(int id) {

    }
}

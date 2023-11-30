package com.example.springboot.service;

import com.example.springboot.dao.PlayerDAOInter;
import com.example.springboot.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerService {

    private final PlayerDAOInter playerDAO;

    public PlayerService(PlayerDAOInter playerDAO) {
        this.playerDAO = playerDAO;
    }

    public Page<Player> listOfPlayers(Pageable pageable){
        return playerDAO.findAll(pageable);
    }

    public List<Player> playerList(){
        ArrayList<Player> list = new ArrayList<>();
        playerDAO.findAll().forEach(list::add);
        return list;
    }

    public Player savePlayer(Player player){
        return playerDAO.save(player);
    }

    public Player getPlayerById(UUID id){
        return playerDAO.findById(id).orElseThrow();
    }

    public void deletePlayerById(UUID id){
        playerDAO.deleteById(id);
    }

    public Player updatePlayer(Player player){
        return playerDAO.save(player);
    }

    public void savePlayers(List<Player> players) {
        playerDAO.saveAll(players);
    }

}

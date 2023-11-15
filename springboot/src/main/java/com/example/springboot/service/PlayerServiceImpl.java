package com.example.springboot.service;

import com.example.springboot.dao.PlayerDAO;
import com.example.springboot.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
        return playerDAO.get(id);
    }

    @Transactional
    @Override
    public void save(Player player) {
        playerDAO.save(player);
    }

    @Transactional
    @Override
    public void delete(int id) {
        playerDAO.delete(id);
    }

    public List<List<Player>> generateTeamsWithBalancedRating(){
        List<Player> players = playerDAO.get();
        Collections.shuffle(players);

        int totalPlayers = 22;
        int half = totalPlayers / 2;

        List<Player> selectedPlayers = players.subList(0, totalPlayers);

        List<Player> team1 = selectedPlayers.subList(0,half);
        List<Player> team2 = selectedPlayers.subList(half, totalPlayers);

        int ratingTeam1 = getTotalRating(team1);
        int ratingTeam2 = getTotalRating(team2);

        while(Math.abs(ratingTeam1 - ratingTeam2) > 10){
            Collections.shuffle(selectedPlayers);
            team1 = selectedPlayers.subList(0, half);
            team2 = selectedPlayers.subList(half, totalPlayers);
            ratingTeam1 = getTotalRating(team1);
            ratingTeam2 = getTotalRating(team2);
        }
        List<List<Player>> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        System.out.println("team1: " + ratingTeam1/(half));
        System.out.println("team2: " + ratingTeam2/(half));
        return teams;
    }

    private int getTotalRating(List<Player> players) {
        return players.stream().mapToInt(Player::getRating).sum();
    }
}

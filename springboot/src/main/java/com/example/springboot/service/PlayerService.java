package com.example.springboot.service;

import com.example.springboot.dao.PlayerDAOInter;
import com.example.springboot.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private final Map<Integer, List<Player>> generatedTeamsMap = new HashMap<>();
    private final PlayerDAOInter playerDAO;

    public PlayerService(PlayerDAOInter playerDAO) {
        this.playerDAO = playerDAO;
    }

    public List<Player> listOfPlayers(){
        ArrayList<Player> list = new ArrayList<>();
        playerDAO.findAll().forEach(list::add);
        return list;
    }

    public List<Player> playerList(){
        ArrayList<Player> list = new ArrayList<>();
        playerDAO.findAll().forEach(list::add);
        return list;
    }

    public Player savePlayer(Player player){
        return playerDAO.save(player);
    }

    public Player getPlayerById(int id){
        return playerDAO.findById(id).orElseThrow();
    }

    public void deletePlayerById(int id){
        playerDAO.deleteById(id);
    }

    public Player updatePlayer(Player player){
        return playerDAO.save(player);
    }

    public List<List<Player>> generateTeamsWithBalancedRating(){
        List<Player> allPlayers = listOfPlayers();

        if (allPlayers.size() < 22) {
            System.out.println("There is no required number of players to create teams");
            return Collections.emptyList();
        }

        List<Player> selectedPlayers = getRandomPlayers(allPlayers, 22);

        List<Player> team1 = new ArrayList<>();
        List<Player> team2 = new ArrayList<>();

        for (int i = 0; i < selectedPlayers.size(); i++) {
            if (i % 2 == 0) {
                team1.add(selectedPlayers.get(i));
            } else {
                team2.add(selectedPlayers.get(i));
            }
        }

        int team1Rating = calculateTeamRating(team1);
        int team2Rating = calculateTeamRating(team2);

        while (Math.abs(team1Rating - team2Rating) > 20) {
            Collections.shuffle(selectedPlayers);

            team1 = new ArrayList<>();
            team2 = new ArrayList<>();
            for (int i = 0; i < selectedPlayers.size(); i++) {
                if (i % 2 == 0) {
                    team1.add(selectedPlayers.get(i));
                } else {
                    team2.add(selectedPlayers.get(i));
                }
            }

            team1Rating = calculateTeamRating(team1);
            team2Rating = calculateTeamRating(team2);
        }

        System.out.println("team1: " + team1Rating/11);
        System.out.println("team2: " + team2Rating/11);
        List<List<Player>> result = new ArrayList<>();
        result.add(team1);
        result.add(team2);

        generatedTeamsMap.put(1, team1);
        generatedTeamsMap.put(2, team2);

        return result;
    }

    private List<Player> getRandomPlayers(List<Player> allPlayers, int count) {
        Collections.shuffle(allPlayers);
        return allPlayers.stream().limit(count).collect(Collectors.toList());
    }

    private int calculateTeamRating(List<Player> team) {
        return team.stream().mapToInt(Player::getRating).sum();
    }

}

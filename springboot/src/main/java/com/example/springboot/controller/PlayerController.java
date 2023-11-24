package com.example.springboot.controller;

import com.example.springboot.dao.PlayerDAOInter;
import com.example.springboot.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PlayerController {

    private final Map<Integer, List<Player>> generatedTeamsMap = new HashMap<>();
    private final PlayerDAOInter playerDAO;

    public PlayerController(PlayerDAOInter dao) {
        this.playerDAO = dao;
    }

    @GetMapping("/players")
    public List<Player> get(){
        ArrayList<Player> list = new ArrayList<>();
        playerDAO.findAll().forEach(list::add);
        return list;
    }

    @PostMapping("/player")
    public Player save(@RequestBody Player playerObj){
         return playerDAO.save(playerObj);
    }

    @GetMapping("/player/{id}")
    public Player get(@PathVariable int id){
        return playerDAO.findById(id).orElseThrow();
    }

    @DeleteMapping("/player/{id}")
    public void delete(@PathVariable int id){
        playerDAO.deleteById(id);
    }


    @PutMapping("/player")
    public Player update(@RequestBody Player playerObj){
        return playerDAO.save(playerObj);
    }


    @GetMapping("/generated-teams")
    public List<List<Player>> generateTeamsWithBalancedRating() {
        List<Player> allPlayers = (List<Player>) playerDAO.findAll();

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

    @GetMapping("/get-saved-teams/{teamId}")
    public ResponseEntity<List<Player>> getSavedTeams(@PathVariable int teamId) {
        Optional<List<Player>> team = Optional.ofNullable(generatedTeamsMap.get(teamId));

        return team.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

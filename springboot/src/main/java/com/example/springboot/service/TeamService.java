package com.example.springboot.service;

import com.example.springboot.dao.TeamDAOInter;
import com.example.springboot.model.Player;
import com.example.springboot.model.Team;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamDAOInter teamDAO;
    private final PlayerService playerService;

    public TeamService(TeamDAOInter teamDAO, PlayerService playerService) {
        this.teamDAO = teamDAO;
        this.playerService = playerService;
    }

    public Optional<Team> getTeamWithPlayersById(int id){
        return teamDAO.findByIdWithPlayers(id);
    }

    public Optional<Team> getTeamByName(String name){return teamDAO.findByTeamName(name);}

    public void deleteTeamById(int id){teamDAO.deleteById(id);}

    public List<List<Player>> generateTeamsWithBalancedRating(String teamName1, String teamName2){
        List<Player> allPlayers = playerService.listOfPlayers();

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

        Team savedTeam1 = saveTeam(team1, team1Rating/11, teamName1);
        Team savedTeam2 = saveTeam(team2, team2Rating/11, teamName2);

        System.out.println("team1: " + team1Rating/11);
        System.out.println("team2: " + team2Rating/11);
        List<List<Player>> result = new ArrayList<>();
        result.add(team1);
        result.add(team2);

        return result;
    }

    public List<Player> getRandomPlayers(List<Player> allPlayers, int count) {
        Collections.shuffle(allPlayers);
        return allPlayers.stream().limit(count).collect(Collectors.toList());
    }

    public int calculateTeamRating(List<Player> team) {
        return team.stream().mapToInt(Player::getRating).sum();
    }

    public Team saveTeam(List<Player> team, int rating, String teamName) {
        Team newTeam = new Team();
        newTeam.setTeamName(teamName);
        newTeam.setRating(rating);

        newTeam.setPlayers(team);

        newTeam = teamDAO.save(newTeam);

        playerService.savePlayers(team);

        return newTeam;
    }
}

package com.example.springboot.service;

import com.example.springboot.dao.PlayerRepository;
import com.example.springboot.dao.TeamRepository;
import com.example.springboot.model.Player;
import com.example.springboot.model.Team;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.springboot.util.ArrayPartition.divided_array;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public TeamService(TeamRepository teamDAO, PlayerRepository playerDAO) {
        this.teamRepository = teamDAO;
        this.playerRepository = playerDAO;
    }

    public Optional<Team> getTeamByName(String name){return teamRepository.findByTeamName(name);}

    public Optional<Team> addPlayerToTeam(Player player, String name){
        Optional<Team> teamOptional = getTeamByName(name);

        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            team.getPlayers().add(player);

            int teamRating = calculateTeamRating(team.getPlayers());
            team.setRating(teamRating);

            teamRepository.save(team);
        }

        return teamOptional;
    }

    public void deletePlayerFromTeam(String teamName, String playerName) {
        Optional<Team> teamOptional = getTeamByName(teamName);

        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            List<Player> players = team.getPlayers();

            Optional<Player> playerOptional = players.stream()
                    .filter(player -> player.getName().equals(playerName))
                    .findFirst();

            int rating = players.stream().mapToInt(Player::getRating).sum()/players.size();

            playerOptional.ifPresent(player -> {
                players.remove(player);
                team.setRating(rating);
                teamRepository.save(team);
            });
        }
    }
    public List<List<Player>> generateTeamsWithBalancedRating(String[] teamNames) {
        List<Player> listOfPlayers = playerRepository.findPlayers();
        if (teamNames.length == 0 || listOfPlayers.isEmpty()) {
            return null;
        }

        int[] playerRatings = listOfPlayers.stream().mapToInt(Player::getRating).toArray();

        List<List<Integer>> partitions = divided_array(playerRatings);

        List<List<Player>> finalTeams = new ArrayList<>();

        for (int i = 0; i < teamNames.length; i++) {
            List<Player> team = new ArrayList<>();
            List<Integer> ratings = partitions.get(i);
            for (Integer rating : ratings) {
                for (Player player : listOfPlayers) {
                    if (player.getRating() == rating) {
                        team.add(player);
                        break;
                    }
                }
            }
            saveTeam(team, calculateTeamRating(team), teamNames[i]);
            finalTeams.add(team);
        }

        return finalTeams;
    }

    public int calculateTeamRating(List<Player> team) {
        return team.stream().mapToInt(Player::getRating).sum() / team.size();
    }

    public void saveTeam(List<Player> team, int rating, String teamName) {
        Team newTeam = new Team();
        newTeam.setTeamName(teamName);
        newTeam.setRating(rating);

        newTeam.setPlayers(team);
        playerRepository.saveAll(team);
        teamRepository.save(newTeam);
    }
}

package com.example.springboot.service;

import com.example.springboot.dao.PlayerRepository;
import com.example.springboot.dao.TeamRepository;
import com.example.springboot.model.Player;
import com.example.springboot.model.Team;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TeamService {

    private final TeamRepository teamDAO;
    private final PlayerRepository playerDAO;

    public TeamService(TeamRepository teamDAO, PlayerRepository playerDAO) {
        this.teamDAO = teamDAO;
        this.playerDAO = playerDAO;
    }

    public Optional<Team> getTeamWithPlayersById(UUID id){
        return teamDAO.findByIdWithPlayers(id);
    }

    public Optional<Team> getTeamByName(String name){return teamDAO.findByTeamName(name);}

    public void deleteTeamById(UUID id){teamDAO.deleteById(id);}

    public Optional<Team> addPlayerToTeam(Player player, String name){
        Optional<Team> teamOptional = getTeamByName(name);

        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            team.getPlayers().add(player);

            int teamRating = calculateTeamRating(team.getPlayers());
            team.setRating(teamRating);

            teamDAO.save(team);
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
                teamDAO.save(team);
            });
        }
    }

    public List<List<Player>> generateTeamsWithBalancedRating(int count, String teamName1, String teamName2) {
        List<Player> listOfRandomPlayers = playerDAO.findRandomPlayers(count);
        int countOfPlayersInTeam = count / 2;

        List<List<Player>> bestTeams = new ArrayList<>();
        int smallestDifference = Integer.MAX_VALUE;

        List<List<Player>> allCombinations = IntStream.range(0, 1 << listOfRandomPlayers.size())
                .mapToObj(i -> IntStream.range(0, listOfRandomPlayers.size())
                        .filter(j -> (i & (1 << j)) > 0)
                        .mapToObj(listOfRandomPlayers::get)
                        .collect(Collectors.toList()))
                .filter(team -> team.size() == countOfPlayersInTeam)
                .toList();

        for (List<Player> team : allCombinations) {
            List<Player> remainingPlayers = listOfRandomPlayers.stream().filter(p -> !team.contains(p)).collect(Collectors.toList());

            if (remainingPlayers.size() != countOfPlayersInTeam) {
                continue;
            }

            int ratingTeam1 = calculateTeamRating(team);
            int ratingTeam2 = calculateTeamRating(remainingPlayers);
            int difference = Math.abs(ratingTeam1 - ratingTeam2);

            if (difference < smallestDifference) {
                smallestDifference = difference;
                bestTeams.clear();
                bestTeams.add(new ArrayList<>(team));
                bestTeams.add(new ArrayList<>(remainingPlayers));
            }
        }

        saveTeam(bestTeams.get(0), calculateTeamRating(bestTeams.get(0)) / countOfPlayersInTeam, teamName1);
        saveTeam(bestTeams.get(1), calculateTeamRating(bestTeams.get(1)) / countOfPlayersInTeam, teamName2);

        for(List<Player> player: bestTeams){
        System.out.println(player.toString());
        }
        return bestTeams;
    }

    public int calculateTeamRating(List<Player> team) {
        return team.stream().mapToInt(Player::getRating).sum();
    }

    public void saveTeam(List<Player> team, int rating, String teamName) {
        Team newTeam = new Team();
        newTeam.setTeamName(teamName);
        newTeam.setRating(rating);

        newTeam.setPlayers(team);
        playerDAO.saveAll(team);
        teamDAO.save(newTeam);
    }
}

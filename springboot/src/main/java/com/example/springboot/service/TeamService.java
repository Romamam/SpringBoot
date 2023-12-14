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

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public TeamService(TeamRepository teamDAO, PlayerRepository playerDAO) {
        this.teamRepository = teamDAO;
        this.playerRepository = playerDAO;
    }

    public Optional<Team> getTeamWithPlayersById(UUID id){
        return teamRepository.findByIdWithPlayers(id);
    }

    public Optional<Team> getTeamByName(String name){return teamRepository.findByTeamName(name);}

    public void deleteTeamById(UUID id){
        teamRepository.deleteById(id);}

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

    public List<List<Player>> generateTeamsWithBalancedRating(int count, String[] teamNames) {
        List<Player> listOfRandomPlayers = playerRepository.findRandomPlayers(count);
        int countOfPlayersInTeam = count / teamNames.length;

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

            int ratingDifference = calculateTeamRatingDifference(team, remainingPlayers);

            if (ratingDifference < smallestDifference) {
                smallestDifference = ratingDifference;
                bestTeams.clear();
                bestTeams.add(new ArrayList<>(team));
                bestTeams.add(new ArrayList<>(remainingPlayers));
            }
        }

        for (int i = 0; i < teamNames.length; i++) {
            String teamName = teamNames[i];
            List<Player> teamPlayers = bestTeams.get(i);
            int teamRating = calculateTeamRating(teamPlayers) / countOfPlayersInTeam;
            saveTeam(teamPlayers, teamRating, teamName);
        }

        for (List<Player> player : bestTeams) {
            System.out.println(player.toString());
        }
        return bestTeams;
    }

    public int calculateTeamRatingDifference(List<Player> team1, List<Player> team2) {
        int ratingTeam1 = calculateTeamRating(team1);
        int ratingTeam2 = calculateTeamRating(team2);
        return Math.abs(ratingTeam1 - ratingTeam2);
    }
    public int calculateTeamRating(List<Player> team) {
        return team.stream().mapToInt(Player::getRating).sum();
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

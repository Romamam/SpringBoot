package com.example.springboot.service;

import com.example.springboot.dao.PlayerRepository;
import com.example.springboot.dao.TeamRepository;
import com.example.springboot.model.Player;
import com.example.springboot.model.Team;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private List<List<Player>> generateAllCombinations(List<Player> players, int countOfPlayersInTeam) {
        List<List<Player>> allCombinations = new ArrayList<>();

        int numberOfPlayers = players.size();

        for (int i = 0; i < 1 << numberOfPlayers; i++) {
            int finalI = i;
            List<Player> team = IntStream.range(0, numberOfPlayers)
                    .filter(j -> (finalI & (1 << j)) > 0)
                    .mapToObj(players::get)
                    .collect(Collectors.toList());

            if (team.size() == countOfPlayersInTeam && hasUniquePlayers(team, allCombinations)) {
                allCombinations.add(team);
            }
        }

        return allCombinations;
    }

    private boolean hasUniquePlayers(List<Player> team, List<List<Player>> existingTeams) {
        Set<UUID> uniquePlayerIds = new HashSet<>();
        for (Player player : team) {
            if (!uniquePlayerIds.add(player.getId())) {
                return false;
            }
        }

        for (List<Player> existingTeam : existingTeams) {
            if (existingTeam.stream().anyMatch(player -> uniquePlayerIds.contains(player.getId()))) {
                return false;
            }
        }

        return true;
    }
public List<List<Player>> generateTeamsWithBalancedRating(String[] teamNames) {
    List<Player> listOfRandomPlayers = playerRepository.findPlayers();
    int countOfPlayersInTeam = listOfRandomPlayers.size() / teamNames.length;

    List<List<Player>> bestTeams = generateTeams(listOfRandomPlayers, countOfPlayersInTeam, teamNames.length);

    for (int i = 0; i < teamNames.length; i++) {
        List<Player> playerList = bestTeams.get(i);
        saveTeam(playerList, calculateTeamRating(playerList), teamNames[i]);
    }

    return bestTeams;
}

    private List<List<Player>> generateTeams(List<Player> players, int countOfPlayersInTeam, int numOfTeams) {
        List<List<Player>> bestTeams = new ArrayList<>();
        int smallestDifference = Integer.MAX_VALUE;

        List<List<Player>> allCombinations = generateAllCombinations(players, countOfPlayersInTeam * numOfTeams);

        for (List<Player> combination : allCombinations) {
            List<List<Player>> teams = new ArrayList<>();
            for (int i = 0; i < numOfTeams; i++) {
                int startIdx = i * countOfPlayersInTeam;
                int endIdx = startIdx + countOfPlayersInTeam;
                List<Player> team = combination.subList(startIdx, endIdx);
                teams.add(new ArrayList<>(team));
            }

            int difference = calculateDifference(teams);

            if (difference < smallestDifference) {
                smallestDifference = difference;
                bestTeams = new ArrayList<>(teams);
            }
        }

        return bestTeams;
    }

    private int calculateDifference(List<List<Player>> teams) {
        int totalDifference = 0;

        for (int i = 0; i < teams.size() - 1; i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                int ratingTeam1 = calculateTeamRating(teams.get(i));
                int ratingTeam2 = calculateTeamRating(teams.get(j));
                totalDifference += Math.abs(ratingTeam1 - ratingTeam2);
            }
        }

        return totalDifference;
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

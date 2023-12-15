package com.example.springboot.service;

import com.example.springboot.dao.PlayerRepository;
import com.example.springboot.dao.TeamRepository;
import com.example.springboot.model.Player;
import com.example.springboot.model.Team;
import com.example.springboot.util.CombinationGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<List<Player>> generateTeamsWithBalancedRating(String[] teamNames) {
        List<Player> listOfPlayers = playerRepository.findPlayers();
        int countOfPlayersInTeam = listOfPlayers.size() / teamNames.length;

        List<List<Player>> allCombinations = CombinationGenerator.generateCombinations(listOfPlayers, countOfPlayersInTeam);

        List<List<List<Player>>> generatedCombinations = CombinationGenerator.generateCombinations(allCombinations, teamNames.length);
        List<List<Player>> finalTeams = null;
        int diff = Integer.MAX_VALUE;
        for(List<List<Player>> teams : generatedCombinations){
            boolean validTeamsCombinations = true;
            for(List<Player> listPlayers : teams){
                validTeamsCombinations = teams.stream().filter(t -> t != listPlayers).flatMap(t -> t.stream()).noneMatch(listPlayers::contains);
                if(!validTeamsCombinations) {
                    break;
                }
            }
            if (validTeamsCombinations) {
                List<Integer> teamRatings = teams.stream().map(this::calculateTeamRating).sorted(Comparator.reverseOrder()).collect(Collectors.toCollection(ArrayList::new));
                System.out.println(teamRatings);
                int currDiff = 0;
                for (int i = 0; i < teamRatings.size() - 1; ++i) {
                    currDiff += teamRatings.get(i) - teamRatings.get(i + 1);
                }
                if (currDiff < diff) {
                    diff = currDiff;
                    finalTeams = teams;
                }
            }
        }
        for(int i = 0; i < teamNames.length; i++){
            List<Player> playerList = finalTeams.get(i);
            saveTeam(playerList, calculateTeamRating(playerList), teamNames[i]);
        }
        return finalTeams;
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

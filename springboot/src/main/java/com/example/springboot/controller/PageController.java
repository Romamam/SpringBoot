package com.example.springboot.controller;

import com.example.springboot.dao.PlayerRepository;
import com.example.springboot.dao.TeamRepository;
import com.example.springboot.model.Player;
import com.example.springboot.model.Team;
import com.example.springboot.service.TeamService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
public class PageController {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final TeamService teamService;

    public PageController(PlayerRepository playerRepository, TeamRepository teamRepository, TeamService teamService) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.teamService = teamService;
    }

    @RequestMapping("/")
    public String homePage(){
        return "home";
    }

    @GetMapping("/players")
    public String playersPage(Model model){
        List<Player> players = (List<Player>) playerRepository.findAll();
        model.addAttribute("players", players);
        return "players";
    }

    @GetMapping("/player/{id}")
    public String playerPage(@PathVariable UUID id, Model model) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid player ID"));
        model.addAttribute("player", player);
        return "player";
    }

    @GetMapping("/edit-player/{id}")
    public String editPlayerPage(@PathVariable UUID id, Model model) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid player ID"));
        model.addAttribute("player", player);
        return "editPlayer";
    }

    @GetMapping("/delete-player/{id}")
    public String deletePlayerPage(@PathVariable UUID id, Model model) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid player ID"));
        model.addAttribute("player", player);
        return "deletePlayer";
    }

    @PostMapping ("/delete-player/{id}")
    public String deletePlayer(@PathVariable UUID id) {
        playerRepository.deleteById(id);
        return "redirect:/players";
    }

    @PostMapping("/editPlayer")
    public String editPlayerSubmit(@ModelAttribute Player updatedPlayer) {
        UUID id = updatedPlayer.getId();
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid player ID"));

        existingPlayer.setName(updatedPlayer.getName());
        existingPlayer.setSecondName(updatedPlayer.getSecondName());
        existingPlayer.setRating(updatedPlayer.getRating());

        playerRepository.save(existingPlayer);

        return "redirect:/player/" + id;
    }

    @GetMapping("/create-Player")
    public String createPlayerPage(Model model){
        model.addAttribute("newPlayer", new Player());
        return "createPlayer";
    }

    @PostMapping("/createPlayer")
    public String createPlayer(@ModelAttribute("newPlayer") Player newPlayer) {
        playerRepository.save(newPlayer);
        return "redirect:/players";
    }
    @GetMapping("/teams")
    public String teamsPage(Model model){
        List<Team> teams = (List<Team>) teamRepository.findAll();
        model.addAttribute("teams", teams);
        return "teams";
    }

    @GetMapping("/team/{id}")
    public String teamPage(@PathVariable UUID id, Model model) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        model.addAttribute("team", team);
        return "team";
    }

    @GetMapping("/generate-teams")
    public String showGenerateTeamsPage() {
        return "generate-teams";
    }

    @PostMapping("/generate-teams")
    public String generateTeams(@RequestParam("teamNames") String[] teamNames,
                                Model model) {
        List<List<Player>> generatedTeams = teamService.generateTeamsWithBalancedRating(teamNames);
        model.addAttribute("generatedTeams", generatedTeams);
        model.addAttribute("teamNames", teamNames);
        System.out.println(generatedTeams);


        for (List<Player> team : generatedTeams) {
            for (Player player : team) {
                System.out.println("Player: " + player.getName() + " " + player.getSecondName());
            }
        }
        return "generated-teams";
    }

    @Transactional
    @PostMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatusPlayerActiveOrNotActive(@PathVariable UUID id) {
        try {
            playerRepository.toggleActiveStatus(id);
            Player player = playerRepository.findById(id).get();
            return ResponseEntity.ok().body(player);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player with ID " + id + " not found");
        }
    }

    @DeleteMapping("/delete-team/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable UUID id){
        teamRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-all-players")
    public ResponseEntity<?> deleteAllPlayers(){
        playerRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-all-teams")
    public ResponseEntity<?> deleteAllTeams(){
        teamRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

}
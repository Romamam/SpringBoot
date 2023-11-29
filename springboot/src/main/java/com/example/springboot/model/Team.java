package com.example.springboot.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "teams")
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "team_name")
    private String teamName;
    @ManyToMany
    @JoinTable(
            name = "team_players",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private List<Player> players;
    @Column(name = "rating")
    private int rating;

}

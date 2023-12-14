package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "players")
@Data
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "second_name")
    private String secondName;
    @Column(name = "rating")
    private int rating;
    private boolean isActive;

    public Player(){
        isActive = true;
    }

    public Player(UUID id, String name, String secondName, int rating) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
        this.rating = rating;
        isActive = true;
    }
}

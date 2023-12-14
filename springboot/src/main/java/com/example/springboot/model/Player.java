package com.example.springboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

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

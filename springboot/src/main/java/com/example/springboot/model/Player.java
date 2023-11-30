package com.example.springboot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "players")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

}

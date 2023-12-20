package com.example.springboot.dao;

import com.example.springboot.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "players", path = "players")
public interface PlayerRepository extends PagingAndSortingRepository<Player, UUID>, CrudRepository<Player, UUID> {
    Page<Player> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM players WHERE is_Active = true", nativeQuery = true)
    List<Player> findPlayers();

    @Modifying
    @Query(value = "UPDATE players SET is_Active = CASE WHEN is_Active = true THEN false ELSE true END WHERE id = :id", nativeQuery = true)
    void toggleActiveStatus(UUID id);

    Player findPlayerByName(String name);
}

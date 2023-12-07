package com.example.springboot.dao;

import com.example.springboot.model.Team;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "team", path = "team")
public interface TeamRepository extends CrudRepository<Team, UUID>, PagingAndSortingRepository<Team, UUID> {
    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.players WHERE t.id = :id")
    Optional<Team> findByIdWithPlayers(@Param("id") UUID id);

    Optional<Team> findByTeamName(String teamName) throws EntityNotFoundException;
}

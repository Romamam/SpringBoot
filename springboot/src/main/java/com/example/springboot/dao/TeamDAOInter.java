package com.example.springboot.dao;

import com.example.springboot.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "team", path = "team")
public interface TeamDAOInter extends CrudRepository<Team, Integer> {
    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.players WHERE t.id = :id")
    Optional<Team> findByIdWithPlayers(@Param("id") int id);

    Optional<Team> findByTeamName(String teamName);
}

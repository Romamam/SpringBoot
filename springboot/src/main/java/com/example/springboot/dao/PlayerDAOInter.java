package com.example.springboot.dao;

import com.example.springboot.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PlayerDAOInter extends PagingAndSortingRepository<Player, Integer>, CrudRepository<Player, Integer> {
    List<Player> findByName(@Param("name") String name);
}

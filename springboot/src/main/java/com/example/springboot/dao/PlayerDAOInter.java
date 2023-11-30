package com.example.springboot.dao;

import com.example.springboot.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "players", path = "players")
public interface PlayerDAOInter extends PagingAndSortingRepository<Player, UUID>, CrudRepository<Player, UUID> {
    Page<Player> findAll(Pageable pageable);

}

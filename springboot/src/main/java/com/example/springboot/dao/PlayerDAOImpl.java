package com.example.springboot.dao;


import com.example.springboot.model.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerDAOImpl implements PlayerDAO {

    private EntityManager entityManager;

    @Override
    public List<Player> get() {
        Session currentSession = entityManager.unwrap(Session.class);
        Query query = currentSession.createQuery("from Player", Player.class);
        return (List<Player>) query.getResultList();
    }

    @Override
    public Player get(int id) {
        return null;
    }

    @Override
    public void save(Player player) {

    }

    @Override
    public void delete(int id) {

    }
}

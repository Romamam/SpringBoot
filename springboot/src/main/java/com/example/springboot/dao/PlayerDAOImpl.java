package com.example.springboot.dao;


import com.example.springboot.model.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerDAOImpl implements PlayerDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Player> get() {
        Session currentSession = entityManager.unwrap(Session.class);
        Query query = currentSession.createQuery("from Player", Player.class);
        return (List<Player>) query.getResultList();
    }

    @Override
    public Player get(int id) {
        Session currentSession = entityManager.unwrap(Session.class);
        return currentSession.get(Player.class, id);
    }

    @Override
    public void save(Player player) {
        Session currentSession = entityManager.unwrap(Session.class);
        currentSession.saveOrUpdate(player);
    }

    @Override
    public void delete(int id) {
        Session currentSession = entityManager.unwrap(Session.class);
        currentSession.delete(currentSession.get(Player.class, id));
    }

    public List<Player> getPlayersByIds(List<Integer> playerIds){
        Session currentSession = entityManager.unwrap(Session.class);
        Query query = currentSession.createQuery("from Player where id in : ids", Player.class);
        query.setParameter("ids", playerIds);
        return query.getResultList();
    }


}

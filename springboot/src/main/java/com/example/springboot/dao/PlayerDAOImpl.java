package com.example.springboot.dao;


import com.example.springboot.model.Player;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PlayerDAOImpl {

    private final PlayerDAOInter dao;

    public PlayerDAOImpl(PlayerDAOInter dao) {
        this.dao = dao;
    }


    public List<Player> get() {
        ArrayList<Player> list = new ArrayList<>();
        dao.findAll().forEach(list::add);
        return list;
    }

    public Player get(int id) {
        return dao.findById(id).orElseThrow();
    }


    public void save(Player player) {
        dao.save(player);
    }

    public void deleteById(int id){
        dao.deleteById(id);
    }


}

package com.techelevator.dao;

import com.techelevator.model.Motivational;

import java.util.List;

public interface MotivationalDao {

    public List<Motivational> getAllMotivationals();
    public Motivational getRandomMotivational();

}

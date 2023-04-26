package com.techelevator.controller;

import com.techelevator.dao.JdbcMotivationalDao;
import com.techelevator.dao.MotivationalDao;
import com.techelevator.model.Motivational;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/motivationals")
public class MotivationalController {

    private MotivationalDao dao;

    public MotivationalController (MotivationalDao dao) {
        this.dao = new JdbcMotivationalDao(dataSource());
    }

    @GetMapping
    public List<Motivational> getAllMotivationals() {
        return dao.getAllMotivationals();
    }

    @GetMapping("/random")
    public Motivational getRandomMotivational() {
        return dao.getRandomMotivational();
    }


    private BasicDataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setUrl("jdbc:postgresql://localhost:5432/final_capstone");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        return dataSource;
    }

}

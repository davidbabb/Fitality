package com.techelevator.dao;

import com.techelevator.model.Motivational;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class JdbcMotivationalDao implements MotivationalDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcMotivationalDao (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Motivational> getAllMotivationals() {
        List <Motivational> list = new ArrayList<>();
        String sql = "SELECT * FROM motivational";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);

        while (sqlRowSet.next()) {
            Motivational motivational = mapRowToMotivational(sqlRowSet);
            list.add(motivational);
        }

        return list;
    }

    public Motivational getRandomMotivational () {
        List <Motivational> list = new ArrayList<>();
        String sql = "SELECT * FROM motivational";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);

        while (sqlRowSet.next()) {
            Motivational motivational = mapRowToMotivational(sqlRowSet);
            list.add(motivational);
        }

        Collections.shuffle(list);

        return list.get(0);
    }

    private Motivational mapRowToMotivational (SqlRowSet rs) {
        Motivational motivational = new Motivational();
        motivational.setAuthor(rs.getString("author"));
        motivational.setQuote(rs.getString("quote"));
        return motivational;
    }


}

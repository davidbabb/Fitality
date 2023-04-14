package com.techelevator.controller;

import com.techelevator.dao.JdbcAccountDao;
import com.techelevator.dao.JdbcWorkoutDao;
import com.techelevator.dao.WorkoutDao;
import com.techelevator.model.GymClass;
import com.techelevator.model.Workout;
import com.techelevator.model.WorkoutTime;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**********************************************************************
 ***                       WorkoutController                        ***
 **                     use to control all                           **
 *       the requests from the client side to end points              *
 **********************************************************************/
@RestController
@CrossOrigin
@RequestMapping("/workouts")
public class WorkoutController {
    private WorkoutDao dao;

    public WorkoutController () {
        this.dao = new JdbcWorkoutDao(dataSource());
    }
    /**4/12/23**/
    @GetMapping("/{workout_id}")
    public WorkoutTime getTimeByWorkoutId (@PathVariable("workout_id") int workoutId) {
        return dao.getTimeByWorkoutId(workoutId);
    }
    @GetMapping("/getUserWorkout/{user_id}")
    public List<Workout> getUserWorkout(@PathVariable("user_id") int userId){
        return dao.checkInListByUser(userId);
    }

    @PostMapping("/")


    @GetMapping("/gymclass")
    public List<GymClass> getUpcomingGymClass (){
        List<GymClass> classList = new ArrayList<>();
        classList = dao.getUpcomingGymClass();
        return classList;
    }

    private BasicDataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setUrl("jdbc:postgresql://localhost:5432/final_capstone");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        return dataSource;
    }
}

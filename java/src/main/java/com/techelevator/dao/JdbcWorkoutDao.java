package com.techelevator.dao;

import com.techelevator.model.*;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.sql.Time;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;

public class JdbcWorkoutDao implements WorkoutDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcWorkoutDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /****************************************************************************
     *                              JdbcWorkoutDao                              *
     *              access Workout Table and GymClass in database               *
     ****************************************************************************/

    @Override
    public boolean createWorkout(Workout workout) {
        String sql = "INSERT INTO public.workout(user_id, start_time) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, workout.getUserId(), workout.getTimeOfEntryAsTimestamp());
        return rowsAffected > 0;
    }

    @Override
    public Workout getWorkoutById(int workoutId) {
        return null; //TODO
    }

    @Override
    public List<Workout> checkInListByUser(int user_id) { //user can see all the dates and times they went to the gym
        List<Workout> workoutList = new ArrayList<>();
        String sql = "SELECT workout_id, user_id, start_time FROM public.workout WHERE user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, user_id);


        while (result.next()) {
            workoutList.add(mapRowToWorkout(result));
        }
        return workoutList;
    }
    /**4/12/23**/
    @Override
    public WorkoutTime getTimeByWorkoutId(int workout_id) {
        String sql = "SELECT workout_id, workout_start_time, workout_end_time\n" +
                "\tFROM public.workout_time" +
                "\tWHERE workout_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, workout_id);

        WorkoutTime workoutTime = null;
        if (result.next()) {
            workoutTime = mapRowToWorkoutTime(result);
        }
        return workoutTime;
    }

    public void createWorkoutTime(WorkoutTime workoutTime) {
        String sql = "INSERT INTO public.workout_time(workout_id, workout_date, workout_duration) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, workoutTime.getWorkoutId(), workoutTime.getDate(), workoutTime.getDuration());
    }

    @Override
    public List<WorkoutTime> getWorkoutTimesByUserId(int userId) {
        String sql = "SELECT * FROM workout_time wt " +
                "JOIN workout w ON w.workout_id = wt.workout_id " +
                "WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        List <WorkoutTime> list = new ArrayList<>();

        while (results.next()) {
            WorkoutTime workoutTime = mapRowToWorkoutTime(results);
            list.add(workoutTime);
        }

        return list;
    }
    @Override
    public Workout getLatestWorkoutByUser(int userId) {
        Workout latestWorkout = null;
        String sql = "SELECT workout_id, user_id, start_time FROM public.workout WHERE user_id = ? AND start_time IS NOT NULL ORDER BY start_time DESC, workout_id DESC LIMIT 1;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);

        if (result.next()) {
            latestWorkout = mapRowToWorkout(result);
        }
        return latestWorkout;
    }

    //let's talk about this one -- will need to return something other than just the number
    //may need another class for deserialization purposes
//    @Override
//    public int getTotalVisitedByUserId(int userId) {
//        String sql = "SELECT  COUNT(wt.workout_date) AS total_visited \n" +
//                "FROM workout_time wt \n" +
//                "JOIN workout w ON w.workout_id = wt.workout_id\n" +
//                "WHERE w.user_id = ?";
//        int total = 0;
//        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
//        if(result.next()){
//            total = result.getInt("total_visited");
//        }
//        return total;
//    }

    @Override
    public List<WorkoutTime> getListOfVisitedDateInMonthByUserId(int userId) {
        String sql ="SELECT DISTINCT wt.workout_id , wt.workout_date, wt.workout_duration " +
                "FROM workout_time wt " +
                "JOIN workout w ON w.workout_id = wt.workout_id " +
                "WHERE EXTRACT(month FROM workout_date) = EXTRACT(month FROM CURRENT_DATE) " +
                "  AND EXTRACT(year FROM workout_date) = EXTRACT(year FROM CURRENT_DATE) " +
                "  AND w.user_id = ? " +
                "ORDER BY workout_date ASC;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        List<WorkoutTime> visitedList = new ArrayList<>();
        while (results.next()){
            visitedList.add(mapRowToWorkoutTime(results));
        }
        return visitedList;
    }

    @Override
    public WorkoutInfo getWorkoutInfoByMonth(String date, int userId) {
        WorkoutInfo workoutInfo = null;
        LocalDate sqlDate = LocalDate.parse(date);
        String sql = "with cte as (\n" +
                "SELECT extract(month from workout_date) || '/' || EXTRACT(YEAR FROM workout_date) as month_year, COUNT (*) as visits, AVG(workout_duration) as avg_duration\n" +
                "FROM workout_time wt\n" +
                "JOIN workout w ON wt.workout_id = w.workout_id\n" +
                "WHERE extract(month from workout_date) = ? AND EXTRACT(YEAR FROM workout_date) = ? AND w.user_id = ?\n" +
                "group by month_year\n" +
                ")\n" +
                "SELECT month_year, visits, CAST(avg_duration as time)\n" +
                "FROM cte;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, sqlDate.getMonth().getValue(), sqlDate.getYear(), userId);

        if (result.next()) {
            workoutInfo = mapRowToWorkoutInfo(result);
        }

        return workoutInfo;
    }


    private Workout mapRowToWorkout(SqlRowSet rowSet) {
        Workout workout = new Workout();
        workout.setWorkoutId(rowSet.getInt("workout_id"));
        workout.setUserId(rowSet.getInt("user_id"));
        Timestamp timestamp = rowSet.getTimestamp("start_time");
        String timestampString = timestamp != null ? timestamp.toString() : "default_value";
        workout.setTimeOfEntry(timestamp.toString());
        return workout;
    }

    private WorkoutTime mapRowToWorkoutTime(SqlRowSet rowSet) {
        WorkoutTime workoutTime = new WorkoutTime();
        workoutTime.setWorkoutId(rowSet.getInt("workout_id"));
        workoutTime.setDate(rowSet.getDate("workout_date"));

        Timestamp durationTimestamp = rowSet.getTimestamp("workout_duration");
        String durationStr = durationTimestamp.toLocalDateTime().toLocalTime().toString();
        workoutTime.setDuration(durationStr);

        return workoutTime;
    }

    private WorkoutInfo mapRowToWorkoutInfo (SqlRowSet rs) {
        WorkoutInfo workoutInfo = new WorkoutInfo();
        workoutInfo.setUserVisits(rs.getInt("visits"));
        workoutInfo.setAvgDuration(rs.getTime("avg_duration"));
        workoutInfo.setMonth(rs.getString("month_year"));

        return workoutInfo;
    }
    /*******************************************************
     ***                 GYM CLASS                       ***
     **                                                   **
     *******************************************************/
    public List<GymClass> getAllGymClasses(){
        String sql =    "SELECT * FROM public.gym_class\n" +
                "ORDER BY class_id ASC;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        List<GymClass> gymClasses = new ArrayList<>();
        while (rowSet.next()) {
            GymClass gymClass = mapRowToGymClass(rowSet);
            gymClasses.add(gymClass);
        }
        return gymClasses;
    }

    public boolean createGymClass (GymClass gymClass){
        String sql = "INSERT INTO gym_class (class_name, datestart, timestart, dateend, timeend, signedup) " +
                "VALUES ( ?, ?, ?, ?, ?, ?); ";
        int row = jdbcTemplate.update(sql, gymClass.getClass_name(), gymClass.getDateStart(), gymClass.getTimeStart(),gymClass.getDateEnd(),gymClass.getTimeEnd(), gymClass.getSignedUp());

        return row == 1 ? true : false;
    }

    public List<GymClass> getGymClassesByName (String class_name){
        String sql =    "SELECT class_id, class_name, datestart, timestart, dateend, timeend, signedup " +
                "FROM gym_class " +
                "WHERE (datestart > NOW()::DATE AND datestart < now()+ interval '30 days' OR (datestart = NOW()::DATE AND timestart >= now()::time)) " +
                "AND class_name LIKE ? " +
                "ORDER BY datestart ASC ";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, "%" + class_name + "%");

        List<GymClass> gymClasses = new ArrayList<>();
        while (rowSet.next()) {
            GymClass gymClass = mapRowToGymClass(rowSet);
            gymClasses.add(gymClass);
        }

        return gymClasses;
    }

    public boolean updateGymClass(GymClass gymclass)
    {
        String sql =    "UPDATE gym_class " +
                "SET class_name = ?, datestart = ?, timestart = ?, dateend = ?, timeend = ?, signedup = ? " +
                "WHERE class_id = ?";

        int rowsAffected = jdbcTemplate.update(sql,
                gymclass.getClass_name(),
                gymclass.getDateStart(),
                gymclass.getTimeStart(),
                gymclass.getDateEnd(),
                gymclass.getTimeEnd(),
                gymclass.getSignedUp(),
                gymclass.getClassId());

        return rowsAffected == 1 ? true : false;
    }

    public List<GymClass> getUpcomingGymClass (){
        List<GymClass> listOfClasses = new ArrayList<>();
        String sql = "SELECT g.class_id, g.class_name, g.datestart, g.timestart, g.dateend, g.timeend, g.signedup \n" +
                "FROM gym_class g\n" +
                "WHERE datestart > NOW()::DATE AND datestart < now()+ interval '14 days' OR (datestart = NOW()::DATE AND timestart >= now()::time) \n" +
                "ORDER BY datestart ASC; ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            listOfClasses.add(mapRowToGymClass(results));
        }
        return listOfClasses;
    }

    public GymClass mapRowToGymClass (SqlRowSet rowSet){
        GymClass gymClass = new GymClass();
        gymClass.setClassId(rowSet.getInt("class_id"));
        gymClass.setClass_name(rowSet.getString("class_name"));
        gymClass.setDateStart(rowSet.getDate("datestart"));
        gymClass.setTimeStart(rowSet.getTime("timestart"));
        gymClass.setDateEnd(rowSet.getDate("dateend"));
        gymClass.setTimeEnd(rowSet.getTime("timeend"));
        gymClass.setSignedUp(rowSet.getInt("signedup"));
        return gymClass;
    }
}

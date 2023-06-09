package com.techelevator.controller;

import com.techelevator.dao.AccountDao;
import com.techelevator.dao.EquipmentDao;
import com.techelevator.dao.JdbcAccountDao;
import com.techelevator.dao.JdbcEquipmentDao;
import com.techelevator.model.Account;
import com.techelevator.model.Equipment;
import com.techelevator.model.EquipmentUsageLog;
import com.techelevator.model.MachineMetric;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.time.Year;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
/***********************************************************************
 ***                      EquipmentController                        ***
 **                     use to control all                            **
 *       the requests from the client side to end points               *
 ***********************************************************************/
@PreAuthorize("isAuthenticated()")
@RestController
@CrossOrigin
@RequestMapping("/equipments")
public class EquipmentController {
    private EquipmentDao dao;

    public EquipmentController () {
            this.dao = new JdbcEquipmentDao(dataSource());
    }

      @GetMapping("")
      public List<Equipment> listAll() {
        return dao.listAll();
      }

      @GetMapping("/{equipment_id}" )
      public Equipment getEquipmentById (@PathVariable("equipment_id") int equipmentId) {
        return dao.findEquipmentByEquipmentId(equipmentId);
      }

      @GetMapping ("/name/{exercise_name}")
      public String getEquipmentNameByExerciseName (@PathVariable("exercise_name") String exerciseName) {
        return dao.getEquipmentNameByExerciseName(exerciseName);
      }

      //TODO equipment usage date time
     @PreAuthorize("hasRole('ADMIN')")
     @GetMapping("/usage/{equipment_usage_date_time}")
     public List<MachineMetric> getMachineMetrics (@PathVariable("equipment_usage_date_time") String date) {
        return dao.getMachineMetrics(date);
     }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usage/{equipment_usage_date_time}/{equipment_name}")
    public List<MachineMetric> getMachineMetricsByName (@PathVariable("equipment_usage_date_time") String date, @PathVariable("equipment_name") String equipmentName) {
        return dao.getMachineMetricsByName(date, equipmentName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/years")
    public List <Integer> getYears () {
        return dao.getYears();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public void createEquipment (@RequestBody Equipment equipment) {
        dao.createEquipment(equipment);
    }

    private BasicDataSource dataSource(){
            BasicDataSource dataSource = new BasicDataSource();

            dataSource.setUrl("jdbc:postgresql://localhost:5432/final_capstone");
            dataSource.setUsername("postgres");
            dataSource.setPassword("postgres1");

            return dataSource;
    }
}


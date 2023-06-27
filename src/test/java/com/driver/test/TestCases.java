package com.driver.test;

import com.driver.EaseMyTrip;
import com.driver.controllers.AirportController;
import com.driver.controllers.AirportService;
import com.driver.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;


@SpringBootTest(classes = EaseMyTrip.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCases {

    @Autowired
    AirportService airportService;

    @BeforeEach
    public void setup(){
        Airport airport1=new Airport("dbc",40,City.DELHI);
        Airport airport2=new Airport("bac",45,City.CHANDIGARH);
        airportService.addAirport(airport1);
        airportService.addAirport(airport2);

        Flight flight1=new Flight(1,City.BANGLORE,City.DELHI,10,new Date(),1.2);
        Flight flight2=new Flight(2,City.DELHI,City.CHANDIGARH,10,new Date(),1.2);
        Flight flight3=new Flight(3,City.JAIPUR,City.DELHI,10,new Date(),1);
        airportService.addFlight(flight1);
        airportService.addFlight(flight2);
        airportService.addFlight(flight3);


        Passenger passenger1=new Passenger(1,"abc@com","rahul",25);
        Passenger passenger2=new Passenger(2,"abc@com","rajesh",24);
        Passenger passenger3=new Passenger(3,"abc@com","raju",26);
        airportService.addPassenger(passenger1);
        airportService.addPassenger(passenger2);
        airportService.addPassenger(passenger3);

        airportService.bookATicket(2,1);
        airportService.bookATicket(3,2);
        airportService.bookATicket(1,3);
    }
    @Test
    public void getLargestAirportName(){
        System.out.println(airportService.getLargestAirportName());
        Assertions.assertEquals("bac",airportService.getLargestAirportName());
    }

    @Test
    public void getNumberOfPeopleOnTest(){
        System.out.println(airportService.getNumberOfPeopleOn(new Date(),"dbc"));

    }
}


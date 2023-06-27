package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {

    Map<Integer, Passenger> passengerMap=new HashMap<>();
    Map<Integer, Flight> flightMap=new HashMap<>();
    Map<City, Airport> airportMap=new HashMap<>();
    Map<Integer,List<Integer>> passengerFlightMap=new HashMap<>();
    Map<Integer, List<Integer>> flightPassengersListMap=new HashMap<>();

    public void addAirport(Airport airport) {
        City city=airport.getCity();
//        if (airportMap.containsKey(city)) return false;
        airportMap.put(city,airport);
//        return true;
    }

    public String getLargestAirportName() {
        String largest="";
        int terminalCnt=0;
        for (Airport airport: airportMap.values()) {
            int cnt=airport.getNoOfTerminals();
            if(cnt>terminalCnt){
                terminalCnt=cnt;
                largest=airport.getAirportName();
            } else if (cnt==terminalCnt) {
                String airportName=airport.getAirportName();
                if(airportName.compareTo(largest)<0) largest=airportName;
            }
        }
        return largest;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        double shortestTime=-1;
        for (Flight flight:flightMap.values()){
            if(flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity)){
                if(shortestTime==-1 || flight.getDuration()<shortestTime) shortestTime=flight.getDuration();
            }
        }
        return shortestTime;
    }

    public void addFlight(Flight flight) {
        int id=flight.getFlightId();
        flightMap.put(id,flight);
        flightPassengersListMap.putIfAbsent(flight.getFlightId(),new ArrayList<>());
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        if(!flightMap.containsKey(flightId)) return null;
        City city=flightMap.get(flightId).getFromCity();
        if (airportMap.containsKey(city)) return airportMap.get(city).getAirportName();
        return null;
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        if (!passengerMap.containsKey(passengerId)) return "FAILURE";

        if(passengerFlightMap.containsKey(passengerId) && passengerFlightMap.get(passengerId).contains(flightId)) return "FAILURE";
        if (!flightMap.containsKey(flightId) || flightPassengersListMap.get(flightId).size()==flightMap.get(flightId).getMaxCapacity()) return "FAILURE";
        passengerFlightMap.putIfAbsent(passengerId,new ArrayList<>());
        List<Integer> flights=passengerFlightMap.get(passengerId);
        flights.add(flightId);
        passengerFlightMap.put(passengerId,flights);

        List<Integer> passengers=flightPassengersListMap.get(flightId);
        passengers.add(passengerId);
        flightPassengersListMap.put(flightId,passengers);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        if (!passengerMap.containsKey(passengerId) || !flightMap.containsKey(flightId)) return "FAILURE";
        if (passengerFlightMap.containsKey(passengerId) && passengerFlightMap.get(passengerId).contains(flightId)){
            passengerFlightMap.get(passengerId).remove(flightId);
            flightPassengersListMap.get(flightId).remove(passengerId);
            return "SUCCESS";
        }
        return "FAILURE";
    }

    public String addPassenger(Passenger passenger) {
        if (passengerMap.containsKey(passenger.getPassengerId())) return "FAILURE";
        passengerMap.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        if (passengerFlightMap.containsKey(passengerId)) {
            return passengerFlightMap.get(passengerId).size();
        }
        return 0;
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        int noOfPassengers=0;
        if (flightPassengersListMap.containsKey(flightId)){
            noOfPassengers = flightPassengersListMap.get(flightId).size();
        }
        int base=3000,diff=50;
        return noOfPassengers*(2*base+(noOfPassengers-1)*diff)/2;
    }

    public int calculateFlightFare(Integer flightId) {
        int noOfPassengers=0;
        if (flightPassengersListMap.containsKey(flightId)) noOfPassengers=flightPassengersListMap.get(flightId).size();
        return 3000+noOfPassengers*50;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        int noOfPassengers=0;
        for (Flight flight:flightMap.values()){
            City fromCity=flight.getFromCity();
            City toCity=flight.getToCity();
            if(airportMap.containsKey(fromCity) && airportMap.get(fromCity).getAirportName().equals(airportName)){
                Date date1=flight.getFlightDate();
                if(date.getDate()==date1.getDate() && date1.getMonth()==date.getMonth() && date1.getYear()==date.getYear())
                    if (flightPassengersListMap.containsKey(flight.getFlightId()))
                        noOfPassengers+=flightPassengersListMap.get(flight.getFlightId()).size();
            }
            if(airportMap.containsKey(toCity) && airportMap.get(toCity).getAirportName().equals(airportName)) {
                Date date1=flight.getFlightDate();
                date1.setTime(date1.getTime()+(long)flight.getDuration()*3600000);
                if(date.getDate()==date1.getDate() && date1.getMonth()==date.getMonth() && date1.getYear()==date.getYear())
                    if (flightPassengersListMap.containsKey(flight.getFlightId()))
                        noOfPassengers+=flightPassengersListMap.get(flight.getFlightId()).size();
            }
        }
        return noOfPassengers;

    }
}
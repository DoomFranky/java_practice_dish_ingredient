package com.example;

import miniDishmanagement.DataRetriever;
import miniDishmanagement.Dish;

public class Main {
    void main(String[] args) {
        DataRetriever dataRetriever = new DataRetriever();
        Dish dish = dataRetriever.findDishbyId(1);
        System.out.println(dish);
    }
}
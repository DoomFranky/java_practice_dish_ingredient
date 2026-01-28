package com.example;

import java.util.ArrayList;
import java.util.List;

import miniDishmanagement.CategoryEnum;
import miniDishmanagement.DataRetriever;
import miniDishmanagement.Dish;
import miniDishmanagement.DishTypeEnum;
import miniDishmanagement.Ingredients;

public class Main {
    void main(String[] args) {
        DataRetriever dataRetriever = new DataRetriever();
        List<Ingredients> listOfIngredients = new ArrayList<>();
        Dish dish = dataRetriever.findDishById(1);
        System.out.println(dataRetriever.findIngredients(1, 12));
    }
}
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
        System.out.println(dataRetriever.findDishById(1));
    }
}
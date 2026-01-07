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
        //System.out.println(dataRetriever.findDishbyId(1));
        //System.out.println(dataRetriever.findDishbyId(999));
        //System.out.println(dataRetriever.findIngredients(2,2));
        //System.out.println(dataRetriever.findIngredients(3,5));

        List<Ingredients> listOfIngredient = new ArrayList<>();
        Dish dish = new Dish (1,"Salade fraîche", DishTypeEnum.START, 2000.0,listOfIngredient);
        listOfIngredient.add(new Ingredients(6,"Fromage",1200.0,CategoryEnum.valueOf("DAIRY"), dish));
        listOfIngredient.add(new Ingredients(7,"Oignon",500.0,CategoryEnum.valueOf("VEGETABLE"), dish));
        //System.out.println(dataRetriever.createIngredients(listOfIngredient));
        listOfIngredient.clear();
        listOfIngredient.add(new Ingredients(5,"Carotte",2000.0,CategoryEnum.valueOf("VEGETABLE"), dish));
        listOfIngredient.add(new Ingredients(1,"Laitue",2000.0,CategoryEnum.valueOf("VEGETABLE") ,dish));
        //System.out.println(dataRetriever.createIngredients(listOfIngredient));
        listOfIngredient.clear();
        listOfIngredient.add(new Ingredients(6,"Fromage", 1200.0,CategoryEnum.valueOf("DAIRY"),dish));
        listOfIngredient.add(new Ingredients(7,"Oignon", 500.0,CategoryEnum.valueOf("VEGETABLE"),dish));

        //System.out.println(dataRetriever.saveDish(new Dish(1,"Salade fraîche", DishTypeEnum.START, listOfIngredient)));
        //System.out.println(dataRetriever.findDishByIngredientName("eur"));
        
        listOfIngredient.clear();
        listOfIngredient.add(new Ingredients(6,"Fromage",1200.0,CategoryEnum.valueOf("DAIRY"),dish));
        listOfIngredient.add(new Ingredients(7,"Oignon",500.0,CategoryEnum.valueOf("VEGETABLE"), dish));
        dish.setIngredients(listOfIngredient);
        //System.out.println(dish.getDishCost());

        dish = dataRetriever.findDishbyId(1);
        System.out.println(dish.getGrossMargin());

        dataRetriever.saveDish(dish);
    }
}
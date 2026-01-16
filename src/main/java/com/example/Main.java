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
        listOfIngredient.add(new Ingredients("Fromage",1200.0,CategoryEnum.valueOf("DAIRY")));
        listOfIngredient.add(new Ingredients("Oignon",500.0,CategoryEnum.valueOf("VEGETABLE")));
        //System.out.println(dataRetriever.createIngredients(listOfIngredient));
        //listOfIngredient.clear();
        //listOfIngredient.add(new Ingredients(5,"Carotte",2000.0,CategoryEnum.valueOf("VEGETABLE"), dish));
        //listOfIngredient.add(new Ingredients(1,"Laitue",2000.0,CategoryEnum.valueOf("VEGETABLE") ,dish));
        //System.out.println(dataRetriever.createIngredients(listOfIngredient));
        listOfIngredient.clear();
        dish = new Dish("Soupe de légume", DishTypeEnum.START, null, listOfIngredient);
        dish = new Dish(1,"Salade fraîche", DishTypeEnum.START, null, listOfIngredient);
        //listOfIngredient.add(new Ingredients(7,"Oignon", 500.0,CategoryEnum.valueOf("VEGETABLE"),dish));
        //listOfIngredient.add(new Ingredients(1,"Lauti", 800.0,CategoryEnum.valueOf("VEGETABLE"),dish));
        //listOfIngredient.add(new Ingredients(2,"Tomato", 600.0,CategoryEnum.valueOf("VEGETABLE"),dish));
        listOfIngredient.add(new Ingredients(6,"Fromage", 1200.0,CategoryEnum.valueOf("DAIRY"),dish));
        dish.setIngredients(listOfIngredient);

        //System.out.println(dataRetriever.saveDish(dish));
        listOfIngredient.clear();
        dish = new Dish(1, "Salade fraîche", DishTypeEnum.START,null, listOfIngredient);
        listOfIngredient.add(new Ingredients(7,"Oignon", 500.0,CategoryEnum.valueOf("VEGETABLE"),dish));

        dish.setIngredients(listOfIngredient);

        
        listOfIngredient.clear();
        listOfIngredient.add(new Ingredients("Carotte",1200.0,CategoryEnum.valueOf("DAIRY")));
        listOfIngredient.add(new Ingredients("Laitue",2000.0,CategoryEnum.valueOf("VEGETABLE")));
        dish.setIngredients(listOfIngredient);
        //System.out.println(dish.getDishCost());
        System.out.println(dataRetriever.findDishByIngredientName("eur"));
        listOfIngredient.clear(); 
        //System.out.println(dish.getGrossMargin());
    }
}
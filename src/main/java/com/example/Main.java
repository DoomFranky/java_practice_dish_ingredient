package com.example;

import java.util.ArrayList;
import java.util.List;

import miniDishmanagement.CategoryEnum;
import miniDishmanagement.DataRetriever;
import miniDishmanagement.Ingredients;

public class Main {
    void main(String[] args) {
        DataRetriever dataRetriever = new DataRetriever();
        System.out.println(dataRetriever.findDishbyId(1));
        //System.out.println(dataRetriever.findDishbyId(999));
        //System.out.println(dataRetriever.findIngredients(2,2));
        //System.out.println(dataRetriever.findIngredients(3,5));

        List<Ingredients> listOfIngredient = new ArrayList<>();
        listOfIngredient.add(new Ingredients("Fromage",CategoryEnum.valueOf("DAIRY"), 1200.0));
        listOfIngredient.add(new Ingredients("Oignon",CategoryEnum.valueOf("VEGETABLE"), 500.0));
        //System.out.println(dataRetriever.createIngredients(listOfIngredient));
        listOfIngredient.clear();
        listOfIngredient.add(new Ingredients("Carotte",CategoryEnum.valueOf("VEGETABLE"), 2000.0));
        listOfIngredient.add(new Ingredients("Laitue",CategoryEnum.valueOf("VEGETABLE"), 2000.0));
        //System.out.println(dataRetriever.createIngredients(listOfIngredient));
        listOfIngredient.clear();
        listOfIngredient.add(new Ingredients(6,"Fromage",CategoryEnum.valueOf("DAIRY"), 1200.0));
        listOfIngredient.add(new Ingredients(7,"Oignon",CategoryEnum.valueOf("VEGETABLE"), 500.0));
        
        //System.out.println(dataRetriever.saveDish(new Dish(1,"Salade fraîche", DishTypeEnum.START, listOfIngredient)));
    }
}
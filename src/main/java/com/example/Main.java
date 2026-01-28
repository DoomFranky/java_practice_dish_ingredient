package com.example;

import java.util.ArrayList;
import java.util.List;

import miniDishmanagement.CategoryEnum;
import miniDishmanagement.DataRetriever;
import miniDishmanagement.Dish;
import miniDishmanagement.DishIngredient;
import miniDishmanagement.DishTypeEnum;
import miniDishmanagement.Ingredients;

public class Main {
    void main(String[] args) {
        DataRetriever dataRetriever = new DataRetriever();
        //createIngredient
        List<Ingredients> listofIngredients = new ArrayList<>();
        listofIngredients.add(new Ingredients(null,"Fromage",1200.0,CategoryEnum.DAIRY,null));
        listofIngredients.add(new Ingredients(null, "Oignon",2000.0,CategoryEnum.VEGETABLE,null)); 
        //System.out.println(dataRetriever.createIngredients(listofIngredients));
        //END createIngredient finish:TRUE FOR NOW

        //saveDish
        List<DishIngredient> listofDishIngredients = new ArrayList<>();

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Salade fraîche");
        dish.setDishType(DishTypeEnum.START);
        listofDishIngredients.add(new DishIngredient(null, null, dataRetriever.findIngredientById(6), null, null));
        dish.setDishIngredients(listofDishIngredients);
        
        System.out.println(dataRetriever.saveDish(dish));
    }
}
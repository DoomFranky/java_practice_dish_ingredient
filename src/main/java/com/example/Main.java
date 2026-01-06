package com.example;

import miniDishmanagement.DataRetriever;

public class Main {
    void main(String[] args) {
        DataRetriever dataRetriever = new DataRetriever();
        System.out.println(dataRetriever.findDishbyId(1));
        //System.out.println(dataRetriever.findDishbyId(999));
        //System.out.println(dataRetriever.findIngredients(2,2));
        //System.out.println(dataRetriever.findIngredients(3,5));
    }
}
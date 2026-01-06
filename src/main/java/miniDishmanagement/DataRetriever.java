package miniDishmanagement;

import java.util.List;

public class DataRetriever {
    Dish findDishbyId (Integer id){
        throw new UnsupportedOperationException("Méthode non disponible");
    }

    List<Ingredients> findIngredients(int page, int size){
        throw new UnsupportedOperationException("Méthode non disponible");
    }

    List<Ingredients> createIngredients(List<Ingredients> newIngredients){
        throw new UnsupportedOperationException("Méthode non disponible");
    }

    Dish saveDish (Dish dishToSave){
        throw new UnsupportedOperationException("Méthode non disponible");
    }

    List<Dish> findDishByIngredientName(String IngredientName){
        throw new UnsupportedOperationException("Méthode non disponible");
    }

    List<Ingredients> findIngredientByCriteria(String ingredientName, CategoryEnum category, String dishName, int page, int size){
        throw new UnsupportedOperationException("Méthode non disponible");
    }
}

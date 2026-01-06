package miniDishmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    public Dish findDishbyId (Integer id){
        Dish dish = null;
        Connection connection = new DBConnection().getBDConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT d.id AS dish_id, d.name AS dish_name ,dish_type, "+
                "i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
                "i.category AS ingredient_category FROM \"Dish\" AS d JOIN \"Ingredient\" AS i ON i.id_dish = i.id WHERE d.id = ?"
            );
            preparedStatement.setInt(id,1);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Ingredients> listOfIngredient = new ArrayList<>();
            
            while(resultSet.next()){
                dish = new Dish(
                    resultSet.getInt("dish_id"),
                    resultSet.getString("dish_name"),
                    DishTypeEnum.valueOf(resultSet.getString("dish_type"))
                );
                listOfIngredient.add(new Ingredients(
                    resultSet.getInt("ingredient_id"), 
                    resultSet.getString("ingredient_name"), 
                    resultSet.getDouble("ingredient_price"), 
                    CategoryEnum.valueOf(resultSet.getString("ingredient_category")), 
                    dish));
            }
            dish.setIngredients(listOfIngredient);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return dish;
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

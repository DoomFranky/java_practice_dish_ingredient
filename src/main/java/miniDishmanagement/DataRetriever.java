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
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Ingredients> listOfIngredient = new ArrayList<>();
            
            while(resultSet.next()){
                dish = new Dish(
                    resultSet.getInt("dish_id"),
                    resultSet.getString("dish_name"),
                    DishTypeEnum.valueOf(resultSet.getString("dish_type")),
                    listOfIngredient
                );
                listOfIngredient.add(new Ingredients(
                    resultSet.getInt("ingredient_id"), 
                    resultSet.getString("ingredient_name"), 
                    resultSet.getDouble("ingredient_price"), 
                    CategoryEnum.valueOf(resultSet.getString("ingredient_category")), 
                    dish));
            }
            dish.setIngredients(listOfIngredient);
            connection.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return dish;
    }

    public List<Ingredients> findIngredients(int page, int size){
        List<Ingredients> listOfIngredients = new ArrayList<>();
        Connection connection =  new DBConnection().getBDConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
                "i.category AS ingredient_category, "+
                "d.id AS dish_id, d.name AS dish_name, dish_type "+
                "FROM \"Ingredient\" AS i JOIN \"Dish\" AS d ON i.id_dish=d.id "+
                "LIMIT ? OFFSET ?"
            );
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, (page-1)*size);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                listOfIngredients.add(new Ingredients(
                    resultSet.getInt("ingredient_id"), 
                    resultSet.getString("ingredient_name"), 
                    resultSet.getDouble("ingredient_price"), 
                    CategoryEnum.valueOf(resultSet.getString("ingredient_category")),
                    new Dish(
                        resultSet.getInt("dish_id"),
                        resultSet.getString("dish_name"),
                        DishTypeEnum.valueOf(resultSet.getString("dish_type"))
                    )
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listOfIngredients;
    }

    public List<Ingredients> createIngredients(List<Ingredients> newIngredients){
        Connection connection =  new DBConnection().getBDConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement udtapeSerial = connection.prepareStatement(
                "SELECT setval(pg_get_serial_sequence('\"Ingredient\"', 'id'), "+
                "(SELECT MAX(id) FROM \"Ingredient\"))"
            );
            System.out.println("wait...");
            udtapeSerial.executeQuery();
            System.out.println("good");
            for (Ingredients ingredient : newIngredients){
                String str = "INSERT INTO \"Ingredient\" ( name, category, price ) "+
                    "VALUES ( ? , ?::\"Category_of_ingredient\" , ? )";
                PreparedStatement preparedStatement = connection.prepareStatement(str);

                System.out.println(ingredient.getCategory().toString());

                preparedStatement.setString(1, ingredient.getName());
                preparedStatement.setString(2, ingredient.getCategory().toString());
                preparedStatement.setDouble(3, ingredient.getPrice());

                preparedStatement.executeUpdate();
            }
            connection.commit();
            connection.close();
            
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return findIngredients(1, 10);
    }

    Dish saveDish (Dish dishToSave){
        Connection connection = DBConnection().getBDConnection();
        throw new UnsupportedOperationException("Méthode non disponible");
    }

    List<Dish> findDishByIngredientName(String IngredientName){
        throw new UnsupportedOperationException("Méthode non disponible");
    }

    List<Ingredients> findIngredientByCriteria(String ingredientName, CategoryEnum category, String dishName, int page, int size){
        throw new UnsupportedOperationException("Méthode non disponible");
    }
}

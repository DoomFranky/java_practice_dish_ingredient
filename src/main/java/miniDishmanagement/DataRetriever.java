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
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT d.id AS dish_id, d.name AS dish_name ,dish_type, "+
                "i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
                "i.category AS ingredient_category FROM \"Dish\" AS d JOIN \"Ingredient\" AS i ON i.id = i.id_dish WHERE d.id = ?"
            );
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Ingredients> listOfIngredient = new ArrayList<>();
            
            while(resultSet.next()){
                System.out.println("loops");
                if (dish == null) {
                    dish = new Dish(
                        resultSet.getInt("dish_id"),
                        resultSet.getString("dish_name"),
                        DishTypeEnum.valueOf(resultSet.getString("dish_type").toUpperCase()),
                        listOfIngredient
                    );
                }
                listOfIngredient.add(new Ingredients(
                    resultSet.getInt("ingredient_id"), 
                    resultSet.getString("ingredient_name"), 
                    resultSet.getDouble("ingredient_price"), 
                    CategoryEnum.valueOf(resultSet.getString("ingredient_category")), 
                    dish
                ));
            }
            if (dish != null) {
                dish.setIngredients(listOfIngredient);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
        return dish;
    }

    public List<Ingredients> findIngredients(int page, int size){
        List<Ingredients> listOfIngredients = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
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
        } finally {
            dbConnection.closeTheConnection(connection);
        }
        return listOfIngredients;
    }

    public List<Ingredients> createIngredients(List<Ingredients> newIngredients){
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
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
            
        }catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
        return findIngredients(1, 10);
    }

    public Dish saveDish (Dish dishToSave){
        Dish dishInDB = findDishbyId(dishToSave.getId());
        List<Ingredients> ingredientsInDB = dishInDB.getIngredients();
        List<Ingredients> ingredientsList = dishToSave.getIngredients();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        try{
            connection.setAutoCommit(false);
            String str = "";
            if (dishToSave.getId()==null) {
                PreparedStatement udtapeSerial = connection.prepareStatement(
                    "SELECT setval(pg_get_serial_sequence('\"Ingredient\"', 'id'), "+
                    "(SELECT MAX(id) FROM \"Ingredient\"))"
                );
                udtapeSerial.executeQuery();
                str = "INSERT INTO \"Dish\" ( name, \"dishType\" ) "+
                "VALUES ( ? , ? )";
            }else {
                str = "UPDATE \"Dish\" SET name = ?, \"dishType\" = ? WHERE id = ?";
            }
            
            PreparedStatement preparedStatement = connection.prepareStatement(str);
            preparedStatement.setString(1, dishToSave.getName());
            preparedStatement.setString(2,dishInDB.getDishType().toString());
            if (str.contains("UPDATE")) {
                preparedStatement.setInt(3,dishToSave.getId());
            }

            String updateString = "";
            String updateOnNull = "";
            List<Integer> ingredientsToNull = new ArrayList<>();
            List<Integer> ingredientsToSave = new ArrayList<>();

            for(Ingredients ingredientInDB : ingredientsInDB){
                if (!ingredientsList.contains(ingredientInDB)) {
                    if (updateOnNull=="") {
                        updateOnNull+="UPDATE \"Ingredient\" SET id_dish = ? WHERE id IN ( ?";
                    }else{
                        updateOnNull+= ", ?";
                    }
                    ingredientsToNull.add(ingredientInDB.getId());
                }else{
                    if (updateString=="") {
                        updateString+="UPDATE \"Ingredient\" SET id_dish = ? WHERE id IN ( ?";
                    }else{
                        updateString+=", ?";
                    }
                    ingredientsToSave.add(ingredientInDB.getId());
                }
            }
            updateOnNull+=" )";
            updateString+=" )";
            if (updateOnNull != " )") {
                PreparedStatement nullUpdateStatement = connection.prepareStatement(updateOnNull);
                nullUpdateStatement.setNull(1, java.sql.Types.INTEGER);
                for (int i = 0; i <= ingredientsToNull.size() ; i++ ){
                    nullUpdateStatement.setInt(i+1, ingredientsToNull.get(i));
                }
                nullUpdateStatement.executeUpdate();
            }

            if (updateString != " )") {
                PreparedStatement updateSaveStatement = connection.prepareStatement(updateString);
                updateSaveStatement.setInt(1, dishToSave.getId());
                for (int i = 0; i <= ingredientsToSave.size() ; i++ ){
                    updateSaveStatement.setInt(i+1, ingredientsToSave.get(i-1));
                }
                updateSaveStatement.executeUpdate();
            }
            connection.commit();
        }catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
        return dishToSave;
    }

    public List<Dish> findDishByIngredientName(String IngredientName){
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        try{
            String str = "";
            PreparedStatement preparedStatement = connection.prepareStatement(str);
            
        }catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
        throw new UnsupportedOperationException("Méthode non disponible");
    }

    public List<Ingredients> findIngredientByCriteria(String ingredientName, CategoryEnum category, String dishName, int page, int size){
        throw new UnsupportedOperationException("Méthode non disponible");
    }
}

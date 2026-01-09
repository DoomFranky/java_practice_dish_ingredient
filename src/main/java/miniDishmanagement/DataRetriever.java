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
                "SELECT d.id AS dish_id, d.name AS dish_name ,dish_type, "+"d.\"dishPrice\" AS dish_price, "+
                "i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
                "i.category AS ingredient_category FROM \"Dish\" AS d JOIN \"Ingredient\" AS i ON d.id = i.id_dish WHERE d.id = ?"
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
                        resultSet.getObject("dish_price") == null ? null : resultSet.getDouble("dish_price"),
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
            preparedStatement.close();
            resultSet.close();
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
                "d.id AS dish_id, d.name AS dish_name, dish_type ,d.\"dishPrice\" AS dish_price, "+
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
                        DishTypeEnum.valueOf(resultSet.getString("dish_type")),
                        resultSet.getDouble("dish_price"),
                        listOfIngredients
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
            udtapeSerial.executeQuery();
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
            udtapeSerial.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
        return findIngredients(1, 10);
    }

    public Dish saveDish (Dish dishToSave){
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        try{
            connection.setAutoCommit(false);
            
            PreparedStatement udtapeSerialStatement = connection.prepareStatement(
                "SELECT setval(pg_get_serial_sequence('\"Dish\"', 'id'), "+
                "(SELECT MAX(id) FROM \"Dish\"))"
            );
            
            ResultSet serialResultSet = udtapeSerialStatement.executeQuery();

            Integer idDish = null;
            if (dishToSave.getId()!=null) {
                idDish = dishToSave.getId();
            } else if (serialResultSet.next()) {
                idDish = serialResultSet.getInt("setval");
            }
            String str = "";
            str = "INSERT INTO \"Dish\" ( name, \"dish_type\", \"DishPrice\" ) "+
                "VALUES ( ? , ?::\"Type_of_dish\" , ? ) ON CONFLICT (name) DO UPDATE SET name = ?, \"dish_type\" = ?::\"Type_of_dish\" , \"DishPrice\"= ?";

            PreparedStatement dishUpdateStatement = connection.prepareStatement(str);

            dishUpdateStatement.setString(1, dishToSave.getName());
            dishUpdateStatement.setString(2, dishToSave.getDishType().toString());
            if(dishToSave.getDishPrice()==null){
                dishUpdateStatement.setNull(3, java.sql.Types.NUMERIC);
            }else{
                dishUpdateStatement.setDouble(3, dishToSave.getDishPrice());
            }
            dishUpdateStatement.setString(4, dishToSave.getName());
            dishUpdateStatement.setString(5, dishToSave.getDishType().toString());
            if(dishToSave.getDishPrice()==null){
                dishUpdateStatement.setNull(6, java.sql.Types.NUMERIC);
            }else{
                dishUpdateStatement.setDouble(6, dishToSave.getDishPrice());
            }
            
            String updateIngredientString = 
                "With null_ingredient AS( "+
                "UPDATE \"Ingredient\" SET id_dish = NULL "+
                "WHERE id_dish = ? ) "+
                "UPDATE \"Ingredient\" SET id_dish = ? "+
                "WHERE id = ANY(?) ";

            Integer[] ingredientIdList = dishToSave.getIngredients().stream().map(i->i.getId()).toArray(Integer[]::new);
            PreparedStatement ingredientUpdateStatement =  connection.prepareStatement(updateIngredientString);

            ingredientUpdateStatement.setInt(1,idDish);
            ingredientUpdateStatement.setInt(2,idDish);
            ingredientUpdateStatement.setArray(3,connection.createArrayOf("INTEGER", ingredientIdList));

            dishUpdateStatement.executeUpdate();
            ingredientUpdateStatement.executeUpdate();

            connection.commit();
            udtapeSerialStatement.close();
            serialResultSet.close();
            dishUpdateStatement.close();
            ingredientUpdateStatement.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
        return dishToSave;
    }

    public List<Dish> findDishByIngredientName(String IngredientName){
        List<Dish> listOfDish = new ArrayList<>(); 
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        try{
            String str = "SELECT d.id AS dish_id, d.name AS dish_name ,dish_type, d.\"dishPrice\" AS dish_price, "+
                "i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
                "i.category AS ingredient_category FROM \"Dish\" AS d JOIN \"Ingredient\" AS i ON d.id = i.id_dish WHERE i.name ILIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(str);
            preparedStatement.setString(1, "%"+IngredientName+"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Ingredients> listOfIngredients = new ArrayList<>();
            while(resultSet.next()){
                Dish dish = new Dish(
                    resultSet.getInt("dish_id"),
                    resultSet.getString("dish_name"),
                    DishTypeEnum.valueOf(resultSet.getString("dish_type").toUpperCase()),
                    resultSet.getDouble("dish_price"),
                    listOfIngredients
            );
            while (resultSet.next()) {
                listOfIngredients.add(new Ingredients(
                    resultSet.getInt("ingredient_id"), 
                    resultSet.getString("ingredient_name"), 
                    resultSet.getDouble("ingredient_price"), 
                    CategoryEnum.valueOf(resultSet.getString("ingredient_category")),
                    dish
                ));
     
            }
            dish.setIngredients(listOfIngredients);
            listOfDish.add(dish);
        }
        preparedStatement.close();
        resultSet.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
        return listOfDish;
    }

    public List<Ingredients> findIngredientByCriteria(String ingredientName, CategoryEnum category, String dishName, int page, int size){
        List<Ingredients> listOfIndrIngredients = new ArrayList<>();
        DBConnection dbConnection =  new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        try{
            String str = "SELECT i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
                "i.category AS ingredient_category, "+
                "d.id AS dish_id, d.name AS dish_name, dish_type ,d.\"dishPrice\" AS dish_price, "+
                "FROM \"Ingredient\" AS i JOIN \"Dish\" AS d ON i.id_dish=d.id ";

            String toAdd = "";
            if(ingredientName!=null){
                toAdd += "WHERE i.name ILIKE ?";
                if (category!=null) {
                    toAdd += "AND i.category = ?";
                }

                if (dishName!=null) {
                    toAdd += "AND d.name = ?";
                }

            } else if (category!=null) {
                toAdd+= "WHERE i.category = ?";
                if (dishName!=null) {
                    toAdd += "AND d.name ILIKE ?";
                }
            } else if (dishName!=null) {
                toAdd += "WHERE d.name ILIKE ?";
            }

            toAdd+= "LIMITE ? OFFSET ?";
            str+=toAdd;

            PreparedStatement preparedStatement = connection.prepareStatement(str);
            if(ingredientName!=null){
                preparedStatement.setString(1,ingredientName);
                if (category==null && dishName==null) {
                    preparedStatement.setInt(2, size);
                    preparedStatement.setInt(3, (page-1)*size);
                }
                if (category!=null) {
                    preparedStatement.setString(2,category.toString());
                }
                if (dishName == null) {
                    preparedStatement.setInt(3, size);
                    preparedStatement.setInt(4, (page-1)*size); 
                }

                if (dishName!=null && category!=null) {
                    preparedStatement.setString(3,dishName);
                    preparedStatement.setInt(4, size);
                    preparedStatement.setInt(5, (page-1)*size);
                } else if (category==null) {
                    preparedStatement.setString(2,dishName);
                    preparedStatement.setInt(3, size);
                    preparedStatement.setInt(4, (page-1)*size);
                }
            } else if (category!=null) {
                preparedStatement.setString(1,category.toString());
                if (dishName!=null) {
                    preparedStatement.setString(2,dishName);
                    preparedStatement.setInt(3, size);
                    preparedStatement.setInt(4, (page-1)*size);
                }
            } else if (dishName!=null) {
                preparedStatement.setString(1,dishName);
                preparedStatement.setInt(2, size);
                preparedStatement.setInt(3, (page-1)*size);
            } else {
                preparedStatement.setInt(1, size);
                preparedStatement.setInt(2, (page-1)*size);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listOfIndrIngredients.add(new Ingredients(
                    resultSet.getInt("ingredient_id"),
                    resultSet.getString("ingredient_name"),
                    resultSet.getDouble("ingredient_price"),
                    CategoryEnum.valueOf(resultSet.getString("ingredientt_category")),
                    new Dish(
                        resultSet.getInt("dish_id"),
                        resultSet.getString("dish_name"),
                        DishTypeEnum.valueOf(resultSet.getString("dish_type")),
                        resultSet.getDouble("dish_price"),
                        listOfIndrIngredients
                    ))
                );
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return listOfIndrIngredients;
    }
}

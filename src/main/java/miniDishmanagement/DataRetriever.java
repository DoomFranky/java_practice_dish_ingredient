package miniDishmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataRetriever {
    public Dish findDishById (Integer id){
        DBConnection DbConnection = new DBConnection();
        Connection connection = DbConnection.getBDConnection();
        Dish dish = null;
        try{
            ResultSet resultSetDish = SQLgetDishById(id,connection);
            ResultSet resultSetDishIngredient = SQLgetIngredientById(id,connection);
            List<DishIngredient> listOfDishIngredient = new ArrayList<>();
            while(resultSetDishIngredient.next()){
                if (dish == null && resultSetDish.next()) {
                    dish = new Dish(
                        resultSetDish.getInt("dish_id"),
                        resultSetDish.getString("dish_name"),
                        DishTypeEnum.valueOf(resultSetDish.getString("dish_type").toUpperCase()),
                        resultSetDish.getObject("dish_price") == null ? null : resultSetDish.getDouble("dish_price"),
                        listOfDishIngredient
                    );
                }
                listOfDishIngredient.add(new DishIngredient(
                    resultSetDishIngredient.getInt("dish_ingredient_id"),
                    dish,
                    new Ingredients(
                        resultSetDishIngredient.getInt("ingredient_id"), 
                        resultSetDishIngredient.getString("ingredient_name"), 
                        resultSetDishIngredient.getDouble("ingredient_price"), 
                        CategoryEnum.valueOf(resultSetDishIngredient.getString("ingredient_category")),
                        dish
                    ),
                    resultSetDishIngredient.getObject("quantity_required") == null ? null : resultSetDishIngredient.getDouble("quantity_required"),
                    Unit_type.valueOf(resultSetDishIngredient.getString("unit"))
                ));
            }
            if (dish != null) {
                dish.setDishIngredients(listOfDishIngredient);
            }
            resultSetDish.close();
            resultSetDishIngredient.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return dish;
    }

    public List<DishIngredient> findDishIngredientByDishId (Integer id){

        throw new RuntimeException("methode non implementer");
    }

    public List<Ingredients> findIngredients(int page, int size){
        List<DishIngredient> listOfDishIngredient = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT d.id AS dish_id, d.name AS dish_name ,dish_type, d.\"DishPrice\" AS dish_price, "+
                "i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
                "i.category AS ingredient_category,  FROM \"DishIngredient\" AS di "+
                "JOIN \"Ingredient\" AS i ON i.id = di.id_ingredient "+
                "JOIN \"Dish\" AS d ON d.id = di.id_dish "+
                "LIMIT ? OFFSET ?"
            );
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, (page-1)*size);
            ResultSet resultSet = preparedStatement.executeQuery();
            Dish dish= null;
            while(resultSet.next()){
                if (dish == null) {
                    dish = new Dish(
                        resultSet.getInt("dish_id"),
                        resultSet.getString("dish_name"),
                        DishTypeEnum.valueOf(resultSet.getString("dish_type").toUpperCase()),
                        resultSet.getObject("dish_price") == null ? null : resultSet.getDouble("dish_price"),
                        listOfDishIngredient
                    );
                }
                listOfDishIngredient.add(new DishIngredient(
                    resultSet.getInt("dish_ingredient_id"),
                    dish,
                    new Ingredients(
                        resultSet.getInt("ingredient_id"), 
                        resultSet.getString("ingredient_name"), 
                        resultSet.getDouble("ingredient_price"), 
                        CategoryEnum.valueOf(resultSet.getString("ingredient_category")),
                        dish
                    ),
                    resultSet.getObject("quatity_require") == null ? null : resultSet.getDouble("quantity_require"),
                    Unit_type.valueOf(resultSet.getString("unit"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
        return listOfDishIngredient.stream().map(i->i.getIngredeint()).collect(Collectors.toList());
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
                "UPDATE \"DishIngredient\" SET id_dish = ? "+
                "WHERE id_ingredient = ANY(?) ";
            String deleteIngredientString = 
                "DELETE FROM \"DishIngredient\" WHERE id_ingredient = ANY(?)";

            List<Integer> ingredientIdtoSave = dishToSave.getIngredients().stream().map(i->i.getId()).collect(Collectors.toList());
            List<Integer> lastIngredientId = findIngredients(1, 12).stream().map(i->i.getId()).collect(Collectors.toList());
            List<Integer> ingredientIdtoDelete = new ArrayList<>();

            for(Integer i : ingredientIdtoSave){
                if (!lastIngredientId.contains(i)) {
                    ingredientIdtoDelete.add(i);
                }
            }
            PreparedStatement ingredientUpdateStatement =  connection.prepareStatement(updateIngredientString);
            PreparedStatement ingredientDeleteStatement = connection.prepareStatement(deleteIngredientString);
            ingredientUpdateStatement.setInt(1,idDish);
            ingredientUpdateStatement.setArray(2,connection.createArrayOf("INTEGER", ingredientIdtoSave.stream().toArray(Integer[]::new)));

            ingredientDeleteStatement.setArray(1, connection.createArrayOf("INTEGER", ingredientIdtoDelete.stream().toArray(Integer[]::new)));
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
            String str = "SELECT d.id AS dish_id, d.name AS dish_name ,dish_type, d.\"DishPrice\" AS dish_price, "+
                "i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
                "i.category AS ingredient_category FROM \"DishIngredient\" AS di "+
                "JOIN \"Ingredient\" AS i ON i.id = di.id_ingredient "+
                "JOIN \"Dish\" AS d ON d.id = di.id_dish "+
                "WHERE d.id IN (" +
                "SELECT ing.id_dish FROM \"Ingredient\" AS ing WHERE ing.name ILIKE ?" +
                ") ORDER BY d.id DESC";

            PreparedStatement preparedStatement = connection.prepareStatement(str);
            preparedStatement.setString(1, "%"+IngredientName+"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<DishIngredient> listOfDishIngredient = new ArrayList<>();
            Dish dish = null;
            System.out.println(resultSet.last());
            while(resultSet.next()){
                if (listOfDish==null) {
                    dish = new Dish(
                        resultSet.getInt("dish_id"),
                        resultSet.getString("dish_name"),
                        DishTypeEnum.valueOf(resultSet.getString("dish_type").toUpperCase()),
                        resultSet.getObject("dish_price") == null ? null : resultSet.getDouble("dish_price"),
                        listOfDishIngredient
                    );
                }else if(listOfDish.getLast().getId() != resultSet.getInt("dish_id")) {
                    dish.setDishIngredients(listOfDishIngredient);
                    listOfDish.add(dish);
                } 
                if (resultSet.next()) {
                    dish.setDishIngredients(listOfDishIngredient);
                    listOfDish.add(dish);
                }
                listOfDishIngredient.add(new DishIngredient(
                    resultSet.getInt("dish_ingredient_id"),
                    dish,
                    new Ingredients(
                        resultSet.getInt("ingredient_id"), 
                        resultSet.getString("ingredient_name"), 
                        resultSet.getDouble("ingredient_price"), 
                        CategoryEnum.valueOf(resultSet.getString("ingredient_category")),
                        dish
                    ),
                    resultSet.getObject("quatity_require") == null ? null : resultSet.getDouble("quantity_require"),
                    Unit_type.valueOf(resultSet.getString("unit"))
                ));
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
        List<DishIngredient> listOfDishIngredients = new ArrayList<>();
        DBConnection dbConnection =  new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        try{
            String str = "SELECT d.id AS dish_id, d.name AS dish_name ,dish_type, d.\"DishPrice\" AS dish_price, "+
                "i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
                "i.category AS ingredient_category FROM \"DishIngredient\" AS di "+
                "JOIN \"Ingredient\" AS i ON i.id = di.id_ingredient "+
                "JOIN \"Dish\" AS d ON d.id = di.id_dish ";

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
            Dish dish = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (dish == null) {
                    dish = new Dish(
                        resultSet.getInt("dish_id"),
                        resultSet.getString("dish_name"),
                        DishTypeEnum.valueOf(resultSet.getString("dish_type").toUpperCase()),
                        resultSet.getObject("dish_price") == null ? null : resultSet.getDouble("dish_price"),
                        listOfDishIngredients
                    );
                }
                listOfDishIngredients.add(new DishIngredient(
                    resultSet.getInt("dish_ingredient_id"),
                    dish,
                    new Ingredients(
                        resultSet.getInt("ingredient_id"), 
                        resultSet.getString("ingredient_name"), 
                        resultSet.getDouble("ingredient_price"), 
                        CategoryEnum.valueOf(resultSet.getString("ingredient_category")),
                        dish
                    ),
                    resultSet.getObject("quatity_require") == null ? null : resultSet.getDouble("quantity_require"),
                    Unit_type.valueOf(resultSet.getString("unit"))
                )
                );
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return listOfDishIngredients.stream().map(i->i.getIngredeint()).collect(Collectors.toList());
    }

    private ResultSet SQLgetDishById (Integer id,Connection connection){
        ResultSet resultSet = null;
        String str = 
            "SELECT d.id AS dish_id ,d.name AS dish_name, dish_type, d.\"DishPrice\" AS dish_price FROM \"Dish\" AS d WHERE d.id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(str);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    private ResultSet SQLgetIngredientById (Integer id,Connection connection){
        ResultSet resultSet = null;

        String str = 
            "SELECT i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
            "i.category AS ingredient_category, quantity_required, unit, di.id AS dish_ingredient_id FROM \"DishIngredient\" AS di "+
            "JOIN \"Ingredient\" AS i ON i.id = di.id_ingredient WHERE di.id_dish = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(str);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return resultSet;
    }
}

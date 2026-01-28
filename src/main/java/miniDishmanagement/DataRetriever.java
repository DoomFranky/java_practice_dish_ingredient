package miniDishmanagement;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataRetriever {
    public Dish findDishById (Integer id){
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        try{
            String str = 
            "SELECT d.id AS dish_id ,d.name AS dish_name, dish_type, d.\"DishPrice\" AS dish_price FROM \"Dish\" AS d WHERE d.id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(str);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Dish dish = new Dish();
                dish.setId(resultSet.getInt("dish_id"));
                dish.setName(resultSet.getString("dish_name"));
                dish.setDishType(DishTypeEnum.valueOf(resultSet.getString("dish_type").toUpperCase()));
                dish.setDishPrice(resultSet.getObject("dish_price") == null ? null : resultSet.getDouble("dish_price"));
                dish.setDishIngredients(findDishIngredientByDishId(id));
                resultSet.close();
                return dish;
            }
            resultSet.close();
            throw new RuntimeException("Dish not found " + id);
        }catch(SQLException e){
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
    }

    public Ingredients findIngredientById (Integer id) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        Ingredients ingredient = new Ingredients();
        try{
            String str = 
            "SELECT i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
            "i.category AS ingredient_category FROM \"Ingredient\" AS i "+
            "WHERE i.id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(str);
            preparedStatement.setInt(1, id);
            ResultSet resultSetDishIngredient = preparedStatement.executeQuery();
            if(resultSetDishIngredient.next()){
                ingredient.setId(resultSetDishIngredient.getInt("ingredient_id"));
                ingredient.setName(resultSetDishIngredient.getString("ingredient_name"));
                ingredient.setPrice(resultSetDishIngredient.getDouble("ingredient_price"));
                CategoryEnum.valueOf(resultSetDishIngredient.getString("ingredient_category"));
            }
            return ingredient;
        }catch(SQLException e){
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
    }

    private List<DishIngredient> findDishIngredientByDishId (Integer id){
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        try{
            String str = 
            "SELECT i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
            "i.category AS ingredient_category, quantity_required, unit, di.id AS dish_ingredient_id FROM \"Ingredient\" AS i "+
            "JOIN \"DishIngredient\" AS di ON i.id = di.id_ingredient WHERE di.id_dish = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(str);
            preparedStatement.setInt(1, id);
            ResultSet resultSetDishIngredient = preparedStatement.executeQuery();
            List<DishIngredient> listOfDishIngredient = new ArrayList<>();
            while(resultSetDishIngredient.next()){
                Ingredients ingredient = new Ingredients();
                ingredient.setId(resultSetDishIngredient.getInt("ingredient_id"));
                ingredient.setName(resultSetDishIngredient.getString("ingredient_name"));
                ingredient.setPrice(resultSetDishIngredient.getDouble("ingredient_price"));
                CategoryEnum.valueOf(resultSetDishIngredient.getString("ingredient_category"));
                ingredient.setStockMouvementList(findStockMouvementByIngredientId(resultSetDishIngredient.getInt("ingredient_id")));

                DishIngredient dishIngredient = new DishIngredient();
                dishIngredient.setId(resultSetDishIngredient.getInt("dish_ingredient_id"));
                dishIngredient.setIngredeint(ingredient);
                dishIngredient.setQuantity_require(resultSetDishIngredient.getObject("quantity_required") == null ? null : resultSetDishIngredient.getDouble("quantity_required"));
                dishIngredient.setUnit_type(Unit_type.valueOf(resultSetDishIngredient.getString("unit")));
                listOfDishIngredient.add(dishIngredient);
            }
            return listOfDishIngredient;
        }catch(SQLException e){
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
    }

    public List<Ingredients> findIngredients(int page, int size){
        List<DishIngredient> listOfDishIngredient = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, "+
                "i.category AS ingredient_category FROM \"Ingredient\" AS i LIMIT ? OFFSET ?"
            );
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, (page-1)*size);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                new Ingredients(
                    resultSet.getInt("ingredient_id"), 
                    resultSet.getString("ingredient_name"), 
                    resultSet.getDouble("ingredient_price"), 
                    CategoryEnum.valueOf(resultSet.getString("ingredient_category")),
                    findStockMouvementByIngredientId(resultSet.getInt("ingredient_id"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
        return listOfDishIngredient.stream().map(i->i.getIngredeint()).collect(Collectors.toList());
    }

    private List<StockMouvement> findStockMouvementByIngredientId(Integer id) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        List<StockMouvement> stockMouvements = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT sm.id stock_id, quantity, type, unit, creation_datetime FROM \"StockMouvement\" AS sm WHERE id_ingredient = ?"
            );
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                StockMouvement stockMouvement = new StockMouvement();
                stockMouvement.setId(resultSet.getInt("stock_id"));
                stockMouvement.setValue(new StockValue(resultSet.getDouble("quantity"), Unit_type.valueOf(resultSet.getString("unit"))));
                stockMouvement.setType(MovementTypeEnum.valueOf(resultSet.getString("type")));
                stockMouvement.setCreationDateTime(Instant.parse(resultSet.getString("creation_datetime")));
                stockMouvements.add(stockMouvement);
            }
            connection.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return stockMouvements;
    }

    public List<Ingredients> createIngredients(List<Ingredients> newIngredients){
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        List<Ingredients> savedIngredients = new ArrayList<>();
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
                savedIngredients.add(ingredient);
            }
            connection.commit();
            connection.close();
            udtapeSerial.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
        return savedIngredients;
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
            
            String str = "INSERT INTO \"Dish\" ( name, \"dish_type\", \"DishPrice\" ) "+
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

            updateTheSaveRelationInSaveDish(connection,idDish,dishToSave);
            deteleNotValideRelationInSaveDish(connection,idDish,dishToSave.getDishIngredients().stream().map(i->i.getIngredeint()).collect(Collectors.toList()));
            dishUpdateStatement.executeUpdate();

            connection.commit();

            udtapeSerialStatement.close();
            serialResultSet.close();
            dishUpdateStatement.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeTheConnection(connection);
        }
        return dishToSave;
    }

    private void deteleNotValideRelationInSaveDish(Connection connection,Integer idDish,List<Ingredients> ingredientToSave) {
        try{
            String deleteIngredientString = 
                "DELETE FROM \"DishIngredient\" WHERE id_dish = ? AND id_ingredient NOT IN (%s)";

                String inClause = ingredientToSave.stream()
                .map(i -> "?")
                .collect(Collectors.joining(","));

            deleteIngredientString = String.format(deleteIngredientString, inClause);
            PreparedStatement ingredientDeleteStatement = connection.prepareStatement(deleteIngredientString);
            ingredientDeleteStatement.setInt(1, idDish);
            int index = 2;
            for (Ingredients ingredient : ingredientToSave) {
                ingredientDeleteStatement.setInt(index++, ingredient.getId());
            }
            ingredientDeleteStatement.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void updateTheSaveRelationInSaveDish(Connection connection, Integer idDish, Dish dishToSave) {
        try{
            PreparedStatement udtapeSerialStatement = connection.prepareStatement(
                "SELECT setval(pg_get_serial_sequence('\"DishIngredient\"', 'id'), "+
                "(SELECT MAX(id) FROM \"DishIngredient\"))"
            );
            ResultSet serialResultSet = udtapeSerialStatement.executeQuery();
            Integer id = null;
            if (serialResultSet.next()) {
                id = serialResultSet.getInt("setval");
            }
            String updateIngredientString =
                "WITH upsert AS ("+
                    "UPDATE \"DishIngredient\" SET id_dish = ? "+
                    "WHERE id_ingredient = ? "+
                    "RETURNING *) "+
                "INSERT INTO \"DishIngredient\" (id,id_dish,id_ingredient,quantity_required,unit) "+
                "SELECT ?,?,?,?,? WHERE NOT EXISTS (SELECT 1 FROM upsert)";

            PreparedStatement ingredientUpdateStatement =  connection.prepareStatement(updateIngredientString);
            List<DishIngredient> dishIngredientsToSave = dishToSave.getDishIngredients();

            for(DishIngredient di : dishIngredientsToSave){
                ingredientUpdateStatement.setInt(1,idDish);
                ingredientUpdateStatement.setInt(2, di.getIngredeint().getId());
                ingredientUpdateStatement.setInt(3, id+1);
                ingredientUpdateStatement.setInt(4,idDish);
                ingredientUpdateStatement.setInt(5, di.getIngredeint().getId());
                if (di.getQuantity_require() == null || di.getUnit_type() == null) {
                    ingredientUpdateStatement.setNull(6, Types.DOUBLE);
                    ingredientUpdateStatement.setNull(7, Types.OTHER);
                }else{
                    ingredientUpdateStatement.setDouble(6, di.getQuantity_require());
                    ingredientUpdateStatement.setString(7, di.getUnit_type().toString());
                }
                ingredientUpdateStatement.addBatch();
            }
            ingredientUpdateStatement.executeBatch();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Ingredients saveIngredients (Ingredients ingredientsToSave) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getBDConnection();
        try{
            connection.setAutoCommit(false);
            
            PreparedStatement udtapeSerialStatement = connection.prepareStatement(
                "SELECT setval(pg_get_serial_sequence('\"Ingredient\"', 'id'), "+
                "(SELECT MAX(id) FROM \"Ingredient\"))"
            );
            
            ResultSet serialResultSet = udtapeSerialStatement.executeQuery();

            Integer idIngredient = null;
            if (ingredientsToSave.getId()!=null) {
                idIngredient = ingredientsToSave.getId();
            } else if (serialResultSet.next()) {
                idIngredient = serialResultSet.getInt("setval");
            }
            
            String str = "INSERT INTO \"Dish\" ( name, price , category ) "+
                "VALUES ( ? , ? , ?::\"Category_of_ingredient\" ) ON CONFLICT (name) DO UPDATE SET name = ?, price = ? , category = ?::\"Category_of_ingredient\" ";

            PreparedStatement ingredientUpdateStatement = connection.prepareStatement(str);

            ingredientUpdateStatement.setString(1, ingredientsToSave.getName());
            if(ingredientsToSave.getPrice()==null){
                ingredientUpdateStatement.setNull(2, java.sql.Types.NUMERIC);
            }else{
                ingredientUpdateStatement.setDouble(2, ingredientsToSave.getPrice());
            }
            ingredientUpdateStatement.setString(3, ingredientsToSave.getCategory().toString());
            ingredientUpdateStatement.setString(4, ingredientsToSave.getName());
            if(ingredientsToSave.getPrice()==null){
                ingredientUpdateStatement.setNull(5, java.sql.Types.NUMERIC);
            }else{
                ingredientUpdateStatement.setDouble(5, ingredientsToSave.getPrice());
            }
            ingredientUpdateStatement.setString(6, ingredientsToSave.getCategory().toString());
            ingredientUpdateStatement.executeUpdate();

            updateStockMouvementInIngredient(connection,idIngredient,ingredientsToSave);
            deteleStockMouvementInIngredient(connection,idIngredient,ingredientsToSave.getStockMouvementList());

            connection.commit();
            ingredientUpdateStatement.close();
            udtapeSerialStatement.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            dbConnection.closeTheConnection(connection);
        }
        return ingredientsToSave;
    }

    private void deteleStockMouvementInIngredient(Connection connection, Integer idIngredient,
            List<StockMouvement> stockMouvementList) {
        try{
            String deleteStockMouvementString = 
                "DELETE FROM \"StockMouvement\" WHERE id_ingredient = ? AND id NOT IN (%s)";

                String inClause = stockMouvementList.stream()
                .map(i -> "?")
                .collect(Collectors.joining(","));

            deleteStockMouvementString = String.format(deleteStockMouvementString, inClause);
            PreparedStatement stockMouvementDeleteStatement = connection.prepareStatement(deleteStockMouvementString);
            stockMouvementDeleteStatement.setInt(1, idIngredient);
            int index = 2;
            for (StockMouvement stockMouvement : stockMouvementList) {
                stockMouvementDeleteStatement.setInt(index++, stockMouvement.getId());
            }
            stockMouvementDeleteStatement.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        throw new UnsupportedOperationException("Unimplemented method 'deteleStockMouvementInIngredient'");
    }

    private void updateStockMouvementInIngredient(Connection connection, Integer idIngredient,
            Ingredients ingredientsToSave) {
                try{
            PreparedStatement udtapeSerialStatement = connection.prepareStatement(
                "SELECT setval(pg_get_serial_sequence('\"StockMouvement\"', 'id'), "+
                "(SELECT MAX(id) FROM \"StockMouvement\"))"
            );
            ResultSet serialResultSet = udtapeSerialStatement.executeQuery();
            Integer id = null;
            if (serialResultSet.next()) {
                id = serialResultSet.getInt("setval");
            }
            String updateStockMouvementString =
                "WITH upsert AS ("+
                    "UPDATE \"StockMouvement\" SET id_ingredient = ? "+
                    "WHERE id = ? "+
                    "RETURNING *) "+
                "INSERT INTO \"StockMouvement\" (id,id_ingredient,quantity,unit,type,creation_datetime) "+
                "SELECT ?,?,?,?,?,? WHERE NOT EXISTS (SELECT 1 FROM upsert)";

            PreparedStatement ingredientUpdateStatement =  connection.prepareStatement(updateStockMouvementString);
            List<StockMouvement> stockMouvementsToSave = ingredientsToSave.getStockMouvementList();

            for(StockMouvement sm : stockMouvementsToSave){
                ingredientUpdateStatement.setInt(1,idIngredient);
                ingredientUpdateStatement.setInt(2, sm.getId());
                ingredientUpdateStatement.setInt(3, id+1);
                ingredientUpdateStatement.setInt(4,idIngredient);
                if (sm.getValue().getQuantity() == null || sm.getValue().getUnit() == null) {
                    ingredientUpdateStatement.setNull(5, Types.DOUBLE);
                    ingredientUpdateStatement.setNull(6, Types.OTHER);
                }else{
                    ingredientUpdateStatement.setDouble(5, sm.getValue().getQuantity());
                    ingredientUpdateStatement.setString(6, sm.getValue().getUnit().toString());
                }
                ingredientUpdateStatement.setString(7, sm.getType().toString());
                ingredientUpdateStatement.setTimestamp(8, Timestamp.from(sm.getCreationDateTime()));
                ingredientUpdateStatement.addBatch();
            }
            ingredientUpdateStatement.executeBatch();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
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
                        findStockMouvementByIngredientId(resultSet.getInt("ingredient_id"))
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
                "i.id AS ingredient_id, i.name AS ingredient_name,  quantity_required, unit, i.price AS ingredient_price, "+
                "i.category AS ingredient_category, di.id AS dish_ingredient_id FROM \"DishIngredient\" AS di "+
                "JOIN \"Ingredient\" AS i ON i.id = di.id_ingredient "+
                "JOIN \"Dish\" AS d ON d.id = di.id_dish ";

            String toAdd = "";
            if(ingredientName!=null){
                toAdd += "WHERE i.name ILIKE ? ";
                if (category!=null) {
                    toAdd += "AND i.category = ?";
                }

                if (dishName!=null) {
                    toAdd += "AND d.name = ?";
                }

            } else if (category!=null) {
                toAdd+= "WHERE i.category = ? ";
                if (dishName!=null) {
                    toAdd += "AND d.name ILIKE ?";
                }
            } else if (dishName!=null) {
                toAdd += "WHERE d.name ILIKE ? ";
            }

            toAdd+= "LIMIT ? OFFSET ?";
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
                        findStockMouvementByIngredientId(resultSet.getInt("ingredient_id"))
                    ),
                    resultSet.getObject("quantity_required") == null ? null : resultSet.getDouble("quantity_required"),
                    Unit_type.valueOf(resultSet.getString("unit"))
                )
                );
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return listOfDishIngredients.stream().map(i->i.getIngredeint()).collect(Collectors.toList());
    }
}
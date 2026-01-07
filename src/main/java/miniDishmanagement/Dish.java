package miniDishmanagement;

import java.util.List;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private Double dishPrice; 
    private List<Ingredients> ingredients;


    public Dish(Integer id, String name, DishTypeEnum dishType) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
    }

    public Dish(Integer id, String name, DishTypeEnum dishType, Double dishPrice,List<Ingredients> ingredients) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.dishPrice = dishPrice;
        this.ingredients = ingredients;
    }

    public Integer getId() {
        return id;
    }



    public void setId(Integer id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public DishTypeEnum getDishType() {
        return dishType;
    }



    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }



    public List<Ingredients> getIngredients() {
        return ingredients;
    }



    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }
    
    public Double getDishCost(){
        return ingredients
            .stream()
            .mapToDouble(ingredients -> ingredients.getPrice())
            .sum();
    }



    @Override
    public String toString() {
        return "Dish [id=" + id + ", name=" + name + ", dishType=" + dishType + " ,dishPrice="+dishPrice+", ingredients=" + ingredients + "]";
    }

    public Double getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(Double dishPrice) {
        this.dishPrice = dishPrice;
    }

    public Double getGrossMargin (){
        return null
    }
}

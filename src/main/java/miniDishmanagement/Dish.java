package miniDishmanagement;

import java.util.List;
import java.util.stream.Collectors;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private Double dishPrice; 
    private List<DishIngredient> ingredients;



    public Dish(Integer id, String name, DishTypeEnum dishType, Double dishPrice,List<DishIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.dishPrice = dishPrice;
        this.ingredients = ingredients;
    }

    public Dish(String name, DishTypeEnum dishType, Double dishPrice,List<DishIngredient> ingredients) {
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
        return ingredients.stream().map(i->i.getIngredeint()).collect(Collectors.toList());
    }



    public void setDishIngredients(List<DishIngredient> ingredient) {
        this.ingredients = ingredient;
    }
    
    public Double getDishCost(){
        return ingredients
            .stream()
            .mapToDouble(ingredients -> ingredients.getIngredeint().getPrice())
            .sum();
    }



    @Override
    public String toString() {
        if (id==null) {
            return "Dish [name=" + name + ", dishType=" + dishType + " ,dishPrice="+dishPrice+", ingredients=" + ingredients + "]";
        }
        return "Dish [id=" + id + ", name=" + name + ", dishType=" + dishType + " ,dishPrice="+dishPrice+", ingredients=" + ingredients + "]";
    }
        

    public Double getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(Double dishPrice) {
        this.dishPrice = dishPrice;
    }

    public Double getGrossMargin (){
        if(this.getDishPrice()==null){
            throw new RuntimeException("Dish don't have a price");
        }
        return this.getDishPrice()-this.getDishCost();
    }
}

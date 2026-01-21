package miniDishmanagement;

public class DishIngredient {
    private Integer id;
    private Dish dish;
    private Ingredients ingredeint;
    private Double quantity_require;
    private Unit_type unit_type;
    
    public DishIngredient(Integer id, Dish dish, Ingredients ingredeint, Double quantity_require, Unit_type unit_type) {
        this.id = id;
        this.dish = dish;
        this.ingredeint = ingredeint;
        this.quantity_require = quantity_require;
        this.unit_type = unit_type;
    }

    
    public Integer getId() {
        return id;
    }
    public Double getQuantity_require() {
        return quantity_require;
    }
    public Unit_type getUnit_type() {
        return unit_type;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setQuantity_require(Double quantity_require) {
        this.quantity_require = quantity_require;
    }
    public void setUnit_type(Unit_type unit_type) {
        this.unit_type = unit_type;
    }


    public Dish getDish() {
        return dish;
    }


    public Ingredients getIngredeint() {
        return ingredeint;
    }


    public void setDish(Dish dish) {
        this.dish = dish;
    }


    public void setIngredeint(Ingredients ingredeint) {
        this.ingredeint = ingredeint;
    }

    
}

package miniDishmanagement;

public class DishOrder {
    private Integer id;
    private Dish dish;
    private Integer quantity;
    public DishOrder(Integer id, Dish dish, Integer quantity) {
        this.id = id;
        this.dish = dish;
        this.quantity = quantity;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Dish getDish() {
        return dish;
    }
    public void setDish(Dish dish) {
        this.dish = dish;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString() {
        return "DishOrder [id=" + id + ", dish=" + dish.getName() + ", quantity=" + quantity + "]";
    } 

    
}

package miniDishmanagement;

import java.time.Instant;

public class StockMouvement {
    private Integer id;
    private Ingredients ingredients;
    private StockValue value;
    private MovementTypeEnum type;
    private Instant creationDateTime;

    public Ingredients getIngredients() {
        return ingredients;
    }
    public void setIngredients(Ingredients ingredients) {
        this.ingredients = ingredients;
    }
    
    public StockMouvement(Integer id, Ingredients ingredients, StockValue value, MovementTypeEnum type,
            Instant creationDateTime) {
        this.id = id;
        this.ingredients = ingredients;
        this.value = value;
        this.type = type;
        this.creationDateTime = creationDateTime;
    }
    public StockMouvement() {
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public StockValue getValue() {
        return value;
    }
    public void setValue(StockValue value) {
        this.value = value;
    }
    public MovementTypeEnum getType() {
        return type;
    }
    public void setType(MovementTypeEnum type) {
        this.type = type;
    }
    public Instant getCreationDateTime() {
        return creationDateTime;
    }
    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
    @Override
    public String toString() {
        return "StockMouvement [id=" + id +", ingredient=" +ingredients.getName()+", value=" + value + ", type=" + type + ", creationDateTime="
                + creationDateTime + "]";
    }

    
    
}

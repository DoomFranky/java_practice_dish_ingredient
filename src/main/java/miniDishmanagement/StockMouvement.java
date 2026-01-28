package miniDishmanagement;

import java.time.Instant;

public class StockMouvement {
    private Integer id;
    private StockValue value;
    private MovementTypeEnum type;
    private Instant creationDateTime;

    
    public StockMouvement() {
    }
    public StockMouvement(Integer id, StockValue value, MovementTypeEnum type, Instant creationDateTime) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.creationDateTime = creationDateTime;
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
        return "StockMouvement [id=" + id + ", value=" + value + ", type=" + type + ", creationDateTime="
                + creationDateTime + "]";
    }

    
    
}

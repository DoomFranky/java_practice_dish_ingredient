package miniDishmanagement;

import java.time.Instant;
import java.util.List;

public class Order {
    private Integer id;
    private String reference;
    private Instant creationDateTime;
    private List<DishOrder> dishOrder;
    private TableOrder table;
    
    public Order() {
    }
    
    
    public Order(Integer id, String reference, Instant creationDateTime, List<DishOrder> dishOrder, TableOrder table) {
        this.id = id;
        this.reference = reference;
        this.creationDateTime = creationDateTime;
        this.dishOrder = dishOrder;
        this.table = table;
    }


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public Instant getCreationDateTime() {
        return creationDateTime;
    }
    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
    public List<DishOrder> getDishOrder() {
        return dishOrder;
    }
    public void setDishOrder(List<DishOrder> dishOrder) {
        this.dishOrder = dishOrder;
    }
    @Override
    public String toString() {
        return "Order [id=" + id + ", reference=" + reference + ", creationDateTime=" + creationDateTime
                + ", DishOrder=" + dishOrder + "]";
    }


    public TableOrder getTable() {
        return table;
    }


    public void setTable(TableOrder table) {
        this.table = table;
    }
    
    public Double getTotalAmountWithoutVAT(){
        return  dishOrder.stream().mapToDouble(om->om.getDish().getGrossMargin()).sum();
    }

    public Double getTotalAmountWithVAT(){
        return dishOrder.stream().mapToDouble(om->om.getDish().getGrossMargin()).sum();
    }
    
}

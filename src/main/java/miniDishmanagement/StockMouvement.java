package miniDishmanagement;

public class StockMouvement {
    private Integer id;
    private Double quantityInStock;
    private Unit_type unit;

    public StockMouvement(Integer id, Double quantityInStock, Unit_type unit) {
        this.id = id;
        this.quantityInStock = quantityInStock;
        this.unit = unit;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Double getQuantityInStock() {
        return quantityInStock;
    }
    public void setQuantityInStock(Double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
    public Unit_type getUnit() {
        return unit;
    }
    public void setUnit(Unit_type unit) {
        this.unit = unit;
    }

    
}

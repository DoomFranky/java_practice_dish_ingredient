package miniDishmanagement;

public class StockValue {
    private Double quantity;
    private Unit_type unit;
    
    public StockValue(Double quantity, Unit_type unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Unit_type getUnit() {
        return unit;
    }

    public void setUnit(Unit_type unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "StockValue [quantity=" + quantity + ", unit=" + unit + "]";
    }

    
}

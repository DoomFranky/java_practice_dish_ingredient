package miniDishmanagement;

import java.time.Instant;

public class TableOrder {
    private RestaurantTable table;
    private Instant arrivalDateTime;
    private Instant departureDateTime;

    
    @Override
    public String toString() {
        return "TableOrder [table=" + table + ", arrivalDateTime=" + arrivalDateTime + ", departureDateTime="
                + departureDateTime + "]";
    }
    public TableOrder() {
    }
    public TableOrder(RestaurantTable table, Instant arrivalDateTime, Instant departureDateTime) {
        this.table = table;
        this.arrivalDateTime = arrivalDateTime;
        this.departureDateTime = departureDateTime;
    }
    public RestaurantTable getTable() {
        return table;
    }
    public void setTable(RestaurantTable table) {
        this.table = table;
    }
    public Instant getArrivalDateTime() {
        return arrivalDateTime;
    }
    public void setArrivalDateTime(Instant arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }
    public Instant getDepartureDateTime() {
        return departureDateTime;
    }
    public void setDepartureDateTime(Instant departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    
}

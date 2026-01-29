package miniDishmanagement;

import java.time.Instant;
import java.util.List;

public class RestaurantTable {
    private Integer id;
    private Integer table_numero;
    private List<Order> orderList;
    public RestaurantTable(Integer id, Integer table_numero, List<Order> orderList) {
        this.id = id;
        this.table_numero = table_numero;
        this.orderList = orderList;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getTable_numero() {
        return table_numero;
    }
    public void setTable_numero(Integer table_numero) {
        this.table_numero = table_numero;
    }
    public List<Order> getOrderList() {
        return orderList;
    }
    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public Boolean isAvalaibleAt(Instant t){
        return orderList.stream().anyMatch(o->t.isAfter(o.getTable().getArrivalDateTime()) && t.isBefore(o.getTable().getDepartureDateTime()));
    }
    @Override
    public String toString() {
        return "RestaurantTable [id=" + id + ", table_numero=" + table_numero + ", orderList=" + orderList + "]";
    }
    
    
}

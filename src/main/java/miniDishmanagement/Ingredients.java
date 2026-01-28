package miniDishmanagement;

import java.util.List;

public class Ingredients {
    private Integer id;
    private String name;
    private Double price;
    private CategoryEnum category;
    private List<StockMouvement> stockMouvementList;
    
    public Ingredients() {
    }
    public Ingredients(Integer id, String name, Double price, CategoryEnum category,
            List<StockMouvement> stockMouvementList) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stockMouvementList = stockMouvementList;
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
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public CategoryEnum getCategory() {
        return category;
    }
    public void setCategory(CategoryEnum category) {
        this.category = category;
    }
    public List<StockMouvement> getStockMouvementList() {
        return stockMouvementList;
    }
    public void setStockMouvementList(List<StockMouvement> stockMouvementList) {
        this.stockMouvementList = stockMouvementList;
    }
    @Override
    public String toString() {
        return "Ingredients [id=" + id + ", name=" + name + ", price=" + price + ", category=" + category
                + ", stockMouvementList=" + stockMouvementList + "]";
    }

    

    
}

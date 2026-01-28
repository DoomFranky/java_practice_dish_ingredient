package miniDishmanagement;

public class Ingredients {
    private Integer id;
    private String name;
    private Double price;
    private CategoryEnum category;
    private Dish dish;

    public Ingredients() {
    }

    public Ingredients(Integer id, String name, Double price, CategoryEnum category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Ingredients(String name, Double price, CategoryEnum category) {
        this.name = name;
        this.price = price;
        this.category = category;
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

    @Override
    public String toString() {
        if (id==null && dish == null) {
            return "Ingredients [name=" + name + ", price=" + price + ", category=" + category +"]";
        }
        return "Ingredients [id=" + id + ", name=" + name + ", price=" + price + ", category=" + category + "]";
    }

    
}

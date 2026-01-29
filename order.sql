CREATE TABLE "Order" (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(7),
    creation_datetime timestamp
)

CREATE TABLE "DishOrder" (
    id SERIAL PRIMARY KEY,
    id_order INT,
    CONSTRAINT fk_id_order FOREIGN KEY (id_order) REFERENCES "Order"(id),
    id_dish INT,
    CONSTRAINT fk_id_dish FOREIGN KEY (id_dish) REFERENCES "Dish"(id),
    quantity NUMERIC(10,2)
)


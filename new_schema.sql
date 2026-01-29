ALTER TABLE "Ingredient" DROP CONSTRAINT fk_id_dish, DROP COLUMN id_dish;

CREATE TYPE "unit_type" AS ENUM (
    'PCS',
    'KG',
    'L'
);

CREATE TABLE "DishIngredient" (
    id SERIAL PRIMARY KEY,
    id_dish INT,
    CONSTRAINT fk_id_dish FOREIGN KEY (id_dish) REFERENCES "Dish"(id),
    id_ingredient INT,
    CONSTRAINT fk_id_ingredient FOREIGN KEY (id_ingredient) REFERENCES "Ingredient"(id),
    quantity_required NUMERIC(10,2),
    unit "unit_type"
);

CREATE TYPE "mouvement_type" AS ENUM (
    'IN',
    'OUT'
);

CREATE TABLE "StockMouvement" (
    id SERIAL PRIMARY KEY,
    id_ingredient INT,
    quantity NUMERIC(10,2),
    "type" "mouvement_type",
    "unit" "unit_type",
    creation_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

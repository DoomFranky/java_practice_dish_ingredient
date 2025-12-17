CREATE TYPE "Type_of_dish" AS ENUM (
    'START',
    'MAIN',
    'DESSERT'
);

CREATE TABLE "Dish" (
    id SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    dish_type "Type_of_dish" NOT NULL
);

CREATE TYPE "Category_of_ingredient" AS ENUM (
    'VEGETABLE',
    'ANIMAL',
    'MARINE',
    'DAIRY',
    'OTHER'
);

CREATE TABLE "Ingredient"  (
    id SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,   
    price   NUMERIC(10,2) NOT NULL,
    category "Category_of_ingredient" NOT NULL,
    id_dish INT NOT NULL,
    CONSTRAINT fk_id_dish FOREIGN KEY (id_dish) REFERENCES "Dish"(id)

);

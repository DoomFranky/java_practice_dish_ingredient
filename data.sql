INSERT INTO "Dish" VALUES 
    (1,'Salade fraîche', 'START'),
    (2, 'Poulet grillé', 'MAIN'),
    (3, 'Riz aux légumes' ,'MAIN'),
    (4, 'Gâteau au chocolat' ,'DESSERT'),
    (5, 'Salade de fruits', 'DESSERT')
;

INSERT INTO "Ingredient" VALUES
    (1,'Laitue', 800.00, 'VEGETABLE', 1),
    (2, 'Tomato', 600.00, 'VEGETABLE', 1),
    (3, 'Poulet', 4500.00, 'ANIMAL', 2),
    (4, 'Chocolat', 3000.00, 'OTHER', 4),
    (5, 'Beurre', 2500.00, 'DAIRY', 4)
;

UPDATE "Dish" SET "DishPrice"= 2000.0 WHERE name='Salade fraîche';
UPDATE "Dish" SET "DishPrice"= 6000.0 WHERE name='Poulet grillé';
UPDATE "Dish" SET "DishPrice"= null WHERE name='Riz aux légume';
UPDATE "Dish" SET "DishPrice"= null WHERE name='Gâteau au chocolat';
UPDATE "Dish" SET "DishPrice"= null WHERE name='Salade de fruits';
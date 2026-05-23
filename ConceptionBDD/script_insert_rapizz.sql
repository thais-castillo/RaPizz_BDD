SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM contient;
DELETE FROM Livraison;
DELETE FROM Pizza;
DELETE FROM Ingredient;
DELETE FROM Vehicule;
DELETE FROM Client;
DELETE FROM Livreur;

ALTER TABLE Pizza AUTO_INCREMENT = 1;
ALTER TABLE Ingredient AUTO_INCREMENT = 1;
ALTER TABLE Client AUTO_INCREMENT = 1;
ALTER TABLE Livreur AUTO_INCREMENT = 1;
ALTER TABLE Vehicule AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO Ingredient (nom) VALUES
('Mozzarella'),
('Tomate'),
('Jambon'),
('Champignons'),
('Poivron'),
('Olives'),
('Pepperoni'),
('Ananas'),
('Fromage de chèvre'),
('Bacon');

INSERT INTO Pizza (nom, prix) VALUES
('Margherita', 8.50),
('Regina', 9.00),
('Pepperoni', 9.50),
('Vegetarienne', 10.00),
('Hawaiienne', 11.00),
('Quatre Fromages', 12.00),
('Calzone', 11.50),
('BBQ Chicken', 12.50),
('Chevre Miel', 13.00),
('Mexicaine', 12.00),
('Carnivore', 13.50),
('Vegan Delight', 11.00);

INSERT INTO contient (id_pizza, Id_Ingredient) VALUES

-- 1. Margherita : Mozzarella + Tomate
(1,1),(1,2),

-- 2. Regina : Mozzarella + Tomate + Jambon
(2,1),(2,2),(2,3),

-- 3. Pepperoni : Mozzarella + Tomate + Pepperoni
(3,1),(3,2),(3,7),

-- 4. Végétarienne : Mozzarella + Tomate + Champignons + Poivron + Olives
(4,1),(4,2),(4,4),(4,5),(4,6),

-- 5. Hawaiienne : Mozzarella + Tomate + Jambon + Ananas
(5,1),(5,2),(5,3),(5,8),

-- 6. Quatre Fromages : Mozzarella + Fromage de chèvre + Bacon
(6,1),(6,9),(6,10),

-- 7. Calzone : Mozzarella + Tomate + Jambon
(7,1),(7,2),(7,3),

-- 8. BBQ Chicken : Mozzarella + Jambon + Bacon
(8,1),(8,3),(8,10),

-- 9. Chevre Miel : Mozzarella + Fromage de chèvre + Ananas
(9,1),(9,9),(9,8),

-- 10. Mexicaine : Mozzarella + Poivron + Pepperoni
(10,1),(10,5),(10,7),

-- 11. Carnivore : Mozzarella + Jambon + Bacon
(11,1),(11,3),(11,10),

-- 12. Vegan Delight : Tomate + Champignons + Poivron + Olives
(12,2),(12,4),(12,5),(12,6);

INSERT INTO Livreur (nom, prenom) VALUES
('Dupont', 'Jean'),
('Martin', 'Sophie'),
('Durand', 'Pierre'),
('Bernard', 'Lucas'),
('Petit', 'Emma'),
('Robert', 'Hugo');

INSERT INTO Client (prenom, nom, solde, date_abonnement, bonification) VALUES
('Alice', 'Smith', 50.00, '2023-01-15', 2),
('Bob', 'Johnson', 30.00, '2023-02-20', 1),
('Charlie', 'Brown', 20.00, '2023-03-10', 0),
('Lucas', 'Martin', 40.00, '2023-04-05', 3),
('Léa', 'Durand', 25.00, '2023-05-12', 1),
('Camille', 'Moreau', 60.00, '2023-06-18', 4),
('Emma', 'Dubois', 35.00, '2023-07-22', 2),
('Nathan', 'Leroy', 45.00, '2023-08-30', 3),
('Chloé', 'Bernard', 55.00, '2023-09-15', 4),
('Maxime', 'Petit', 70.00, '2023-10-01', 5),
('Ines', 'Robert', 15.00, '2023-10-05', 0),
('Paul', 'Garcia', 80.00, '2023-10-10', 6);

INSERT INTO Vehicule (type, immatricule) VALUES
('Voiture', 'AB-123-CD'),
('Moto', 'EF-456-GH'),
('Moto', 'IJ-789-KL'),
('Velo', 'MN-012-OP'),
('Voiture', 'QR-345-ST'),
('Scooter', 'UV-678-WX');

INSERT INTO Livraison (date_, duree, taille, prix_pizza, gratuit, heure, Id_Livreur, Id_Client, Id_Vehicule, id_pizza) VALUES

-- 1. Alice → Margherita (livraison normale)
('2023-10-01', 30, 'Humaine', 8.50, FALSE, '18:30:00', 1, 1, 1, 1),

-- 2. Alice → Chevre Miel (livraison longue → possible retard)
('2023-10-01', 45, 'Humaine', 13.00, FALSE, '19:00:00', 2, 1, 2, 9),


-- 3. Bob → Regina
('2023-10-02', 25, 'Naine', 9.00, FALSE, '19:10:00', 2, 2, 2, 2),

-- 4. Bob → Carnivore (très long → gros retard)
('2023-10-02', 50, 'Hogresse', 13.50, FALSE, '20:00:00', 3, 2, 3, 11),


-- 5. Charlie → Pepperoni
('2023-10-03', 20, 'Hogresse', 9.50, FALSE, '20:15:00', 3, 3, 3, 3),

-- 6. Charlie → BBQ Chicken (très longue livraison)
('2023-10-03', 60, 'Humaine', 12.50, FALSE, '21:00:00', 1, 3, 1, 8),


-- 7. Lucas → Végétarienne
('2023-10-04', 35, 'Humaine', 10.00, FALSE, '18:45:00', 1, 4, 1, 4),

-- 8. Lucas → Hawaiienne
('2023-10-04', 40, 'Naine', 11.00, FALSE, '19:30:00', 2, 4, 2, 5),


-- 9. Léa → Hawaiienne
('2023-10-05', 40, 'Naine', 11.00, FALSE, '19:30:00', 2, 5, 2, 5),

-- 10. Léa → Mexicaine (longue livraison)
('2023-10-05', 55, 'Hogresse', 12.00, FALSE, '20:30:00', 3, 5, 3, 10),


-- 11. Camille → Quatre Fromages
('2023-10-06', 45, 'Hogresse', 12.00, FALSE, '20:00:00', 3, 6, 3, 6),

-- 12. Camille → Margherita gratuite (bonification client)
('2023-10-06', 25, 'Naine', 8.50, TRUE, '19:45:00', 2, 6, 2, 1),


-- 13. Emma → Calzone
('2023-10-07', 30, 'Humaine', 11.50, FALSE, '18:15:00', 1, 7, 1, 7),

-- 14. Emma → Carnivore (très long → retard)
('2023-10-07', 70, 'Hogresse', 13.50, FALSE, '21:00:00', 4, 7, 4, 11),


-- 15. Nathan → Margherita gratuite
('2023-10-08', 25, 'Naine', 8.50, TRUE, '19:45:00', 2, 8, 2, 1),

-- 16. Nathan → Mexicaine
('2023-10-08', 50, 'Humaine', 12.00, FALSE, '20:30:00', 5, 8, 5, 10),


-- 17. Chloé → Chevre Miel
('2023-10-09', 30, 'Humaine', 13.00, FALSE, '18:30:00', 6, 9, 6, 9),

-- 18. Chloé → BBQ Chicken
('2023-10-09', 35, 'Hogresse', 12.50, FALSE, '19:00:00', 6, 9, 6, 8),


-- 19. Maxime → Regina
('2023-10-10', 20, 'Naine', 9.00, FALSE, '18:00:00', 1, 10, 1, 2),

-- 20. Maxime → Carnivore (très long → retard)
('2023-10-10', 60, 'Hogresse', 13.50, FALSE, '21:00:00', 3, 10, 3, 11);
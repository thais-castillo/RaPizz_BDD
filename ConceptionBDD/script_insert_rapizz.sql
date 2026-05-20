DELETE FROM contient;
DELETE FROM Livraison;
DELETE FROM Pizza;
DELETE FROM Ingredient;
DELETE FROM Vehicule;
DELETE FROM Client;
DELETE FROM Livreur;

INSERT INTO Ingredient (nom) VALUES
   ('Mozzarella'),
   ('Tomate'),
   ('Jambon'),
   ('Champignons'),
   ('Poivron'),
   ('Olives'),
   ('Pepperoni');

   INSERT INTO Pizza (nom, prix) VALUES
   ('Margherita', 8.50),
   ('REGINA', 9.00),
   ('Pepperoni', 9.50),
   ('Vegetarienne', 10.00),
   ('Hawaiienne', 11.00),
   ('Quatre Fromages', 12.00),
   ('Calzone', 11.50);

   INSERT INTO contient (id_pizza, Id_Ingredient) VALUES
   (1, 1), (1, 2),           -- Margherita : mozzarella, tomate
   (2, 1), (2, 2), (2, 3),   -- Regina : mozzarella, tomate, jambon
   (3, 1), (3, 2), (3, 7),   -- Pepperoni : mozzarella, tomate, pepperoni
   (4, 1), (4, 2), (4, 4), (4, 5), (4, 6), -- Végétarienne : mozza, tomate, champignons, poivron, olives
   (5, 1), (5, 2), (5, 3), (5, 6), -- Hawaiienne : mozzarella, tomate, jambon, olives
   (6, 1), (6, 2), (6, 4), (6, 5), -- Quatre Fromages : mozzarella, tomate, champignons, poivron
   (7, 1), (7, 2), (7, 3); -- Calzone : mozzarella, tomate, jambon

   INSERT INTO Livreur (nom, prenom) VALUES
   ('Dupont', 'Jean'),
   ('Martin', 'Sophie'),
   ('Durand', 'Pierre');

   INSERT INTO Client (prenom, nom, solde, date_abonnement, bonification) VALUES
   ('Alice', 'Smith', 50.00, '2023-01-15', 2),
   ('Bob', 'Johnson', 30.00, '2023-02-20', 1),
   ('Charlie', 'Brown', 20.00, '2023-03-10', 0),
   ('Lucas',  'Martin', 40.00, '2023-04-05', 3),
   ('Léa',  'Durand', 25.00, '2023-05-12', 1),
   ('Camille',  'Moreau', 60.00, '2023-06-18', 4),
   ('Emma',  'Dubois', 35.00, '2023-07-22', 2),
   ('Nathan',   'Leroy', 45.00, '2023-08-30', 3),
   ('Chloé', 'Bernard', 55.00, '2023-09-15', 4);

   INSERT INTO Vehicule (type, immatricule) VALUES
   ('Voiture', 'AB-123-CD'),
   ('Moto',    'EF-456-GH'),
   ('Moto',    'IJ-789-KL'),
   ('Velo',    'MN-012-OP'); 

   INSERT INTO Livraison (date_, duree, taille, prix_pizza, gratuit, heure, Id_Livreur, Id_Client, Id_Vehicule, id_pizza) VALUES
   ('2023-10-01', 30, 'Humaine', 8.50, FALSE, '18:30:00', 1, 1, 1, 1), -- Margherita pour Alice
   ('2023-10-02', 25, 'Naine', 9.00, FALSE, '19:00:00', 2, 2, 2, 2), -- Regina pour Bob
   ('2023-10-03', 20, 'Hogresse', 9.50, FALSE, '20:15:00', 3, 3, 3, 3), -- Pepperoni pour Charlie
   ('2023-10-04', 35, 'Humaine', 10.00, FALSE, '18:45:00', 1, 4, 1, 4), -- Végétarienne pour Lucas
   ('2023-10-05', 40, 'Naine', 11.00, FALSE, '19:30:00', 2, 5, 2, 5), -- Hawaiienne pour Léa
   ('2023-10-06', 45, 'Hogresse', 12.00, FALSE, '20:00:00', 3, 6, 3, 6), -- Quatre Fromages pour Camille
   ('2023-10-07', 30, 'Humaine', 11.50, FALSE, '18:15:00', 1, 7, 1, 7), -- Calzone pour Emma
   ('2023-10-08', 25, 'Naine', 8.50, TRUE , '19:45:00', 2, 8, 2 ,1); -- Margherita gratuite pour Nathan (bonification)
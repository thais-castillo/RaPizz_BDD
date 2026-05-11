CREATE TABLE Pizza(
   id_pizza COUNTER,
   nom VARCHAR(50),
   prix CURRENCY,
   PRIMARY KEY(id_pizza)
);

CREATE TABLE Ingredient(
   Id_Ingredient COUNTER,
   nom VARCHAR(50),
   PRIMARY KEY(Id_Ingredient)
);

CREATE TABLE Livreur(
   Id_Livreur COUNTER,
   nom VARCHAR(50),
   prenom VARCHAR(50),
   PRIMARY KEY(Id_Livreur)
);

CREATE TABLE Client(
   Id_Client COUNTER,
   prenom VARCHAR(50),
   nom VARCHAR(50),
   solde CURRENCY,
   date_abonnement DATE,
   bonificaction INT,
   PRIMARY KEY(Id_Client)
);

CREATE TABLE Vehicule(
   Id_Vehicule COUNTER,
   type VARCHAR(50),
   immatricule INT,
   PRIMARY KEY(Id_Vehicule),
   UNIQUE(immatricule)
);

CREATE TABLE Livraison(
   id_livraison COUNTER,
   date_ DATE,
   duree INT,
   prix_pizza CURRENCY,
   gratuit LOGICAL,
   heure TIME,
   Id_Livreur INT NOT NULL,
   Id_Client INT NOT NULL,
   Id_Vehicule INT NOT NULL,
   id_pizza INT NOT NULL,
   PRIMARY KEY(id_livraison),
   FOREIGN KEY(Id_Livreur) REFERENCES Livreur(Id_Livreur),
   FOREIGN KEY(Id_Client) REFERENCES Client(Id_Client),
   FOREIGN KEY(Id_Vehicule) REFERENCES Vehicule(Id_Vehicule),
   FOREIGN KEY(id_pizza) REFERENCES Pizza(id_pizza)
);

CREATE TABLE contient(
   id_pizza INT,
   Id_Ingredient INT,
   PRIMARY KEY(id_pizza, Id_Ingredient),
   FOREIGN KEY(id_pizza) REFERENCES Pizza(id_pizza),
   FOREIGN KEY(Id_Ingredient) REFERENCES Ingredient(Id_Ingredient)
);
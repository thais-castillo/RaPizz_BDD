DROP DATABASE pizzeria;
CREATE DATABASE pizzeria;
USE pizzeria;

CREATE TABLE Pizza(
   id_pizza INT AUTO_INCREMENT,
   nom VARCHAR(50) NOT NULL,
   prix DECIMAL(10,2) NOT NULL,
   PRIMARY KEY(id_pizza),
   CONSTRAINT chk_pizza CHECK (prix > 0), 
   UNIQUE(nom),
   CONSTRAINT chk_taille CHECK (nom IN ('Naine', 'Humaine', 'Hogresse')) 
);

CREATE TABLE Ingredient(
   Id_Ingredient INT AUTO_INCREMENT,
   nom VARCHAR(50) NOT NULL,
   PRIMARY KEY(Id_Ingredient),
   UNIQUE(nom)
);

CREATE TABLE Livreur(
   Id_Livreur INT AUTO_INCREMENT,
   nom VARCHAR(50) NOT NULL,
   prenom VARCHAR(50) NOT NULL,
   PRIMARY KEY(Id_Livreur)
);

CREATE TABLE Client(
   Id_Client INT AUTO_INCREMENT,
   prenom VARCHAR(50) NOT NULL,
   nom VARCHAR(50) NOT NULL,
   solde DECIMAL(10,2) NOT NULL DEFAULT 0,
   date_abonnement DATE NOT NULL,
   bonificaction INT NOT NULL DEFAULT 0,
   PRIMARY KEY(Id_Client),
   CONSTRAINT chk_solde CHECK (solde >= 0),
   CONSTRAINT chk_bonification CHECK (bonification BETWEEN 0 AND 9) 
);

CREATE TABLE Vehicule(
   Id_Vehicule INT AUTO_INCREMENT,
   type VARCHAR(50) NOT NULL,
   immatricule INT NOT NULL,
   PRIMARY KEY(Id_Vehicule),
   UNIQUE(immatricule),
   CONSTRAINT chk_vehicule CHECK (type IN ('Voiture', 'Moto', 'Velo')) 
);

CREATE TABLE Livraison(
   id_livraison INT AUTO_INCREMENT,
   date_ DATE NOT NULL,
   duree INT NOT NULL,
   prix_pizza DECIMAL(10,2) NOT NULL,
   gratuit BOOLEAN NOT NULL,
   heure TIME NOT NULL,
   Id_Livreur INT NOT NULL,
   Id_Client INT NOT NULL,
   Id_Vehicule INT NOT NULL,
   id_pizza INT NOT NULL,
   PRIMARY KEY(id_livraison),
   FOREIGN KEY(Id_Livreur) REFERENCES Livreur(Id_Livreur) ON DELETE RESTRICT ON UPDATE CASCADE,
   FOREIGN KEY(Id_Client) REFERENCES Client(Id_Client) ON DELETE RESTRICT ON UPDATE CASCADE,
   FOREIGN KEY(Id_Vehicule) REFERENCES Vehicule(Id_Vehicule) ON DELETE RESTRICT ON UPDATE CASCADE,
   FOREIGN KEY(id_pizza) REFERENCES Pizza(id_pizza) ON DELETE RESTRICT ON UPDATE CASCADE,
   CONSTRAINT chk_taille    CHECK (taille IN ('Naine', 'Humaine', 'Ogresse')),  
   CONSTRAINT chk_delai     CHECK (duree IS NULL OR duree >= 0),
   CONSTRAINT chk_prix_liv  CHECK (prix_pizza > 0)
);

CREATE TABLE contient(
   id_pizza INT,
   Id_Ingredient INT,
   PRIMARY KEY(id_pizza, Id_Ingredient),
   FOREIGN KEY(id_pizza) REFERENCES Pizza(id_pizza) ON DELETE CASCADE ON UPDATE CASCADE,
   FOREIGN KEY(Id_Ingredient) REFERENCES Ingredient(Id_Ingredient) ON DELETE CASCADE ON UPDATE CASCADE
);
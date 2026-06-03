DROP DATABASE pizzeria;
CREATE DATABASE pizzeria;
USE pizzeria;

CREATE TABLE Pizza(
   id_pizza INT AUTO_INCREMENT,
   nom VARCHAR(50) NOT NULL,
   prix DECIMAL(10,2) NOT NULL,
   PRIMARY KEY(id_pizza),
   CONSTRAINT chk_pizza CHECK (prix > 0), 
   UNIQUE(nom)
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
   bonification INT NOT NULL DEFAULT 0,
   PRIMARY KEY(Id_Client),
   CONSTRAINT chk_solde CHECK (solde >= 0),
   CONSTRAINT chk_bonification CHECK (bonification BETWEEN 0 AND 9) 
);

CREATE TABLE Vehicule(
   Id_Vehicule INT AUTO_INCREMENT,
   type VARCHAR(50) NOT NULL,
   immatricule VARCHAR(20) NOT NULL,
   PRIMARY KEY(Id_Vehicule),
   UNIQUE(immatricule),
   CONSTRAINT chk_vehicule CHECK (type IN ('Voiture', 'Moto', 'Velo','Scooter')) 
);

CREATE TABLE Livraison(
   id_livraison INT AUTO_INCREMENT,
   date_ DATE NOT NULL,
   duree INT,
   taille VARCHAR(10) NOT NULL,
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
   CONSTRAINT chk_taille    CHECK (taille IN ('Naine', 'Humaine', 'Hogresse')),  
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

-- Procedure: insertion d'une livraison avec fidelite geree par la base
DROP PROCEDURE IF EXISTS sp_ajouter_livraison;
DELIMITER //
CREATE PROCEDURE sp_ajouter_livraison(
   IN p_date DATE,
   IN p_heure TIME,
   IN p_duree INT,
   IN p_taille VARCHAR(10),
   IN p_prix DECIMAL(10,2),
   IN p_id_livreur INT,
   IN p_id_client INT,
   IN p_id_vehicule INT,
   IN p_id_pizza INT,
   OUT o_gratuit BOOLEAN,
   OUT o_solde DECIMAL(10,2)
)
BEGIN
   DECLARE v_solde DECIMAL(10,2);
   DECLARE v_bonif INT;
   DECLARE v_montant DECIMAL(10,2);

   DECLARE EXIT HANDLER FOR SQLEXCEPTION
   BEGIN
      ROLLBACK;
      RESIGNAL;
   END;

   START TRANSACTION;

   SELECT solde, bonification INTO v_solde, v_bonif
   FROM Client
   WHERE Id_Client = p_id_client
   FOR UPDATE;

   IF v_solde IS NULL THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'CLIENT_INEXISTANT';
   END IF;

   SET o_gratuit = (v_bonif >= 9);
   SET v_montant = CASE WHEN o_gratuit THEN 0 ELSE p_prix END;

   IF v_solde < v_montant THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'SOLDE_INSUFFISANT';
   END IF;

   UPDATE Client
   SET solde = solde - v_montant,
       bonification = CASE WHEN o_gratuit THEN 0 ELSE v_bonif + 1 END
   WHERE Id_Client = p_id_client;

   INSERT INTO Livraison (
      date_, heure, duree, taille, prix_pizza, gratuit,
      Id_Livreur, Id_Client, Id_Vehicule, id_pizza
   )
   VALUES (
      p_date, p_heure, p_duree, p_taille, p_prix, o_gratuit,
      p_id_livreur, p_id_client, p_id_vehicule, p_id_pizza
   );

   SELECT solde INTO o_solde
   FROM Client
   WHERE Id_Client = p_id_client;

   COMMIT;
END//
DELIMITER ;

-- Trigger: force gratuit si duree > 30 a la cloture
DROP TRIGGER IF EXISTS trg_livraison_gratuite;
DELIMITER //
CREATE TRIGGER trg_livraison_gratuite
BEFORE UPDATE ON Livraison
FOR EACH ROW
BEGIN
   IF NEW.duree IS NOT NULL AND NEW.duree > 30 THEN
      SET NEW.gratuit = TRUE;
   END IF;
END//
DELIMITER ;

-- Trigger: remboursement automatique si retard
DROP TRIGGER IF EXISTS trg_livraison_rembourse;
DELIMITER //
CREATE TRIGGER trg_livraison_rembourse
AFTER UPDATE ON Livraison
FOR EACH ROW
BEGIN
   IF NEW.duree IS NOT NULL
      AND NEW.duree > 30
      AND OLD.gratuit = FALSE
      AND NEW.gratuit = TRUE THEN
      UPDATE Client
      SET solde = solde + NEW.prix_pizza
      WHERE Id_Client = NEW.Id_Client;
   END IF;
END//
DELIMITER ;
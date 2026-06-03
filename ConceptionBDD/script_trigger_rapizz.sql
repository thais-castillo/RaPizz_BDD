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
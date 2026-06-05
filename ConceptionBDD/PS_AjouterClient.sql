DELIMITER $$

CREATE PROCEDURE AjouterClient(
    IN p_nom       VARCHAR(100),
    IN p_prenom    VARCHAR(100),
    IN p_solde     DECIMAL(10,2)
)
BEGIN
    -- Validation métier
    IF p_solde < 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Le solde initial ne peut pas être négatif.';
    END IF;

    INSERT INTO Client (nom, prenom, solde, date_abonnement, bonification)
    VALUES (p_nom, p_prenom, p_solde, CURDATE(), 0);
END$$

DELIMITER ;
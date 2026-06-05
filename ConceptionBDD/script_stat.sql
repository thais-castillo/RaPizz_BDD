-- Suivre le chiffre d'affaire de la pizzéria
-- CA = prix_pizza * nb_pizza vendues non gratuite
-- Si gratuit from Livraison == False && duree from livraison <= 30(la pizza n'est pas gratuite) :
-- CA += select prix_pizza from 


-- Créer une vue qui permet de récupérer le nombre de livraison pour un client
-- select count(id_livraison) from livraison

CREATE OR REPLACE VIEW vue_classement_clients AS
SELECT 
    c.Id_Client,
    c.prenom,
    c.nom,
    COUNT(l.id_livraison) AS nb_de_livraisons
FROM 
    Client c
LEFT JOIN 
    Livraison l ON c.Id_Client = l.Id_Client
GROUP BY 
    c.Id_Client, c.prenom, c.nom
ORDER BY 
    nombre_de_livraisons DESC;
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



CREATE OR REPLACE VIEW vue_top_pizzas AS
SELECT 
    p.id_pizza,
    p.nom AS nom_pizza,
    p.prix AS prix_unitaire,
    COUNT(l.id_livraison) AS nb_de_ventes
FROM 
    Pizza p
LEFT JOIN 
    Livraison l ON p.id_pizza = l.id_pizza
GROUP BY 
    p.id_pizza, p.nom, p.prix
ORDER BY 
    nb_de_ventes DESC;
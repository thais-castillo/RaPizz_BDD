/*MENU*/
SELECT 
    p.nom,
    p.prix,
    i.nom AS ingredient FROM Pizza p
JOIN contient c ON p.id_pizza = c.id_pizza
JOIN Ingredient i ON c.id_ingredient = i.id_ingredient
ORDER BY p.nom;

/*Fiche de livraison */
SELECT 
    li.nom AS nom_livreur,
    li.prenom AS prenom_livreur,
    v.type AS type_vehicule,
    v.immatricule,
    c.nom AS nom_client,
    c.prenom AS prenom_client,
    l.date_ AS date_livraison,
    l.heure AS heure_livraison,
    p.nom AS nom_pizza,
    l.prix_pizza,
       CASE
        WHEN l.duree > 30 THEN 'Oui'
        ELSE 'Non'
    END AS retard
FROM Livraison l, Livreur li, Vehicule v, Client c, Pizza p
WHERE l.Id_Livreur = li.Id_Livreur
AND l.Id_Vehicule = v.Id_Vehicule
AND l.Id_Client = c.Id_Client
AND l.Id_Pizza = p.Id_Pizza
ORDER BY l.date_ DESC, l.heure DESC;

/*Questins diverses */
/*Quels sont les véhicules n'ayant jamais servi ?*/
SELECT * FROM Vehicule
WHERE Id_Vehicule NOT IN (SELECT DISTINCT Id_Vehicule FROM Livraison);
/*Calcul du nombre de commandes par client */
SELECT c.prenom, c.nom, COUNT(*) AS nombre_commandes
FROM Client c, Livraison l
WHERE c.Id_Client = l.Id_Client
GROUP BY c.Id_Client, c.prenom, c.nom;
/*Calcul de la moyenne des commandes */
SELECT AVG(nombre_commandes) AS moyenne_commandes
FROM (
    SELECT COUNT(*) AS nombre_commandes
    FROM Livraison
    GROUP BY Id_Client
) AS commandes_clients;
/*Extraction des clients ayant commandé plus que la moyenne*/
SELECT 
    c.nom,
    c.prenom,
    COUNT(*) AS nombre_commandes
FROM Client c, Livraison l
WHERE c.Id_Client = l.Id_Client
GROUP BY c.Id_Client, c.nom, c.prenom
HAVING COUNT(*) > (
    SELECT AVG(nombre_commandes)
    FROM (
        SELECT COUNT(*) AS nombre_commandes
        FROM Livraison
        GROUP BY Id_Client
    ) AS commandes_clients
);

/*Requête statistique */
/*Meilleur client */
Select 
    c.nom,
    c.prenom,
    COUNT(*) AS nombre_commandes
FROM Client c, Livraison l
WHERE c.Id_Client = l.Id_Client
GROUP BY c.Id_Client, c.nom, c.prenom
ORDER BY nombre_commandes DESC
LIMIT 1;
/*Pire livreur (le plus en retard)*/
Select
    li.nom,
    li.prenom,
    COUNT(*) AS nombre_retards
FROM Livreur li, Livraison l
WHERE li.Id_Livreur = l.Id_Livreur
AND l.duree > 30
GROUP BY li.Id_Livreur, li.nom, li.prenom
ORDER BY nombre_retards DESC
LIMIT 1;
/*Meilleur livreur */
Select
    li.nom,
    li.prenom,
    COUNT(*) AS nombre_livraisons
FROM Livreur li, Livraison l
WHERE li.Id_Livreur = l.Id_Livreur
GROUP BY li.Id_Livreur, li.nom, li.prenom
ORDER BY nombre_livraisons DESC
LIMIT 1;
/*Livraison la plus rapide */
Select *
FROM Livraison 
ORDER BY duree ASC
LIMIT 1;
/*Vehicule le plus utilisé*/
Select
    v.immatricule,
    COUNT(*) AS nombre_utilisations
FROM Vehicule v, Livraison l
WHERE v.Id_Vehicule = l.Id_Vehicule
GROUP BY v.Id_Vehicule, v.immatricule
ORDER BY nombre_utilisations DESC
LIMIT 1;
/*Pizza la plus commandé*/
Select
    p.nom,
    COUNT(*) AS nombre_commandes
FROM Pizza p, Livraison l
WHERE p.Id_Pizza = l.Id_Pizza
GROUP BY p.Id_Pizza, p.nom
ORDER BY nombre_commandes DESC
LIMIT 1;
/*Pizza la moins commandé */
Select
    p.nom,
    COUNT(*) AS nombre_commandes
FROM Pizza p, Livraison l
WHERE p.Id_Pizza = l.Id_Pizza
GROUP BY p.Id_Pizza, p.nom
ORDER BY nombre_commandes ASC
LIMIT 1;
/*Ingredient favori */
Select 
    i.nom,
    COUNT(*) AS nombre_utilisations
FROM Ingredient i, contient c, Pizza p, Livraison l
WHERE i.Id_Ingredient = c.Id_Ingredient
AND c.Id_Pizza = p.Id_Pizza
AND p.Id_Pizza = l.Id_Pizza
GROUP BY i.Id_Ingredient, i.nom
ORDER BY nombre_utilisations DESC
LIMIT 1;
/*Jour avec le plus de livraisons*/
SELECT 
    date_,
    COUNT(*) AS nb_commandes
FROM Livraison
GROUP BY date_
ORDER BY nb_commandes DESC;

/*Chiffre d'affaire total*/
SELECT SUM(prix_pizza) AS chiffre_affaire_total
FROM Livraison;

/*Chiffre d'affaire par client */
SELECT 
    c.nom,
    c.prenom,
    SUM(l.prix_pizza) AS chiffre_affaire_client
FROM Client c, Livraison l
WHERE c.Id_Client = l.Id_Client
GROUP BY c.Id_Client, c.nom, c.prenom;
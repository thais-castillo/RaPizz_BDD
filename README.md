# Application de Gestion de RaPizz

Une application Java de bureau utilisant **Java Swing** et une architecture **DAO** connectée à une base de données SQL pour gérer les livraisons et analyser les performances commerciales de la pizzeria.

---

## Fonctionnalités Principales

- **Tableau de bord dynamique** : Visualisation des indicateurs clés (Chiffre d'Affaires Net, Délai moyen de livraison, Journée la plus chargée).
- **Analyse Performance Produits** : Identification de la "Pizza Star", de l'ingrédient favori ainsi que des pizzas les moins populaires.
- **Gestion de la Relation Client** : Suivi des clients les plus fidèles, du panier/commandes moyens, et du chiffre d'affaires généré par client.
- **Gestion de la Flotte & Logistique** : Suivi de l'utilisation des véhicules (les plus utilisés vs jamais utilisés) et des performances des livreurs (meilleur livreur vs retards).
- **Gestion des commandes et des livraisons** : Suivi des livraisons en cours, de leur statut, et possibilité d'ajouter de nouvelles commandes.
- **Gestion des clients** : Possibilité d'ajouter de nouveaux comptes clients et suivi de leur solde.

---

## Architecture du Projet

Le projet respecte une architecture de type **MVC** couplée à des **DAO** pour l'accès aux données :

| Dossier | Contenu |
|---|---|
| `src/Controleur/` | Contrôleurs pour la gestion des actions sur les vues |
| `src/DAO/` | Classes d'accès à la base de données |
| `src/Model/` | Objets métiers et connecteurs |
| `src/Vue/` | Interfaces graphiques en Java Swing |

---

## Compilation et Lancement

### 1. Compilation du projet

Placez-vous à la racine du projet (là où se trouve le dossier `src/`) et compilez tous les fichiers `.java` vers un dossier `bin/` :

```bash
javac -cp "lib/mariadb-client.jar" -d bin src/*.java src/Controleur/*.java src/DAO/*.java src/Model/*.java src/Vue/*.java
```

### 2. Lancement de l'application

Exécutez la classe principale (contenant la méthode `main`) :

```bash
java -cp "bin:lib/mariadb-client.jar" Main
```

> **Note :** Utilisez un point-virgule `;` à la place du deux-points `:` sous Windows pour séparer le classpath.

---

## Accès à la Base de Données

La base de données est disponible sur PHPMyAdmin, à cette adresse : https://dwarves.iut-fbleau.fr/phpmyadmin/

Identifiant : bribant
Mot de passe : Chocolat

**Note** : Certaines tables ne font pas partie du projet RaPizz.

### Schéma SQL principal

| Table | Colonnes |
|---|---|
| `Client` | `Id_Client`, `nom`, `prenom` |
| `Livreur` | `Id_Livreur`, `nom`, `prenom` |
| `Vehicule` | `Id_Vehicule`, `type`, `immatricule` |
| `Pizza` | `Id_Pizza`, `nom`, `prix` |
| `Livraison` | `Id_Livraison`, `date_`, `heure`, `prix_pizza`, `gratuit`, `duree`, `Id_Client`, `Id_Livreur`, `Id_Vehicule`, `Id_Pizza` |
| `contient` | `Id_Pizza`, `Id_Ingredient` |

---

## Technologies Utilisées

| Technologie | Détail |
|---|---|
| **Langage** | Java (Object-Oriented Programming) |
| **Interface Graphique** | Java Swing / AWT |
| **Base de données** | SQL via l'API JDBC (Java Database Connectivity) |
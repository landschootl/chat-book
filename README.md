# chat-book
Projet de COO en M1MIAGEFA - Plate-forme de discussion / Réseau social 

## Présentation générale
Le principe général consiste à réaliser une plate-forme de discussion / forum / réseau social.  
Pour utiliser l'application, les utilisateurs devront s'identifier avec leur compte (et un mot de passe). Ils pourront ensuite s'ajouter en ami, et s'envoyer des messages (entre amis uniquement).  
Puisque l'application ne permet pas d'avoir plusieurs utilisateurs simultanément, le système de messagerie fonctionne en différé.  
Chaque utilisateur peut également créer un groupe de discussion, il en devient automatiquement le modérateur, et il peut ensuite ajouter ou supprimer des utilisateurs.  
Il existe un compte unique nommé Administrateur qui dispose de droits étendus : créer, modifier ou supprimer des comptes utilisateurs, modifier les groupe de discussion, etc.  

## Cas d'utilisation
* CU1 - Connexion / Déconnexion ✓
* CU2 - CRUD utilisateur ✓
* CU3 - Création d'un groupe ✓
* CU4 - Recherche et ajout d'amis ✓
* CU5 - Affichage de la liste des discussions disponibles ✓
* CU6 - Envoi et réception de messages ✖
* CU7 - Modération d'un groupe ✖
* CU8 - Gestion des types de message ✖

## Architecture
* gui : IHM - Intéraction de l'utilisateur en Swing et appels de services
* service : Traitements métiers de l'application - Communication controller / mapper
* persistence : Accès à la base de données - Transformation en objet Java
* domain : Objets métiers

## Tâches Maven
* `mvn clean install` : Installation des dépendances du projet
* `mvn exec:java` : Execution du projet
* `mvn clean compile assembly:single` : Génération d'un fichier JAR exécutable

## Ne pas oublier
* Installer le plugin Lombok sur IntelliJ
* Dans GUI Designer d'IntelliJ, générer en code Java
* Ajouter un fichier config.yml dans resources contenant :  
db:  
    driver: jdbc:mysql  
    hote: localhost:8889  
    base: chat_book  
    login: root  
    password: root  

## Suivi
Trello : https://trello.com/b/fks7bbBR/chat-book

## Contributeurs
[Ludovic LANDSCHOOT](http://github.com/landschoot) & [Laurent THIEBAULT](http://github.com/lauthieb)

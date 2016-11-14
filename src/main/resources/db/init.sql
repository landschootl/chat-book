/****************** Insertion centres intêrets ******************/
INSERT INTO HOBBIES VALUES (default, "Sport");
INSERT INTO HOBBY VALUES (default, 1, "Football");
INSERT INTO HOBBY VALUES (default, 1, "Rugby");
INSERT INTO HOBBY VALUES (default, 1, "Natation");

INSERT INTO HOBBIES VALUES (default, "Musique");
INSERT INTO HOBBY VALUES (default, 2, "Jazz");
INSERT INTO HOBBY VALUES (default, 2, "Rap");
INSERT INTO HOBBY VALUES (default, 2, "Classique");

INSERT INTO HOBBIES VALUES (default, "Voyage");
INSERT INTO HOBBY VALUES (default, 3, "Europe");
INSERT INTO HOBBY VALUES (default, 3, "Monde");
INSERT INTO HOBBY VALUES (default, 3, "Allemagne");

INSERT INTO HOBBIES VALUES (default, "Theatre");
INSERT INTO HOBBY VALUES (default, 4, "Comedie");
INSERT INTO HOBBY VALUES (default, 4, "Tragedie");
INSERT INTO HOBBY VALUES (default, 4, "Moliere");

/****************** Insertion compte utilisateur ******************/
-- Compte administrateur
-- Identifiant : admin
-- Mot de passe : admin
INSERT INTO USER VALUES (default, "admin", "toto", "titi", "FG9b0v0WVGm7BaKVh6E3xw==", "USER_ADMIN");

-- Comptes utilisateur
-- Identifiant : lauthieb
-- Mot de passe : MDP2lauthieb
INSERT INTO USER VALUES (default, "lauthieb", "Laurent", "Thiebault", "O+nWSpoJ6gpM9iuTlnaLLw==", "USER_DEFAULT");
INSERT INTO USER_HOBBIES VALUES (default, 2, 1);
INSERT INTO USER_HOBBY VALUES (default, 2, 5);

-- Identifiant : ludo
-- Mot de passe : MDP2ludo
INSERT INTO USER VALUES (default, "ludo", "Ludovic", "Landschoot", "8zpKVPgVEEA1zJVEWd95FQ==", "USER_DEFAULT");
INSERT INTO USER_HOBBIES VALUES (default, 3, 1);
INSERT INTO USER_HOBBIES VALUES (default, 3, 3);

/****************** Insertion groupe ******************/
INSERT INTO CONNECTION VALUES (default, 1, "ludo-lauthieb");
INSERT INTO USER_CONNECTION VALUES (default, (SELECT id FROM CONNECTION WHERE name="ludo-laut"), 1);
INSERT INTO USER_CONNECTION VALUES (default, (SELECT id FROM CONNECTION WHERE name="ludo-laut"), 2);
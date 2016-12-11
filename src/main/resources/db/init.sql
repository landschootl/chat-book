/****************** Insertion compte utilisateur ******************/
-- Compte administrateur
-- Identifiant : admin
-- Mot de passe : admin
INSERT INTO USER VALUES (default, "admin", "toto", "titi", "FG9b0v0WVGm7BaKVh6E3xw==", "USER_ADMIN");

-- Comptes utilisateur
-- Identifiant : lauthieb
-- Mot de passe : MDP2lauthieb
INSERT INTO USER VALUES (default, "lauthieb", "Laurent", "Thiebault", "O+nWSpoJ6gpM9iuTlnaLLw==", "USER_DEFAULT");

-- Identifiant : ludo
-- Mot de passe : MDP2ludo
INSERT INTO USER VALUES (default, "ludo", "Ludovic", "Landschoot", "8zpKVPgVEEA1zJVEWd95FQ==", "USER_DEFAULT");

-- Identifiant : alain
-- Mot de passe : MDP2alain
INSERT INTO USER VALUES (default, "alain", "Alain", "Thiebault", "c9nVIzFxDItViBId+4v7Hw==", "USER_DEFAULT");

/****************** Insertion amitié ******************/
INSERT INTO FRIENDSHIP VALUES (default, 2, 3, true);
INSERT INTO FRIENDSHIP VALUES (default, 2, 4, false);

/****************** Insertion groupe ******************/
INSERT INTO CONNECTION VALUES (default, 1, "ludothieb");
INSERT INTO USER_CONNECTION VALUES (default, (SELECT id FROM CONNECTION WHERE name="ludothieb"), (SELECT id FROM USER WHERE firstname="Ludovic"));
INSERT INTO USER_CONNECTION VALUES (default, (SELECT id FROM CONNECTION WHERE name="ludothieb"), (SELECT id FROM USER WHERE firstname="Laurent"));
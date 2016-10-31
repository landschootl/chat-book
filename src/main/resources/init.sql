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
-- compte administrateur
INSERT INTO USER VALUES (default, "admin", "toto", "titi", "admin", "USER_ADMIN");

-- compte utilisateur
INSERT INTO USER VALUES (default, "TonyLandschoot", "Tony", "Landschoot", "123456", "USER_DEFAULT");
INSERT INTO USER_HOBBIES VALUES (default, 2, 1);
INSERT INTO USER_HOBBY VALUES (default, 2, 5);

INSERT INTO USER VALUES (default, "SegoleneFrison", "Segolene", "Frison", "123456", "USER_DEFAULT");
INSERT INTO USER_HOBBIES VALUES (default, 3, 1);
INSERT INTO USER_HOBBIES VALUES (default, 3, 3);

INSERT INTO USER VALUES (default, "BeatriceDailly", "Beatrice", "Dailly", "123456", "USER_DEFAULT");
INSERT INTO USER_HOBBY VALUES (default, 4, 2);
INSERT INTO USER_HOBBY VALUES (default, 4, 9);
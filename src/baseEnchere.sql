CREATE TABLE admin (
  id          SERIAL NOT NULL, 
  identifiant varchar(10) NOT NULL, 
  password    varchar(50) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE Categorie (
  id     SERIAL NOT NULL, 
  nomCat varchar(50) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE demande_credit (
  id            SERIAL NOT NULL, 
  dateDemande   date NOT NULL, 
  valeur        float8 NOT NULL, 
  utilisateurid int4 NOT NULL, 
  etat          int4 DEFAULT 0 NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE enchere (
  id            SERIAL NOT NULL, 
  titre         varchar(50) NOT NULL, 
  description   text NOT NULL, 
  prixMinimal   float8 NOT NULL, 
  duree         float8 NOT NULL, 
  dateEnchere   timestamp NOT NULL, 
  Categorieid   int4 NOT NULL, 
  utilisateurid int4 NOT NULL, 
  statut        int4 DEFAULT 0 NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE image (
  id        SERIAL NOT NULL, 
  photo     bytea NOT NULL, 
  nomImage  varchar(50), 
  enchereid int4 NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE mouvement_encheres (
  id            SERIAL NOT NULL, 
  dateMouvement timestamp NOT NULL, 
  valeurEnchere float8 NOT NULL, 
  utilisateurid int4 NOT NULL, 
  enchereid     int4 NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE parametre (
  id              SERIAL NOT NULL, 
  commission      float8 NOT NULL, 
  dureeEnchereMin float8 NOT NULL, 
  dureeEnchereMax float8 NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE utilisateur (
  id       SERIAL NOT NULL, 
  login    varchar(20) NOT NULL, 
  password varchar(50) NOT NULL, 
  nom      varchar(50) NOT NULL, 
  prenom   varchar(50) NOT NULL, 
  credit   float8 NOT NULL, 
  PRIMARY KEY (id));
ALTER TABLE enchere ADD CONSTRAINT FKenchere452566 FOREIGN KEY (Categorieid) REFERENCES Categorie (id);
ALTER TABLE enchere ADD CONSTRAINT FKenchere944116 FOREIGN KEY (utilisateurid) REFERENCES utilisateur (id);
ALTER TABLE mouvement_encheres ADD CONSTRAINT FKmouvement_224746 FOREIGN KEY (utilisateurid) REFERENCES utilisateur (id);
ALTER TABLE mouvement_encheres ADD CONSTRAINT FKmouvement_288374 FOREIGN KEY (enchereid) REFERENCES enchere (id);
ALTER TABLE demande_credit ADD CONSTRAINT FKdemande_cr781755 FOREIGN KEY (utilisateurid) REFERENCES utilisateur (id);
ALTER TABLE image ADD CONSTRAINT FKimage201377 FOREIGN KEY (enchereid) REFERENCES enchere (id);


CREATE or replace view v_enchere AS 
SELECT e.id,titre,description,prixMinimal,duree,dateEnchere,Categorieid,utilisateurid,statut,nom,prenom,nomCat FROM enchere e
JOIN Categorie c 
ON e.Categorieid = c.id
JOIN utilisateur u
ON e.utilisateurid = u.id;

<-------meilleure mise ------>
CREATE or REPLACE VIEW v_UtilisateurMise as
select utilisateur.nom as nomUtilisateur , mouvement_encheres.utilisateurid,mouvement_encheres.valeurEnchere as utilisateurEnchere, enchere.titre,enchereid
from mouvement_encheres
join utilisateur on utilisateur.id=mouvement_encheres.utilisateurid
join enchere on enchere.id=mouvement_encheres.enchereid
where valeurEnchere = (SELECT MAX (valeurEnchere) from mouvement_encheres);


CREATE or REPLACE VIEW v_MouvementEnchere as
SELECT enchereid,titre,description,prixMinimal,duree,dateEnchere,Categorieid,ve.utilisateurid as idHote,statut,ve.nom,ve.prenom,nomCat,dateMouvement,valeurEnchere,me.utilisateurid as idClient,u.nom as nomClient,u.prenom as prenomClient FROM v_enchere ve 
JOIN mouvement_encheres me 
ON ve.id = me.enchereid
JOIN utilisateur u
ON me.utilisateurid = u.id;

<----Vente par rapport au categorie---->
SELECT nomCat,sum(max(valeurEnchere)) from v_MouvementEnchere where statut = 1 group by nomCat;

select enchereid,utilisateurEnchere from v_UtilisateurMise group by enchereid,utilisateurEnchere;




CREATE TABLE Categorie_desactive (
  id          SERIAL NOT NULL, 
  Categorieid int4 NOT NULL, 
  PRIMARY KEY (id));


CREATE OR REPLACE VIEW v_demande_credit AS 
SELECT c.id,dateDemande,valeur,utilisateurid,nom,prenom,etat FROM demande_credit c 
JOIN utilisateur u 
ON c.utilisateurid = u.id; 


CREATE or replace View v_valeurMax as  
SELECT max(valeurEnchere) as valeurEnchere,enchereid FROM v_mouvementEnchere WHERE statut = 1 group by enchereid ;

CREATE or replace View v_maxMise AS
SELECT ve.*,va.*FROM v_enchere ve JOIN v_valeurMax va ON
ve.id = va.enchereid;


CREATE or replace View v_rentabiliteCat AS
select sum(valeurEnchere) as somme,categorieid,nomCat FROM v_maxMise GROUP BY categorieid,nomCat;

CREATE or replace VIEW v_categorie_parEnchere AS
SELECT count(categorieid) as nombre,nomCat,categorieid FROM v_enchere group by nomCat,categorieid;

ALTER TABLE utilisateur
ALTER COLUMN login TYPE varchar(100);
CREATE SEQUENCE IF NOT EXISTS types_seq
  START WITH 1
  INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS types (
  id   INT DEFAULT nextval('types_seq') PRIMARY KEY,
  name VARCHAR(80)
);

INSERT INTO types(name) VALUES ('cat');
INSERT INTO types(name) VALUES ('dog');
INSERT INTO types(name) VALUES ('lizard');
INSERT INTO types(name) VALUES ('snake');
INSERT INTO types(name) VALUES ('bird');
INSERT INTO types(name) VALUES ('hamster');
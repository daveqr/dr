DROP TABLE IF EXISTS users cascade;

CREATE TABLE documents (
  id         INTEGER PRIMARY KEY,
  longtext   CLOB
);
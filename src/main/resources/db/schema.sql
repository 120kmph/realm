DROP TABLE IF EXISTS ALL_TYPE;

CREATE TABLE ALL_TYPE
(
    id         int NOT NULL auto_increment,
    name       varchar(255),
    index      int,
    index_name varchar(255),
    code       int,
    desc       varchar(255),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS FOOTPRINT;

CREATE TABLE FOOTPRINT
(
    uid           varchar(255) NOT NULL,
    name          varchar(255),
    type          int,
    status        int,
    province      varchar(255),
    city          varchar(255),
    lat           varchar(255),
    lng           varchar(255),
    date          varchar(255),
    evaluate_type int,
    comment       varchar(255),
    PRIMARY KEY (uid)
);

DROP TABLE IF EXISTS FOOD;

CREATE TABLE FOOD
(
    id       int NOT NULL auto_increment,
    name     varchar(255),
    material varchar(255),
    steps    varchar(255),
    types    varchar(255),
    comment  varchar(255),
    PRIMARY KEY (id)
);
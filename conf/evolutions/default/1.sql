# --- !Ups

CREATE TABLE "user"
(
    "id"       INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "username" VARCHAR NOT NULL,
    "password" VARCHAR NOT NULL
);

CREATE TABLE "category"
(
    "id"   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name" VARCHAR NOT NULL
);

CREATE TABLE "item"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name"        VARCHAR NOT NULL,
    "description" TEXT    NOT NULL,
    "category"    INTEGER NOT NULL,
    FOREIGN KEY (category) references category (id)
);

CREATE TABLE "account"
(
    "id"         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "first_name" VARCHAR NOT NULL,
    "last_name"  VARCHAR NOT NULL,
    "city"       VARCHAR NOT NULL
);

CREATE TABLE "cart"
(
    "id"     INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "number" INTEGER NOT NULL
);

CREATE TABLE "shipping"
(
    "id"     INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "street" VARCHAR NOT NULL,
    "city"   VARCHAR NOT NULL,
    "house"  INTEGER NOT NULL,
    "phone"  INTEGER NOT NULL
);

CREATE TABLE "payment"
(
    "id"           INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "amount"       INTEGER NOT NULL,
    "payment_type" VARCHAR NOT NULL
);

CREATE TABLE "rate"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "amount"      INTEGER NOT NULL,
    "description" TEXT    NOT NULL,
    "username_id" INTEGER NOT NULL,
    FOREIGN KEY (username_id) references user (id)
);

CREATE TABLE "wishlist"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "username_id" INTEGER NOT NULL,
    "item_id"     INTEGER NOT NULL,
    FOREIGN KEY (username_id) references user (id),
    FOREIGN KEY (item_id) references item (id)

);

CREATE TABLE "order"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "payment"     INTEGER NOT NULL,
    FOREIGN KEY (payment) references payment (id)
);

# --- !Downs

DROP TABLE "category";
DROP TABLE "item";
DROP TABLE "account";
DROP TABLE "cart";
DROP TABLE "shipping";
DROP TABLE "payment";
DROP TABLE "rate";
DROP TABLE "user";
DROP TABLE "wishlist";
DROP TABLE "order";
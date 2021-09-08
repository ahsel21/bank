CREATE TABLE CLIENT
(
    ClientId    BIGINT NOT NULL PRIMARY KEY,
    FullName    VARCHAR(70),
    PhoneNumber VARCHAR(11),
    Email       VARCHAR(40),
    PassportId  VARCHAR(6)
);
CREATE TABLE CREDIT
(
    CREDITID     BIGINT NOT NULL PRIMARY KEY,
    LIMIT        BIGINT,
    INTERESTRATE DOUBLE,
    NAME         VARCHAR(50)
);
CREATE TABLE CREDITOFFER
(
    CREDITOFFERID BIGINT NOT NULL PRIMARY KEY,
    CLIENTID      BIGINT,
    CREDITID      BIGINT,
    BANKID        BIGINT,
    AMOUNT           BIGINT,
    MONTHCOUNT      BIGINT

);


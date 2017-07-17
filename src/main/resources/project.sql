CREATE TABLE users (
    telephone varchar(11) PRIMARY KEY,
    pwd varchar(80) NOT NULL
);

CREATE TABLE user_balance (
    telephone varchar(11) references users(telephone),
    balance numeric(5, 2) NOT NULL
);
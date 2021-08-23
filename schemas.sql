DROP DATABASE IF EXISTS bank_accounts;

CREATE DATABASE bank_accounts
    OWNER postgres
    ENCODING 'UTF8';

DROP TABLE IF EXISTS clients, investments, credit_card, account;

CREATE TABLE clients ();
CREATE TABLE investments ();
CREATE TABLE credit_card ();
CREATE TABLE account ();
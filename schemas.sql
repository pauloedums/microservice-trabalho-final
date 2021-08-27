DROP DATABASE IF EXISTS bank_accounts;
DROP DATABASE IF EXISTS clients_credit;
DROP DATABASE IF EXISTS clients_debit;
DROP DATABASE IF EXISTS client_extract_balance;


CREATE DATABASE bank_accounts
    OWNER postgres
    ENCODING 'UTF8';

CREATE DATABASE clients_credit
    OWNER postgres
    ENCODING 'UTF8';


CREATE DATABASE clients_debit
    OWNER postgres
    ENCODING 'UTF8';


CREATE DATABASE client_extract_balance
    OWNER postgres
    ENCODING 'UTF8';
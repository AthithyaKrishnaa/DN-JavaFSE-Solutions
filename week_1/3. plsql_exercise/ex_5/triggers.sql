-- ============================================================
-- Exercise 5: Triggers
-- ============================================================

-- ------------------------------------------------------------
-- Schema and sample data
-- ------------------------------------------------------------
CREATE TABLE Customers (
    CustomerID NUMBER PRIMARY KEY,
    Name VARCHAR2(100),
    DOB DATE,
    Balance NUMBER,
    LastModified DATE
);

CREATE TABLE Accounts (
    AccountID NUMBER PRIMARY KEY,
    CustomerID NUMBER,
    AccountType VARCHAR2(20),
    Balance NUMBER,
    LastModified DATE
);

CREATE TABLE Transactions (
    TransactionID NUMBER PRIMARY KEY,
    AccountID NUMBER,
    TransactionDate DATE,
    Amount NUMBER,
    TransactionType VARCHAR2(10)
);

CREATE TABLE AuditLog (
    AuditID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    TransactionID NUMBER,
    AccountID NUMBER,
    Amount NUMBER,
    TransactionType VARCHAR2(10),
    LoggedOn DATE DEFAULT SYSDATE
);

INSERT INTO Customers VALUES (1, 'John Doe', TO_DATE('1985-05-15', 'YYYY-MM-DD'), 1000, SYSDATE);
INSERT INTO Accounts VALUES (1, 1, 'Savings', 1000, SYSDATE);
COMMIT;

-- ------------------------------------------------------------
-- Scenario 1: Auto-update LastModified when a customer record
-- is updated.
-- ------------------------------------------------------------
CREATE OR REPLACE TRIGGER UpdateCustomerLastModified
BEFORE UPDATE ON Customers
FOR EACH ROW
BEGIN
    :NEW.LastModified := SYSDATE;
END UpdateCustomerLastModified;
/

-- ------------------------------------------------------------
-- Scenario 2: Maintain an audit log for all transactions
-- ------------------------------------------------------------
CREATE OR REPLACE TRIGGER LogTransaction
AFTER INSERT ON Transactions
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TransactionID, AccountID, Amount, TransactionType)
    VALUES (:NEW.TransactionID, :NEW.AccountID, :NEW.Amount, :NEW.TransactionType);
END LogTransaction;
/

-- ------------------------------------------------------------
-- Scenario 3: Enforce business rules on deposits and withdrawals
-- ------------------------------------------------------------
CREATE OR REPLACE TRIGGER CheckTransactionRules
BEFORE INSERT ON Transactions
FOR EACH ROW
DECLARE
    v_balance NUMBER;
BEGIN
    SELECT Balance INTO v_balance FROM Accounts WHERE AccountID = :NEW.AccountID;

    IF :NEW.TransactionType = 'Deposit' AND :NEW.Amount <= 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Deposit amount must be positive.');
    END IF;

    IF :NEW.TransactionType = 'Withdrawal' AND :NEW.Amount > v_balance THEN
        RAISE_APPLICATION_ERROR(-20002, 'Withdrawal amount exceeds account balance.');
    END IF;
END CheckTransactionRules;
/

-- ------------------------------------------------------------
-- Test block
-- ------------------------------------------------------------
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Scenario 1: Update a customer record ---');
    UPDATE Customers SET Balance = 1200 WHERE CustomerID = 1;
    DBMS_OUTPUT.PUT_LINE('Customer 1 balance updated. LastModified auto-set by trigger.');
    COMMIT;
END;
/

BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Scenario 2 & 3: Insert a valid deposit ---');
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES (1, 1, SYSDATE, 200, 'Deposit');
    UPDATE Accounts SET Balance = Balance + 200 WHERE AccountID = 1;
    DBMS_OUTPUT.PUT_LINE('Deposit of 200 recorded and logged to AuditLog.');
    COMMIT;
END;
/

BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Scenario 3: Attempt an over-withdrawal (should fail) ---');
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES (2, 1, SYSDATE, 5000, 'Withdrawal');
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        ROLLBACK;
END;
/

BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Scenario 3: Valid withdrawal within balance ---');
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES (3, 1, SYSDATE, 100, 'Withdrawal');
    UPDATE Accounts SET Balance = Balance - 100 WHERE AccountID = 1;
    DBMS_OUTPUT.PUT_LINE('Withdrawal of 100 recorded and logged to AuditLog.');
    COMMIT;
END;
/

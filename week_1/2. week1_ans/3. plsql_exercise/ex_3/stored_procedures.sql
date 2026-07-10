-- ============================================================
-- Exercise 3: Stored Procedures
-- ============================================================

-- ------------------------------------------------------------
-- Schema and sample data
-- ------------------------------------------------------------
CREATE TABLE Accounts (
    AccountID NUMBER PRIMARY KEY,
    CustomerID NUMBER,
    AccountType VARCHAR2(20),
    Balance NUMBER,
    LastModified DATE
);

CREATE TABLE Employees (
    EmployeeID NUMBER PRIMARY KEY,
    Name VARCHAR2(100),
    Position VARCHAR2(50),
    Salary NUMBER,
    Department VARCHAR2(50),
    HireDate DATE
);

INSERT INTO Accounts VALUES (1, 1, 'Savings', 1000, SYSDATE);
INSERT INTO Accounts VALUES (2, 2, 'Checking', 1500, SYSDATE);
INSERT INTO Accounts VALUES (3, 3, 'Savings', 25000, SYSDATE);
INSERT INTO Accounts VALUES (4, 4, 'Savings', 8500, SYSDATE);

INSERT INTO Employees VALUES (1, 'Alice Johnson', 'Manager', 70000, 'HR', TO_DATE('2015-06-15', 'YYYY-MM-DD'));
INSERT INTO Employees VALUES (2, 'Bob Brown', 'Developer', 60000, 'IT', TO_DATE('2017-03-20', 'YYYY-MM-DD'));
INSERT INTO Employees VALUES (3, 'Carol White', 'Developer', 65000, 'IT', TO_DATE('2018-09-01', 'YYYY-MM-DD'));

COMMIT;

-- ------------------------------------------------------------
-- Scenario 1: Process monthly interest for all savings accounts
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE ProcessMonthlyInterest IS
    v_rate CONSTANT NUMBER := 0.01; -- 1% monthly interest
    v_count NUMBER := 0;
BEGIN
    FOR acc_rec IN (SELECT AccountID, Balance FROM Accounts WHERE AccountType = 'Savings') LOOP
        UPDATE Accounts
           SET Balance = Balance + (Balance * v_rate),
               LastModified = SYSDATE
         WHERE AccountID = acc_rec.AccountID;

        v_count := v_count + 1;
        DBMS_OUTPUT.PUT_LINE('Account ' || acc_rec.AccountID || ': interest of ' ||
                              ROUND(acc_rec.Balance * v_rate, 2) || ' applied. New balance: ' ||
                              ROUND(acc_rec.Balance * (1 + v_rate), 2));
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('Monthly interest processed for ' || v_count || ' savings account(s).');
    COMMIT;
END ProcessMonthlyInterest;
/

BEGIN
    ProcessMonthlyInterest;
END;
/

-- ------------------------------------------------------------
-- Scenario 2: Apply performance bonus to a department's employees
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE UpdateEmployeeBonus (
    p_department  IN VARCHAR2,
    p_bonus_pct   IN NUMBER
) IS
    v_count NUMBER := 0;
BEGIN
    FOR emp_rec IN (SELECT EmployeeID, Name, Salary FROM Employees WHERE Department = p_department) LOOP
        UPDATE Employees
           SET Salary = Salary + (Salary * p_bonus_pct / 100)
         WHERE EmployeeID = emp_rec.EmployeeID;

        v_count := v_count + 1;
        DBMS_OUTPUT.PUT_LINE(emp_rec.Name || ': bonus of ' || p_bonus_pct ||
                              '% applied. New salary: ' ||
                              ROUND(emp_rec.Salary * (1 + p_bonus_pct / 100), 2));
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('Bonus applied to ' || v_count || ' employee(s) in ' || p_department || '.');
    COMMIT;
END UpdateEmployeeBonus;
/

BEGIN
    UpdateEmployeeBonus('IT', 5);
END;
/

-- ------------------------------------------------------------
-- Scenario 3: Transfer funds between accounts with balance check
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE TransferFunds (
    p_from_account IN NUMBER,
    p_to_account   IN NUMBER,
    p_amount       IN NUMBER
) IS
    v_balance NUMBER;
BEGIN
    SELECT Balance INTO v_balance FROM Accounts WHERE AccountID = p_from_account;

    IF v_balance >= p_amount THEN
        UPDATE Accounts SET Balance = Balance - p_amount WHERE AccountID = p_from_account;
        UPDATE Accounts SET Balance = Balance + p_amount WHERE AccountID = p_to_account;
        DBMS_OUTPUT.PUT_LINE('Transferred ' || p_amount || ' from Account ' ||
                              p_from_account || ' to Account ' || p_to_account || '.');
        COMMIT;
    ELSE
        DBMS_OUTPUT.PUT_LINE('Transfer failed: Account ' || p_from_account ||
                              ' has insufficient balance (' || v_balance || ').');
    END IF;
END TransferFunds;
/

BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Test 1: Sufficient balance ---');
    TransferFunds(2, 1, 300);

    DBMS_OUTPUT.PUT_LINE('--- Test 2: Insufficient balance ---');
    TransferFunds(2, 1, 50000);
END;
/

-- ============================================================
-- Exercise 6: Cursors
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

CREATE TABLE Loans (
    LoanID NUMBER PRIMARY KEY,
    CustomerID NUMBER,
    LoanAmount NUMBER,
    InterestRate NUMBER,
    StartDate DATE,
    EndDate DATE
);

INSERT INTO Customers VALUES (1, 'John Doe', TO_DATE('1985-05-15', 'YYYY-MM-DD'), 1000, SYSDATE);
INSERT INTO Customers VALUES (2, 'Jane Smith', TO_DATE('1990-07-20', 'YYYY-MM-DD'), 1500, SYSDATE);

INSERT INTO Accounts VALUES (1, 1, 'Savings', 1000, SYSDATE);
INSERT INTO Accounts VALUES (2, 2, 'Checking', 1500, SYSDATE);

INSERT INTO Transactions VALUES (1, 1, SYSDATE, 200, 'Deposit');
INSERT INTO Transactions VALUES (2, 2, SYSDATE, 300, 'Withdrawal');
INSERT INTO Transactions VALUES (3, 1, SYSDATE - 45, 150, 'Deposit'); -- last month, excluded

INSERT INTO Loans VALUES (1, 1, 5000, 5, SYSDATE, ADD_MONTHS(SYSDATE, 60));
INSERT INTO Loans VALUES (2, 2, 10000, 6.5, SYSDATE, ADD_MONTHS(SYSDATE, 36));

COMMIT;

-- ------------------------------------------------------------
-- Scenario 1: Generate monthly statements using an explicit
-- cursor.
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE GenerateMonthlyStatements IS
    CURSOR c_statements IS
        SELECT c.Name, t.TransactionID, t.Amount, t.TransactionType, t.TransactionDate
          FROM Transactions t
          JOIN Accounts a ON a.AccountID = t.AccountID
          JOIN Customers c ON c.CustomerID = a.CustomerID
         WHERE TRUNC(t.TransactionDate, 'MM') = TRUNC(SYSDATE, 'MM')
         ORDER BY c.Name, t.TransactionID;

    v_rec c_statements%ROWTYPE;
BEGIN
    OPEN c_statements;
    LOOP
        FETCH c_statements INTO v_rec;
        EXIT WHEN c_statements%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Statement - ' || v_rec.Name || ': Txn #' || v_rec.TransactionID ||
                              ' (' || v_rec.TransactionType || ') of ' || v_rec.Amount ||
                              ' on ' || TO_CHAR(v_rec.TransactionDate, 'YYYY-MM-DD'));
    END LOOP;
    CLOSE c_statements;
END GenerateMonthlyStatements;
/

BEGIN
    GenerateMonthlyStatements;
END;
/

-- ------------------------------------------------------------
-- Scenario 2: Apply an annual maintenance fee to all accounts
-- using an explicit cursor.
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE ApplyAnnualFee IS
    v_fee CONSTANT NUMBER := 25;

    CURSOR c_accounts IS
        SELECT AccountID, Balance FROM Accounts FOR UPDATE;
BEGIN
    FOR acc_rec IN c_accounts LOOP
        UPDATE Accounts
           SET Balance = Balance - v_fee
         WHERE AccountID = acc_rec.AccountID;

        DBMS_OUTPUT.PUT_LINE('Account ' || acc_rec.AccountID || ': annual fee of ' || v_fee ||
                              ' deducted. New balance: ' || (acc_rec.Balance - v_fee));
    END LOOP;
    COMMIT;
END ApplyAnnualFee;
/

BEGIN
    ApplyAnnualFee;
END;
/

-- ------------------------------------------------------------
-- Scenario 3: Update loan interest rates based on a new policy
-- using an explicit cursor.
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE UpdateLoanInterestRates IS
    CURSOR c_loans IS
        SELECT LoanID, InterestRate FROM Loans FOR UPDATE;

    v_new_rate NUMBER;
BEGIN
    FOR loan_rec IN c_loans LOOP
        -- New policy: increase all rates by 0.5 percentage points
        v_new_rate := loan_rec.InterestRate + 0.5;

        UPDATE Loans
           SET InterestRate = v_new_rate
         WHERE LoanID = loan_rec.LoanID;

        DBMS_OUTPUT.PUT_LINE('Loan ' || loan_rec.LoanID || ': rate updated from ' ||
                              loan_rec.InterestRate || '% to ' || v_new_rate || '%.');
    END LOOP;
    COMMIT;
END UpdateLoanInterestRates;
/

BEGIN
    UpdateLoanInterestRates;
END;
/

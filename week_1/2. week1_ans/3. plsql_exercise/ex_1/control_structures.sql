-- ============================================================
-- Exercise 1: Control Structures
-- ============================================================

-- ------------------------------------------------------------
-- Schema and sample data (shared across all PL/SQL exercises)
-- ------------------------------------------------------------
CREATE TABLE Customers (
    CustomerID NUMBER PRIMARY KEY,
    Name VARCHAR2(100),
    DOB DATE,
    Balance NUMBER,
    LastModified DATE,
    IsVIP CHAR(1) DEFAULT 'N'
);

CREATE TABLE Loans (
    LoanID NUMBER PRIMARY KEY,
    CustomerID NUMBER,
    LoanAmount NUMBER,
    InterestRate NUMBER,
    StartDate DATE,
    EndDate DATE,
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
);

INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (1, 'John Doe', TO_DATE('1985-05-15', 'YYYY-MM-DD'), 1000, SYSDATE);

INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (2, 'Jane Smith', TO_DATE('1990-07-20', 'YYYY-MM-DD'), 1500, SYSDATE);

INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (3, 'Robert Lee', TO_DATE('1958-03-02', 'YYYY-MM-DD'), 25000, SYSDATE);

INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (4, 'Maria Gomez', TO_DATE('1962-11-30', 'YYYY-MM-DD'), 8500, SYSDATE);

INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (1, 1, 5000, 5, SYSDATE, ADD_MONTHS(SYSDATE, 60));

INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (2, 3, 10000, 6.5, SYSDATE, ADD_MONTHS(SYSDATE, 36));

INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (3, 4, 7500, 4.25, SYSDATE, SYSDATE + 20);

COMMIT;

-- ------------------------------------------------------------
-- Scenario 1: Apply a 1% discount to loan interest rates for
-- customers above 60 years old.
-- ------------------------------------------------------------
DECLARE
    v_age NUMBER;
BEGIN
    FOR cust_rec IN (SELECT CustomerID, Name, DOB FROM Customers) LOOP
        v_age := TRUNC(MONTHS_BETWEEN(SYSDATE, cust_rec.DOB) / 12);

        IF v_age > 60 THEN
            UPDATE Loans
               SET InterestRate = InterestRate - (InterestRate * 0.01)
             WHERE CustomerID = cust_rec.CustomerID;

            DBMS_OUTPUT.PUT_LINE(cust_rec.Name || ' (Age ' || v_age ||
                                  ') is above 60. 1% interest discount applied.');
        ELSE
            DBMS_OUTPUT.PUT_LINE(cust_rec.Name || ' (Age ' || v_age ||
                                  ') is not eligible for the senior discount.');
        END IF;
    END LOOP;

    COMMIT;
END;
/

-- ------------------------------------------------------------
-- Scenario 2: Promote customers with balance over $10,000 to
-- VIP status.
-- ------------------------------------------------------------
DECLARE
    v_count NUMBER := 0;
BEGIN
    FOR cust_rec IN (SELECT CustomerID, Name, Balance FROM Customers) LOOP
        IF cust_rec.Balance > 10000 THEN
            UPDATE Customers
               SET IsVIP = 'Y'
             WHERE CustomerID = cust_rec.CustomerID;

            v_count := v_count + 1;
            DBMS_OUTPUT.PUT_LINE(cust_rec.Name || ' (Balance $' || cust_rec.Balance ||
                                  ') promoted to VIP.');
        ELSE
            DBMS_OUTPUT.PUT_LINE(cust_rec.Name || ' (Balance $' || cust_rec.Balance ||
                                  ') does not qualify for VIP status.');
        END IF;
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('Total customers promoted to VIP: ' || v_count);
    COMMIT;
END;
/

-- ------------------------------------------------------------
-- Scenario 3: Send reminders for loans due within the next 30
-- days.
-- ------------------------------------------------------------
DECLARE
    v_found BOOLEAN := FALSE;
BEGIN
    FOR loan_rec IN (
        SELECT l.LoanID, c.Name, l.EndDate
          FROM Loans l
          JOIN Customers c ON c.CustomerID = l.CustomerID
         WHERE l.EndDate BETWEEN SYSDATE AND SYSDATE + 30
    ) LOOP
        v_found := TRUE;
        DBMS_OUTPUT.PUT_LINE('Reminder: Loan #' || loan_rec.LoanID || ' for ' ||
                              loan_rec.Name || ' is due on ' ||
                              TO_CHAR(loan_rec.EndDate, 'YYYY-MM-DD') || '.');
    END LOOP;

    IF NOT v_found THEN
        DBMS_OUTPUT.PUT_LINE('No loans are due within the next 30 days.');
    END IF;
END;
/

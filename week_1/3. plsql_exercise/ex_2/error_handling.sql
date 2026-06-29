-- ============================================================
-- Exercise 2: Error Handling
-- ============================================================

-- ------------------------------------------------------------
-- Additional schema needed for this exercise
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

CREATE TABLE Customers (
    CustomerID NUMBER PRIMARY KEY,
    Name VARCHAR2(100),
    DOB DATE,
    Balance NUMBER,
    LastModified DATE
);

CREATE TABLE ErrorLog (
    LogID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ProcedureName VARCHAR2(50),
    ErrorMessage VARCHAR2(500),
    LoggedOn DATE DEFAULT SYSDATE
);

INSERT INTO Accounts VALUES (1, 1, 'Savings', 1000, SYSDATE);
INSERT INTO Accounts VALUES (2, 2, 'Checking', 1500, SYSDATE);

INSERT INTO Employees VALUES (1, 'Alice Johnson', 'Manager', 70000, 'HR', TO_DATE('2015-06-15', 'YYYY-MM-DD'));
INSERT INTO Employees VALUES (2, 'Bob Brown', 'Developer', 60000, 'IT', TO_DATE('2017-03-20', 'YYYY-MM-DD'));

INSERT INTO Customers VALUES (1, 'John Doe', TO_DATE('1985-05-15', 'YYYY-MM-DD'), 1000, SYSDATE);
INSERT INTO Customers VALUES (2, 'Jane Smith', TO_DATE('1990-07-20', 'YYYY-MM-DD'), 1500, SYSDATE);

COMMIT;

-- ------------------------------------------------------------
-- Scenario 1: Safe fund transfer between accounts
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE SafeTransferFunds (
    p_from_account IN NUMBER,
    p_to_account   IN NUMBER,
    p_amount       IN NUMBER
) IS
    v_from_balance NUMBER;
    e_insufficient_funds EXCEPTION;
BEGIN
    SELECT Balance INTO v_from_balance
      FROM Accounts
     WHERE AccountID = p_from_account
     FOR UPDATE;

    IF v_from_balance < p_amount THEN
        RAISE e_insufficient_funds;
    END IF;

    UPDATE Accounts SET Balance = Balance - p_amount WHERE AccountID = p_from_account;
    UPDATE Accounts SET Balance = Balance + p_amount WHERE AccountID = p_to_account;

    DBMS_OUTPUT.PUT_LINE('Transferred ' || p_amount || ' from Account ' ||
                          p_from_account || ' to Account ' || p_to_account || '.');
    COMMIT;

EXCEPTION
    WHEN e_insufficient_funds THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: Insufficient funds in Account ' || p_from_account || '.');
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage)
        VALUES ('SafeTransferFunds', 'Insufficient funds in Account ' || p_from_account);
        COMMIT;
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: One of the accounts does not exist.');
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage)
        VALUES ('SafeTransferFunds', 'Account not found: ' || p_from_account || ' or ' || p_to_account);
        COMMIT;
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage) VALUES ('SafeTransferFunds', SQLERRM);
        COMMIT;
END SafeTransferFunds;
/

-- Test calls
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Test 1: Valid transfer ---');
    SafeTransferFunds(1, 2, 200);

    DBMS_OUTPUT.PUT_LINE('--- Test 2: Insufficient funds ---');
    SafeTransferFunds(1, 2, 50000);

    DBMS_OUTPUT.PUT_LINE('--- Test 3: Non-existent account ---');
    SafeTransferFunds(99, 2, 100);
END;
/

-- ------------------------------------------------------------
-- Scenario 2: Update employee salary with error handling
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE UpdateSalary (
    p_employee_id IN NUMBER,
    p_percentage  IN NUMBER
) IS
    v_current_salary NUMBER;
BEGIN
    SELECT Salary INTO v_current_salary
      FROM Employees
     WHERE EmployeeID = p_employee_id;

    UPDATE Employees
       SET Salary = Salary + (Salary * p_percentage / 100)
     WHERE EmployeeID = p_employee_id;

    DBMS_OUTPUT.PUT_LINE('Salary for Employee ' || p_employee_id || ' increased by ' ||
                          p_percentage || '%.');
    COMMIT;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Error: Employee ID ' || p_employee_id || ' does not exist.');
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage)
        VALUES ('UpdateSalary', 'Employee not found: ' || p_employee_id);
        COMMIT;
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage) VALUES ('UpdateSalary', SQLERRM);
        COMMIT;
END UpdateSalary;
/

-- Test calls
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Test 1: Valid employee ---');
    UpdateSalary(1, 10);

    DBMS_OUTPUT.PUT_LINE('--- Test 2: Invalid employee ID ---');
    UpdateSalary(999, 10);
END;
/

-- ------------------------------------------------------------
-- Scenario 3: Add a new customer with duplicate-ID handling
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE AddNewCustomer (
    p_customer_id IN NUMBER,
    p_name        IN VARCHAR2,
    p_dob         IN DATE,
    p_balance     IN NUMBER
) IS
BEGIN
    INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
    VALUES (p_customer_id, p_name, p_dob, p_balance, SYSDATE);

    DBMS_OUTPUT.PUT_LINE('Customer ' || p_customer_id || ' (' || p_name || ') added successfully.');
    COMMIT;

EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE('Error: Customer with ID ' || p_customer_id || ' already exists.');
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage)
        VALUES ('AddNewCustomer', 'Duplicate CustomerID: ' || p_customer_id);
        COMMIT;
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage) VALUES ('AddNewCustomer', SQLERRM);
        COMMIT;
END AddNewCustomer;
/

-- Test calls
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Test 1: New unique customer ---');
    AddNewCustomer(3, 'Robert Lee', TO_DATE('1958-03-02', 'YYYY-MM-DD'), 25000);

    DBMS_OUTPUT.PUT_LINE('--- Test 2: Duplicate customer ID ---');
    AddNewCustomer(1, 'Duplicate John', TO_DATE('1985-05-15', 'YYYY-MM-DD'), 500);
END;
/

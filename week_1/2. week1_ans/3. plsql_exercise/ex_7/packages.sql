-- ============================================================
-- Exercise 7: Packages
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

CREATE TABLE Employees (
    EmployeeID NUMBER PRIMARY KEY,
    Name VARCHAR2(100),
    Position VARCHAR2(50),
    Salary NUMBER,
    Department VARCHAR2(50),
    HireDate DATE
);

INSERT INTO Customers VALUES (1, 'John Doe', TO_DATE('1985-05-15', 'YYYY-MM-DD'), 1000, SYSDATE);
INSERT INTO Accounts VALUES (1, 1, 'Savings', 1000, SYSDATE);
INSERT INTO Accounts VALUES (2, 1, 'Checking', 500, SYSDATE);
COMMIT;

-- ------------------------------------------------------------
-- Scenario 1: CustomerManagement package
-- ------------------------------------------------------------
CREATE OR REPLACE PACKAGE CustomerManagement AS
    PROCEDURE AddCustomer(p_id IN NUMBER, p_name IN VARCHAR2, p_dob IN DATE, p_balance IN NUMBER);
    PROCEDURE UpdateCustomerDetails(p_id IN NUMBER, p_name IN VARCHAR2, p_balance IN NUMBER);
    FUNCTION GetCustomerBalance(p_id IN NUMBER) RETURN NUMBER;
END CustomerManagement;
/

CREATE OR REPLACE PACKAGE BODY CustomerManagement AS

    PROCEDURE AddCustomer(p_id IN NUMBER, p_name IN VARCHAR2, p_dob IN DATE, p_balance IN NUMBER) IS
    BEGIN
        INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
        VALUES (p_id, p_name, p_dob, p_balance, SYSDATE);
        DBMS_OUTPUT.PUT_LINE('Customer ' || p_id || ' (' || p_name || ') added.');
    END AddCustomer;

    PROCEDURE UpdateCustomerDetails(p_id IN NUMBER, p_name IN VARCHAR2, p_balance IN NUMBER) IS
    BEGIN
        UPDATE Customers
           SET Name = p_name, Balance = p_balance, LastModified = SYSDATE
         WHERE CustomerID = p_id;
        DBMS_OUTPUT.PUT_LINE('Customer ' || p_id || ' updated: Name=' || p_name ||
                              ', Balance=' || p_balance);
    END UpdateCustomerDetails;

    FUNCTION GetCustomerBalance(p_id IN NUMBER) RETURN NUMBER IS
        v_balance NUMBER;
    BEGIN
        SELECT Balance INTO v_balance FROM Customers WHERE CustomerID = p_id;
        RETURN v_balance;
    END GetCustomerBalance;

END CustomerManagement;
/

-- ------------------------------------------------------------
-- Scenario 2: EmployeeManagement package
-- ------------------------------------------------------------
CREATE OR REPLACE PACKAGE EmployeeManagement AS
    PROCEDURE HireEmployee(p_id IN NUMBER, p_name IN VARCHAR2, p_position IN VARCHAR2,
                            p_salary IN NUMBER, p_dept IN VARCHAR2);
    PROCEDURE UpdateEmployeeDetails(p_id IN NUMBER, p_position IN VARCHAR2, p_salary IN NUMBER);
    FUNCTION CalculateAnnualSalary(p_id IN NUMBER) RETURN NUMBER;
END EmployeeManagement;
/

CREATE OR REPLACE PACKAGE BODY EmployeeManagement AS

    PROCEDURE HireEmployee(p_id IN NUMBER, p_name IN VARCHAR2, p_position IN VARCHAR2,
                            p_salary IN NUMBER, p_dept IN VARCHAR2) IS
    BEGIN
        INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
        VALUES (p_id, p_name, p_position, p_salary, p_dept, SYSDATE);
        DBMS_OUTPUT.PUT_LINE('Employee ' || p_id || ' (' || p_name || ') hired as ' || p_position ||
                              ' in ' || p_dept || '.');
    END HireEmployee;

    PROCEDURE UpdateEmployeeDetails(p_id IN NUMBER, p_position IN VARCHAR2, p_salary IN NUMBER) IS
    BEGIN
        UPDATE Employees
           SET Position = p_position, Salary = p_salary
         WHERE EmployeeID = p_id;
        DBMS_OUTPUT.PUT_LINE('Employee ' || p_id || ' updated: Position=' || p_position ||
                              ', Salary=' || p_salary);
    END UpdateEmployeeDetails;

    FUNCTION CalculateAnnualSalary(p_id IN NUMBER) RETURN NUMBER IS
        v_monthly_salary NUMBER;
    BEGIN
        SELECT Salary INTO v_monthly_salary FROM Employees WHERE EmployeeID = p_id;
        RETURN v_monthly_salary * 12;
    END CalculateAnnualSalary;

END EmployeeManagement;
/

-- ------------------------------------------------------------
-- Scenario 3: AccountOperations package
-- ------------------------------------------------------------
CREATE OR REPLACE PACKAGE AccountOperations AS
    PROCEDURE OpenAccount(p_account_id IN NUMBER, p_customer_id IN NUMBER,
                           p_type IN VARCHAR2, p_balance IN NUMBER);
    PROCEDURE CloseAccount(p_account_id IN NUMBER);
    FUNCTION GetTotalBalance(p_customer_id IN NUMBER) RETURN NUMBER;
END AccountOperations;
/

CREATE OR REPLACE PACKAGE BODY AccountOperations AS

    PROCEDURE OpenAccount(p_account_id IN NUMBER, p_customer_id IN NUMBER,
                           p_type IN VARCHAR2, p_balance IN NUMBER) IS
    BEGIN
        INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastModified)
        VALUES (p_account_id, p_customer_id, p_type, p_balance, SYSDATE);
        DBMS_OUTPUT.PUT_LINE('Account ' || p_account_id || ' (' || p_type ||
                              ') opened for Customer ' || p_customer_id || '.');
    END OpenAccount;

    PROCEDURE CloseAccount(p_account_id IN NUMBER) IS
    BEGIN
        DELETE FROM Accounts WHERE AccountID = p_account_id;
        DBMS_OUTPUT.PUT_LINE('Account ' || p_account_id || ' closed.');
    END CloseAccount;

    FUNCTION GetTotalBalance(p_customer_id IN NUMBER) RETURN NUMBER IS
        v_total NUMBER;
    BEGIN
        SELECT SUM(Balance) INTO v_total FROM Accounts WHERE CustomerID = p_customer_id;
        RETURN NVL(v_total, 0);
    END GetTotalBalance;

END AccountOperations;
/

-- ------------------------------------------------------------
-- Test block exercising all three packages
-- ------------------------------------------------------------
DECLARE
    v_balance NUMBER;
    v_annual_salary NUMBER;
    v_total NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Scenario 1: CustomerManagement ---');
    CustomerManagement.AddCustomer(2, 'Jane Smith', TO_DATE('1990-07-20', 'YYYY-MM-DD'), 1500);
    CustomerManagement.UpdateCustomerDetails(1, 'John A. Doe', 1100);
    v_balance := CustomerManagement.GetCustomerBalance(1);
    DBMS_OUTPUT.PUT_LINE('Customer 1 balance via package function: ' || v_balance);

    DBMS_OUTPUT.PUT_LINE('--- Scenario 2: EmployeeManagement ---');
    EmployeeManagement.HireEmployee(1, 'Alice Johnson', 'Manager', 7000, 'HR');
    EmployeeManagement.UpdateEmployeeDetails(1, 'Senior Manager', 7500);
    v_annual_salary := EmployeeManagement.CalculateAnnualSalary(1);
    DBMS_OUTPUT.PUT_LINE('Employee 1 annual salary via package function: ' || v_annual_salary);

    DBMS_OUTPUT.PUT_LINE('--- Scenario 3: AccountOperations ---');
    AccountOperations.OpenAccount(3, 1, 'Fixed Deposit', 2000);
    v_total := AccountOperations.GetTotalBalance(1);
    DBMS_OUTPUT.PUT_LINE('Total balance across all accounts for Customer 1: ' || v_total);
    AccountOperations.CloseAccount(2);
    v_total := AccountOperations.GetTotalBalance(1);
    DBMS_OUTPUT.PUT_LINE('Total balance for Customer 1 after closing Account 2: ' || v_total);

    COMMIT;
END;
/

-- ============================================================
-- Exercise 4: Functions
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

INSERT INTO Accounts VALUES (1, 1, 'Savings', 1000, SYSDATE);
INSERT INTO Accounts VALUES (2, 2, 'Checking', 1500, SYSDATE);
COMMIT;

-- ------------------------------------------------------------
-- Scenario 1: Calculate a customer's age from date of birth
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION CalculateAge (
    p_dob IN DATE
) RETURN NUMBER IS
    v_age NUMBER;
BEGIN
    v_age := TRUNC(MONTHS_BETWEEN(SYSDATE, p_dob) / 12);
    RETURN v_age;
END CalculateAge;
/

-- ------------------------------------------------------------
-- Scenario 2: Calculate the monthly installment (EMI) for a loan
-- EMI = P * r * (1 + r)^n / ((1 + r)^n - 1)
--   P = principal, r = monthly interest rate, n = number of months
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION CalculateMonthlyInstallment (
    p_loan_amount    IN NUMBER,
    p_annual_rate    IN NUMBER,  -- annual interest rate in %
    p_years          IN NUMBER
) RETURN NUMBER IS
    v_monthly_rate NUMBER;
    v_months       NUMBER;
    v_emi          NUMBER;
BEGIN
    v_monthly_rate := (p_annual_rate / 100) / 12;
    v_months       := p_years * 12;

    IF v_monthly_rate = 0 THEN
        v_emi := p_loan_amount / v_months;
    ELSE
        v_emi := p_loan_amount * v_monthly_rate * POWER(1 + v_monthly_rate, v_months)
                 / (POWER(1 + v_monthly_rate, v_months) - 1);
    END IF;

    RETURN ROUND(v_emi, 2);
END CalculateMonthlyInstallment;
/

-- ------------------------------------------------------------
-- Scenario 3: Check whether an account has sufficient balance
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION HasSufficientBalance (
    p_account_id IN NUMBER,
    p_amount     IN NUMBER
) RETURN BOOLEAN IS
    v_balance NUMBER;
BEGIN
    SELECT Balance INTO v_balance FROM Accounts WHERE AccountID = p_account_id;
    RETURN (v_balance >= p_amount);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN FALSE;
END HasSufficientBalance;
/

-- ------------------------------------------------------------
-- Test block calling all three functions
-- ------------------------------------------------------------
DECLARE
    v_age NUMBER;
    v_emi NUMBER;

    FUNCTION bool_to_str(b BOOLEAN) RETURN VARCHAR2 IS
    BEGIN
        IF b THEN RETURN 'TRUE'; ELSE RETURN 'FALSE'; END IF;
    END;
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Scenario 1: CalculateAge ---');
    v_age := CalculateAge(TO_DATE('1985-05-15', 'YYYY-MM-DD'));
    DBMS_OUTPUT.PUT_LINE('Age for DOB 1985-05-15: ' || v_age);

    v_age := CalculateAge(TO_DATE('1958-03-02', 'YYYY-MM-DD'));
    DBMS_OUTPUT.PUT_LINE('Age for DOB 1958-03-02: ' || v_age);

    DBMS_OUTPUT.PUT_LINE('--- Scenario 2: CalculateMonthlyInstallment ---');
    v_emi := CalculateMonthlyInstallment(5000, 5, 5);
    DBMS_OUTPUT.PUT_LINE('EMI for loan of 5000 at 5% for 5 years: ' || v_emi);

    v_emi := CalculateMonthlyInstallment(10000, 6.5, 3);
    DBMS_OUTPUT.PUT_LINE('EMI for loan of 10000 at 6.5% for 3 years: ' || v_emi);

    DBMS_OUTPUT.PUT_LINE('--- Scenario 3: HasSufficientBalance ---');
    DBMS_OUTPUT.PUT_LINE('Account 1 has sufficient balance for 500? ' ||
                          bool_to_str(HasSufficientBalance(1, 500)));
    DBMS_OUTPUT.PUT_LINE('Account 1 has sufficient balance for 5000? ' ||
                          bool_to_str(HasSufficientBalance(1, 5000)));
    DBMS_OUTPUT.PUT_LINE('Account 99 (non-existent) has sufficient balance for 100? ' ||
                          bool_to_str(HasSufficientBalance(99, 100)));
END;
/

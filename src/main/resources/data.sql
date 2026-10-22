BEGIN;

-- Insert addresses, including street names
INSERT INTO address (address_line, suburb, postal_code)
SELECT s.address_line, s.suburb, s.postal_code
FROM (
    VALUES
        ('Rosebank Mall, Bath Avenue', 'Rosebank, Johannesburg', '2196'),
        ('Sandton City, 83 Rivonia Road', 'Sandhurst, Sandton', '2196'),
        ('Benmore Gardens Shopping Centre, Grayston Drive', 'Benmore Gardens, Sandton', '2196'),
        ('Centurion Mall, Gordon Hood Road', 'Centurion Central, Centurion', '0157'),
        ('Centurion Lifestyle Centre, Lenchen Avenue', 'Centurion', '0157'),
        ('Mall of Africa, Magwa Crescent', 'Waterfall City, Midrand', '1686'),
        ('Quagga Centre, Court Street', 'Pretoria West, Pretoria', '0183'),
        ('Lyttleton Centre, Botha Avenue', 'Lyttelton Manor, Centurion', '0157')
) AS s(address_line, suburb, postal_code)
WHERE NOT EXISTS (
    SELECT 1
    FROM address a
    WHERE a.address_line = s.address_line
      AND a.suburb = s.suburb
      AND a.postal_code = s.postal_code
);

-- Insert branches using shop numbers as codes
INSERT INTO branch (code, address_id, active)
SELECT s.code, a.address_id, TRUE
FROM (
    VALUES
        ('131B-132A', 'Rosebank Mall, Bath Avenue', 'Rosebank, Johannesburg', '2196'),
        ('L14E', 'Sandton City, 83 Rivonia Road', 'Sandhurst, Sandton', '2196'),
        ('G39', 'Benmore Gardens Shopping Centre, Grayston Drive', 'Benmore Gardens, Sandton', '2196'),
        ('116B', 'Centurion Mall, Gordon Hood Road', 'Centurion Central, Centurion', '0157'),
        ('F2', 'Centurion Lifestyle Centre, Lenchen Avenue', 'Centurion', '0157'),
        ('1041A', 'Mall of Africa, Magwa Crescent', 'Waterfall City, Midrand', '1686'),
        ('24', 'Quagga Centre, Court Street', 'Pretoria West, Pretoria', '0183'),
        ('D09', 'Lyttleton Centre, Botha Avenue', 'Lyttelton Manor, Centurion', '0157')
) AS s(code, address_line, suburb, postal_code)
JOIN address a
    ON a.address_line = s.address_line
   AND a.suburb = s.suburb
   AND a.postal_code = s.postal_code
WHERE NOT EXISTS (
    SELECT 1
    FROM branch b
    WHERE b.code = s.code
);

-- 1. SERVICE CATEGORIES
INSERT INTO service_category (name)
SELECT v.name
FROM (VALUES
          ('Insure'),
          ('Transact'),
          ('Credit'),
          ('Save'),
          ('Business Inquiry')
     ) AS v(name)
WHERE NOT EXISTS (
    SELECT 1
    FROM service_category sc
    WHERE LOWER(sc.name) = LOWER(v.name)
);

-- 2. PRODUCTS
INSERT INTO product (category_id, name)
SELECT sc.category_id, v.product_name
FROM (
         VALUES
             ('Insure', 'Life Cover'),
             ('Insure', 'Funeral Cover'),
             ('Transact', 'Global One Account'),
             ('Transact', 'Card Transaction Query'),
             ('Transact', 'Cash Deposit or Withdrawal Dispute'),
             ('Transact', 'Debit Order Dispute or Amendment'),
             ('Credit', 'Credit Card'),
             ('Credit', 'Access Facility'),
             ('Credit', 'Personal Loan'),
             ('Credit', 'Home Loan'),
             ('Save', 'Savings Plan'),
             ('Save', 'Savings Options'),
             ('Business Inquiry', 'Business Banking')
     ) AS v(category_name, product_name)
         JOIN service_category sc
              ON LOWER(sc.name) = LOWER(v.category_name)
WHERE NOT EXISTS (
    SELECT 1
    FROM product p
    WHERE p.category_id = sc.category_id
      AND LOWER(p.name) = LOWER(v.product_name)
);

COMMIT;

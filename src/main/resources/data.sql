BEGIN;

-- Insert addresses
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

-- Insert branches
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

-- 1. INSERT SERVICE CATEGORIES
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

-- 2. INSERT PRODUCTS
INSERT INTO product (category_id, name)
SELECT sc.category_id, v.product_name
FROM (
         VALUES
             ('Insure', 'Life Cover'),
             ('Insure', 'Funeral Cover'),
             ('Transact', 'Global One Account'),
             ('Credit', 'Credit Card'),
             ('Credit', 'Access Facility'),
             ('Credit', 'Personal Loan'),
             ('Credit', 'Home Loan'),
             ('Save', 'Savings Plan'),
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

-- 2. INSERT SERVICE TYPE
INSERT INTO service_type (product_id, name)
SELECT p.product_id, v.service_type_name
FROM (
         VALUES
             ('Life Cover', 'New Policy'),
             ('Life Cover', 'Policy Enquiry'),
             ('Life Cover', 'Claim Enquiry'),
             ('Funeral Cover', 'New Policy'),
             ('Funeral Cover', 'Policy Enquiry'),
             ('Funeral Cover', 'Claim Enquiry'),
             ('Global One Account', 'Open New Account'),
             ('Global One Account', 'Account Enquiry'),
             ('Credit Card', 'New Application'),
             ('Credit Card', 'Account Enquiry'),
             ('Access Facility', 'New Application'),
             ('Access Facility', 'Account Enquiry'),
             ('Personal Loan', 'New Application'),
             ('Personal Loan', 'Account Enquiry'),
             ('Home Loan', 'New Application'),
             ('Home Loan', 'Account Enquiry'),
             ('Savings Plan', 'Open Savings Plan'),
             ('Business Banking', 'Business Product Enquiry'),
             ('Business Banking', 'Other Business Assistance')
     ) AS v(product_name, service_type_name)
         INNER JOIN product p ON p.name = v.product_name
WHERE NOT EXISTS (
    SELECT 1 FROM service_type st
    WHERE st.product_id = p.product_id
      AND st.name = v.service_type_name
);

-- 4. SERVICE REASONS - Using explicit JOINs
INSERT INTO service_reason (service_type_id, description)
SELECT st.service_type_id, v.reason_name
FROM (
  VALUES
    ('Life Cover', 'Claim Enquiry', 'Accidental Death'),
    ('Life Cover', 'Claim Enquiry', 'Natural Death'),
    ('Funeral Cover', 'Policy Enquiry', 'Debit Order Not Taking Place'),
    ('Personal Loan', 'New Application', 'Home Improvements'),
    ('Personal Loan', 'New Application', 'Vehicle'),
    ('Personal Loan', 'New Application', 'Consolidation'),
    ('Personal Loan', 'New Application', 'Education'),
    ('Savings Plan', 'Open Savings Plan', 'Open Savings Plan')
) AS v(product_name, service_type_name, reason_name)
INNER JOIN product p ON p.name = v.product_name
INNER JOIN service_type st ON st.product_id = p.product_id AND st.name = v.service_type_name
WHERE NOT EXISTS (
    SELECT 1 FROM service_reason sr
    WHERE sr.service_type_id = st.service_type_id
      AND sr.description = v.reason_name
);

-- 5. APPOINTMENT SLOTS
INSERT INTO appointment_slot
(branch_id, start_time, end_time, capacity)
SELECT
    b.branch_id,
    (d.slot_date + t.slot_time)::timestamp,
    (d.slot_date + t.slot_time + INTERVAL '30 minutes')::timestamp,
    1
FROM branch b
         CROSS JOIN generate_series(
                CURRENT_DATE,
                CURRENT_DATE + 14,
                INTERVAL '1 day'
                    ) AS d(slot_date)
         CROSS JOIN (
    VALUES
        (TIME '09:00'),
        (TIME '10:00'),
        (TIME '11:00'),
        (TIME '12:00'),
        (TIME '14:00'),
        (TIME '15:00'),
        (TIME '16:00'),
        (TIME '17:00')
) AS t(slot_time)
WHERE b.active = TRUE
  AND EXTRACT(ISODOW FROM d.slot_date) BETWEEN 1 AND 5
  AND NOT EXISTS (
    SELECT 1
    FROM appointment_slot existing
    WHERE existing.branch_id = b.branch_id
      AND existing.start_time =
          (d.slot_date + t.slot_time)::timestamp
);

COMMIT;

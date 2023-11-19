IF NOT EXISTS(
    SELECT
        *
    FROM
        sys.databases
    WHERE
        name = 'tacocloud'
) BEGIN CREATE DATABASE [tacocloud]
END
GO
    USE [tacocloud]
GO
--     --You need to check if the table exists
--     CREATE TABLE IF NOT EXISTS Taco_Order (
--         id IDENTITY,
--         delivery_Name VARCHAR(100) NOT NULL,
--         delivery_Street VARCHAR(100) NOT NULL,
--         delivery_City VARCHAR(100) NOT NULL,
--         delivery_State VARCHAR(10) NOT NULL,
--         delivery_Zip VARCHAR(10) NOT NULL,
--         cc_Number VARCHAR(16) NOT NULL,
--         cc_Expiration VARCHAR(10) NOT NULL,
--         cc_Cvv VARCHAR(10) NOT NULL,
--         place_At TIMESTAMP NOT NULL,
--     );

-- CREATE TABLE IF NOT EXISTS Taco(
--     id IDENTITY,
--     taco_Name VARCHAR(50) NOT NULL,
--     taco_Order BIGINT NOT NULL,
--     taco_Order_Key BIGINT NOT NULL,
--     create_At TIMESTAMP NOT NULL
-- );

-- CREATE TABLE IF NOT EXISTS Ingredient_Ref(
--     ingredient VARCHAR(4) NOT NULL,
--     taco BIGINT NOT NULL,
--     taco_Key BIGINT NOT NULL
-- );

-- CREATE TABLE IF NOT EXISTS Ingredient(
--     id VARCHAR(4) NOT NULL,
--     ingredient_Name VARCHAR(25) NOT NULL,
--     ingredient_Type VARCHAR(10) NOT NULL
-- );

-- ALTER TABLE
--     TACO
-- ADD
--     FOREIGN KEY (taco_Order) REFERENCES Taco_Order(id);

-- ALTER TABLE Ingredient_Ref
-- ADD FOREIGN KEY (ingredient) REFERENCES Ingredient(id)
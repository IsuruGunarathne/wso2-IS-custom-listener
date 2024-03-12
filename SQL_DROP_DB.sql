USE master;
GO

-- Drop the database if it exists
IF EXISTS (SELECT 1 FROM sys.databases WHERE name = 'wso2is')
    DROP DATABASE wso2is;
GO

-- Create the database
CREATE DATABASE wso2is;
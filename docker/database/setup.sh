#!/bin/bash
echo "******CREATING DOCKER DATABASE******"

gosu postgres postgres --single <<- EOSQL
   CREATE DATABASE rest_api;
   CREATE ROLE dbuser WITH LOGIN PASSWORD 'dbpass';
   GRANT ALL PRIVILEGES ON DATABASE rest_api TO dbuser;
EOSQL

echo ""

echo "******DOCKER DATABASE CREATED******"

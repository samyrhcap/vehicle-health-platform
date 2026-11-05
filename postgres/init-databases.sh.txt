#!/bin/bash

set -e

echo "Waiting for PostgreSQL..."

until pg_isready -h postgres -U postgres
do
  sleep 2
done

echo "Ensuring databases exist..."

psql -h postgres -U postgres -d postgres <<EOF

SELECT 'CREATE DATABASE vehicle_telemetry_db'
WHERE NOT EXISTS (
    SELECT FROM pg_database
    WHERE datname = 'vehicle_telemetry_db'
)\gexec

SELECT 'CREATE DATABASE vehicle_analysis_db'
WHERE NOT EXISTS (
    SELECT FROM pg_database
    WHERE datname = 'vehicle_analysis_db'
)\gexec

EOF

echo "Database initialization completed."
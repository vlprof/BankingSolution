psql -v ON_ERROR_STOP=1 -v db="$DATABASE_NAME" -v user="$POSTGRES_USER" --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE :"db" TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C';
    GRANT ALL PRIVILEGES ON DATABASE :"db" TO :"user";
EOSQL
#wait for the SQL Server to come up
sleep 30s

/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P superSecure123 -d master -i init.sql

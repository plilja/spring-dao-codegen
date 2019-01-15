FROM microsoft/mssql-server-linux:latest
ENV ACCEPT_EULA Y
ENV SA_PASSWORD superSecure123
ENV MSSQL_SA_PASSWORD superSecure123
ENV MSSQL_PID Express
COPY init.sql .
COPY import-data.sh .
COPY entrypoint.sh .
RUN chmod +x ./import-data.sh
CMD /bin/bash ./entrypoint.sh

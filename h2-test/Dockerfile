FROM postgres:11.1
ENV POSTGRES_USER docker
ENV POSTGRES_PASSWORD docker
ENV POSTGRES_DB docker
COPY init.sql /docker-entrypoint-initdb.d/10-init.sql

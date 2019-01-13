FROM mysql:8.0
ENV MYSQL_USER docker
ENV MYSQL_PASSWORD docker
ENV MYSQL_DATABASE docker
ENV MYSQL_ROOT_PASSWORD docker
COPY init.sql /docker-entrypoint-initdb.d/10-init.sql

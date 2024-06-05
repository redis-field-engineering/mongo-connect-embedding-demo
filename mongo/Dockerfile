FROM mongo

RUN mkdir -p /data/keyfile && chmod 700 /data/keyfile

COPY keyfile/mongo-keyfile /data/keyfile

RUN chmod 600 /data/keyfile/mongo-keyfile && chown -R mongodb:mongodb /data/keyfile

EXPOSE 27017

CMD ["mongod", "--replSet", "rs", "--keyFile", "/data/keyfile/mongo-keyfile", "--bind_ip_all", "sslMode", "disabled"]
FROM pomerium/pomerium:v0.17.2 as pomerium

FROM homecentr/base:3.1.2-alpine

ENV AUTOCERT_DIR=/data/autocert
ENV POMERIUM_ARGS='-config /config/config.yml'

COPY --from=pomerium /bin/pomerium* /bin/
COPY --from=pomerium /pomerium/config.yaml /pomerium/config.yaml

RUN apk add --no-cache \
        libc6-compat=1.2.2-r3 \
        gcompat=1.0.0-r2 \
        ca-certificates=20191127-r5 \
        libcap=2.50-r0 && \
        # Grant the pomerium process to open a well-known port (1-1024) which normally requires root permissions
        setcap 'cap_net_bind_service=+ep' /bin/pomerium

COPY ./fs /
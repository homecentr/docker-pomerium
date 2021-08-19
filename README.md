[![Project status](https://badgen.net/badge/project%20status/stable%20%26%20actively%20maintaned?color=green)](https://github.com/homecentr/docker-pomerium/graphs/commit-activity) [![](https://badgen.net/github/label-issues/homecentr/docker-pomerium/bug?label=open%20bugs&color=green)](https://github.com/homecentr/docker-pomerium/labels/bug) [![](https://badgen.net/github/release/homecentr/docker-pomerium)](https://hub.docker.com/repository/docker/homecentr/pomerium)
[![](https://badgen.net/docker/pulls/homecentr/pomerium)](https://hub.docker.com/repository/docker/homecentr/pomerium) 
[![](https://badgen.net/docker/size/homecentr/pomerium)](https://hub.docker.com/repository/docker/homecentr/pomerium)

![CI/CD on master](https://github.com/homecentr/docker-pomerium/workflows/CI/CD%20on%20master/badge.svg)


# Homecentr - pomerium

## Usage

Please see the [docker-compose file](./docker-compose.yml).

## Environment variables

| Name | Default value | Description |
|------|---------------|-------------|
| PUID | `7077` | UID of the user pomerium should be running as. |
| PGID | `7077` | GID of the user pomerium should be running as. |
| POMERIUM_ARGS | `-config /config/config.yml` | Command line arguments passed to the Pomerium executable. |

## Exposed ports

The container does not explicitly expose any ports because the port on which Pomerium listens depends on the configuration.

## Volumes

| Container path | Description |
|------------|---------------|
| /config | The Pomerium config is expected to be in this directory by default. |

## Security
The container is regularly scanned for vulnerabilities and updated. Further info can be found in the [Security tab](https://github.com/homecentr/docker-pomerium/security).

### Container user
The container supports privilege drop. Even though the container starts as root, it will use the permissions only to perform the initial set up. The pomerium process runs as UID/GID provided in the PUID and PGID environment variables.

:warning: Do not change the container user directly using the `user` Docker compose property or using the `--user` argument. This would break the privilege drop logic.
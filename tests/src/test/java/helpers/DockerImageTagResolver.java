package helpers;

import io.homecentr.testcontainers.images.EnvironmentImageTagResolver;

public class DockerImageTagResolver extends EnvironmentImageTagResolver {
  public DockerImageTagResolver() {
    super("homecentr/pomerium:local");
  }
}
import helpers.DockerImageTagResolver;
import io.homecentr.testcontainers.containers.GenericContainerEx;
import io.homecentr.testcontainers.containers.HttpResponse;
import io.homecentr.testcontainers.containers.ProcessNotFoundException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.LogUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class PomeriumContainerRunningAsRootShould {
    private static final Logger logger = LoggerFactory.getLogger(PomeriumContainerRunningAsRootShould.class);

    private static GenericContainerEx _container;

    @BeforeClass
    public static void setUp() {
        Path configPath = Paths.get(System.getProperty("user.dir"), "src/test/resources/config.yml").normalize();

        _container = new GenericContainerEx<>(new DockerImageTagResolver())
                .withFileSystemBind(configPath.toString(), "/config/config.yml")
                .withEnv("IDP_CLIENT_ID", "dummy")
                .withEnv("IDP_CLIENT_SECRET", "dummy")
                .withEnv("IDP_PROVIDER_URL", "https://dummy")
                .withEnv("PUID", "0")
                .withEnv("PGID", "0")
                .withExposedPorts(80)
                // Waits for log message because waiting for a port does not work in containers without bash
                .waitingFor(Wait.forLogMessage(".*using in-memory registry.*", 1));

        _container.start();
        LogUtils.followOutput(DockerClientFactory.instance().client(), _container.getContainerId(), new Slf4jLogConsumer(logger));
    }

    @AfterClass
    public static void cleanUp() {
        _container.close();
    }

    @Test
    public void returnKeysOnConfiguredPort() throws Exception {
        HttpResponse response = _container.makeHttpRequest(80, "/.well-known/pomerium/jwks.json");

        assertEquals(200, response.getResponseCode());
    }

    @Test
    public void executePomeriumAsPassedPuid() throws InterruptedException, ProcessNotFoundException, IOException {
        int uid = _container.getProcessUid("/bin/pomerium -config /config/config.yml");

        assertEquals(0, uid);
    }

    @Test
    public void executePomeriumAsPassedPgid() throws InterruptedException, ProcessNotFoundException, IOException {
        int gid = _container.getProcessGid("/bin/pomerium -config /config/config.yml");

        assertEquals(0, gid);
    }
}
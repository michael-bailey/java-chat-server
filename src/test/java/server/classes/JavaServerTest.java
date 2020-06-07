package server.classes;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class JavaServerTest {

    public static JavaServer server;

    @BeforeClass
    public static void setup() throws IOException {
        server = new JavaServer();
    }

    @Test
    public void canStartup() {
        assertTrue(server.start());
    }
}

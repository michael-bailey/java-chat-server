package io.github.michael_bailey.java_server.Protocol;

import org.junit.Test;
import static org.junit.Assert.*;
import static io.github.michael_bailey.java_server.Protocol.Command.*;

public class CommandTest {
    @Test
    public void checkIfoCommand() {
        var command = new Command(INFO);
        var message = Command.valueOf(command.toString());
        assertEquals(INFO, message.command);
    }
}

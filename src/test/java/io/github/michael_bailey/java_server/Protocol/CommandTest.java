package io.github.michael_bailey.java_server.Protocol;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;
import static io.github.michael_bailey.java_server.Protocol.Command.*;

public class CommandTest {
    @Test
    public void checkInfoCommand() {
        var command = new Command(INFO);
        var message = Command.valueOf(command.toString());
        assertEquals(INFO, message.command);
    }

    @Test
    public void checkParameters() {
        var parameters = new HashMap<String, String>();
        parameters.put("hello", "world");
        parameters.put("hallo", "welt");

        var command = new Command(SUCCESS, parameters);
        System.out.println("command.toString() = " + command.toString());
        var message = Command.valueOf(command.toString());
        assertEquals(SUCCESS, message.command);
        assertEquals("world", message.getParam("hello"));
        assertEquals("welt", message.getParam("hallo"));
    }
}

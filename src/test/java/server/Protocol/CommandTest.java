package server.Protocol;

import org.junit.Test;
import static org.junit.Assert.*;
import static server.Protocol.Command.*;

public class CommandTest {
    @Test
    public void checkIfoCommand() {
        var command = new Command(INFO);
        var message = Command.valueOf(command.toString());
        assertEquals(INFO, message.command);
    }
}

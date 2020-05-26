package server.delegates;

import server.classes.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class JavaServerDelegate implements IJavaServerDelegate {

    @Override
    public void updatedClientList(ArrayList<Worker> clients) {
        System.out.println("received update client list");
        System.out.println(clients.stream().map((x) -> "worker: " + x.getUUID()).collect(Collectors.toList()).toString());
    }
}

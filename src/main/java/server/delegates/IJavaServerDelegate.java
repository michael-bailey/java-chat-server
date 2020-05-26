package server.delegates;

import server.classes.Worker;

import java.util.ArrayList;

public interface IJavaServerDelegate {
    void updatedClientList(ArrayList<Worker> clients);
}

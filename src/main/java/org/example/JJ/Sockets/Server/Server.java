package org.example.JJ.Sockets.Server;

import java.rmi.RemoteException;

public class Server {
    public static void main(String[] args) throws RemoteException {
        String mysqlUrl = "jdbc:mysql://localhost:3306/testdb";
        String mysqlUser = "root";
        String mysqlPassword = "Gerodot5";
        RemoteServerRepository repository = new RemoteServerRepository(innerRepository, 8008);
        repository.start();
    }
}

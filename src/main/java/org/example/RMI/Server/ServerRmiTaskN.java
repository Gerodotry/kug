package org.example.RMI.Server;

import org.example.JJ.SQL.SQLask;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRmiTaskN {
    public static void main(String[] args) {
        try {

            String mysqlUrl = "jdbc:mysql://localhost:3306/testdb";
            String mysqlUser = "root";
            String mysqlPassword = "Gerodot5";


            SQLask interfaceSQL = new SQLask(mysqlUrl, mysqlUser, mysqlPassword);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("interfaceSQL", interfaceSQL);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
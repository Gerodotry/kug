package org.example.RMI.Client;

import org.example.JJ.Models.Concert;
import org.example.JJ.Models.Artist;
import org.example.JJ.SQL.interfaceSQL;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRmiTaskN {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            interfaceSQL interfaceSQL = (interfaceSQL) registry.lookup("interfaceSQL");

            System.out.println("Concerts: " + interfaceSQL.countConcerts());
            System.out.println("Artists: " + interfaceSQL.countArtist());

            Concert concert = new Concert();
            concert.setName("Calculus");
            concert.setDate(20);
            concert.setVenue("London");
            interfaceSQL.insertConcert(concert);

            Artist artist = new Artist();
            artist.setName("dew");
            artist.setGenre("rock");
            interfaceSQL.insertArtist(1, artist);

            System.out.println("Concert: " + interfaceSQL.getConcert(1));
            System.out.println("Artist: " + interfaceSQL.getArtist(1));

            System.out.println("Concerts: " + interfaceSQL.getConcerts());
            System.out.println("Artists: " + interfaceSQL.getArtists());
        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}

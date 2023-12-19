package org.example.JJ.SQL;

import org.example.JJ.Models.Artist;
import org.example.JJ.Models.Concert;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface interfaceSQL extends Remote {
    int countConcerts() throws RemoteException;
    int countArtist() throws RemoteException;

    void updateConcert(Concert concert) throws RemoteException;
    void updateArtist(Artist artist) throws RemoteException;

    void moveArtistToConcert(int studentId, int groupId) throws RemoteException;

    void insertConcert(Concert concert) throws RemoteException;
    void insertArtist(int groupId, Artist artist) throws RemoteException;
    List<Concert> getConcertsSortedByDate() throws RemoteException;
    void deleteConcert(int id) throws RemoteException;
    void deleteArtist(int id) throws RemoteException;

    Concert getConcert(int id) throws RemoteException;
    Artist getArtist(int id) throws RemoteException;

    List<Concert> getConcerts() throws RemoteException;
    List<Artist> getArtists() throws RemoteException;
}

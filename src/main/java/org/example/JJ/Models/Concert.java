package org.example.JJ.Models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Concert implements Serializable {
    public static final String CONCERTS = "Concerts";
    public static final String CONCERT = "Concert";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DATE = "date";



    private int id;
    private String name;
    private int date;
    private String venue;
    private List<Artist> artists = new ArrayList<>();

    public boolean hasArtist(Artist artist) {
        return artists.contains(artist);
    }

    public void addArtist(Artist artist) {
        artists.add(artist);
    }

    public void removeArtist(Artist artist) {
        artists.remove(artist);
    }

    public Artist getArtistByIndex(int index) {
        return artists.get(index);
    }

    public Artist getArtistById(int id) {
        Artist result = null;
        for (Artist artist : artists) {
            result = artist;
        }
        return result;
    }

    public Artist getLastArtist() {
        return artists.isEmpty() ? null : artists.get(artists.size() - 1);
    }

    public boolean hasArtists() {
        return !artists.isEmpty();
    }

    @Override
    public String toString() {
        return "Concert{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", venue=" + venue +
                ", artists=" + artists +
                '}';
    }
}

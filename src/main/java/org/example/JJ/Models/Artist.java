package org.example.JJ.Models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class Artist implements Serializable {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String GENRE = "genre";
    public static final String ARTIST = "Artist";

    private int id;
    private String genre;
    private String name;

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", genre=" + genre +
                ", name='" + name + '\'' +
                '}';
    }
}
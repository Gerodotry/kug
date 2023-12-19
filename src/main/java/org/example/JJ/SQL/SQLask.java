package org.example.JJ.SQL;

import org.example.JJ.Models.Artist;
import org.example.JJ.Models.Concert;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLask extends UnicastRemoteObject implements interfaceSQL {
    private Connection connection;

    public SQLask(String url, String user, String password) throws RemoteException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countConcerts() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) as count FROM concerts";

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }


    @Override
    public int countArtist() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) as count FROM artists";

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public void updateConcert(Concert concert) {
        try {
            Concert item = getConcert(concert.getId());
            if (item != null) {
                Statement statement = connection.createStatement();
                String query = "UPDATE concerts SET name='" + concert.getName() + "', date=" + concert.getDate() + " WHERE id=" + concert.getId();
                statement.execute(query);

                for (Artist artist : item.getArtists()) {
                    if (!concert.hasArtist(artist)) {
                        deleteArtist(artist.getId());
                    }
                }
                for (Artist artist : concert.getArtists()) {
                    if (!item.hasArtist(artist)) {
                        insertArtist(concert.getId(), artist);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateArtist(Artist artist) {
        try {
            Statement statement = connection.createStatement();
            String query = "UPDATE artists SET name='" + artist.getName() + "', genre=" + artist.getGenre() + " WHERE id=" + artist.getId();
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void moveArtistToConcert(int studentId, int groupId) {
        try {
            if (getConcert(groupId) != null) {
                Statement statement = connection.createStatement();
                String query = "UPDATE artists SET concert_id=" + groupId + " WHERE id=" + studentId;
                statement.execute(query);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertConcert(Concert concert) {
        try {
            Statement statement = connection.createStatement();
            String groupQuery = "INSERT INTO concerts(name, date) VALUES('" + concert.getName() + "'," + concert.getDate() + ")";
            statement.execute(groupQuery);

            String findQuery = "SELECT * FROM concerts WHERE name='" + concert.getName() + "' AND date=" + concert.getDate();
            ResultSet resultSet = statement.executeQuery(findQuery);
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                for (Artist artist : concert.getArtists()) {
                    String studentQuery = "INSERT INTO artists(concert_id, name, genre) VALUES(" + id + ",'" + artist.getName() + "'," + artist.getGenre() + ")";
                    statement.execute(studentQuery);
                }
            }
        } catch (SQLException e) {
            System.out.println("Cannot add concert as it already exists");
        }
    }

    @Override
    public void insertArtist(int concertId, Artist artist) {
        try {
            Statement statement = connection.createStatement();
            String studentsQuery = "INSERT INTO artists(concert_id, name, genre) VALUES(" + concertId + ",'" + artist.getName() + "'," + artist.getGenre() + ")";
            statement.execute(studentsQuery);
        } catch (SQLException e) {
            System.out.println("Cannot add artist as it already exists");
        }
    }
    @Override
    public List<Concert> getConcertsSortedByDate() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM concerts AS g " +
                    "LEFT JOIN (SELECT id as artist_id, concert_id as id, name as student_name, genre as student_age FROM artists) AS s " +
                    "USING (id) " +
                    "ORDER BY g.date";
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteConcert(int id) {
        try {
            Statement statement = connection.createStatement();
            String studentsQuery = "DELETE FROM artists WHERE concert_id = " + id;
            statement.execute(studentsQuery);
            String groupQuery = "DELETE FROM concerts WHERE id = " + id;
            statement.execute(groupQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteArtist(int id) {
        try {
            Statement statement = connection.createStatement();
            String studentQuery = "DELETE FROM artists WHERE id = " + id;
            statement.execute(studentQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Concert getConcert(int id) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM concerts " +
                    "LEFT JOIN (SELECT id as artist_id, concert_id as id, name as student_name, genre as student_age FROM artists) AS s " +
                    "USING (id) " +
                    "WHERE id = " + id;

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Concert concert = new Concert();
                concert.setId(resultSet.getInt("id"));
                concert.setName(resultSet.getString("name"));
                concert.setDate(resultSet.getInt("date"));
                concert.setVenue(resultSet.getString("venue"));
                do {
                    Artist artist = new Artist();
                    artist.setId(resultSet.getInt("artist_id"));
                    artist.setName(resultSet.getString("student_name"));
                    artist.setGenre(resultSet.getString("student_age"));
                    concert.addArtist(artist);
                } while (resultSet.next());

                return concert;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    @Override
    public Artist getArtist(int id) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM artists WHERE id=" + id;

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Artist artist = new Artist();
                artist.setId(resultSet.getInt("id"));
                artist.setName(resultSet.getString("name"));
                artist.setGenre(resultSet.getString("genre"));
                return artist;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Concert> getConcerts() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM concerts AS g " +
                    "LEFT JOIN (SELECT id as artist_id, concert_id as id, name as student_name, genre as student_age FROM artists) AS s " +
                    "USING (id)";

            List<Concert> concerts = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Concert concert = null;

                do {
                    if (concert == null || resultSet.getInt("id") != concert.getId()) {
                        if (concert != null) {
                            concerts.add(concert);
                        }

                        concert = new Concert();
                        concert.setId(resultSet.getInt("id"));
                        concert.setName(resultSet.getString("name"));
                        concert.setDate(resultSet.getInt("date"));
                    }

                    Artist artist = new Artist();
                    artist.setId(resultSet.getInt("artist_id"));
                    if (resultSet.wasNull()) {
                        continue;
                    }
                    artist.setName(resultSet.getString("student_name"));
                    artist.setGenre(resultSet.getString("student_age"));
                    concert.addArtist(artist);
                } while (resultSet.next());

                concerts.add(concert);
            }

            return concerts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Artist> getArtists() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM artists";

            List<Artist> artists = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Artist artist = new Artist();
                artist.setId(resultSet.getInt("id"));
                artist.setName(resultSet.getString("name"));
                artist.setGenre(resultSet.getString("genre"));
                artists.add(artist);
            }

            return artists;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

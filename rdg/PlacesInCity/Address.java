package rdg.PlacesInCity;

import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Address {
    private Integer id;
    private String street;
    private int place_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public void insert() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO " +
                        "addresses (street, place_id) VALUES (?,?) ON CONFLICT do nothing",
                Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, street);
            s.setInt(2, place_id);
            s.executeUpdate();
            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void delete() throws SQLException {

        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM addresses WHERE id = ?")) {
            s.setInt(1, id);
            s.executeUpdate();
        }

    }

}

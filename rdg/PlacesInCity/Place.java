package rdg.PlacesInCity;

import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Place {

    private Integer id;
    private String name_city_district;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName_city_district() {
        return name_city_district;
    }

    public void setName_city_district(String name_city_district) {
        this.name_city_district = name_city_district;
    }

    public void insert() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO " +
                        "places (name_city_district) VALUES (?) ON CONFLICT do nothing",
                Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, name_city_district);
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

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM places WHERE id = ?")) {
            s.setInt(1, id);
            s.executeUpdate();
        }

    }

}

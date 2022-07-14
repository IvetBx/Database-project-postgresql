package rdg.PlacesInCity;

import DbConnect.DbContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaceFinder {

    private static final PlaceFinder INSTANCE = new PlaceFinder();

    public static PlaceFinder getInstance() { return INSTANCE; }

    private PlaceFinder(){

    }

    public Place findById(int id) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM places WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Place p = new Place();

                    p.setId(r.getInt("id"));
                    p.setName_city_district(r.getString("name_city_district"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return p;

                } else {
                    return null;
                }
            }
        }
    }

}

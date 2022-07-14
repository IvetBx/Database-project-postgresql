package rdg.PlacesInCity;

import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressFinder {

    private static final AddressFinder INSTANCE = new AddressFinder();

    public static AddressFinder getInstance() { return INSTANCE; }

    private AddressFinder(){

    }

    public Address findById(int id) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM addresses WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Address a = new Address();

                    a.setId(r.getInt("id"));
                    a.setStreet(r.getString("street"));
                    a.setPlace_id(r.getInt("place_id"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return a;

                } else {
                    return null;
                }
            }
        }
    }

}

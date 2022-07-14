package rdg.Person;

import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleFinder {


    private static final RoleFinder INSTANCE = new RoleFinder();

    public static RoleFinder getInstance() { return INSTANCE; }

    private RoleFinder(){

    }

    public Role findById(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM roles WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {

                    Role role = new Role();
                    role.setId(r.getInt("id"));
                    role.setType(r.getString("type"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return role;
                } else {
                    return null;
                }
            }
        }
    }

    public Role findByType(String type) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM roles WHERE type = ?")) {
            s.setString(1, type);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {

                    Role role = new Role();
                    role.setId(r.getInt("id"));
                    role.setType(r.getString("type"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return role;
                } else {
                    return null;
                }
            }
        }
    }


}

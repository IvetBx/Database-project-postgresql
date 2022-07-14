package rdg.Person;

import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonFinder {

    private static final PersonFinder INSTANCE = new PersonFinder();

    public static PersonFinder getInstance() {
        return INSTANCE;
    }

    private PersonFinder(){

    }

    public Person findById(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM persons WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {

                    Person p = new Person();

                    p.setId(r.getInt("id"));
                    p.setFirst_name(r.getString("first_name"));
                    p.setLast_name(r.getString("last_name"));
                    p.setDate_of_birth(r.getDate("date_of_birth"));
                    p.setTel_number(r.getString("tel_number"));
                    p.setAddress_id(r.getInt("address_id"));

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

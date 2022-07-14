package rdg.Investigation;


import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryFinder {

    private static final CategoryFinder INSTANCE = new CategoryFinder();

    public static CategoryFinder getInstance() {
        return INSTANCE;
    }

    private CategoryFinder(){

    }

    public Category findById(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM categories WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {

                    Category c = new Category();

                    c.setId(r.getInt("id"));
                    c.setType_act(r.getString("type_act"));
                    c.setIs_crime(r.getBoolean("is_crime"));
                    c.setIs_misdemeanor(r.getBoolean("is_misdemeanor"));
                    c.setIs_protecting(r.getBoolean("is_protecting"));
                    c.setSerious(r.getInt("serious"));


                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return c;

                } else {
                    return null;
                }
            }
        }
    }

}

package rdg.Job;

import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JobFinder {

    private static final JobFinder INSTANCE = new JobFinder();

    public static JobFinder getInstance() {
        return INSTANCE;
    }

    private JobFinder(){

    }


    public Job findById(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM jobs WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {

                    Job j = new Job();

                    j.setId(r.getInt("id"));
                    j.setType(r.getString("type"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return j;
                } else {
                    return null;
                }
            }
        }
    }

    public Job findByType(String type) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM jobs WHERE type = lower(?)")) {
            s.setString(1, type);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Job j = new Job();

                    j.setId(r.getInt("id"));
                    j.setType(r.getString("type"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return j;

                } else {
                    return null;
                }
            }
        }
    }

}

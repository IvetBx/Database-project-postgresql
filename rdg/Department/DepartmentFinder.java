package rdg.Department;

import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentFinder {

    private static final DepartmentFinder INSTANCE = new DepartmentFinder();

    public static DepartmentFinder getInstance() { return INSTANCE; }

    private DepartmentFinder(){

    }


    public Department findById(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM departments WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {

                    Department d = new Department();
                    d.setId(r.getInt("id"));
                    d.setName(r.getString("name"));
                    d.setManager_id(r.getInt("manager_id"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return d;
                } else {
                    return null;
                }
            }
        }
    }

    public Department findByName(String name) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM departments WHERE name = lower(?)")) {
            s.setString(1, name);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Department d = new Department();

                    d.setId(r.getInt("id"));
                    d.setName(r.getString("name"));
                    d.setManager_id(r.getInt("manager_id"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return d;

                } else {
                    return null;
                }
            }
        }
    }

    public Department findByManagerId(int manager_id) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM departments WHERE manager_id = ?")) {
            s.setInt(1, manager_id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Department d = new Department();

                    d.setId(r.getInt("id"));
                    d.setName(r.getString("name"));
                    d.setManager_id(r.getInt("manager_id"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return d;

                } else {
                    return null;
                }
            }
        }
    }

    public List<Department> findAll(int page) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM departments OFFSET ? LIMIT 20")) {

            s.setInt(1, page);

            try (ResultSet r = s.executeQuery()) {

                List<Department> allDepartments = new ArrayList<>();

                while (r.next()) {
                    Department d = new Department();
                    d.setId(r.getInt("id"));
                    d.setName(r.getString("name"));
                    d.setManager_id(r.getInt("manager_id"));
                    allDepartments.add(d);
                }
                return allDepartments;
            }
        }
    }

}

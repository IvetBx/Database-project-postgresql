package rdg.Department;

import DbConnect.DbContext;
import rdg.Employee.EmployeeFinder;
import ui.Print.PrintDepartment;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Department {

    private Integer id;
    private String name;
    private int manager_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }


    public void insert() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO " +
                        "departments (name, manager_id) VALUES (?,?) ON CONFLICT do nothing",
                Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, name);
            s.setInt(2, manager_id);

            s.executeUpdate();
            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }

    }

    public void update() throws SQLException {

        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE " +
                "departments SET name = ?, manager_id = ? WHERE id = ?")) {
            s.setString(1, name);
            s.setInt(2, manager_id);
            s.setInt(3, id);

            s.executeUpdate();
        }

    }

    public void delete() throws SQLException {

        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM departments WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }

    }


    public static Department addDepartment(String name, int managerId) throws IOException, SQLException {

        Department d = new Department();
        d.setName(name);
        try {
            d.setManager_id(EmployeeFinder.getInstance().findById(managerId).getId());
        } catch (NullPointerException ex){
            throw new IllegalArgumentException("Employee with this id doesn't exist");
        }
        try {
            d.insert();
        } catch (SQLException ex){
            throw new IllegalStateException("This employee is already manager of another department");
        }
        return d;
    }

    public static void editDepartment(String name, String newName, int managerId) throws IOException, SQLException {
        Department d = DepartmentFinder.getInstance().findByName(name);

        if (d == null) {
            throw new IllegalArgumentException("No such department exists");
        }

        d.setName(newName);
        try {
            EmployeeFinder.getInstance().findById(managerId);
        } catch (NullPointerException ex){
            throw new IllegalArgumentException("Employee with this id doesn't exist");
        }
        d.setManager_id(managerId);
        try{
            d.update();
        } catch (SQLException ex){
            throw new IllegalArgumentException("This employee is already manager of another department");
        }

        PrintDepartment.getInstance().print(d);
    }
}

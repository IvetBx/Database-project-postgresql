package rdg.Investigation;

import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WorkOn {

    private Integer employee_id;
    private Integer investigation_id;

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getInvestigation_id() {
        return investigation_id;
    }

    public void setInvestigation_id(int investigation_id) {
        this.investigation_id = investigation_id;
    }

    public void insert() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO " +
                        "work_on (employee_id, investigation_id) VALUES (?,?)")) {
            s.setInt(1, employee_id);
            s.setInt(2, investigation_id);

            s.executeUpdate();
        }
    }

    public void delete() throws SQLException {

        if (employee_id == null || investigation_id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM work_on WHERE employee_id = ? AND investigation_id = ?")) {
            s.setInt(1, employee_id);
            s.setInt(2, investigation_id);
            s.executeUpdate();
        }

    }

    public static void addTuple(Integer emplId, Integer invest_id){
        WorkOn wo = new WorkOn();
        try {
            wo.setEmployee_id(emplId);
            wo.setInvestigation_id(invest_id);
            wo.insert();
        } catch (NullPointerException ex){
            throw new IllegalArgumentException("The employee doesn't exist");
        } catch (SQLException ex) {
            throw new IllegalArgumentException("The employee has been already working on this case");
        }
    }


}

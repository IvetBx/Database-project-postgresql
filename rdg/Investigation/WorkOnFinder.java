package rdg.Investigation;

import DbConnect.DbContext;
import rdg.Employee.Employee;
import rdg.Employee.EmployeeFinder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class WorkOnFinder {

    private static final WorkOnFinder INSTANCE = new WorkOnFinder();

    public static WorkOnFinder getInstance() {
        return INSTANCE;
    }

    private WorkOnFinder(){

    }

    public List<Investigation> findByEmployeeId(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM work_on WHERE employee_id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {

                List<Investigation> allInvestigations = new ArrayList<>();

                while (r.next()) {
                    Investigation i = InvestigationFinder.getInstance().findById(r.getInt("investigation_id"));
                    allInvestigations.add(i);
                }
                return allInvestigations;
            }
        }
    }

    public List<Employee> findByInvestigationId(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM work_on WHERE investigation_id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {

                List<Employee> allEmployees = new ArrayList<>();

                while (r.next()) {
                    Employee e = EmployeeFinder.getInstance().findById(r.getInt("employee_id"));
                    allEmployees.add(e);
                }
                return allEmployees;
            }
        }
    }

    public WorkOn findTupleEmployeeInvestigation (int employee_id, int investigation_id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM work_on WHERE employee_id = ? AND investigation_id = ?")) {
            s.setInt(1, employee_id);
            s.setInt(2, investigation_id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {

                    WorkOn w = new WorkOn();
                    w.setEmployee_id(r.getInt("employee_id"));
                    w.setInvestigation_id(r.getInt("investigation_id"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return w;

                } else {
                    return null;
                }
            }
        }
    }

}

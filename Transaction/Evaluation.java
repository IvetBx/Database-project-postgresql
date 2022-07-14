package Transaction;
import DbConnect.DbContext;
import rdg.Department.Department;
import rdg.Department.DepartmentFinder;
import rdg.Employee.Employee;
import rdg.Employee.EmployeeFinder;
import rdg.Job.JobFinder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Evaluation {

    public static List<List<Employee>> all = new ArrayList<>();

    public static void findPeopleToEvaluation() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT leaderAndManager.id AS leaderAndManager_id, leaderOnly.id AS leaderOnly_id FROM\n" +
                "(SELECT manager_id AS id, d.id AS department_id, COUNT(*) AS numberOfCases\n" +
                "FROM investigation AS i RIGHT JOIN departments AS d ON i.leader_id = d.manager_id AND i.resolved\n" +
                "GROUP BY manager_id, department_id) AS leaderAndManager\n" +
                "JOIN\n" +
                "(SELECT leader_id AS id, e.department_id AS department_id, count(*) AS numberOfCases\n" +
                "FROM investigation AS i JOIN employees AS e ON e.id = i.leader_id AND i.resolved\n" +
                "GROUP BY leader_id, department_id) AS leaderOnly\n" +
                "ON leaderAndManager.id != leaderOnly.id AND leaderAndManager.department_id = leaderOnly.department_id AND\n" +
                "leaderAndManager.numberOfCases < 1.2 * leaderOnly.numberOfCases")) {

            try (ResultSet r = s.executeQuery()) {

                while (r.next()) {
                    List<Employee> l = new ArrayList<>();
                    l.add(EmployeeFinder.getInstance().findById(r.getInt("leaderAndManager_id"))); //manager
                    l.add(EmployeeFinder.getInstance().findById(r.getInt("leaderOnly_id")));     //leader
                    all.add(l);
                }
            }
        }
    }

    public static boolean changeRank(Employee e2) throws SQLException {
        if("police officer".equals(JobFinder.getInstance().findById(e2.getJob_id()).getType())){
            int temp = e2.getRank() + 1;
            e2.setRank(temp);
            e2.update();
            return true;
        }
        return false;
    }

    public static void evaluate(Employee e1, Employee e2) {
        try {
            Department d = DepartmentFinder.getInstance().findByManagerId(e1.getId());
            try {
                d.setManager_id(e2.getId());
                d.update();
            } catch (SQLException ex) {
                throw new IllegalStateException("Already manager of another department");
            }
        } catch (NullPointerException ex) {
            throw new IllegalStateException();
        } catch (SQLException e) {
            throw new IllegalStateException("We can't find department");
        }
    }
}

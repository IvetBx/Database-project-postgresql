package rdg.Employee;

import DbConnect.DbContext;
import org.postgresql.util.PSQLException;
import rdg.Investigation.WorkOn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeFinder {

    private static final EmployeeFinder INSTANCE = new EmployeeFinder();

    public static EmployeeFinder getInstance() {
        return INSTANCE;
    }

    private EmployeeFinder(){

    }

    private Employee setValue(Integer id, String firstName, String lastName, int jobId, int departmentId, int rank){
        Employee e = new Employee();

        e.setId(id);
        e.setFirst_name(firstName);
        e.setLast_name(lastName);
        e.setJob_id(jobId);
        e.setDepartment_id(departmentId);
        e.setRank(rank);
        return e;
    }

    public Employee findById(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM employees WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {

                    Employee e = setValue(r.getInt("id"), r.getString("first_name"),
                            r.getString("last_name"), r.getInt("job_id"), r.getInt("department_id"), r.getInt("rank"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return e;

                } else {
                    return null;
                }
            }
        }
    }

    public List<Employee> findAll(int page) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM employees OFFSET ? LIMIT 20")) {

            s.setInt(1, page);

            try (ResultSet r = s.executeQuery()) {

                List<Employee> allEmployees = new ArrayList<>();

                while (r.next()) {
                    Employee e = setValue(r.getInt("id"), r.getString("first_name"),
                            r.getString("last_name"), r.getInt("job_id"), r.getInt("department_id"), r.getInt("rank"));

                    allEmployees.add(e);
                }
                return allEmployees;
            }
        }
    }


    public Integer findEmployeeToPunishing(boolean isCrime) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT id FROM employees TABLESAMPLE SYSTEM_ROWS (30)" +
                "WHERE job_id = 1 OR ((job_id = 2 OR job_id = 4) AND ?) ORDER BY random() LIMIT 1")) {

            s.setBoolean(1, isCrime);

            try (ResultSet r = s.executeQuery()) {
                if(r.next()){
                    return r.getInt("id");
                }
                throw new IllegalStateException("No employee can't impose punishment");
            }
        }
    }

    public static List<Employee> assign(int investigationId) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("    WITH najmenejVyt AS (\n" +
                "            SELECT employee_id AS id, count(*) AS pocet FROM work_on AS wo\n" +
                "    JOIN investigation AS i ON i.id = wo.investigation_id AND i.resolved = false\n" +
                "    GROUP BY employee_id\n" +
                "),\n" +
                "\n" +
                "    sJob AS (\n" +
                "            SELECT e.id AS id, j.type AS type, e.rank FROM employees AS e\n" +
                "            JOIN najmenejVyt AS nv ON nv.id = e.id\n" +
                "            JOIN jobs AS j ON j.id = e.job_id\n" +
                "    ),\n" +
                "\n" +
                "    pripad AS (\n" +
                "            SELECT i.id, is_crime, is_misdemeanor, is_protecting, leader_id FROM investigation AS i JOIN categories AS c\n" +
                "            ON c.id = i.category_id\n" +
                "            WHERE i.id = ? AND resolved = false\n" +
                "    ),\n" +
                "\n" +
                "    velitel AS (\n" +
                "            SELECT p.id, is_crime, is_misdemeanor, is_protecting, j.type, e.rank\n" +
                "            FROM pripad AS p JOIN employees AS e ON e.id = p.leader_id\n" +
                "                    JOIN jobs AS j ON e.job_id = j.id\n" +
                "    ),\n" +
                "\n" +
                "    vysledok AS (\n" +
                "            SELECT p.id AS investigation, s.id AS employee FROM\n" +
                "\tvelitel AS p CROSS JOIN sJob AS s\n" +
                "            WHERE ((p.is_protecting AND (s.type = 'forensic analyst' OR s.type = 'inspector' OR s.type = 'police officer'))\n" +
                "    OR (p.is_misdemeanor AND (s.type = 'forensic analyst' OR s.type = 'inspector'))\n" +
                "    OR p.is_crime) AND (s.rank IS NULL OR s.rank <= p.rank)\n" +
                ")\n" +
                "\n" +
                "    SELECT * FROM vysledok" )) {

            s.setInt(1, investigationId);

            try (ResultSet r = s.executeQuery()) {
                List<Employee> all = new ArrayList<>();
                while (r.next()) {

                    WorkOn wo = new WorkOn();
                    wo.setEmployee_id(r.getInt("employee"));
                    wo.setInvestigation_id(r.getInt("investigation"));
                    try {
                        wo.insert();
                        all.add(EmployeeFinder.getInstance().findById(r.getInt("employee")));
                    } catch (PSQLException e){
                        continue;
                    }

                }
                return all;
            }
        }
    }
}

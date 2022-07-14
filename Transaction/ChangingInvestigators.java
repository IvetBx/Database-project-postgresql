package Transaction;

import DbConnect.DbContext;
import rdg.Employee.Employee;
import rdg.Investigation.InvestigationFinder;
import rdg.Investigation.WorkOn;
import rdg.Investigation.WorkOnFinder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangingInvestigators {

    static int pokus = 0;
    public static Map<Integer, Integer> moreEmployees = new HashMap<>();
    static List<Integer> morePeople = new ArrayList<>();

    public static void investigationForChange() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("WITH empl AS (SELECT investigation_id, count(*) AS pocet\n" +
                "FROM work_on\n" +
                "GROUP BY investigation_id\n" +
                "),\n" +
                "people AS (\n" +
                "SELECT investigation_id, count(*) AS pocet\n" +
                "FROM acts\n" +
                "WHERE role_id = 2 OR role_id = 3 \n" +
                "GROUP BY investigation_id\n" +
                ")\n" +
                "\n" +
                "SELECT i.id, empl.pocet AS p, people.pocet AS p1\n" +
                "FROM investigation AS i LEFT JOIN people ON i.id = people.investigation_id \n" +
                "LEFT JOIN empl ON i.id = empl.investigation_id \n" +
                "WHERE NOT(people.pocet IS NULL AND empl.pocet IS NULL)\n" +
                "AND (people.pocet > 10*empl.pocet OR people.pocet < empl.pocet/10 OR \n" +
                "people.pocet IS NULL OR empl.pocet IS NULL) AND i.resolved = false LIMIT 100")) {

            try (ResultSet r = s.executeQuery()) {

                while (r.next()) {
                    int employee = r.getInt("p");           //pocet zamestnancov
                    int people = r.getInt("p1");          //pocet poskodenych a podozrivych
                    if(employee > people){
                        moreEmployees.put(r.getInt("id"), (employee - people) / 10);
                    } else{
                        morePeople.add(r.getInt("id"));
                    }
                }
            }
        }
    }

    public static void changing() throws SQLException {
        Connection connection = DbContext.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(connection.TRANSACTION_REPEATABLE_READ);
        investigationForChange();
        try{

            for (int i : moreEmployees.keySet()) {
                int investigationId;
                if (morePeople.size() != 0) {
                    investigationId = morePeople.get(morePeople.size() - 1);           //na aky pripad budem pridavat
                } else {
                    investigationId = InvestigationFinder.getInstance().findRandom(i).getId();
                }
                for (int j = 0; j < moreEmployees.get(i); j++) {
                    List<Employee> temp = WorkOnFinder.getInstance().findByInvestigationId(i);
                    for (Employee e : temp) {
                        WorkOn wo = new WorkOn();
                        wo.setInvestigation_id(investigationId);
                        wo.setEmployee_id(e.getId());
                        try {
                            wo.insert();
                        } catch (SQLException ex) {
                            continue;
                        }
                        WorkOn wo1 = WorkOnFinder.getInstance().findTupleEmployeeInvestigation(e.getId(), i);
                        wo1.delete();
                    }
                }
                if (morePeople.size() != 0) {
                    morePeople.remove(morePeople.size() - 1);
                }
            }

        } catch (SQLException ex){
            if(pokus < 1){
                connection.rollback();
                changing();
                pokus++;
            } else{
                throw new RuntimeException ("There are some problems, you should run it again");
            }
        } finally {
            connection.setAutoCommit(true);
            pokus = 0;
        }

    }
}

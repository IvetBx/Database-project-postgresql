package Transaction;

import DbConnect.DbContext;
import rdg.Employee.Employee;
import rdg.Employee.EmployeeFinder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AssignInvestigation {


    public static List<Employee> assignInvestigation(int investigationId) throws SQLException {

        try {
            Connection connection = DbContext.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(connection.TRANSACTION_READ_COMMITTED);

            List<Employee> all = EmployeeFinder.assign(investigationId);
            connection.commit();
            connection.setAutoCommit(true);
            return all;
        } catch (SQLException e) {
            throw new RuntimeException("There are some problems you should run it again.");
        }


    }













}

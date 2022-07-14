package Transaction;

import DbConnect.DbContext;
import rdg.Employee.EmployeeFinder;
import rdg.Punishment.Impose_punishmentTable;
import rdg.Punishment.PunishmentFinder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ImposePunishment {

    public static Integer imposePunishment(int personId, int investigationId) throws SQLException {
        try{

            Connection connection = DbContext.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(connection.TRANSACTION_REPEATABLE_READ);
            try{
                int i =  ImposePunishment.imposePunishment(personId, investigationId);
                connection.commit();
                return i;
            } catch (SQLException ex) {
                connection.rollback();
                throw new RuntimeException("There are some problems, you should run it again");
            } finally{
                connection.setAutoCommit(true);
            }

        } catch (IllegalStateException ex){
            throw new IllegalStateException(ex.getMessage());
        } catch (RuntimeException ex){
            throw new IllegalStateException(ex.getMessage());
        } catch (SQLException ex){
            throw new IllegalStateException(ex.getMessage());
        }
    }

}

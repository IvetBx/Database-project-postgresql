package Transaction;

import DbConnect.DbContext;
import rdg.Investigation.Investigation;
import rdg.Investigation.InvestigationFinder;
import rdg.Person.Person;
import rdg.Person.PersonFinder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaseClosing {

    public static List<Person> allnotPunished = new ArrayList<>();
    public static List<Person> allnotConfirmed = new ArrayList<>();



    public static void closeCase(int investigationId) throws SQLException {
        InvestigationFinder.notConfirmedAndPunishedPerson(investigationId);
        if(allnotConfirmed.size() != 0){
            throw new IllegalArgumentException(("Not all of accused and victims are confirmed"));
        } else{
            if(allnotPunished.size() != 0){
                throw new IllegalStateException("Not all of accused are punished");
            } else{
                Investigation c = InvestigationFinder.getInstance().findById(investigationId);
                c.setResolved(true);
                c.update();
            }
        }
    }


    public static void closeCaseMenu(int investigationId) throws SQLException {
        Connection connection = DbContext.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(connection.TRANSACTION_SERIALIZABLE);
        try{
            CaseClosing.closeCase(investigationId);
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw new RuntimeException("There are some problems, you should run it again");
        } finally{
            connection.setAutoCommit(true);
        }
    }


}
